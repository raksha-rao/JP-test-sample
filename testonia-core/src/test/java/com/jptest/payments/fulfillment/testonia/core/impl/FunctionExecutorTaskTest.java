package com.jptest.payments.fulfillment.testonia.core.impl;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jpinc.kernel.executor.TaskExecutor;
import com.jptest.payments.fulfillment.testonia.core.Context;

public class FunctionExecutorTaskTest {

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

        FunctionExecutorTask<String> fTask = new FunctionExecutorTask<>(this::getTestString);
        Assert.assertEquals("test", fTask.process(null));
    }

    @Test
    public void testFunctionExecutorTaskForNull() {

        FunctionExecutorTask<String> fTask = new FunctionExecutorTask<>(this::getNullString);
        Assert.assertNull(fTask.process(null));
    }

    private String getTestString(Context context) {
        return "test";
    }

    private String getNullString(Context context) {
        return null;
    }

}
