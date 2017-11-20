package com.jptest.payments.fulfillment.testonia.core.exception;



/**
 * This is used as a wrapper over the exceptions we capture while running any {@link //TestComponent} for the test case
 * execution This class will be evolved as needed to populate more details for better reporting.
 *
 * @see //TestExecutionExceptionTest
 */
public class TestExecutionException extends RuntimeException {

    public enum TestoniaExceptionReasonCode {
        FAILURE_USER_CREATION, // User creation errors
        FAILURE_PYMT_ASSERTION, // PYMT tool related failures
        FAILURE_WTRANSACTION_NOT_UPDATED, // Slow AMQ
        FAILURE_PLAN_CREATION, // planning, request building errors
        FAILURE_TXN_NOT_PRESENT_IN_DB, // May be because of slow AMQ
        FAILURE_GENERIC_BUSINESS_ERROR, // random failures etc
        FAILURE_GENERIC_SYSTEM_ERROR, // random timeouts etc

        // DBDiffs
        FAILURE_FULFILL_ACTIVITY_DBDIFF,
        FAILURE_IPN_DBDIFF,
        FAILURE_PAYLOAD_DBDIFF,
        FAILURE_FINANCIAL_JOURNAL_DBDIFF,
        FAILURE_PAYMENT_SENDER_DBDIFF,
        FAILURE_PYMT_DBDIFF,
        FAILURE_PAYMENT_RECIPIENT_DBDIFF,
        FAILURE_PAYMENT_RECIPIENT_SUBBALANCE_DBDIFF,
        FAILURE_PRS_DBDIFF,
        FAILURE_LEGACY_EVENTS_DBDIFF,
        FAILURE_ACTIVITY_LOG_IPN_DBDIFF,
        FAILURE_ACTIVITY_LOG_SOR_DBDIFF,
        FAILURE_ACTIVITY_LOG_SETTLEMENT_DBDIFF,
        FAILURE_SPT_LIFTOFF_DBDIFF, // used by TFS-FT
        FAILURE_TOPUP_DBDIFF, // used by TFS-FT

        FAILURE_ACTIVITY_LOG_PAYMENT_MESSAGE_DBDIFF,
        FAILURE_POST_PAYMENT_RESPONSE_DBDIFF,
        FAILURE_POST_PAYMENT_SETUP, // failures during post payment operations
        FAILURE_TIMEOUT, // failures related to timeouts
        FAILURE_GENERIC_ERROR; // default generic error
    }

    public TestExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TestExecutionException(final Throwable cause) {
        super(cause);
    }

    public TestExecutionException(final String message) {
        super(message);
    }

    public TestExecutionException() {

    }

    public TestoniaExceptionReasonCode getReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_GENERIC_ERROR;
    }

}
