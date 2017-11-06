package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.CancelPendingPaymentReversalRequest;
import com.jptest.money.CancelPendingPaymentReversalResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.EngineActivityIdRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentReadServRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * CancelPendingReversalOperationTask takes care of constructing the CancelPendingPaymentReversalRequest and executing
 * cancel pending payment and validates the response
 *
 * @JP Inc.
 */
public class CancelPendingReversalOperationTask
        extends
        BasePostPaymentOperationTask<CancelPendingPaymentReversalRequest, CancelPendingPaymentReversalResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CancelPendingReversalOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private PostPaymentExecService postPaymentService;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private TransactionHelper transactionHelper;
    @Inject
    private PaymentReadServRetrieverTask paymentReadServRetrieverTask;
    @Inject
    private CryptoUtil cryptoUtil;

    @Override
    public CancelPendingPaymentReversalRequest constructPostPayRequest(
            final CancelPendingPaymentReversalRequest cancelPendingPaymentReversalRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String accountNumber = seller.getAccountNumber();

        cancelPendingPaymentReversalRequest
                .setOperationIdempotencyData(operationIdempotencyVO);
        final List<WTransactionDTO> pendingTransactions = this.wTxnDAO
                .getTransactionsByAccountNumberAndTypeAndStatus(
                        new BigInteger(accountNumber), String.valueOf(WTransactionConstants.Type.REVERSAL.getValue()),
                        String.valueOf(WTransactionConstants.Status.PENDING.getValue()));

        final WTransactionDTO pendingReversalTransaction = this.transactionHelper
                .getPendingReversalTransaction(pendingTransactions);

        cancelPendingPaymentReversalRequest
                .setTransactionId(pendingReversalTransaction.getId());

        cancelPendingPaymentReversalRequest
                .setOperationIdempotencyData(operationIdempotencyVO);

        return cancelPendingPaymentReversalRequest;
    }

    @Override
    public CancelPendingPaymentReversalResponse executePostPay(
            final CancelPendingPaymentReversalRequest cancelPendingPaymentReversalRequest, final boolean call2PEX) {
        Assert.assertNotNull(cancelPendingPaymentReversalRequest);

        return this.postPaymentService.cancelPendingReversalService(cancelPendingPaymentReversalRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final CancelPendingPaymentReversalResponse cancelPendingPaymentReversalResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(cancelPendingPaymentReversalResponse,
                "Cancel Pending Payment Response should not be null");
        Assert.assertEquals(cancelPendingPaymentReversalResponse.getResult().toString(), postPayRequest.getReturnCode(),
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (cancelPendingPaymentReversalResponse.getEncryptedReversalTransactionId() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    cancelPendingPaymentReversalResponse.getEncryptedReversalTransactionId());

        LOGGER.info("Cancel Pending Reversal - Encrypted Txn Id - {}",
                cancelPendingPaymentReversalResponse.getEncryptedReversalTransactionId());
        LOGGER.info("Cancel Pending Reversal Operation Passed");
    }

    @Override
    protected void populateActivityId(Context context, CancelPendingPaymentReversalResponse response) {

        final BigInteger transactionId = response.getReversalTransId();
        if (transactionId == null) {
            LOGGER.warn("Transaction ID is not present in ReversePaymentResponse");
            return;
        }
        final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(transactionId);
        final BigInteger activityId = activityIdRetriever.process(context);

        context.setData(this.getActivityIdKey(), activityId);
        LOGGER.info("CANCEL_PENDING_REVERSAL_ACTIVITY_ID: Activity ID - {}, Transaction ID - {}", activityId,
                transactionId);

    }

    @Override
    protected void populateIpnEncryptedId(Context context, CancelPendingPaymentReversalResponse response) {

        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse =
                paymentReadServRetrieverTask.execute(context);

        List<WTransactionVO> wTransactionVOList = getLegacyEquivalentByPaymentReferenceResponse
                .getLegacyEquivalent().getWtransactions();

        WTransactionVO wTransactionVO = null;

        for (WTransactionVO wtxnVO : wTransactionVOList) {
            if (wtxnVO.getEncryptedId().equals(response.getEncryptedReversalTransactionId())) {
                wTransactionVO = wtxnVO;
                break;
            }
        }

        if (wTransactionVO == null) return;

        try {
            String encryptedParentId = this.cryptoUtil.encryptTxnId(
                    wTransactionVO.getParentId().longValue(),
                    wTransactionVO.getAccountNumber().longValue());

            context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(), encryptedParentId);
        } catch (Exception e) {
            throw new TestExecutionException("Encryption of Parent ID failed", e);
        }
    }
}
