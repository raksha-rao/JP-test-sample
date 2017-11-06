package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.jptest.ffxfx.CurrencyConvertCustomerResponse;
import com.jptest.payments.fulfillment.testonia.bridge.FFXBridge;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FXPlaceHolder;

/**
 * Resolves placeholder string starting with "${FX:".
 * <p>
 * Represents ffxFX.currency_convert_customer() operation.
 * 
 * @see FXPlaceHolder
 * @see CompositePlaceHolderResolver
 * @see CompositePlaceHolderResolverTest
 */
@Singleton
public class FXPlaceHolderResolver extends AbstractPlaceHolderResolver<FXPlaceHolder> {

	@Inject
	private FFXBridge ffxBridge;

	@Override
	public boolean shouldResolve(String placeholder) {
		return placeholder.startsWith(FXPlaceHolder.FX_PLACEHOLDER_PREFIX);
	}

	@Override
	protected FXPlaceHolder getPlaceHolderInstance(String placeholder) {
		return new FXPlaceHolder(placeholder);
	}

	/**
	 * Get FX response set in FXPlaceHolder
	 * 
	 * @param context
	 * @param placeholder
	 */
	@Override
    public void setPlaceHolderValues(FXPlaceHolder placeHolder) {
		CurrencyConvertCustomerResponse response = ffxBridge.currencyConvertCustomer(placeHolder.getCurrencyFrom(),
				placeHolder.getAmountFrom(), placeHolder.getCurrencyOut(), placeHolder.getCountry(),
				placeHolder.isReverse(), placeHolder.getFxConversionType().toLowerCase());
		placeHolder.setValues(response);
	}
}
