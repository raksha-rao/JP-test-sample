package com.jptest.payments.fulfillment.testonia.core.impl;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jpinc.kernel.bean.configuration.BeanConfigCategoryInfo;
import com.jpinc.kernel.bean.configuration.ConfigCategoryCreateException;
import com.jpinc.kernel.executor.ExecutorPropertyBean;
import com.jpinc.kernel.executor.ExecutorType;
import com.jpinc.kernel.executor.TaskExecutor;
import com.jptest.payments.fulfillment.testonia.core.Asserter;
import com.jptest.payments.fulfillment.testonia.core.Task;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionResult.ComponentExecutionStatus;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionUnit.ExecutionUnitBuilder;
import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;
import com.jptest.payments.fulfillment.testonia.core.notification.impl.TestExecutionStatusManager;
import java.lang.reflect.Field;


public class ExecutionUnitTest {

    private TaskExecutor executor;

    @BeforeClass
    public void setup() {
        mockTestResultInReporter();
        initializeTaskExecutor();
    }

    private void initializeTaskExecutor() {
        BeanConfigCategoryInfo categoryInfo = null;
        try {
            categoryInfo = BeanConfigCategoryInfo.createBeanConfigCategoryInfo("ExecutionUnitTest", "ExecutionUnitTest",
                    "stageExecutorGroup", false, false, null, "executor for test stageExecutors");
        } catch (ConfigCategoryCreateException e) {
            throw new TestExecutionException("Couldn't create the executor ", e);
        }
        executor = TaskExecutor
                .newExecutor(new ExecutorPropertyBean("ExecutionUnitTest", ExecutorType.FIXED, categoryInfo));
    }

    private void mockTestResultInReporter() {
        ITestResult mockTestResult = Mockito.mock(ITestResult.class);
        Reporter.setCurrentTestResult(mockTestResult);
    }

    @Test
    public void testExecutionUnit() {

        Task<String> mockTask = getMockTask("Output");

        boolean shouldFail = Boolean.FALSE;
        Asserter mockAsserter = getMockAsserter(shouldFail);
        ExecutionUnit unit = new ExecutionUnitBuilder().addId("testunit").addTask(mockTask).addAsserters(mockAsserter)
                .build();
        TestCaseGlobalContext context = getInitializedContext("testExecutionUnit");
        unit.setExecutor(executor);
        Field configurationField = ReflectionUtils.findField(ExecutionUnit.class, "configuration");
        configurationField.setAccessible(true);
        ReflectionUtils.setField(configurationField, unit, new BaseConfiguration());
        ExecutionResult result = unit.execute(context);
        Assert.assertNotNull(unit.toString());
        Assert.assertNotNull(result);
        Assert.assertEquals(ComponentExecutionStatus.SUCCESS, result.getStatus());
        Assert.assertEquals("Output", context.getData("testunit").toString());
    }

    private TestCaseGlobalContext getInitializedContext(String name) {
        TestCaseGlobalContext context = new TestCaseGlobalContext(name);
        context.setData(ContextKeys.CURRENT_STAGE_NAME_KEY.toString(), "test_stage");
        TestExecutionStatusManager.getInstance()
                .start(new TestCaseReferenceData("testSuite", "testcase", "", System.currentTimeMillis()));
        return context;
    }

    @Test
    public void testExecutionUnitForException() {

        Task<String> mockTask = getMockTask("expectedOutput");

        boolean shouldFail = Boolean.TRUE;
        Asserter mockAsserter = getMockAsserter(shouldFail);
        ExecutionUnit unit = new ExecutionUnitBuilder().addId("testunit").addTask(mockTask).addAsserters(mockAsserter)
                .build();

        TestCaseGlobalContext context = getInitializedContext("testcase_context");
        unit.setExecutor(executor);
        ExecutionResult result = unit.execute(context);
        Assert.assertNotNull(result);
        Assert.assertEquals(ComponentExecutionStatus.FAILED, result.getStatus());
        Assert.assertNotNull(result.getErrorCause());
    }

    private Asserter getMockAsserter(boolean shouldThrowException) {
        Asserter mockAsserter = Mockito.mock(Asserter.class);
        if (shouldThrowException) {
            Mockito.doThrow(new RuntimeException("expected failure")).when(mockAsserter).execute(Matchers.anyObject());
        } else {
            Mockito.doReturn(null).when(mockAsserter).execute(Matchers.anyObject());
        }
        return mockAsserter;
    }

    private Task<String> getMockTask(String result) {
        Task<String> mockTask = Mockito.mock(Task.class);
        // Mockito.when(mockTask.getId()).thenReturn(taskId);
        Mockito.when(mockTask.process(Matchers.anyObject())).thenReturn(result);
        return mockTask;
    }

}
