package com.jptest.payments.fulfillment.testonia.core.notification.impl;

import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;
import com.jptest.payments.fulfillment.testonia.core.notification.TestExecutionStatusObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Implementation of {@link TestExecutionStatusObserver} where the status is 
 * just logged using the standard logger.
 */
public class TestExecutionStatusLogger implements TestExecutionStatusObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestExecutionStatusLogger.class);

    private long startTime;

    @Override
    public void start(TestCaseReferenceData data) {
        long currentTimeMillis = System.currentTimeMillis();
        startTime = currentTimeMillis;
        LOGGER.info("---Test {} Execution start at: {}", data.getTestCaseName(),
                new Date(currentTimeMillis).toString() + "\n");
        LOGGER.info("---Execution Plan: {}", data.getExecutionPlan());

    }

    @Override
    public void finish(TestCaseReferenceData data) {
        long endTimeInMs = System.currentTimeMillis();
        LOGGER.info("---Execution finished at: {}", new Date(endTimeInMs).toString());
        LOGGER.info("---Total duration for the test in ms: {}", (endTimeInMs - startTime));
    }

  /*  @Override
    public void newStatus(TestCaseReferenceData refData, ComponentExecutionData execData) {
        StringBuilder sb = new StringBuilder();
        sb.append(execData.getType().name()).append(": ").append(execData.getName()).append(" ")
                .append(execData.getStatus().name());
        if (execData.getExecutionError() != null) {
            sb.append(" Error: ").append(ExceptionUtils.getStackTrace(execData.getExecutionError()));
        }

        LOGGER.info(sb.toString());
    }*/

}
