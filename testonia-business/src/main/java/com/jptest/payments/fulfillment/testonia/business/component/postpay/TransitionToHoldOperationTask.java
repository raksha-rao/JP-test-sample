package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.TransitionToHoldRequest;
import com.jptest.money.TransitionToHoldResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentSideReferenceExistsValidation;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;


/**
 * TransitionToHoldOperationTask takes care of constructing the transitiontohold Request and executing
 * transition_to_hold and validates the response
 *
 * @JP Inc.
 */
public class TransitionToHoldOperationTask
        extends
        BasePostPaymentOperationTask<TransitionToHoldRequest, TransitionToHoldResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransitionToHoldOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private PopulateActivityIdHelper populateActivityIdHelper;

    private static final String ACTIVITY_TYPE = "TransitionHoldActivity";

    @Override
    public TransitionToHoldRequest constructPostPayRequest(
            final TransitionToHoldRequest transitionToHoldRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());

        transitionToHoldRequest.setIdempotencyString(activityId.toString());
        transitionToHoldRequest.setTransactionHandle(fulfillResponse.getFulfillmentHandle());

        return transitionToHoldRequest;
    }

    @Override
    public TransitionToHoldResponse executePostPay(
            final TransitionToHoldRequest TransitionToHoldRequest, final boolean call2PEX) {
        Assert.assertNotNull(TransitionToHoldRequest);
        final TransitionToHoldResponse TransitionToHoldResponse = this.paymentServBridge
                .transitionToHold(TransitionToHoldRequest);
        return TransitionToHoldResponse;
    }

    @Override
    public void assertPostPayResponse(final TransitionToHoldResponse transitionToHoldResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(transitionToHoldResponse,
                "Transition to Hold response should not be null");
        Assert.assertEquals(transitionToHoldResponse.getSuccess().booleanValue(), true,
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (transitionToHoldResponse.getHoldHandle() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), transitionToHoldResponse.getHoldHandle());

        LOGGER.info("Transition to hold - Encrypted Txn Id - {}", transitionToHoldResponse.getHoldHandle());
        LOGGER.info("Transition to hold Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final TransitionToHoldResponse response) {
        if (response.getSuccess()) {
            final String decryptedSubBalanceHandle = this.populateActivityIdHelper
                    .getdecryptedSubBalanceHandle(response.getHoldHandle());
            final PaymentSideReferenceExistsValidation paymentSideReferenceExistsValidation = new PaymentSideReferenceExistsValidation(
                    decryptedSubBalanceHandle);
            paymentSideReferenceExistsValidation.validate(context);
            final BigInteger activityId = this.populateActivityIdHelper
                    .getActivityIdFromSubBalanceHandle(decryptedSubBalanceHandle, ACTIVITY_TYPE);
            Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
            		+ ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(), activityId);
            LOGGER.info("TRANSITION_TO_HOLD_ACTIVITY_ID: Activity ID - {}, Transition_To_Hold Handle - {}", activityId,
                    response.getHoldHandle());
        }

    }

}
