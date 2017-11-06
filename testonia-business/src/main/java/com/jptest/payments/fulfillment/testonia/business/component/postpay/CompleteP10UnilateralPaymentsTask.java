package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.CompleteUnilateralPaymentsForAliasRequest;
import com.jptest.money.CompleteUnilateralPaymentsForAliasResponse;
import com.jptest.money.PaymentCompletionResult;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * Task to support unilateral completion operation for 1.0 txns
 * 
 */
public class CompleteP10UnilateralPaymentsTask extends 
	BasePostPaymentOperationTask<CompleteUnilateralPaymentsForAliasRequest, CompleteUnilateralPaymentsForAliasResponse> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompleteP10UnilateralPaymentsTask.class);
	private static final char ALIAS_TYPE_EMAIL = 'E';
	
	@Inject
	private PaymentServBridge paymentServBridge;

	@Override
	public CompleteUnilateralPaymentsForAliasRequest constructPostPayRequest(
			CompleteUnilateralPaymentsForAliasRequest postPaymentRequest, Context context) {
		postPaymentRequest.setAliasType((byte)ALIAS_TYPE_EMAIL);
		User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
		postPaymentRequest.setAlias(seller.getEmailAddress());
		postPaymentRequest.setAccountNumber(new BigInteger(seller.getAccountNumber()));
		return postPaymentRequest;
	}

	@Override
	public CompleteUnilateralPaymentsForAliasResponse executePostPay(
			CompleteUnilateralPaymentsForAliasRequest postPayRequest, boolean call2pex) {
		Assert.assertNotNull(postPayRequest, this.getClass().getSimpleName() + ".validate() - "
				+ "CompleteUnilateralPaymentsForAliasRequest should not be null");
		final CompleteUnilateralPaymentsForAliasResponse postPayResponse = 
				this.paymentServBridge.completeUnilateralPaymentsForAlias(postPayRequest);
		return postPayResponse;
	}

	@Override
	public void assertPostPayResponse(CompleteUnilateralPaymentsForAliasResponse postPayResponse,
			PostPaymentRequest postPayRequest, Context context) {
		Assert.assertNotNull(postPayResponse, this.getClass().getSimpleName() + ".validate() - "
				+ "CompleteUnilateralPaymentsForAliasResponse should not be null");

		for (PaymentCompletionResult paymentCompletionResult:postPayResponse.getPaymentCompletionResult()) {
			Assert.assertEquals(paymentCompletionResult.getResult(), Integer.valueOf("0"), 
					this.getClass().getSimpleName() + ".validate() - "
							+ "CompleteUnilateralPaymentsForAliasResponse status should be zero");
		}
		LOGGER.info("CompleteUnilateralPaymentsForAlias Operation Passed");
	}

}
