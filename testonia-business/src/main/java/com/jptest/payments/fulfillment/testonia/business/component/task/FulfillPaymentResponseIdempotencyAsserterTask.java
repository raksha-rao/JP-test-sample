package com.jptest.payments.fulfillment.testonia.business.component.task;

import com.jptest.money.FulfillPaymentResponse;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

import org.testng.Assert;

/**
 * Compares idempotence response for fulfill_payment operation
 * 
 */
public class FulfillPaymentResponseIdempotencyAsserterTask extends BaseAsserter {
	 @Override
	    public void validate(Context testContext) {
	        FulfillPaymentResponse originalResponse = (FulfillPaymentResponse) getDataFromContext(testContext, 
	        		ContextKeys.FULFILL_PAYMENT_RESPONSE_KEY.getName());
	        FulfillPaymentResponse idempotentResponse = (FulfillPaymentResponse) getDataFromContext(testContext, 
	        		ContextKeys.IDEMPOTENCY_RESPONSE.getName());
	        Assert.assertEquals(originalResponse.getFulfillmentHandle(), idempotentResponse.getFulfillmentHandle(),
	        		this.getClass().getSimpleName() + ".validate() failed while "
	        				+ "validating fulfillment handle in the idempotent response");
	        Assert.assertEquals(originalResponse.getTransactionUnitStatus().get(0).getHandleDetails()
	        		.getDebitSideHandle(), idempotentResponse.getTransactionUnitStatus().get(0).getHandleDetails()
	        		.getDebitSideHandle(), this.getClass().getSimpleName() + ".validate() failed while "
	        				+ "validating Debit handle in the idempotent response");
	        Assert.assertEquals(originalResponse.getTransactionUnitStatus().get(0).getHandleDetails()
	        		.getCreditSideHandle(), idempotentResponse.getTransactionUnitStatus().get(0).getHandleDetails()
	        		.getCreditSideHandle(), this.getClass().getSimpleName() + ".validate() failed while validating "
	        				+ "credit handle in the idempotent response");
	        Assert.assertEquals(originalResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle(), 
	        		idempotentResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle(),
	        		this.getClass().getSimpleName() + ".validate() failed while validating TXNUH in the "
	        				+ "idempotent response");
	        Assert.assertEquals(originalResponse.getFulfillmentRequestStatusAsEnum(), 
	        		idempotentResponse.getFulfillmentRequestStatusAsEnum(),
	        		this.getClass().getSimpleName() + ".validate() failed while validating status in the "
	        				+ "idempotent response");
	        
	        Assert.assertTrue(idempotentResponse.getIsDuplicatePlan(), this.getClass().getSimpleName() + ".validate() "
	        		+ "failed while validating duplicate plan is set to true");
	        Assert.assertEquals(idempotentResponse.getDebugLog(), "Fulfillment response generated from PRS data "
	        		+ "using Idem-potency token", this.getClass().getSimpleName() + ".validate() failed while "
	        				+ "validating debug log idempotent response");
	    }
	}

