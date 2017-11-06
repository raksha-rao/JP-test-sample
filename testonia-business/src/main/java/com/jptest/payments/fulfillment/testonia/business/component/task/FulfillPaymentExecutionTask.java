package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import org.testng.Assert;

import com.jptest.money.FulfillPaymentRequest;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.payments.fulfillment.testonia.bridge.TxnFulfillmentServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * this task is on deprecation path in favour of FulfillPaymentTask
 * @JP Inc.
 *
 */
@Deprecated
public class FulfillPaymentExecutionTask extends BaseTask<FulfillPaymentResponse> {

    @Inject
    private TxnFulfillmentServBridge txnFulfillmentServBridge;

    @Override
    public final FulfillPaymentResponse process(final Context context) {
        try {
            final FulfillPaymentRequest request = (FulfillPaymentRequest) this.getDataFromContext(context,
                    ContextKeys.FULFILL_PAYMENT_REQUEST_KEY.getName());
            Assert.assertNotNull(request);
            return this.txnFulfillmentServBridge.fulfillPayment(request);
        } catch (final Exception e) {
            throw new TestExecutionException("Failed executing FulfillPaymentExecutionTask", e);
        }
    }
}
