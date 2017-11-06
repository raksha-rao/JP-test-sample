package com.jptest.payments.fulfillment.testonia.core.impl;

//import com.jptest.payments.fulfillment.testonia.core.Unit;

/**
 * Represents the result of the execution of {@link //Unit}
 */
public class ExecutionResult {

    public enum ComponentExecutionStatus {
        NOT_STARTED,
        SUBMITTED,
        STARTED,
        FINISHED,
        SUCCESS,
        FAILED;
    }

    private ComponentExecutionStatus status;

    private Exception errorCause;

    public ExecutionResult(ComponentExecutionStatus status, Exception error) {
        super();
        this.status = status;
        errorCause = error;
    }

    public ExecutionResult(ComponentExecutionStatus status) {
        super();
        this.status = status;
    }

    public ComponentExecutionStatus getStatus() {
        return status;
    }

    public Exception getErrorCause() {
        return errorCause;
    }

}
