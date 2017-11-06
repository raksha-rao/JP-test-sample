package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpinc.kernel.executor.LoggingFuture;
import com.jpinc.kernel.executor.NamedTask;
import com.jpinc.kernel.executor.Options;
import com.jpinc.kernel.executor.TaskExecutor;
import com.jptest.payments.fulfillment.testonia.core.Asserter;
import com.jptest.payments.fulfillment.testonia.core.ComponentType;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TestComponent;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionSystemException;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionResult.ComponentExecutionStatus;
import com.jptest.payments.fulfillment.testonia.core.notification.ComponentExecutionData;
import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;
import com.jptest.payments.fulfillment.testonia.core.notification.impl.TestExecutionStatusManager;

/**
 * This is an execution engine for {@link CallableComponent} which represents a
 * {@link Callable} wrapper over {@link TestComponent} This is used to execute
 * multiple instances of {@link CallableComponent} in parallel threads and then
 * blocking till they are all finished or their execution exceeds specified
 * timeout represented by {@link TimeoutAwareComponent}
 *
 * @param <V>
 */
public class ComponentExecutor<V> {

    private final long DEFAULT_TIMEOUT_IN_MS;

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentExecutor.class);

    private TaskExecutor executor;

    private TestExecutionStatusManager statusManager;

    private final boolean ignoreTimeouts;

    public ComponentExecutor(TaskExecutor executor, boolean ignoreTimeouts, long defaultTimeoutMs) {
        this.executor = executor;
        statusManager = TestExecutionStatusManager.getInstance();
        this.ignoreTimeouts = ignoreTimeouts;
        this.DEFAULT_TIMEOUT_IN_MS = defaultTimeoutMs;
    }

    public List<V> executeAll(List<? extends TestComponent<V>> components, Context context)
            throws TestExecutionException {

        if (CollectionUtils.isEmpty(components)) {
            LOGGER.debug("Nothing to execute...");
            return null;
        }

        List<LoggingFuture<V>> resultFutures = executeComponents(components, context);
        List<V> executionResults = getResults(context, resultFutures);
        return executionResults;
    }

    private List<LoggingFuture<V>> executeComponents(List<? extends TestComponent<V>> components, Context context) {
        List<LoggingFuture<V>> resultFutures = new ArrayList<LoggingFuture<V>>();
        for (TestComponent<V> component : components) {
            if (!component.shouldParticipate(context)) {
                LOGGER.warn("This TestComponent {} should not be participated", component.getName());
                continue;
            }
            CallableComponent<V> callableComponent = new CallableComponent<V>(component, context);
            statusManager.newStatus(getReferenceData(context), new ComponentExecutionData(component.getName(),
                    getComponentType(component), ComponentExecutionStatus.SUBMITTED));
            resultFutures.add(executor.add(callableComponent, new Options(false)));
        }
        return resultFutures;
    }

    private List<V> getResults(Context context, List<LoggingFuture<V>> resultFutures) {

        List<V> executionResults = new ArrayList<V>(resultFutures.size());
        ComponentType type = null;
        String currentComponentName = "not_initialized";
        try {
            for (LoggingFuture<V> future : resultFutures) {
                NamedTask task = future.getTask();
                TestComponent<?> component = ((CallableComponent<?>) task).getComponent();
                currentComponentName = component.getName();
                type = getComponentType(component);
                V result;
                if (ignoreTimeouts) {
                    result = future.get();
                } else {
                    long timeout = getTimeoutForTask(component);
                    result = future.get(timeout, TimeUnit.MILLISECONDS);
                }
                statusManager.newStatus(getReferenceData(context), new ComponentExecutionData(component.getName(),
                        type, ComponentExecutionStatus.SUCCESS));
                LOGGER.info("{}_STATUS: SUCCESS", currentComponentName);
                executionResults.add(result);
            }
        } catch (TimeoutException e) {
            statusManager.newStatus(getReferenceData(context),
                    new ComponentExecutionData(currentComponentName, type, ComponentExecutionStatus.FAILED, e));
            StringBuilder builder = new StringBuilder("The execution time for the component: ")
                    .append(currentComponentName).append(" exceeded allowed time");
            throw new TestoniaTimeoutException(builder.toString(), e);
        } catch (InterruptedException | ExecutionException e) {
            statusManager.newStatus(getReferenceData(context),
                    new ComponentExecutionData(currentComponentName, type, ComponentExecutionStatus.FAILED, e));
            StringBuilder builder = new StringBuilder("Error executing: ").append(currentComponentName).append(", cause:").append(e.getMessage());
            TestExecutionException exceptionToThrow = new TestExecutionException(builder.toString(), e);
            if (e.getCause() instanceof TestExecutionException) {
                exceptionToThrow = (TestExecutionException) e.getCause();
            }
            throw exceptionToThrow;
        }
        return executionResults;

    }

    private TestCaseReferenceData getReferenceData(Context context) {
        return new TestCaseReferenceData(TestExecutionIdGenerator.getTestId(), context.getContextIdentifier(), "",
                System.currentTimeMillis());
    }

    private ComponentType getComponentType(TestComponent<?> component) {
        if (component instanceof ExecutionUnit) {
            return ComponentType.UNIT;
        } else if (component instanceof Asserter) {
            return ComponentType.ASSERTER;
        }
        return null;
    }

    private long getTimeoutForTask(TestComponent<?> component) {
        long timeout = DEFAULT_TIMEOUT_IN_MS;
        if (component instanceof TimeoutAwareComponent) {
            timeout = ((TimeoutAwareComponent) component).getTimeoutInMs();
        }
        return timeout;
    }

    private static class TestoniaTimeoutException extends TestExecutionSystemException {

        private TestoniaTimeoutException(String message, Throwable cause) {
            super(message, cause);
        }

        @Override
        public TestoniaExceptionReasonCode getReasonCode() {
            return TestoniaExceptionReasonCode.FAILURE_TIMEOUT;
        }

    }

}
