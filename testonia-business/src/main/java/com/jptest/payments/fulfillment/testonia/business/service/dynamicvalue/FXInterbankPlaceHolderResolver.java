package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import com.jptest.payments.fulfillment.testonia.bridge.FFXBridge;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FXInterbankPlaceHolder;

import javax.inject.Inject;
import javax.inject.Singleton;

//import com.jptest.ffxfx.CurrencyConvertRefundInterbankResponse;
//import com.jptest.ffxfx.SerializeFxAuditTrailResponse;


/**
 * Resolves placeholder string starting with "${FX-INTERBANK:".
 * <p>
 * Represents ffxFX.currency_convert_refund_interbank() call.
 * 
 * @see FXInterbankPlaceHolder
 * @see CompositePlaceHolderResolver
 * @see //CompositePlaceHolderResolverTest
 */
@Singleton
public class FXInterbankPlaceHolderResolver extends AbstractPlaceHolderResolver<FXInterbankPlaceHolder> {

	@Inject
	private FFXBridge ffxBridge;

	@Override
	public boolean shouldResolve(String placeholder) {
        return placeholder.startsWith(FXInterbankPlaceHolder.FX_INTERBANK_PLACEHOLDER_PREFIX);
	}

	@Override
    protected FXInterbankPlaceHolder getPlaceHolderInstance(String placeholder) {
        return new FXInterbankPlaceHolder(placeholder);
	}

	/**
	 * Get FX response set in FXPlaceHolder
	 * 
	 * @param //context
	 * @param //placeholder
	 */
	@Override
    public void setPlaceHolderValues(FXInterbankPlaceHolder placeHolder) {
        /*CurrencyConvertRefundInterbankResponse currencyConvertInterbankResponse = ffxBridge
                .currencyConvertInterBankRefundCustomer(placeHolder.getCurrencyFrom(),
                placeHolder.getAmountFrom(), placeHolder.getCurrencyOut(), placeHolder.isReverse());
        SerializeFxAuditTrailResponse fxAuditTrailResponse = ffxBridge
                .serializeFxAuditTrail(currencyConvertInterbankResponse.getConversionInfoOut().getFxAuditTrail());
        placeHolder.setValues(currencyConvertInterbankResponse, fxAuditTrailResponse);*/
	}
}
