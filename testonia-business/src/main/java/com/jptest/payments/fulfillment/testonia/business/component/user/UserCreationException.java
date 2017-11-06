package com.jptest.payments.fulfillment.testonia.business.component.user;

import com.jptest.payments.fulfillment.testonia.core.TestComponent;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionSystemException;

/**
 * This is used as a wrapper over the exceptions we capture while
 * running any {@link TestComponent} for the test case execution
 * This class will be evolved as needed to populate more details
 * for better reporting.
 */
public class UserCreationException extends TestExecutionSystemException {

    public UserCreationException(String message) {
        super(message);
    }

    @Override
    public TestoniaExceptionReasonCode getReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_USER_CREATION;
    }

}
