package com.jptest.payments.fulfillment.testonia.business.component.postpay;

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
import com.jptest.payments.fulfillment.testonia.business.component.validation.EngineActivityIdRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * CreatePendingReversalOperationTask takes care of constructing the CreatePendingReversalRequest and executing
 * create_pending_payment_reversal and validates response
 *
 * @JP Inc.
 */
public class CreatePendingReversalOperationTask
        extends
        BasePostPaymentOperationTask<CreatePendingPaymentReversalRequest, CreatePendingPaymentReversalResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePendingReversalOperationTask.class);

    @Inject
    private PostPaymentExecService postPaymentService;
    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;

    private ContextKeys userContextKey = ContextKeys.SELLER_VO_KEY;

    public CreatePendingReversalOperationTask() {

    }

    public CreatePendingReversalOperationTask(ContextKeys userContextKey) {
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
        final List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndTypeAndStatus(
                new BigInteger(accountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
                String.valueOf(WTransactionConstants.Status.SUCCESS.getValue()));

        createPendingPaymentRevversalRequest
                .setOperationIdempotencyData(operationIdempotencyVO);
        createPendingPaymentRevversalRequest
                .setPaymentTransactionId(new BigInteger(txnId.get(0).toString()));

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

        if (createPendingPaymentReversalResponse.getEncryptedReversalTransactionId() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    createPendingPaymentReversalResponse.getEncryptedReversalTransactionId());

        LOGGER.info("Create Pending Reversal - Encrypted Txn Id - {}",
                createPendingPaymentReversalResponse.getEncryptedReversalTransactionId());
        LOGGER.info("Create Pending Reversal Operation Passed");
    }

    @Override
    protected void populateActivityId(Context context, CreatePendingPaymentReversalResponse response) {

        final BigInteger transactionId = response.getReversalTransId();
        Assert.assertNotNull(transactionId, this.getClass().getSimpleName() 
        		+ ". populateActivityId() - Found null transactionId");
        final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(transactionId);
        final BigInteger activityId = activityIdRetriever.process(context);
        Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
        		+ ". populateActivityId() - Found null activityId");
        context.setData(this.getActivityIdKey(), activityId);
        LOGGER.info("CREATE_PENDING_REVERSAL_ACTIVITY_ID: Activity ID - {}, Transaction ID - {}", activityId,
                transactionId);

    }

    @Override
    protected void populateIpnEncryptedId(Context context, CreatePendingPaymentReversalResponse response) {
        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        if (response.getEncryptedReversalTransactionId() != null) {
            context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(), response.getEncryptedReversalTransactionId());
        }
    }
}
