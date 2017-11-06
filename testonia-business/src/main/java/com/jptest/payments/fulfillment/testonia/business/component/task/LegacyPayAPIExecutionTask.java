package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.util.function.Function;

import javax.inject.Inject;

import com.jptest.money.PayRequest;
import com.jptest.money.PayResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;

/**
 * PayOperationTask calls pay operation on paymentserv with request built using provided Function
 * reference provided by the test
 * 
 * Function reference expects a method that will accept Context as param and returns
 *         PayRequest. Request is built using data available in context.
 */
public class LegacyPayAPIExecutionTask extends BaseTask<PayResponse> {

    @Inject
    private PaymentServBridge paymentSerBridge;

    private Function<Context, PayRequest> requestBuilder;

    public LegacyPayAPIExecutionTask(Function<Context, PayRequest> requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @Override
    public PayResponse process(Context context) {
        PayRequest rq = requestBuilder.apply(context);
        PayResponse response = paymentSerBridge.pay(rq);
        return response;
    }
}
