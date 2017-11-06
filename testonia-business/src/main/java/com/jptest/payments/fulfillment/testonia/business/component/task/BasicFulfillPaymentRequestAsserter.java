package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.testng.Assert;

import com.jptest.money.FulfillPaymentRequest;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

public class BasicFulfillPaymentRequestAsserter extends BaseAsserter {

    @Override
    public void validate(final Context testContext) {
        final Object obj = this.getDataFromContext(testContext, ContextKeys.FULFILL_PAYMENT_REQUEST_KEY.getName());
        Assert.assertTrue(obj instanceof FulfillPaymentRequest);
        final FulfillPaymentRequest request = (FulfillPaymentRequest) obj;
        Assert.assertNotNull(request.getFulfillmentContext());
    }

}
