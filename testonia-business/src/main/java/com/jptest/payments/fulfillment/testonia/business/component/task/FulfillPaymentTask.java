package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.util.function.Function;
import javax.inject.Inject;
import com.jptest.money.FulfillPaymentRequest;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.payments.fulfillment.testonia.bridge.TxnFulfillmentServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;

/**
 * FulfillPaymentTask calls fulfill_payment operation on txnfulfillmentserv 
 * with request built using provided Function reference provided by the test.
 * 
 * This task is stateful and expected to be created within a test case. It 
 * replaces FulfillPaymentExecutionTask.
 * 
 * @JP Inc.
 *
 * Function reference expects a method that will accept Context as param and
 * returns FulfillPaymentResponse.  Request is built using data available in 
 * context.
 *
 */
public class FulfillPaymentTask extends BaseTask<FulfillPaymentResponse> {

    @Inject
    private TxnFulfillmentServBridge bridge;
    
    private Function<Context, FulfillPaymentRequest> fulfillPaymentRequestBuilder;
    
    
    public FulfillPaymentTask(Function<Context, FulfillPaymentRequest> requestBuilder) {
        fulfillPaymentRequestBuilder = requestBuilder;
    }
    

    @Override
    public FulfillPaymentResponse process(Context context) {
        FulfillPaymentRequest rq = fulfillPaymentRequestBuilder.apply(context);
        return bridge.fulfillPayment(rq);
    }

}
