package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.ReversePaymentRequest;
import com.jptest.money.ReversePaymentResponse;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.EngineActivityIdRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentReadServRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * ReversePaymentOperationTask takes care of constructing the Reverse Payment Request and executing reverse payment and
 * validates the response
 *
 * @JP Inc.
 */
public class ReversePaymentOperationTask
        extends BasePostPaymentOperationTask<ReversePaymentRequest, ReversePaymentResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReversePaymentOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private PostPaymentExecService postPaymentService;
    @Inject
    private PaymentReadServRetrieverTask paymentReadServRetrieverTask;

    @Override
    public ReversePaymentRequest constructPostPayRequest(final ReversePaymentRequest reversePaymentRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String accountNumber = seller.getAccountNumber();
        List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndType(
                new BigInteger(accountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()));

        // Check if buyer side txn is available e.g. to handle cases like unilateral txns
        if (txnId.size() == 0) {
            final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
            txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndType(
                    new BigInteger(buyer.getAccountNumber()),
                    String.valueOf(WTransactionConstants.Type.USERUSER.getValue()));
        }

        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        operationIdempotencyVO.setIdempotencyString(activityId.toString());

        if (reversePaymentRequest.getOperationIdempotencyData() != null
                && reversePaymentRequest.getOperationIdempotencyData().getActivityId() == BigInteger.ZERO) {
            operationIdempotencyVO.setActivityId(null);
        }

        if (reversePaymentRequest.getOperationIdempotencyData() != null
                && reversePaymentRequest.getOperationIdempotencyData().getIdempotencyString() == null) {
            operationIdempotencyVO.setIdempotencyString(null);
        }

        if (reversePaymentRequest.getOperationIdempotencyData() != null
                && reversePaymentRequest.getOperationIdempotencyData().getIdempotencyString() != null) {
            operationIdempotencyVO
                    .setIdempotencyString(reversePaymentRequest.getOperationIdempotencyData().getIdempotencyString()
                            + "_" + accountNumber);
        }

        reversePaymentRequest.setOperationIdempotencyData(operationIdempotencyVO);
        reversePaymentRequest
                .setPaymentTransactionId(new BigInteger(txnId.get(0).toString()));
        reversePaymentRequest.getBusinessContext()
                .setActorId(new BigInteger(accountNumber));
        return reversePaymentRequest;
    }

    @Override
    public ReversePaymentResponse executePostPay(final ReversePaymentRequest reversePaymentRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(reversePaymentRequest);
        return this.postPaymentService.reversePaymentService(reversePaymentRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final ReversePaymentResponse reversePaymentResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        final ReversePaymentResponse response = reversePaymentResponse;
        Assert.assertNotNull(response,
                "post payment response should not be null");
        Assert.assertEquals(response.getResult().toString(), postPayRequest.getReturnCode(),
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (reversePaymentResponse.getEncryptedReversalTransactionId() != null) {
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    reversePaymentResponse.getEncryptedReversalTransactionId());
        }

        LOGGER.info("Reverse Payment - Encrypted Txn Id - {}",
                reversePaymentResponse.getEncryptedReversalTransactionId());
        LOGGER.info("Reverse Payment Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final ReversePaymentResponse response) {
        BigInteger transactionId = response.getReversalTransId();

        if (transactionId == null) {

            boolean reverasalRecordFound = false;

            /*
             * Use case: 1. Initial payment is under compliance hold 2. Payment is denied and funds are moved to seller
             * This will create a reversal row on Payer side and Denied row on Payee side. There won't be any Reversal
             * row on Payee side, hence no ReversalTransactionid in response. In that case get the buyer side txn id to
             * fetch the activity id
             */

            final GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse = this.paymentReadServRetrieverTask
                    .execute(context);
            final List<WTransactionVO> wTransactionVOList = getLegacyEquivalentByPaymentReferenceResponse
                    .getLegacyEquivalent().getWtransactions();
            for (final WTransactionVO wTransactionVO : wTransactionVOList) {
                if (wTransactionVO.getType() == WTransactionConstants.Type.REVERSAL.getValue()
                        && wTransactionVO.getSubtype() == WTransactionConstants.Subtype.USERUSER_POSTAGE.getValue()) {
                    transactionId = wTransactionVO.getId();
                    Assert.assertNotNull(transactionId, this.getClass().getSimpleName()
                            + ".validate() failed for transaction id should not be Null");
                    reverasalRecordFound = true;
                    break;
                }
            }

            /*
             * if reversal record is not found, we assume it to be denied transaction and return without populating
             * activity Id
             */
            if (!reverasalRecordFound) {
                return;
            }
            LOGGER.warn(
                    "Transaction ID is not present in ReversePaymentResponse. Using buyer side V row txn id [{}] to fetch Activity id",
                    transactionId);
        }

        Assert.assertNotNull(transactionId, this.getClass().getSimpleName()
                + ". populateActivityId() - Found null transactionId");
        final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(transactionId);
        final BigInteger activityId = activityIdRetriever.process(context);
        Assert.assertNotNull(activityId, this.getClass().getSimpleName()
                + ". populateActivityId() - Found null activityId");
        context.setData(this.getActivityIdKey(), activityId);
        LOGGER.info("REVERSAL_ACTIVITY_ID: Activity ID - {}, Transaction ID - {}", activityId,
                transactionId);

    }

    @Override
    protected void populateIpnEncryptedId(final Context context, final ReversePaymentResponse response) {
        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        if (response.getEncryptedReversalTransactionId() != null) {
            context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(), response.getEncryptedReversalTransactionId());
        }
    }
}
