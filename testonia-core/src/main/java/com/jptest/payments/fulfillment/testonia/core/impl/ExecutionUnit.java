package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpinc.kernel.executor.TaskExecutor;
import com.jptest.payments.fulfillment.testonia.core.Asserter;
import com.jptest.payments.fulfillment.testonia.core.ComponentType;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.Task;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.Unit;
import com.jptest.payments.fulfillment.testonia.core.guice.GuiceInjector;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionResult.ComponentExecutionStatus;
import com.jptest.payments.fulfillment.testonia.core.notification.ComponentExecutionData;
import com.jptest.payments.fulfillment.testonia.core.notification.impl.TestExecutionStatusManager;

/**
 * Represents a single Unit of execution within a Test case Stage. Unit combines
 * a {@link Task} which is responsible of creating a domain Object or running a
 * particular operation which is required for a test case execution along with a
 * list of {@link Asserter} which is responsible of validating the object that
 * Retriever produced.
 *
 * The assumption here is that a test case contains a set of stages and each
 * stage contains independent units which can run in parallel. Each unit
 * involves creating an instance or calling some operation followed by
 * validations to check whether that operation was successful or not.
 *
 * E.g. Test case for fulfill_payment operation involves following stages 1.
 * Setup Stage which has 3 execution units viz. A. BuyerCreationUnit B.
 * SellerCreationUnit C. FulfillPaymentRequestBuilderUnit 2. Stage where test
 * case calls the actual operation A. Call fulfill_payment operation Unit. 3.
 * Additional validation Stage A. XML Diff validation unit B. IPN validation
 * unit Each of these units can be represented by an ExecutionUnit which sets up
 * the operation and validates it.
 *
 * The ExecutionUnit supports more than one asserters since we can have
 * logically grouped validations in different classes that still validate the
 * same object (e.g. Response in step 5 where we have at least 3 different types
 * of validations)
 *
 */
public class ExecutionUnit implements TimeoutAwareComponent, Unit, GuiceInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionUnit.class);

    private List<Asserter> asserters = new ArrayList<Asserter>();

    private Task<? extends Object> task;

    private long slaInMs = 90000;

    private String name;

    @Inject
    private TaskExecutor executor;

    @Inject
    private Configuration configuration;

    private TestExecutionStatusManager statusManager;

    private ExecutionUnit(String name, long slaInMs, Task<? extends Object> task,
            Asserter... asserters) {
        this.name = name;
        this.slaInMs = slaInMs;
        this.task = task;
        this.asserters = Arrays.asList(asserters);
        statusManager = TestExecutionStatusManager.getInstance();
        initializeDependencies();
    }

    @Override
    public boolean shouldParticipate(Context context) {
        // The unit should be participate even if task is null.
        // If the task does not participate, then unit should not participate as well.
        if (task != null) {
            return task.shouldParticipate(context);
        } else {
            return true;
        }
    }

    @Override
    public ExecutionResult execute(Context context) {
    	try {
            initializeExecution(context);
            // 1. create local context
            Context localContext = new TestCaseLocalContext(context, name);
            // 2. execute task
            if (task != null) {
                executeTask(localContext);
            }
            // 3. execute asserters
            boolean ignoreTaskTimeouts = configuration.getBoolean(CoreConfigKeys.IGNORE_TASK_TIMEOUTS.getName(), false);
            long defaultTimeoutMs = configuration.getLong(CoreConfigKeys.DEFAULT_TASK_TIMEOUT_IN_MS.getName(), 500);
            ComponentExecutor<Void> exec = new ComponentExecutor<Void>(executor, ignoreTaskTimeouts, defaultTimeoutMs);
            exec.executeAll(asserters, localContext);
            // TODO: Process the result from asserters and compile aggregated
            // error list and send it back.
            return new ExecutionResult(ComponentExecutionStatus.SUCCESS);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder("Got exception running ").append(getName())
                    .append(" for test case ").append(context.getContextIdentifier());
            LOGGER.error(message.toString(), e);
            return new ExecutionResult(ComponentExecutionStatus.FAILED, e);
        }
    }

    private void executeTask(Context localContext) {
        statusManager.newStatus(null,
                new ComponentExecutionData(task.getName(), ComponentType.TASK, ComponentExecutionStatus.STARTED));
        Object result = task.process(localContext);
        statusManager.newStatus(null,
                new ComponentExecutionData(task.getName(), ComponentType.TASK, ComponentExecutionStatus.SUCCESS));
        if (result != null) {
            localContext.setData(name, result);
        }
    }

    private void initializeExecution(Context context) {
        Thread.currentThread().setName(name);
    }

    //For unit test cases.
    void setExecutor(TaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getTimeoutInMs() {
        return slaInMs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(":");
        if (task != null) {
            sb.append(" Task: ").append(task.getName());
        }
        if (!CollectionUtils.isEmpty(asserters)) {
            sb.append(" Asserters: ");
            for (Asserter asserter : asserters) {
                sb.append(asserter.getName()).append(" | ");
            }
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        return sb.toString();
    }

    public static class ExecutionUnitBuilder {

        private static final long SLA_IN_MS = 90000;

        private Asserter[] asserters = new Asserter[] {};

        private Task<?> task;

        private long slaInMs = SLA_IN_MS;

        private String id;

        public ExecutionUnitBuilder addId(String id) {
            this.id = id;
            return this;
        }

        public ExecutionUnitBuilder addTask(Task<?> task) {
            this.task = task;
            return this;
        }

        public ExecutionUnitBuilder addAsserters(Asserter... val) {
            asserters = val;
            return this;
        }

        public ExecutionUnitBuilder addSlaInMS(long val) {
            slaInMs = val;
            return this;
        }

        public ExecutionUnit build() {
            // Override the execution unit's SLA if the task is timeout aware
            // so that unit doesn't time out before task does. (more important in
            // case if the unit is wrapped in ConcurrentExecutionUnit
            overrideUnitSLA();
            return new ExecutionUnit(id, slaInMs, task, asserters);
        }

        private void overrideUnitSLA() {
            if (task != null && task instanceof TimeoutAwareComponent) {
                // add 100 ms just to make sure we don't run out of time before task times out.
                slaInMs = ((TimeoutAwareComponent) task).getTimeoutInMs() + 100;
            }
        }

    }

}
