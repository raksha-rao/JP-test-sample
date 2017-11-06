package com.jptest.payments.fulfillment.testonia.core.exception;

import com.jptest.payments.fulfillment.testonia.core.TestComponent;

/**
 * This is used as a wrapper over the exceptions we capture while
 * running any {@link TestComponent} for the test case execution
 * This class will be evolved as needed to populate more details
 * for better reporting.
 * 
 * @see TestExecutionSystemExceptionTest
 */
public abstract class TestExecutionSystemException extends TestExecutionException {

	public TestExecutionSystemException() {
    }
	
	public TestExecutionSystemException(String message) {
        super(message);
    }

    public TestExecutionSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestExecutionSystemException(Throwable cause) {
        super(cause);
    }

    @Override
    public TestoniaExceptionReasonCode getReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_GENERIC_SYSTEM_ERROR;
    }

}
