package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.CloseRefundNegotiationRequest;
import com.jptest.money.CloseRefundNegotiationResponse;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;


public class CloseRefundNegotiationOperationTask
        extends BasePostPaymentOperationTask<CloseRefundNegotiationRequest, CloseRefundNegotiationResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseRefundNegotiationRequest.class);

    @Inject
    private PostPaymentExecService postPaymentService;

    @Override
    public CloseRefundNegotiationRequest constructPostPayRequest(
            final CloseRefundNegotiationRequest closeRefundNegotiationRequest, final Context context) {

        final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());

        // This API is not OperationIdempotency hence not constructing the idempotency
        closeRefundNegotiationRequest.setPaymentHandle(fulfillResponse.getFulfillmentHandle());

        return closeRefundNegotiationRequest;
    }

    @Override
    public CloseRefundNegotiationResponse executePostPay(final CloseRefundNegotiationRequest request,
            final boolean call2PEX) {

        Assert.assertNotNull(request);

        return this.postPaymentService.closeRefundNegotiationService(request, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final CloseRefundNegotiationResponse post_pay_response,
            final PostPaymentRequest postPayRequest, final Context context) {

        Assert.assertNotNull(post_pay_response, "close refund negotiation operation response should not be null");

        Assert.assertEquals(post_pay_response.getResult().getSuccess().toString(), postPayRequest.getReturnCode(), 
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        LOGGER.info("Close Refund Negotiation Operation Passed");

    }

}
