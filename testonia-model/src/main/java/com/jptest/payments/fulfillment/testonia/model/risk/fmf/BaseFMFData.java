package com.jptest.payments.fulfillment.testonia.model.risk.fmf;

/**
 * Represents the base class for the required input for all types of FMF filters.
 */
public abstract class BaseFMFData {

    private RiskFMFActionType action;

    public RiskFMFActionType getAction() {
        return action;
    }

    public void setAction(RiskFMFActionType action) {
        this.action = action;
    }

    public abstract RiskFMFFilterType getFilterType();

}
