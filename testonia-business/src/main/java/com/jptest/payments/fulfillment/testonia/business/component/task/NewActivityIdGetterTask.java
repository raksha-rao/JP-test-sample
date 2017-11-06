package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;

import javax.inject.Inject;

import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;

/**
 * Calls paymentserv and gets a new activity id.
 * 
 */
public class NewActivityIdGetterTask extends BaseTask<BigInteger> {
    @Inject
    private PaymentServBridge paymentServBridge;

    @Override
    public BigInteger process(Context context) {
        return paymentServBridge.createActivityId();
    }
}
