package com.jptest.payments.fulfillment.testonia.business.component.task.exception;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionSystemException;

/**
 * This is used as a wrapper over the exceptions we capture while
 * running any {@link TestComponent} for the test case execution
 * This class will be evolved as needed to populate more details
 * for better reporting.
 */
public class PlanCreationFailedException extends TestExecutionSystemException {

    public PlanCreationFailedException(String message) {
        super(message);
    }

    /**
     * @param string
     * @param e
     */
    public PlanCreationFailedException(String string, Throwable e) {
        super(string, e);
    }

    @Override
    public TestoniaExceptionReasonCode getReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_PLAN_CREATION;
    }

}
