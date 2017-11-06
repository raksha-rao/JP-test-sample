package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.MBeanException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.jpinc.kernel.cal.CalServiceFactory;
import com.jpinc.kernel.cal.ICalService;
import com.jpinc.kernel.cal.api.CalTransaction;
import com.jpinc.kernel.cal.api.sync.CalTransactionFactory;
import com.jpinc.kernel.cal.java.file.FileCalService;
import com.jpinc.kernel.cal.mxbean.CalClientConfigMXBeanImpl;
import com.jptest.infra.util.cal.CorrelationId;
import com.jptest.payments.fulfillment.testonia.core.ComponentType;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.Unit;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionResult.ComponentExecutionStatus;
import com.jptest.payments.fulfillment.testonia.core.notification.ComponentExecutionData;
import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;
import com.jptest.payments.fulfillment.testonia.core.notification.impl.TestExecutionStatusManager;
import com.jptest.payments.fulfillment.testonia.core.reporting.CoreLoggingUtil;
import com.jptest.payments.fulfillment.testonia.core.reporting.TestReporterAttributeKeys;

/**
 * Represents an execution engine for a test case. Typically a list of
 * {@link Unit} represents the steps involved in the test case. This class
 * takes that list and executes each stage one after another. The units within a
 * Stage are executed in parallel however the stage.execute is a blocking call
 * which waits for all the units are done with their execution. If the stage
 * execution is successful only then the next one starts.
 *
 */

public class TestExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionUnit.class);

    static {
        try {
            ICalService calService = new FileCalService(CalClientConfigMXBeanImpl.getInstance());
            CalServiceFactory.setCalService(calService);
        } catch (MBeanException e) {
            LOGGER.warn("Error while setting up cal service", e);
        }
    };

    private static final String CAL_TXN_SUCCESS = "0";
    private static final String CAL_TXN_ERROR = "1";
    private static final String STAGE_END_EVENT_NAME = "STAGE_END";
    private static final String STAGE_START_EVENT_NAME = "STAGE_START";
    private Context context;
    private List<Unit> units;
    private TestExecutionStatusManager statusManager;
    private TestCaseReferenceData refData;

    private TestExecutor(Context context, List<Unit> stages) {
        this.context = context;
        units = stages;
        statusManager = TestExecutionStatusManager.getInstance();
        refData = getTestReferenceData(context);
        statusManager.start(refData);
    }

    private TestCaseReferenceData getTestReferenceData(Context testCaseContext) {
        return new TestCaseReferenceData(TestExecutionIdGenerator.getTestId(), context.getContextIdentifier(),
                this.toString(),
                System.currentTimeMillis());
    }

    private TestExecutor(Context context) {
        this(context, new ArrayList<Unit>());
    }

    public void execute() {

        CalTransaction txn = CalTransactionFactory.create("TEST");
        txn.setName(context.getContextIdentifier());
        String correlationId = CorrelationId.getNextId();
        txn.setCorrelationId(correlationId);

        initializeContextCommonData(correlationId);

        LOGGER.info("Test Execution Started for: {}, correlationId: {}", context.getContextIdentifier(), correlationId);
        LOGGER.info(
                "http://mscal.qa.jptest.com/cgi/idsearch_manager.py?id_type=corr_id&id_value={}&fetchlog=0&submit=Search",
                correlationId);
        boolean isError = false;
        String stageName = "not_initialized";
        if (CollectionUtils.isEmpty(units)) {
            LOGGER.error("No execution units added in the test executor");
            return;
        }
        try {
            for (Unit currentUnit : units) {
                if (!currentUnit.shouldParticipate(context)) {
                    LOGGER.warn("Unit '{}' is skipped since its shouldParticipate returned false",
                            currentUnit.getName());
                    continue;
                }
                stageName = currentUnit.getName();
                context.setData(ContextKeys.CURRENT_STAGE_NAME_KEY.getName(), stageName);
                addLogData(txn, stageName, STAGE_START_EVENT_NAME);
                statusManager.newStatus(refData,
                        new ComponentExecutionData(stageName, ComponentType.STAGE, ComponentExecutionStatus.STARTED));
                ExecutionResult result = currentUnit.execute(context);
                if (!result.getStatus().equals(ComponentExecutionStatus.SUCCESS)) {
                    // we don't check for soft errors in case of hard failure
                    throw getExceptionFromResult(currentUnit, result);

                }
                statusManager.newStatus(refData,
                        new ComponentExecutionData(stageName, ComponentType.STAGE, ComponentExecutionStatus.SUCCESS));
                addLogData(txn, stageName, STAGE_END_EVENT_NAME);
            }
            // no other failures, check for soft errors
            handleSoftErrorsIfAny();
        } catch (Exception e) {
            statusManager.newStatus(refData,
                    new ComponentExecutionData(stageName, ComponentType.STAGE, ComponentExecutionStatus.FAILED, e));
            isError = true;
            throw e;
        } finally {
            statusManager.finish(refData);
            addLogStatus(txn, isError ? CAL_TXN_ERROR : CAL_TXN_SUCCESS);
            txn.completed();
        }
    }

    /**
     * add correlation id to TestResult so that we can use it in summary report for ELK logging
     * For usage refer to TestResultSummaryReporter.java
     *  
     * @param correlationId
     */
    private void initializeContextCommonData(String correlationId) {
        ((TestCaseGlobalContext) context).setCorrelationId(correlationId);
        ITestResult currentTestResult = Reporter.getCurrentTestResult();
        currentTestResult.setAttribute(ContextKeys.CORRELATION_ID.getName(), correlationId);
        currentTestResult.setAttribute(ContextKeys.CONTEXT_KEY.getName(), context);
        context.setData(ContextKeys.TESTCASE_UNIQUE_ID.getName(),
                CoreLoggingUtil.getTestCaseName(currentTestResult));
        MDC.put(TestReporterAttributeKeys.CORRELATION_ID.getKeyName(), correlationId);
    }
    
    private TestExecutionException getExceptionFromResult(Unit currentUnit, ExecutionResult result) {
        if (result.getErrorCause() instanceof TestExecutionException) {
            return (TestExecutionException) result.getErrorCause();
        } else {
        	String message = "The unit didn't finish correctly: " + currentUnit.getName() + ", cuase:";
            return new TestExecutionException(message + result.getErrorCause().getMessage(), result.getErrorCause());
        }
    }

    private void addLogData(CalTransaction txn, String eventName, String eventData) {
        txn.addData(eventName, eventData);
        LOGGER.info("even name: {} data: {}", eventName, eventData);
    }

    private void addLogStatus(CalTransaction txn, String status) {
        txn.setStatus(status);
        LOGGER.info("{}_STATUS: {}", txn.getName(), status);
    }

    private void handleSoftErrorsIfAny() {
    	if (context.getAggregateAssert().hasSoftErrors()) {
    		throw context.getAggregateAssert().getAggregateException();
    	}
    }

    public static class TestExecutorBuilder {
        private Context context;
        private List<Unit> units = new ArrayList<Unit>();

        public TestExecutorBuilder addContext(Context context) {
            this.context = context;
            return this;
        }

        public TestExecutorBuilder addUnit(Unit... units) {
            this.units.addAll(Arrays.asList(units));
            return this;
        }

        public TestExecutor build() {
            return new TestExecutor(context, units);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (!CollectionUtils.isEmpty(units)) {
            sb.append("Test Name: ").append(context.getContextIdentifier());
            sb.append(System.getProperty("line.separator"));
            for (Unit unit : units) {
                sb.append(unit.toString());
                sb.append(System.getProperty("line.separator"));
            }
        } else {
            sb.append("not initialized");
        }
        return sb.toString();
    }

}
