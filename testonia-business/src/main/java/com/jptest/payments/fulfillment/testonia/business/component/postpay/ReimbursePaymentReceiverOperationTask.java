package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.ReimbursePaymentReceiverRequest;
import com.jptest.money.ReimbursePaymentReceiverResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.EngineActivityIdRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * ReimbursePaymentReceiverOperationTask takes care of constructing the reimburse_payment_receiver request , executes
 * and validates the response
 *
 * @JP Inc.
 */
public class ReimbursePaymentReceiverOperationTask
        extends BasePostPaymentOperationTask<ReimbursePaymentReceiverRequest, ReimbursePaymentReceiverResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReimbursePaymentReceiverOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private CryptoUtil cryptoUtil;
    @Inject
    private PostPaymentExecService postPaymentService;

    @Override
    public ReimbursePaymentReceiverRequest constructPostPayRequest(
            final ReimbursePaymentReceiverRequest reimbursePaymentReceiverRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final String idempotencyString = "IdempotencyString-" + activityId;
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
        final String sellerAccountNumber = seller.getAccountNumber();
        final String buyerAccountNumber = buyer.getAccountNumber();
        final List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndTypeAndStatus(
                new BigInteger(buyerAccountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
                String.valueOf(WTransactionConstants.Status.SUCCESS.getValue()));
        if (reimbursePaymentReceiverRequest.getReimbursementFunderAccountNumber() != null) {
            if (reimbursePaymentReceiverRequest.getReimbursementFunderAccountNumber()
                    .equals(ReimburseFunderAccountNumberType.BUYER.value)) {
                LOGGER.info("Setting reimbursement_funder_account_number parameter with Buyer Account Number {}",
                        buyerAccountNumber);
                reimbursePaymentReceiverRequest.setReimbursementFunderAccountNumber(new BigInteger(buyerAccountNumber));
            }
            else if (reimbursePaymentReceiverRequest.getReimbursementFunderAccountNumber()
                    .equals(ReimburseFunderAccountNumberType.SELLER.value)) {
                LOGGER.info("Setting reimbursement_funder_account_number parameter with Seller Account Number {}",
                        sellerAccountNumber);
                reimbursePaymentReceiverRequest
                        .setReimbursementFunderAccountNumber(new BigInteger(sellerAccountNumber));
            }
            else if (reimbursePaymentReceiverRequest.getReimbursementFunderAccountNumber()
                    .equals(ReimburseFunderAccountNumberType.FUNDER.value)) {
                final User funder = (User) context.getData(ContextKeys.FUNDER_VO_KEY.getName());
                final String funderAccountNumber = funder.getAccountNumber();
                LOGGER.info("Setting reimbursement_funder_account_number parameter with Funder Account Number {}",
                        funderAccountNumber);
                reimbursePaymentReceiverRequest
                        .setReimbursementFunderAccountNumber(new BigInteger(funderAccountNumber));
            }
        }
        try {
            reimbursePaymentReceiverRequest.setPaymentHandle(this.cryptoUtil.encryptTxnId(
                    Long.parseLong(txnId.get(0).toString()),
                    Long.parseLong(sellerAccountNumber)));
        }
        catch (final Exception e) {
            throw new TestExecutionException(
                    "Encryption of Transaction ID failed", e);
        }
        reimbursePaymentReceiverRequest.setIdempotencyString(idempotencyString);
        return reimbursePaymentReceiverRequest;
    }

    @Override
    public ReimbursePaymentReceiverResponse executePostPay(
            final ReimbursePaymentReceiverRequest reimbursePaymentReceiverRequest, final boolean call2PEX) {
        Assert.assertNotNull(reimbursePaymentReceiverRequest);

        return this.postPaymentService.reimbursePaymentReceiverService(reimbursePaymentReceiverRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final ReimbursePaymentReceiverResponse reimbursePaymentReceiverResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        final ReimbursePaymentReceiverResponse response = reimbursePaymentReceiverResponse;
        Assert.assertNotNull(response,
                "ReimbursePayment Receiver response should not be null");

        Assert.assertEquals(response.getSuccess().toString(), postPayRequest.getReturnCode(),
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");
        LOGGER.info("ReimbursePayment Receiver Operation Passed");

        final String success = null;
        if (postPayRequest.getDeclineReason() != success) {
            Assert.assertEquals(response.getDeclineDetails().getReasonAsEnum().getValue().toString(),
                    postPayRequest.getDeclineReason().toString(),
                    this.getClass().getSimpleName() + ".assertPostPayResponse() failed for decline reason:");
            LOGGER.info(
                    "Validating decline reason = {} for the failure", postPayRequest.getDeclineReason());
        }

        if (reimbursePaymentReceiverResponse.getEncryptedTransactionId() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    reimbursePaymentReceiverResponse.getEncryptedTransactionId());

        LOGGER.info("Reimburse Payment Receiver - Encrypted Txn Id - {}",
                reimbursePaymentReceiverResponse.getEncryptedTransactionId());
    }

    /**
     * Enumerations for Account Numbers
     */

    public enum ReimburseFunderAccountNumberType {
        BUYER(1),
        SELLER(2),
        FUNDER(3);
        private final BigInteger value;

        private ReimburseFunderAccountNumberType(final long value) {
            this.value = BigInteger.valueOf(value);
        }

        public BigInteger getValue() {
            return this.value;
        }
    }

    @Override
    protected void populateActivityId(final Context context, final ReimbursePaymentReceiverResponse response) {
        Assert.assertNotNull(response, "Reimburse Payment Receiver response should not be null");

        long transactionId;
        final String encTransactionId = response.getEncryptedTransactionId();

        try {
            transactionId = this.cryptoUtil.decryptTxnId(encTransactionId);
        } catch (final Exception e) {
            throw new TestExecutionException("Decryption of Transaction ID failed", e);
        }

        Assert.assertNotNull(transactionId, "Transaction ID should NOT be null");

        final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(
                BigInteger.valueOf(transactionId));
        final BigInteger activityId = activityIdRetriever.process(context);
        Assert.assertNotNull(activityId);
        context.setData(this.getActivityIdKey(), activityId);
        LOGGER.info("ReimbursePaymentReceiver: Activity ID - {}, Transaction ID - {}", activityId, transactionId);
    }

    @Override
    protected void populateIpnEncryptedId(Context context, ReimbursePaymentReceiverResponse response) {

        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        if (response.getReimbursementTransactionHandle() != null) {
            context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(), response.getReimbursementTransactionHandle());
        }
    }
}
