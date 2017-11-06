package com.jptest.payments.fulfillment.testonia.model;

import com.jptest.types.Currency;

/**
 * FFX Request class with all the necessary information to carry out FX conversion
 */
public class FXRequest {
	
	private Currency fromAmount;
	private String toCurrency;
	private String country;
	private boolean reverse;
	private String conversionType;
	
	public Currency getFromAmount() {
		return fromAmount;
	}
	
	public void setFromAmount(Currency fromAmount) {
		this.fromAmount = fromAmount;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public String getConversionType() {
		return conversionType;
	}

	public void setConversionType(String conversionType) {
		this.conversionType = conversionType;
	}

}
