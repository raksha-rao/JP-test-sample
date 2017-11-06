package com.jptest.payments.fulfillment.testonia.core.notification.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jptest.payments.fulfillment.testonia.core.ComponentType;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionResult.ComponentExecutionStatus;
import com.jptest.payments.fulfillment.testonia.core.notification.ComponentExecutionData;
import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;

public class TestExecutionStatusLoggerTest {

    @Test
    public void testIdGenerator() {
        TestExecutionStatusLogger statusLogger = new TestExecutionStatusLogger();
        TestCaseReferenceData data = new TestCaseReferenceData("test", "test_case", "exec_plan", 12345);
        try {
            statusLogger.start(data);
            statusLogger.newStatus(data,
                    new ComponentExecutionData("unit", ComponentType.UNIT, ComponentExecutionStatus.STARTED));
            statusLogger.finish(data);
        } catch (Exception e) {
            Assert.fail("didn't expect exception here");
        }
    }

}
