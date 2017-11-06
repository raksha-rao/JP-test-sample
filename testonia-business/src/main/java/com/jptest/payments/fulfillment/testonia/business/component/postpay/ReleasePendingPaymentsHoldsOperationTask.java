package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.PlanPaymentV2Response;
import com.jptest.money.ReleasePendingPaymentsHoldsRequest;
import com.jptest.money.ReleasePendingPaymentsHoldsResponse;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentReadServRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.test.money.constants.WTransactionConstants;


/**
 * ReleasePendingPaymentHoldsTask takes care of 1. building {@link ReleasePendingPaymentsHoldsRequest} 2. executing
 * release_pending_payments_holds api with the request built 3. Validating the
 * {@link ReleasePendingPaymentsHoldsResponse}
 *
 * @JP Inc.
 */

public class ReleasePendingPaymentsHoldsOperationTask
        extends
        BasePostPaymentOperationTask<ReleasePendingPaymentsHoldsRequest, ReleasePendingPaymentsHoldsResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReleasePendingPaymentsHoldsOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private PostPaymentExecService postPaymentService;

    @Inject
    private PaymentReadServRetrieverTask paymentReadServRetrieverTask;
    @Inject
    private PopulateActivityIdHelper populateActivityIdHelper;

    private String fulfillmentHandle;

    @Override
    public ReleasePendingPaymentsHoldsRequest constructPostPayRequest(
            final ReleasePendingPaymentsHoldsRequest releasePendingPaymentsHoldsRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);

        String paymentResponseKey = ContextKeys.FULFILL_PLAN_RESPONSE_KEY.getName();
        if(paymentResponseKey == null || this.getDataFromContext(context, paymentResponseKey) instanceof
                PlanPaymentV2Response) {
            paymentResponseKey = ContextKeys.FULFILL_PAYMENT_RESPONSE_KEY.getName();
        }
        final FulfillPaymentResponse fulfillResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                paymentResponseKey);

        fulfillmentHandle = fulfillResponse.getFulfillmentHandle();

        final List<String> encryptedPaymentTransactionIdList = new ArrayList<String>();
        encryptedPaymentTransactionIdList
                .add(fulfillResponse.getTransactionUnitStatus().get(0).getTransactionUnitHandle());

        releasePendingPaymentsHoldsRequest.setEncryptedPaymentTransactionId(encryptedPaymentTransactionIdList);

        return releasePendingPaymentsHoldsRequest;
    }

    @Override
    public ReleasePendingPaymentsHoldsResponse executePostPay(
            final ReleasePendingPaymentsHoldsRequest releasePendingPaymentsHoldsRequest, final boolean call2PEX) {

        Assert.assertNotNull(releasePendingPaymentsHoldsRequest,
                "Release Pending Payments Holds request should not be null");

        return this.postPaymentService.releasePendingPaymentsHoldsService(releasePendingPaymentsHoldsRequest,
                call2PEX);
    }

    @Override
    public void assertPostPayResponse(final ReleasePendingPaymentsHoldsResponse releasePendingPaymentsHoldsResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(releasePendingPaymentsHoldsResponse,
                "Release Pending Payments Holds response should not be null");

        Assert.assertEquals(
                releasePendingPaymentsHoldsResponse.getPaymentCompletionResult().get(0).getResult().toString(),
                postPayRequest.getReturnCode(), this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        LOGGER.info("Release Pending Payments Holds Operation Passed");

    }

    @Override
    protected void populateActivityId(final Context context,
            final ReleasePendingPaymentsHoldsResponse postPayResponse) {

        GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse =
                paymentReadServRetrieverTask.execute(context);
        List<WTransactionVO> wTransactionVOList = getLegacyEquivalentByPaymentReferenceResponse
                .getLegacyEquivalent().getWtransactions();

        BigInteger transactionID = BigInteger.ZERO;
        String encryptedTxnId = null;

        // Get seller side 'U' row
        for (WTransactionVO wTransactionVO : wTransactionVOList) {
            if (wTransactionVO.getType() == WTransactionConstants.Type.USERUSER.getValue()
                    && wTransactionVO.getAmount().getAmount() > 0) {
                transactionID = wTransactionVO.getId();
                encryptedTxnId = wTransactionVO.getEncryptedId();
                break;
            }
        }

        if (encryptedTxnId != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), encryptedTxnId);

        final BigInteger activityId = populateActivityIdHelper.getLatestActivityIdForReferenceValue(transactionID.toString());
        Assert.assertNotNull(activityId, this.getClass().getSimpleName() + "activityId should not be null");

        context.setData(this.getActivityIdKey(), activityId);

        LOGGER.info("ReleasePendingPaymentHolds Operation: Activity ID - {}, Transaction ID - {} - {}", activityId,
                transactionID, this.getActivityIdKey());
    }

    @Override
    protected void populateIpnEncryptedId(Context context, ReleasePendingPaymentsHoldsResponse response) {
        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        if (fulfillmentHandle != null)
            context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(), fulfillmentHandle);
    }
}
