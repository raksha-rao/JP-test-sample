package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.RefundPaymentRequest;
import com.jptest.money.RefundPaymentResponse;
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
 * RefundPaymentOperationTask takes care of constructing the refund request , executes and validates teh response
 *
 * @JP Inc.
 */
public class RefundPaymentOperationTask
        extends BasePostPaymentOperationTask<RefundPaymentRequest, RefundPaymentResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefundPaymentOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private PostPaymentExecService postPaymentService;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private PaymentReadServRetrieverTask paymentReadServRetrieverTask;

    private boolean forceRefund;

    @Override
    public RefundPaymentRequest constructPostPayRequest(final RefundPaymentRequest refundPaymentRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String accountNumber = seller.getAccountNumber();
        final List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndType(
                new BigInteger(accountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()));
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        operationIdempotencyVO.setIdempotencyString(activityId.toString());

        if (refundPaymentRequest.getOperationIdempotencyData() != null
                && refundPaymentRequest.getOperationIdempotencyData().getActivityId() == BigInteger.ZERO) {
            operationIdempotencyVO.setActivityId(null);
        }

        if (refundPaymentRequest.getOperationIdempotencyData() != null
                && refundPaymentRequest.getOperationIdempotencyData().getIdempotencyString() == null) {
            operationIdempotencyVO.setIdempotencyString(null);
        }

        if (refundPaymentRequest.getOperationIdempotencyData() != null
                && refundPaymentRequest.getOperationIdempotencyData().getIdempotencyString() != null) {
            operationIdempotencyVO
                    .setIdempotencyString(refundPaymentRequest.getOperationIdempotencyData().getIdempotencyString()
                            + "_" + accountNumber);
        }

        refundPaymentRequest.setOperationIdempotencyData(operationIdempotencyVO);
        refundPaymentRequest
                .setPaymentTransactionId(new BigInteger(txnId.get(0).toString()));
        refundPaymentRequest.getBusinessContext()
                .setActorId(new BigInteger(accountNumber));
        return refundPaymentRequest;
    }

    @Override
    public RefundPaymentResponse executePostPay(final RefundPaymentRequest refundPaymentRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(refundPaymentRequest);
        this.forceRefund = refundPaymentRequest.getForceRefund();
        return this.postPaymentService.refundPaymentService(refundPaymentRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final RefundPaymentResponse refundPaymentResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        final RefundPaymentResponse response = refundPaymentResponse;
        Assert.assertNotNull(response,
                "post payment response should not be null");

        Assert.assertEquals(response.getResult().toString(), postPayRequest.getReturnCode(),
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (refundPaymentResponse.getEncryptedRefundTransactionId() != null) {
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    refundPaymentResponse.getEncryptedRefundTransactionId());
        }

        LOGGER.info("Refund - Encrypted Txn Id - {}", refundPaymentResponse.getEncryptedRefundTransactionId());
        LOGGER.info("Refund Payment Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final RefundPaymentResponse response) {
        if (response.getResult() == 0) {
            final BigInteger transactionId = response.getRefundTransId();
            Assert.assertNotNull(transactionId, this.getClass().getSimpleName()
                    + ". populateActivityId() - Found null transactionId");
            final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(transactionId);
            final BigInteger activityId = activityIdRetriever.process(context);
            Assert.assertNotNull(activityId, this.getClass().getSimpleName()
                    + ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(), activityId);
            // context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), transactionId.toString());

            LOGGER.info("REFUND_ACTIVITY_ID: Activity ID - {}, Transaction ID - {}", activityId, transactionId);
        }
    }

    /**
     * When forceRefund is set as true for refund_payment request, it gives buyer side encrypted id in the response. But
     * the IPN is stored using seller side encrypted id.
     *
     * @param context
     * @param response
     */
    @Override
    protected void populateIpnEncryptedId(final Context context, final RefundPaymentResponse response) {

        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        if (response.getEncryptedRefundTransactionId() != null) {
            String encryptedTxnId = response.getEncryptedRefundTransactionId();
            if (this.forceRefund) {
                final GetLegacyEquivalentByPaymentReferenceResponse prsResponse = this.paymentReadServRetrieverTask
                        .execute(context);
                final List<WTransactionVO> wTransactionVOList = prsResponse.getLegacyEquivalent().getWtransactions();

                for (final WTransactionVO wTransactionVO : wTransactionVOList) {
                    if (wTransactionVO.getType() == WTransactionConstants.Type.REVERSAL.getValue() &&
                            wTransactionVO.getSubtype() == WTransactionConstants.Subtype.USERUSER_POSTAGE.getByte()) {
                        encryptedTxnId = wTransactionVO.getEncryptedId();
                        break;
                    }
                }
            }
            context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(), encryptedTxnId);
        }
    }
}
