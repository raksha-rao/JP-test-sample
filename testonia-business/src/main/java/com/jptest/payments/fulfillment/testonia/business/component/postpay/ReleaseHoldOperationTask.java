package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.ReleaseHoldRequest;
import com.jptest.money.ReleaseHoldResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentSideReferenceExistsValidation;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;


/**
 * ReleaseHoldOperationTask takes care of constructing the release hold Request and executing release hold and validates
 * the response
 *
 * @JP Inc.
 */
public class ReleaseHoldOperationTask
        extends
        BasePostPaymentOperationTask<ReleaseHoldRequest, ReleaseHoldResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseHoldOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private PopulateActivityIdHelper populateActivityIdHelper;

    private static final String ACTIVITY_TYPE = "ReleaseHoldActivity";

    @Override
    public ReleaseHoldRequest constructPostPayRequest(
            final ReleaseHoldRequest releaseHoldRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);

        final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());

        releaseHoldRequest.setIdempotencyString(activityId.toString());
        releaseHoldRequest.getHoldReleaseRequest().get(0).setTransactionHandle(fulfillResponse.getFulfillmentHandle());

        return releaseHoldRequest;
    }

    @Override
    public ReleaseHoldResponse executePostPay(
            final ReleaseHoldRequest releaseHoldRequest, final boolean call2PEX) {
        Assert.assertNotNull(releaseHoldRequest);
        final ReleaseHoldResponse ReleaseHoldResponse = this.paymentServBridge
                .releaseHold(releaseHoldRequest);
        return ReleaseHoldResponse;
    }

    @Override
    public void assertPostPayResponse(final ReleaseHoldResponse releaseHoldResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(releaseHoldResponse,
                "release hold response should not be null");

        Assert.assertEquals(releaseHoldResponse.getSuccess().booleanValue(), true,
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed:");


        if (releaseHoldResponse.getReleasedHoldHandle() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), releaseHoldResponse.getReleasedHoldHandle());

        LOGGER.info("Release Hold - Encrypted Txn Id - {}", releaseHoldResponse.getReleasedHoldHandle());
        LOGGER.info("Release Hold Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final ReleaseHoldResponse response) {
        if (response.getSuccess()) {
            final String decryptedSubBalanceHandle = this.populateActivityIdHelper
                    .getdecryptedSubBalanceHandle(response.getReleasedHoldHandle());
            final PaymentSideReferenceExistsValidation paymentSideReferenceExistsValidation = new PaymentSideReferenceExistsValidation(
                    decryptedSubBalanceHandle);
            paymentSideReferenceExistsValidation.validate(context);
            final BigInteger activityId = this.populateActivityIdHelper
                    .getActivityIdFromSubBalanceHandle(decryptedSubBalanceHandle, ACTIVITY_TYPE);
            Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
            		+ ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(), activityId);
            LOGGER.info("RELEASE_HOLD_ACTIVITY_ID: Activity ID - {}, Release Handle - {}", activityId,
                    decryptedSubBalanceHandle);
        }

    }
}
