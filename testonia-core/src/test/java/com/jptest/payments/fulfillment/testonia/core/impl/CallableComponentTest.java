package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.concurrent.ExecutionException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jpinc.kernel.executor.ExecutorPropertyBean;
import com.jpinc.kernel.executor.ExecutorType;
import com.jpinc.kernel.executor.LoggingFuture;
import com.jpinc.kernel.executor.Options;
import com.jpinc.kernel.executor.TaskExecutor;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.Task;
import com.jptest.payments.fulfillment.testonia.core.TestComponent;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.CallableComponent;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.impl.TestCaseGlobalContext;
import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;
import com.jptest.payments.fulfillment.testonia.core.notification.impl.TestExecutionStatusManager;

public class CallableComponentTest {

    private TaskExecutor executor;

    @BeforeClass
    public void setup() {
        executor = TaskExecutor
                .newExecutor(new ExecutorPropertyBean("ExecutionUnitTest", ExecutorType.FIXED, null));
    }

    @Test
    public void testCallableComponentForString() {

        Context context = getInitializedContext("test");
        TestComponent<String> task = new TestTask("returnData", false);
        CallableComponent<String> component = new CallableComponent<String>(task, context);
        LoggingFuture<String> result = executor.add(component, new Options(false));
        try {
            Assert.assertEquals(result.get(), "returnData");
        } catch (Exception e) {
            Assert.fail("Didn't expect this exception: ", e);
        }
    }

    private TestCaseGlobalContext getInitializedContext(String name) {
        TestCaseGlobalContext context = new TestCaseGlobalContext(name);
        context.setData(ContextKeys.CURRENT_STAGE_NAME_KEY.toString(), "test_stage");
        TestExecutionStatusManager.getInstance()
                .start(new TestCaseReferenceData("testSuite", "testcase", "", System.currentTimeMillis()));
        return context;
    }

    @Test
    public void testCallableComponentForException() {

        boolean shouldFail = Boolean.TRUE;
        Task<String> testTask = new TestTask("returnData", shouldFail);
        TestCaseGlobalContext context = getInitializedContext("testcase_context");

        CallableComponent<String> component = new CallableComponent<String>(testTask, context);
        try {
            LoggingFuture<String> result = executor.add(component, new Options(false));
            result.get();
            Assert.fail("Should've thrown exception");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ExecutionException);
        }
    }

    private static class TestTask extends BaseTask<String> {

        private TestTask(String result, Boolean shouldFail) {
            super();
            this.result = result;
            this.shouldFail = shouldFail;
        }

        private String result;

        private Boolean shouldFail;

        @Override
        public String process(Context context) {
            if (shouldFail)
                throw new RuntimeException("Expected failure");
            return result;
        }

    }

}
