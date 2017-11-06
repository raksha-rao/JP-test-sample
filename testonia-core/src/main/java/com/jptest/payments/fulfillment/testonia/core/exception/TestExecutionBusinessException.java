package com.jptest.payments.fulfillment.testonia.core.exception;


/**
 * This is used as a wrapper over the exceptions we capture while
 * running any {@link //TestComponent} for the test case execution
 * This class will be evolved as needed to populate more details
 * for better reporting.
 * 
 * @see// TestExecutionBusinessExceptionTest
 */
public abstract class TestExecutionBusinessException extends TestExecutionException {

	public TestExecutionBusinessException() {
    }
	
    /**
     * @param message
     */
    public TestExecutionBusinessException(String message) {
        super(message);
    }

    public TestExecutionBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestExecutionBusinessException(Throwable cause) {
        super(cause);
    }

    @Override
    public TestoniaExceptionReasonCode getReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_GENERIC_BUSINESS_ERROR;
    }

}
