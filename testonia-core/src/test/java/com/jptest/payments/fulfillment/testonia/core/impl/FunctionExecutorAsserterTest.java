package com.jptest.payments.fulfillment.testonia.core.impl;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jpinc.kernel.executor.TaskExecutor;
import com.jptest.payments.fulfillment.testonia.core.Context;

public class FunctionExecutorAsserterTest {

    private TaskExecutor executor;

    @BeforeClass
    public void setup() {
        mockTestResultInReporter();
    }

    private void mockTestResultInReporter() {
        ITestResult mockTestResult = Mockito.mock(ITestResult.class);
        Reporter.setCurrentTestResult(mockTestResult);
    }

    @Test
    public void testFunctionExecutorTask() {

        FunctionExecutorAsserter fAsserter = new FunctionExecutorAsserter(this::getAssertPassMethod);
        try {
            fAsserter.validate(null);
        } catch (Throwable t) {
            Assert.fail("shouldn't have failed");
        }
    }

    @Test
    public void testFunctionExecutorTaskForFail() {

        FunctionExecutorAsserter fAsserter = new FunctionExecutorAsserter(this::getAssertFailMethod);
        try {
            fAsserter.validate(null);
            Assert.fail("should've failed");
        } catch (Throwable t) {
            Assert.assertTrue(t instanceof AssertionError);
        }

    }

    private Void getAssertPassMethod(Context context) {
        return null;
    }

    private Void getAssertFailMethod(Context context) {
        Assert.fail("test failure");
        return null;
    }

}
