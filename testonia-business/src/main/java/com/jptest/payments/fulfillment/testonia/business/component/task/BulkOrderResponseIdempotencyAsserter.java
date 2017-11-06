package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.testng.Assert;

import com.jptest.money.FulfillBulkOrderResponse;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * Compares idempotence response for fulfill_bulk_order operation
 */
public class BulkOrderResponseIdempotencyAsserter extends BaseAsserter {
	
	@Override
	public void validate(Context testContext) {
		FulfillBulkOrderResponse originalResponse = (FulfillBulkOrderResponse) getDataFromContext(testContext, 
        		ContextKeys.FULFILL_BULKORDER_RESPONSE_KEY.getName());
		FulfillBulkOrderResponse idempotentResponse = (FulfillBulkOrderResponse) getDataFromContext(testContext, 
        		ContextKeys.BULKORDER_IDEMPOTENT_RESPONSE_KEY.getName());
		
		for (int i = 0; i < originalResponse.getPurchaseUnitToOrderMap().size(); i++) {
	        Assert.assertEquals(originalResponse.getPurchaseUnitToOrderMap().get(i).getOrderHandle(), 
	        		idempotentResponse.getPurchaseUnitToOrderMap().get(i).getOrderHandle(),
	        		this.getClass().getSimpleName() + ".validate() failed while "
	        				+ "validating order handle in the idempotent response");
		}
		
        Assert.assertEquals(originalResponse.getStatus(), idempotentResponse.getStatus(), 
        		this.getClass().getSimpleName() + ".validate() failed while validating status idempotent response");
        Assert.assertEquals(originalResponse.getDeclineReason().getFulfillmentDeclineCode(), 
        		idempotentResponse.getDeclineReason().getFulfillmentDeclineCode(), 
        		this.getClass().getSimpleName() + ".validate() failed while validating "
        				+ "decline code in the idempotent response");
        
        Assert.assertTrue(idempotentResponse.getIsDuplicate(), this.getClass().getSimpleName() + ".validate() "
        		+ "failed while validating duplicate request is set to true");
        Assert.assertTrue(idempotentResponse.getDebugLog().startsWith("Idempotency violated for client ID"), 
        		this.getClass().getSimpleName() + ".validate() failed while validating debug log idempotent response");
	}

}
