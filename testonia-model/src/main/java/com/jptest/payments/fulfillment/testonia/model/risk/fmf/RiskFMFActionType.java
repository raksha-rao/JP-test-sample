package com.jptest.payments.fulfillment.testonia.model.risk.fmf;

/**
 *  Represents the allowed values for FMF filter actions
 */
public enum RiskFMFActionType {

    Accept(1),
    Review(2),
    Reject(4),
    Flagged(8);

    private int actionType;

    private RiskFMFActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getValue() {
        return this.actionType;
    }
}
