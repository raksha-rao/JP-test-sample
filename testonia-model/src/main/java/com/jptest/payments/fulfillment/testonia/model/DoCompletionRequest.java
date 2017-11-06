package com.jptest.payments.fulfillment.testonia.model;

/**
 * Completion Request for payment completion use-cases
 */
public class DoCompletionRequest {
    private int holdType;
    private boolean nonReceivableCountry;

    public int getHoldType() {
        return holdType;
    }

    public void setHoldType(int holdType) {
        this.holdType = holdType;
    }
    
	public boolean isNonReceivableCountry() {
		return nonReceivableCountry;
	}

	public void setnonReceivableCountry(boolean nonReceivableCountry) {
		this.nonReceivableCountry = nonReceivableCountry;
	}
}
