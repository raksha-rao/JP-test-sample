package com.jptest.payments.fulfillment.testonia.business.component.task;

import com.jptest.money.FulfillBulkOrderRequest;
import com.jptest.money.FulfillBulkOrderResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServCABridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;

import javax.inject.Inject;
import java.util.function.Function;

/**
@JP Inc.
 * <p>
 * Task to fulfill a bulk order request.
 * Calls paymentserv_ca's fulfill_bulk_order API
 */
public class FulfillBulkOrderTask extends BaseTask<FulfillBulkOrderResponse> {

    @Inject
    private PaymentServCABridge bridge;

    private Function<Context, FulfillBulkOrderRequest> fulfillBulkOrderRequestBuilder;

    public FulfillBulkOrderTask(Function<Context, FulfillBulkOrderRequest> requestBuilder) {
        fulfillBulkOrderRequestBuilder = requestBuilder;
    }

    @Override
    public FulfillBulkOrderResponse process(Context context) {
        FulfillBulkOrderRequest rq = fulfillBulkOrderRequestBuilder.apply(context);
        FulfillBulkOrderResponse response = bridge.processFulfillBulkOrder(rq);
        return response;
    }
}
