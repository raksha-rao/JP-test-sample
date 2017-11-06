package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.CompletePendingPaymentReversalRequest;
import com.jptest.money.CompletePendingPaymentReversalResponse;
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
 * CompletePendingReversalOperationTask takes care of constructing the CompletePendingPaymentReversalRequest and
 * executing complete_pending_payment_reversal and validates the response
 *
 * @JP Inc.
 */
public class CompletePendingReversalOperationTask
        extends
        BasePostPaymentOperationTask<CompletePendingPaymentReversalRequest, CompletePendingPaymentReversalResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompletePendingReversalOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private TransactionHelper transactionHelper;
    @Inject
    private PostPaymentExecService postPaymentService;
    @Inject
    private PaymentReadServRetrieverTask paymentReadServRetrieverTask;
    @Inject
    private CryptoUtil cryptoUtil;

    @Override
    public CompletePendingPaymentReversalRequest constructPostPayRequest(
            final CompletePendingPaymentReversalRequest completePendingPaymentRevversalRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String accountNumber = seller.getAccountNumber();

        completePendingPaymentRevversalRequest
                .setOperationIdempotencyData(operationIdempotencyVO);
        final List<WTransactionDTO> pendingTransactions = this.wTxnDAO
                .getTransactionsByAccountNumberAndTypeAndStatus(
                        new BigInteger(accountNumber), String.valueOf(WTransactionConstants.Type.REVERSAL.getValue()),
                        String.valueOf(WTransactionConstants.Status.PENDING.getValue()));

        final WTransactionDTO pendingReversalTransaction = this.transactionHelper
                .getPendingReversalTransaction(pendingTransactions);

        completePendingPaymentRevversalRequest
                .setTransactionId(pendingReversalTransaction.getId());

        completePendingPaymentRevversalRequest
                .setOperationIdempotencyData(operationIdempotencyVO);

        return completePendingPaymentRevversalRequest;
    }

    @Override
    public CompletePendingPaymentReversalResponse executePostPay(
            final CompletePendingPaymentReversalRequest completePendingPaymentReversalRequest, final boolean call2PEX) {
        Assert.assertNotNull(completePendingPaymentReversalRequest);

        return this.postPaymentService.completePendingPaymentReversalService(completePendingPaymentReversalRequest,
                call2PEX);
    }

    @Override
    public void assertPostPayResponse(
            final CompletePendingPaymentReversalResponse completePendingPaymentReversalResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(completePendingPaymentReversalResponse,
        		this.getClass().getSimpleName() 
        		+ ". assertPostPayResponse() Complete Pending Payment Response should not be null");

        Assert.assertEquals(completePendingPaymentReversalResponse.getResult().toString(), postPayRequest.getReturnCode(),
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (completePendingPaymentReversalResponse.getEncryptedReversalTransactionId() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    completePendingPaymentReversalResponse.getEncryptedReversalTransactionId());

        LOGGER.info("Complete Pending Reversal - Encrypted Txn Id - {}",
                completePendingPaymentReversalResponse.getEncryptedReversalTransactionId());
        LOGGER.info("Complete Pending Reversal Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final CompletePendingPaymentReversalResponse response) {
        if (response.getResult() == 0) {
            final BigInteger transactionId = response.getReversalTransId();
            Assert.assertNotNull(transactionId, this.getClass().getSimpleName() 
            		+ ". populateActivityId() - Found null transactionId");
            final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(transactionId);
            final BigInteger activityId = activityIdRetriever.process(context);
            Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
            		+ ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(),
                    activityId);
            LOGGER.info("COMPLETE_PENDING_REVERSAL_ACTIVITY_ID: Activity ID - {}, Transaction ID - {}", activityId,
                    transactionId);
        }
    }

    @Override
    protected void populateIpnEncryptedId(Context context, CompletePendingPaymentReversalResponse response) {

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
