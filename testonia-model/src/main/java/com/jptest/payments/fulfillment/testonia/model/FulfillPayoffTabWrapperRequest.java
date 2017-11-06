package com.jptest.payments.fulfillment.testonia.model;

import com.jptest.money.FulfillPayoffTabRequest;
import com.jptest.types.Currency;

/**
 * FulfillPayoffTabWrapperRequest forms a wrapper to encapsulate the meta data
 * needed to plan and fulfill Singlepartyfulfillment::fulfill_payoff_tab
 *
 * @JP Inc.
 */
public class FulfillPayoffTabWrapperRequest {

	private CurrencyData payOffAmount;

	private FulfillPayoffTabRequest fulfillRequest;

	public CurrencyData getPayOffAmount() {
		return payOffAmount;
	}

	public void setPayOffAmount(CurrencyData amount) {
		this.payOffAmount = amount;
	}

	public FulfillPayoffTabRequest getFulfillRequest() {
		return fulfillRequest;
	}

	public void setFulfillRequest(FulfillPayoffTabRequest request) {
		this.fulfillRequest = request;
	}

	public static class CurrencyData {
		private String currencyCode;
		private String amount;

		private CurrencyData() {
			super();
		}

		public String getCurrencyCode() {
			return this.currencyCode;
		}

		public void setCurrencyCode(final String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public void setAmount(final String amount) {
			this.amount = amount;
		}

		public String getAmount() {
			return this.amount;
		}

		public Currency getCurrency() {
			return new Currency(currencyCode, Long.parseLong(amount));
		}
	}
}
