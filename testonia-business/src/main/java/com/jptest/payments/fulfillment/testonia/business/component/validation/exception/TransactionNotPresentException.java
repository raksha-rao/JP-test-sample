package com.jptest.payments.fulfillment.testonia.business.component.validation.exception;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionSystemException;

/**
 * This is used as a wrapper over the exceptions we capture while
 * running any {@link TestComponent} for the test case execution
 * This class will be evolved as needed to populate more details
 * for better reporting.
 */
public class TransactionNotPresentException extends TestExecutionSystemException {

    public TransactionNotPresentException(String message) {
        super(message);
    }

    @Override
    public TestoniaExceptionReasonCode getReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_TXN_NOT_PRESENT_IN_DB;
    }

}
