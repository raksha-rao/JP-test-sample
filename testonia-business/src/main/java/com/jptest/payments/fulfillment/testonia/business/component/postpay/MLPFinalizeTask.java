package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.MlpFinalizeRequest;
import com.jptest.money.MlpFinalizeResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServCABridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;

/**
 * Task to support MLP Finalize API invocation, its response processing and response validations
 * 
 */
public class MLPFinalizeTask extends BasePostPaymentOperationTask<MlpFinalizeRequest, MlpFinalizeResponse> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MLPFinalizeTask.class);

	@Inject
	private PaymentServCABridge paymentServCABridge;

	@Override
	public MlpFinalizeRequest constructPostPayRequest(MlpFinalizeRequest mlpFinalizeRequest,
			Context context) {
		List<String> txnHandles = new ArrayList<String>();
		txnHandles.add((String) context.getData(ContextKeys.WTRANSACTION_ID_KEY.getName()));
		mlpFinalizeRequest.setTxnHandles(txnHandles);
		
		return mlpFinalizeRequest;
	}

	@Override
	public MlpFinalizeResponse executePostPay(MlpFinalizeRequest mlpFinalizeRequest,
			boolean call2pex) {
		Assert.assertNotNull(mlpFinalizeRequest);
		final MlpFinalizeResponse mlpFinalizeResponse = this.paymentServCABridge.processMlpFinalize(mlpFinalizeRequest);
		return mlpFinalizeResponse;
	}

	@Override
	public void assertPostPayResponse(MlpFinalizeResponse mlpFinalizeResponse,
			PostPaymentRequest postPayRequest, Context context) {
		Assert.assertNotNull(mlpFinalizeResponse, this.getClass().getSimpleName() + ".validate() - "
				+ "mlpFinalizeResponse should not be null");

		Assert.assertEquals(mlpFinalizeResponse.getResultAsEnum().getName(), postPayRequest.getReturnCode(), 
				this.getClass().getSimpleName() + ".validate() mlpFinalizeResponse status did not match "
						+ "expected value of " + postPayRequest.getReturnCode());
		LOGGER.info("MLP Finalize Operation Passed");
	}

}
