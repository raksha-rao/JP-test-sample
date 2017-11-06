package com.jptest.payments.fulfillment.testonia.model.group;

/**
 * @JP Inc.
 */
public enum PaymentSubtype {

	FAST_FUNDS("fast_funds"),
	ACH("ach");

	private final String subType;

	private PaymentSubtype(final String subtype) {
		this.subType = subtype;
	}

	public String getType() {
		return this.subType;
	}

}
