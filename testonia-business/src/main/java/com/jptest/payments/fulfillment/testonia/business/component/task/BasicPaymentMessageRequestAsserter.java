package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.testng.Assert;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;

/**
 * Validating the request is whether the object of PamentMessage.
 */
public class BasicPaymentMessageRequestAsserter extends BaseAsserter {

	@Override
	public void validate(Context testContext) {
		Object obj = getDataFromContext(testContext, ContextKeys.PAYMENT_MESSAGE_REQUEST_KEY.getName());
		Assert.assertTrue(obj instanceof PaymentMessage);
	}

}
