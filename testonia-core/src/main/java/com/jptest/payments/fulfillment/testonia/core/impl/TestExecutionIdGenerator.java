package com.jptest.payments.fulfillment.testonia.core.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.core.guice.ConfigurationHelper;
import com.jptest.payments.fulfillment.testonia.core.reporting.TestResultSummaryReporter;

/**
 * Generates unique id for each execution. 
 * 
 * The Id will be generated once per process - meaning it will be shared by all tests for that run.
 */
public class TestExecutionIdGenerator {

    private static final int STRING_LENGTH = 24;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestResultSummaryReporter.class);

    private static final String EXECUTION_ID = generateExecutionId();

    /**
     * Generates alphanumeric string of input length 
     * @return
     */
    private static String generateExecutionId() {
        String executionId;
        try {
            executionId = ConfigurationHelper.getTestConfiguration()
                    .getString(CoreConfigKeys.EXECUTION_ID.getName(),
                            RandomStringUtils.random(STRING_LENGTH, true, true));
        } catch (Exception e) {
            LOGGER.warn(
                    "configuration is not initialized yet, moving on with generating id instead of using it from VM args");
            executionId = RandomStringUtils.random(STRING_LENGTH, true, true);
        }
        return executionId;
    }

    public static String getTestId() {
        return EXECUTION_ID;
    }

}
