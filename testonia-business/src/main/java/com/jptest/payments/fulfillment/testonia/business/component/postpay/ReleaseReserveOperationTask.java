package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.ReleaseReserveRequest;
import com.jptest.money.ReleaseReserveResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentSideReferenceExistsValidation;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;


/**
 * ReleaseReserveOperationTask takes care of constructing the releaseReserveRequest and executing release_reserve and
 * validates response
 *
 * @JP Inc.
 */
public class ReleaseReserveOperationTask extends
        BasePostPaymentOperationTask<ReleaseReserveRequest, ReleaseReserveResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseReserveOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private PopulateActivityIdHelper populateActivityIdHelper;

    private static final String ACTIVITY_TYPE = "ReleaseHoldActivity";

    @Override
    public ReleaseReserveRequest constructPostPayRequest(
            final ReleaseReserveRequest releaseReserveRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);

        final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName());

        releaseReserveRequest.setIdempotencyString(activityId.toString());
        releaseReserveRequest.getReserveReleaseRequest().get(0)
                .setTransactionHandle(fulfillResponse.getFulfillmentHandle());

        return releaseReserveRequest;
    }

    @Override
    public ReleaseReserveResponse executePostPay(final ReleaseReserveRequest releaseReserveRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(releaseReserveRequest);
        final ReleaseReserveResponse ReleaseReserveResponse = this.paymentServBridge
                .releaseReserve(releaseReserveRequest);
        return ReleaseReserveResponse;
    }

    @Override
    public void assertPostPayResponse(final ReleaseReserveResponse post_pay_response,
            final PostPaymentRequest postPayRequest,
            final Context context) {
        Assert.assertNotNull(post_pay_response,
                "release reserve response should not be null");

        Assert.assertEquals(post_pay_response.getSuccess().booleanValue(), true,
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed:");

        if (post_pay_response.getReserveReleaseHandle() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), post_pay_response.getReserveReleaseHandle());

        LOGGER.info("Release reserve - Encrypted Txn Id - {}", post_pay_response.getReserveReleaseHandle());
        LOGGER.info("Release reserve Operation Passed");

    }

    @Override
    protected void populateActivityId(final Context context, final ReleaseReserveResponse response) {
        if (response.getSuccess()) {
            final String decryptedSubBalanceHandle = this.populateActivityIdHelper
                    .getdecryptedSubBalanceHandle(response.getReserveReleaseHandle());
            final PaymentSideReferenceExistsValidation paymentSideReferenceExistsValidation = new PaymentSideReferenceExistsValidation(
                    decryptedSubBalanceHandle);
            paymentSideReferenceExistsValidation.validate(context);
            final BigInteger activityId = this.populateActivityIdHelper
                    .getActivityIdFromSubBalanceHandle(decryptedSubBalanceHandle, ACTIVITY_TYPE);
            Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
            		+ ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(), activityId);
            LOGGER.info("RELEASE_RESERVE_ACTIVITY_ID: Activity ID - {}, Release_Reserve Handle - {}", activityId,
                    decryptedSubBalanceHandle);
        }

    }

}
