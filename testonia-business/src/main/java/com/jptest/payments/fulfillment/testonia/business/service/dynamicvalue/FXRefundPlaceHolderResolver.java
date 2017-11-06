package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.jptest.ffxfx.CurrencyConvertRefundCustomerResponse;
import com.jptest.payments.fulfillment.testonia.bridge.FFXBridge;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FXRefundPlaceHolder;

/**
 * Resolves placeholder string starting with "${FX-REFUND:".
 * <p>
 * Represents ffxFX.currency_convert_refund_customer() operation.
 * 
 * @see FXRefundPlaceHolder
 * @see CompositePlaceHolderResolver
 * @see CompositePlaceHolderResolverTest
 */
@Singleton
public class FXRefundPlaceHolderResolver extends AbstractPlaceHolderResolver<FXRefundPlaceHolder> {

	@Inject
	private FFXBridge ffxBridge;

	@Override
	public boolean shouldResolve(String placeholder) {
		return placeholder.startsWith(FXRefundPlaceHolder.FX_REFUND_PLACEHOLDER_PREFIX);
	}

	@Override
	protected FXRefundPlaceHolder getPlaceHolderInstance(String placeholder) {
		return new FXRefundPlaceHolder(placeholder);
	}

	/**
	 * Get FX-Refund response set in FXRefundPlaceHolder
	 * 
	 * @param context
	 * @param placeholder
	 */
	@Override
    public void setPlaceHolderValues(FXRefundPlaceHolder placeHolder) {
		CurrencyConvertRefundCustomerResponse response = ffxBridge.currencyConvertRefundCustomer(placeHolder.getCurrencyFrom(),
				placeHolder.getAmountFrom(), placeHolder.getCurrencyOut(), placeHolder.getCountry(),
				placeHolder.isReverse(), placeHolder.getFxConversionType().toLowerCase());
		placeHolder.setValues(response);
	}
}
