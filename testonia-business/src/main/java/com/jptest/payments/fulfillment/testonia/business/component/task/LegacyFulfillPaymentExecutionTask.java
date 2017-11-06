package com.jptest.payments.fulfillment.testonia.business.component.task;

import com.jptest.money.FulfillPaymentRequest;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServCABridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import org.testng.Assert;

import javax.inject.Inject;
import java.util.function.Function;

public class LegacyFulfillPaymentExecutionTask extends BaseTask<FulfillPaymentResponse> {

    @Inject
    private PaymentServCABridge paymentservCABridge;
    private Function<Context, FulfillPaymentRequest> fulfillPaymentRequestBuilder;

    public LegacyFulfillPaymentExecutionTask() { }

    public LegacyFulfillPaymentExecutionTask(Function<Context, FulfillPaymentRequest> requestBuilder) {
        fulfillPaymentRequestBuilder = requestBuilder;
    }

    @Override
    public final FulfillPaymentResponse process(final Context context) {
        FulfillPaymentRequest request;
        try {
            if (fulfillPaymentRequestBuilder != null) {
                request = fulfillPaymentRequestBuilder.apply(context);
            } else {
                request = (FulfillPaymentRequest) this.getDataFromContext(context,
                        ContextKeys.FULFILL_PAYMENT_REQUEST_KEY.getName());
            }
            Assert.assertNotNull(request);
            FulfillPaymentResponse response = this.paymentservCABridge.fulfillPayment(request);
            return response;
        } catch (final Exception e) {
            throw new TestExecutionException("FulfillPaymentExecutionTask execution failed with error " + e.getMessage(), e);
        }
    }
}
