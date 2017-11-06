package com.jptest.payments.fulfillment.testonia.business.component.validation;

import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.notification.impl.TestExecutionStatusManager;
import org.mockito.Mockito;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.TestCaseGlobalContext;
import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;
import org.testng.annotations.Test;

public class E2EGoldenFileComparisonAsserterTest {

    private GoldenFileComparisonAsserter goldenFileComparisonAsserter;

    @Test
    public void testValidate() {
        Context context = getInitializedContext("text");
        goldenFileComparisonAsserter.validate(context);
    }

    private TestCaseGlobalContext getInitializedContext(String name) {
        TestCaseGlobalContext context = new TestCaseGlobalContext(name);
        context.setData(ContextKeys.CURRENT_STAGE_NAME_KEY.toString(), "test_stage");
        TestExecutionStatusManager.getInstance()
                .start(new TestCaseReferenceData("testSuite", "testcase", "", System.currentTimeMillis()));
        return context;
    }

    @Test
    public void testGetActualResponseXml() {
    }

    @Test
    public void testGetValidationType() {
    }

    @Test
    public void testGetTimeoutInMs() {
    }

    @Test
    public void testIsBestEffort() {
    }

    @Test
    public void testIsUserCreationMode() {
    }
}
