package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.fx.ConversionInfoOutVO;
import com.jptest.payments.fulfillment.testonia.bridge.FFXBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.model.FXRequest;

/**
 * Uses ffx bridge to invoke pricing currency_convert_customer API and fetch the needed ffx info
 */
@Singleton
public class FFXCustomerTask extends BaseTask<ConversionInfoOutVO> {
	 
	@Inject
	private FFXBridge ffxBridge;
	
	private final FXRequest fxRequest;
	
	public FFXCustomerTask(FXRequest fxRequest) {
		this.fxRequest = fxRequest;
	}

	@Override
	public ConversionInfoOutVO process(Context context) {
		return ffxBridge.currencyConvertCustomer(fxRequest.getFromAmount().getCurrencyCode(), 
				fxRequest.getFromAmount().getAmount(), fxRequest.getToCurrency(), fxRequest.getCountry(), 
				fxRequest.isReverse(), fxRequest.getConversionType()).getConversionInfoOut();
	}
	
}
