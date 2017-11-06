package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jpinc.kernel.executor.ExecutorPropertyBean;
import com.jpinc.kernel.executor.ExecutorType;
import com.jpinc.kernel.executor.TaskExecutor;
import com.google.common.collect.Lists;
import com.jptest.payments.fulfillment.testonia.core.Asserter;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;
import com.jptest.payments.fulfillment.testonia.core.notification.impl.TestExecutionStatusManager;

public class ComponentExecutorTest {

    private TaskExecutor executor;

    @BeforeClass
    public void setup() {
        executor = TaskExecutor
                .newExecutor(new ExecutorPropertyBean("ExecutionUnitTest", ExecutorType.FIXED, null));

    }

    @Test
    public void testComponentExecutor() {

        Context context = getInitializedContext("test");
        boolean shouldFail = false;
        Asserter asserter = new TestAsserter(shouldFail);

        assertComponentExecutor(context, shouldFail, asserter, true);
    }

    private void assertComponentExecutor(Context context, boolean shouldFail, Asserter asserter, boolean taskTimeouts) {
        ComponentExecutor<Void> componentExecutor = new ComponentExecutor<>(executor, taskTimeouts, 500);
        try {
            List<Void> resultList = componentExecutor.executeAll(Lists.newArrayList(asserter), context);
            if (!shouldFail) {
                Assert.assertNotNull(resultList);
                Assert.assertEquals(1, resultList.size());
            } else {
                Assert.fail("Should've thrown exception instead of continuing execution");
            }
        } catch (Exception e) {
            if (!shouldFail) {
                Assert.fail("Didn't expect this exception: ", e);
                Assert.assertTrue(e instanceof TestExecutionException);
                Assert.assertFalse(e.getCause() instanceof TestExecutionException);
            }
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
    public void testExecutionUnitForException() {

        boolean shouldFail = Boolean.TRUE;
        Asserter asserter = new TestAsserter(shouldFail);
        TestCaseGlobalContext context = getInitializedContext("testcase_context");

        assertComponentExecutor(context, shouldFail, asserter, false);
    }

}
