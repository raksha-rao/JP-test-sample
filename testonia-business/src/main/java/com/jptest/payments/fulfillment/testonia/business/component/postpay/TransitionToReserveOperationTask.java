package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.TransitionToReserveRequest;
import com.jptest.money.TransitionToReserveResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentSideReferenceExistsValidation;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;


/**
 * TransitionToReserveOperationTask takes care of constructing the TransitionToReserve request and executing
 * transition_to_reserver and validates the response
 *
 * @JP Inc.
 */
public class TransitionToReserveOperationTask
        extends
        BasePostPaymentOperationTask<TransitionToReserveRequest, TransitionToReserveResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransitionToReserveOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private PopulateActivityIdHelper populateActivityIdHelper;

    private static final String ACTIVITY_TYPE = "TransitionHoldActivity";

    @Override
    public TransitionToReserveRequest constructPostPayRequest(
            final TransitionToReserveRequest transitionToReserveRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());

        transitionToReserveRequest.setIdempotencyString(activityId.toString());
        transitionToReserveRequest.setTransactionHandle(fulfillResponse.getFulfillmentHandle());

        return transitionToReserveRequest;
    }

    @Override
    public TransitionToReserveResponse executePostPay(
            final TransitionToReserveRequest transitionToReserveRequest, final boolean call2PEX) {
        Assert.assertNotNull(transitionToReserveRequest);
        final TransitionToReserveResponse TransitionToReserveResponse = this.paymentServBridge
                .transitionToReserve(transitionToReserveRequest);
        return TransitionToReserveResponse;
    }

    @Override
    public void assertPostPayResponse(final TransitionToReserveResponse transitionToReserveResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(transitionToReserveResponse,
                "Transition to reserve response should not be null");
        Assert.assertEquals(transitionToReserveResponse.getSuccess().toString(), postPayRequest.getReturnCode(),
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (transitionToReserveResponse.getReserveHandle() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    transitionToReserveResponse.getReserveHandle().get(0));

        LOGGER.info("Transition to reserve - Encrypted Txn Id - {}",
                transitionToReserveResponse.getReserveHandle().get(0));
        LOGGER.info("Transition to reserve Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final TransitionToReserveResponse response) {
        if (response.getSuccess()) {
            final String decryptedSubBalanceHandle = this.populateActivityIdHelper
                    .getdecryptedSubBalanceHandle(response.getReserveHandle().get(0));
            final PaymentSideReferenceExistsValidation paymentSideReferenceExistsValidation = new PaymentSideReferenceExistsValidation(
                    decryptedSubBalanceHandle);
            paymentSideReferenceExistsValidation.validate(context);
            final BigInteger activityId = this.populateActivityIdHelper
                    .getActivityIdFromSubBalanceHandle(decryptedSubBalanceHandle, ACTIVITY_TYPE);
            Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
            		+ ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(), activityId);
            LOGGER.info("TRANSITION_TO_RESERVE_ACTIVITY_ID: Activity ID - {}, Transition_To_Reserve Handle - {}",
                    activityId, decryptedSubBalanceHandle);
        }

    }

}
