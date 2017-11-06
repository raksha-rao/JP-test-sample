package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FeePlaceHolder;

import javax.inject.Inject;
import javax.inject.Singleton;

//import com.jptest.ffxfee.CalculateFeeAmountFromResponse;
//import com.jptest.ffxfx.CurrencyConvertInterbankResponse;
//import com.jptest.payments.fulfillment.testonia.bridge.FFXBridge;

/**
 * Resolves placeholder string starting with "${FEE:".
 * <p>
 * Represents ffxFee.calculate_fee_amount_from() operation.
 * 
 * @see FeePlaceHolder
 * @see CompositePlaceHolderResolver
 * @see //CompositePlaceHolderResolverTest
 */
@Singleton
public class FeePlaceHolderResolver extends AbstractPlaceHolderResolver<FeePlaceHolder> {

	@Inject
	//private FFXBridge ffxBridge;

	@Override
	public boolean shouldResolve(String placeholder) {
		return placeholder.startsWith(FeePlaceHolder.FEE_PLACEHOLDER_PREFIX);
	}

	@Override
	protected FeePlaceHolder getPlaceHolderInstance(String placeholder) {
		return new FeePlaceHolder(placeholder);
	}

	/**
	 * Get FEE response and set in FeePlaceHolder
	 * 
	 * @param //context
	 * @param placeholder
	 */
    public void setPlaceHolderValues(FeePlaceHolder placeholder) {

		/*CalculateFeeAmountFromResponse response = ffxBridge.calculateFeeAmountFrom(placeholder.getCurrencyFrom(),
				placeholder.getAmountFrom(), placeholder.getSenderCountry(), placeholder.getCurrencyOut(),
				placeholder.getCountry(), placeholder.getFeeType().toLowerCase(),
                placeholder.getFundingSource().toLowerCase(), placeholder.getChargeBackGrade());
		long usdAmount = response.getFeeAmount().getAmount();
		if (!Currency.Info.US_Dollar.getIsoCurrency().getCurrencyCode()
				.equals(response.getFeeAmount().getCurrencyCode())) {
			CurrencyConvertInterbankResponse usdEquivalentResponse = ffxBridge.getUsdEquivalent(
					response.getFeeAmount().getCurrencyCode(), response.getFeeAmount().getAmount(), false);
			usdAmount = usdEquivalentResponse.getConversionInfoOut().getAmountTo().getAmount();
		}
		placeholder.setValues(response, usdAmount);*/
	}

}
