package com.jptest.payments.fulfillment.testonia.business.component.postpay.p10;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.CompletePendingPaymentReversalRequest;
import com.jptest.money.CompletePendingPaymentReversalResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionP10DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * CompletePendingReversalOperation10Task takes care of constructing the CompletePendingPaymentReversalRequest and
 * executing complete_pending_payment_reversal and validates the response This task is specific for 1.0 stack
 */
public class CompletePendingReversalOperation10Task
        extends
        BasePostPayment10OperationTask<CompletePendingPaymentReversalRequest, CompletePendingPaymentReversalResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompletePendingReversalOperation10Task.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP10DaoImpl wTxnDAO;
    @Inject
    private TransactionHelper transactionHelper;
    @Inject
    private PostPaymentExecService postPaymentService;

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
                "Complete Pending Payment Response should not be null");

        Assert.assertEquals(completePendingPaymentReversalResponse.getResult().toString(),
                postPayRequest.getReturnCode(),
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (completePendingPaymentReversalResponse.getEncryptedReversalTransactionId() != null) {
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    completePendingPaymentReversalResponse.getEncryptedReversalTransactionId());
        }

        LOGGER.info("Complete Pending Reversal 1.0  - Encrypted Txn Id - {}",
                completePendingPaymentReversalResponse.getEncryptedReversalTransactionId());
        LOGGER.info("Complete Pending Reversal 1.0 Operation Passed");
    }
}
