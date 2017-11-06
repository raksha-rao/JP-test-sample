package com.jptest.payments.fulfillment.testonia.business.component.postpay.p10;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.CreatePendingPaymentReversalRequest;
import com.jptest.money.CreatePendingPaymentReversalResponse;
import com.jptest.money.OperationIdempotencyVO;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionP10DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * CreatePendingReversalOperation10Task takes care of constructing the CreatePendingReversalRequest and executing
 * create_pending_payment_reversal and validates response This task is specific to 1.0 stack
 */
public class CreatePendingReversalOperation10Task
extends
BasePostPayment10OperationTask<CreatePendingPaymentReversalRequest, CreatePendingPaymentReversalResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePendingReversalOperation10Task.class);

    @Inject
    private PostPaymentExecService postPaymentService;
    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP10DaoImpl wTxnDAO;
    private ContextKeys userContextKey = ContextKeys.SELLER_VO_KEY;

    public CreatePendingReversalOperation10Task() {

    }

    public CreatePendingReversalOperation10Task(final ContextKeys userContextKey) {
        this.userContextKey = userContextKey;
    }

    @Override
    public CreatePendingPaymentReversalRequest constructPostPayRequest(
            final CreatePendingPaymentReversalRequest createPendingPaymentRevversalRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        final User seller = (User) context.getData(this.userContextKey.getName());
        final String accountNumber = seller.getAccountNumber();
        final List<WTransactionDTO> transactionInfo = this.wTxnDAO.getTransactionsByAccountNumberAndTypeAndStatus(
                new BigInteger(accountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
                String.valueOf(WTransactionConstants.Status.SUCCESS.getValue()));

        LOGGER.info("Pending Reversal to be created for the Transaction ID - {}",
                transactionInfo.get(0).getId().toString());

        createPendingPaymentRevversalRequest
        .setOperationIdempotencyData(operationIdempotencyVO);
        createPendingPaymentRevversalRequest
        .setPaymentTransactionId(new BigInteger(transactionInfo.get(0).getId().toString()));

        return createPendingPaymentRevversalRequest;
    }

    @Override
    public CreatePendingPaymentReversalResponse executePostPay(
            final CreatePendingPaymentReversalRequest createPendingPaymentReversalRequest, final boolean call2PEX) {
        Assert.assertNotNull(createPendingPaymentReversalRequest);

        return this.postPaymentService.createPendingPaymentReversalService(createPendingPaymentReversalRequest,
                call2PEX);
    }

    @Override
    public void assertPostPayResponse(final CreatePendingPaymentReversalResponse createPendingPaymentReversalResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(createPendingPaymentReversalResponse,
                "createPendingPaymentReversalResponse should not be null");

        Assert.assertEquals(createPendingPaymentReversalResponse.getResult().toString(), postPayRequest.getReturnCode(),
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (createPendingPaymentReversalResponse.getEncryptedReversalTransactionId() != null) {
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    createPendingPaymentReversalResponse.getEncryptedReversalTransactionId());
        }

        LOGGER.info("Create Pending Reversal 1.0  - Encrypted Txn Id - {}",
                createPendingPaymentReversalResponse.getEncryptedReversalTransactionId());
        LOGGER.info("Create Pending Reversal 1.0  Operation Passed");
    }

}
