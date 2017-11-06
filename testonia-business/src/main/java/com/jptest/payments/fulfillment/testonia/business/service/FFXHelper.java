package com.jptest.payments.fulfillment.testonia.business.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.bridge.FFXBridge;
import com.jptest.payments.payments.PaymentsCommonPb;
import com.jptest.payments.payments.ValueTypePb;
import com.jptest.types.Currency;

@Named
@Singleton
public class FFXHelper {

	@Inject
	private FFXBridge ffxBridge;

	public PaymentsCommonPb.Value getEquivalentValue(PaymentsCommonPb.Value value, final String newCurrencyCode)
			throws RuntimeException {
		Currency currency = ffxBridge.getEquivalent(value.getCode(), value.getCoefficient(), newCurrencyCode);
		final int exponent = 0;

		return PaymentsCommonPb.Value.newBuilder().setType(ValueTypePb.ValueType.CURRENCY)
				.setCode(currency.getCurrencyCode()).setCoefficient(currency.getAmount()).setExponent(exponent).build();

	}

	public PaymentsCommonPb.Value getEquivalentValue(final String currencyCode, final long amount,
			final String newCurrencyCode) throws RuntimeException {
		final int exponent = 0;

		return getEquivalentValue(PaymentsCommonPb.Value.newBuilder().setType(ValueTypePb.ValueType.CURRENCY)
				.setCode(currencyCode).setCoefficient(amount).setExponent(exponent).build(), newCurrencyCode);
	}

	public PaymentsCommonPb.Value getUSDEquivalentValue(PaymentsCommonPb.Value value) throws RuntimeException {
		return getEquivalentValue(value, "USD");
	}

	public Currency getUSDEquivalent(String currencyCode, long amount) {
		return ffxBridge.getUSDEquivalent(currencyCode, amount);
	}

}
