package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.InitiateRefundNegotiationRequest;
import com.jptest.money.InitiateRefundNegotiationResponse;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;


public class InitiateRefundNegotiationTask
        extends BasePostPaymentOperationTask<InitiateRefundNegotiationRequest, InitiateRefundNegotiationResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitiateRefundNegotiationRequest.class);

    @Inject
    private PostPaymentExecService postPaymentService;

    @Override
    public InitiateRefundNegotiationRequest constructPostPayRequest(
            final InitiateRefundNegotiationRequest initiateRefundNegotiationRequest,
            final Context context) {

        final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());

        // This API is not OperationIdempotency hence not constructing the idempotency
        initiateRefundNegotiationRequest.setPaymentHandle(fulfillResponse.getFulfillmentHandle());

        return initiateRefundNegotiationRequest;

    }

    @Override
    public InitiateRefundNegotiationResponse executePostPay(
            final InitiateRefundNegotiationRequest initiateRefundNegotiationRequest, final boolean call2PEX) {

        Assert.assertNotNull(initiateRefundNegotiationRequest);

        return this.postPaymentService.initiateRefundNegotiationService(initiateRefundNegotiationRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final InitiateRefundNegotiationResponse post_pay_response,
            final PostPaymentRequest postPayRequest, final Context context) {

        Assert.assertNotNull(post_pay_response,
                "initiate refund negotiation operation response should not be null");

        Assert.assertEquals(post_pay_response.getResult().getSuccess().toString(), postPayRequest.getReturnCode(),
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        LOGGER.info("Initiate Refund Negotiation Operation Passed");

    }
}
