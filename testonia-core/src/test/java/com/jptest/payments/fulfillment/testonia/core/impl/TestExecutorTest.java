package com.jptest.payments.fulfillment.testonia.core.impl;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jpinc.kernel.executor.ExecutorPropertyBean;
import com.jpinc.kernel.executor.ExecutorType;
import com.jpinc.kernel.executor.TaskExecutor;
import com.jptest.payments.fulfillment.testonia.core.Asserter;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionUnit.ExecutionUnitBuilder;
import com.jptest.payments.fulfillment.testonia.core.impl.TestExecutor.TestExecutorBuilder;
import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;
import com.jptest.payments.fulfillment.testonia.core.notification.impl.TestExecutionStatusManager;

public class TestExecutorTest {

    private TaskExecutor taskExecutor;

    @BeforeClass
    public void setup() {
        taskExecutor = TaskExecutor
                .newExecutor(new ExecutorPropertyBean("ExecutionUnitTest", ExecutorType.FIXED, null));

    }

    @Test
    public void testExecutorForEmptyStages() {

        Context context = getInitializedContext("test");
        TestExecutor exec = new TestExecutorBuilder().addContext(context).build();
        try {
            exec.execute();
        } catch (Exception e) {
            Assert.fail("didn't expect exception here");
        }
    }

    @Test
    public void testExecutorForAsserterFailure() {

        Context context = getInitializedContext("test");
        TestAsserter asserter = new TestAsserter(true /* should fail*/);
        ExecutionUnit testUnit = new ExecutionUnitBuilder().addAsserters(asserter).addId("test_unit").build();
        TestExecutor exec = new TestExecutorBuilder().addContext(context).addUnit(testUnit).build();
        try {
            exec.execute();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof TestExecutionException);
        }
    }

    @Test
    public void testExecutorForTestExecutionException() {

        Context context = getInitializedContext("test");
        BaseAsserter asserter = new BaseAsserter() {
            @Override
            public void validate(Context context) {
                throw new TestExecutionException("test case exception");

            }
        };
        ExecutionUnit testUnit = new ExecutionUnitBuilder().addAsserters(asserter).addId("test_unit").build();
        testUnit.setExecutor(taskExecutor);
        TestExecutor exec = new TestExecutorBuilder().addContext(context).addUnit(testUnit).build();
        try {
            exec.execute();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof TestExecutionException);
            Assert.assertFalse(e.getCause() instanceof TestExecutionException);
        }
    }

    private TestCaseGlobalContext getInitializedContext(String name) {
        TestCaseGlobalContext context = new TestCaseGlobalContext(name);
        context.setData(ContextKeys.CURRENT_STAGE_NAME_KEY.toString(), "test_stage");
        TestExecutionStatusManager.getInstance()
                .start(new TestCaseReferenceData("testSuite", "testcase", "", System.currentTimeMillis()));
        return context;
    }

}
