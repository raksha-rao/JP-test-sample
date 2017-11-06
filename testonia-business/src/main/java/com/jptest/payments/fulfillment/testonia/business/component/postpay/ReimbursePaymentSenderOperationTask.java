package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import com.jptest.money.ReimbursePaymentSenderRequest;
import com.jptest.money.ReimbursePaymentSenderResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.business.util.PActivityTransMapConstants;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.List;


/**
 * ReimbursePaymentSenderOperationTask takes care of constructing the reimburse_payment_sender request , executes
 * and validates the response
 *
 * @JP Inc.
 */
public class ReimbursePaymentSenderOperationTask
        extends BasePostPaymentOperationTask<ReimbursePaymentSenderRequest, ReimbursePaymentSenderResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReimbursePaymentSenderOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private CryptoUtil cryptoUtil;
    @Inject
    private PostPaymentExecService postPaymentService;

    @Override
    public ReimbursePaymentSenderRequest constructPostPayRequest(
            final ReimbursePaymentSenderRequest reimbursePaymentSenderRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
        final String sellerAccountNumber = seller.getAccountNumber();
        final String buyerAccountNumber = buyer.getAccountNumber();
        final List<BigInteger> txnId;

        txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndTypeAndStatus(
                new BigInteger(sellerAccountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
                String.valueOf(WTransactionConstants.Status.SUCCESS.getValue()));

        if (reimbursePaymentSenderRequest.getReimbursementFunderAccountNumber() != null) {

            if (reimbursePaymentSenderRequest.getReimbursementFunderAccountNumber().equals(ReimburseFunderAccountNumberType.BUYER.value)) {
                LOGGER.info("Setting reimbursement_funder_account_number parameter with Buyer Account Number {}", buyerAccountNumber);
                reimbursePaymentSenderRequest.setReimbursementFunderAccountNumber(new BigInteger(buyerAccountNumber));

            } else if (reimbursePaymentSenderRequest.getReimbursementFunderAccountNumber().equals(ReimburseFunderAccountNumberType.SELLER.value)) {
                LOGGER.info("Setting reimbursement_funder_account_number parameter with Seller Account Number {}", sellerAccountNumber);
                reimbursePaymentSenderRequest.setReimbursementFunderAccountNumber(new BigInteger(sellerAccountNumber));

            } else if (reimbursePaymentSenderRequest.getReimbursementFunderAccountNumber().equals(ReimburseFunderAccountNumberType.FUNDER.value)) {
                final User funder = (User) context.getData(ContextKeys.FUNDER_VO_KEY.getName());
                final String funderAccountNumber = funder.getAccountNumber();
                LOGGER.info("Setting reimbursement_funder_account_number parameter with Funder Account Number {}", funderAccountNumber);
                reimbursePaymentSenderRequest.setReimbursementFunderAccountNumber(new BigInteger(funderAccountNumber));
            }
        }

        try {
            reimbursePaymentSenderRequest.setPaymentHandle(this.cryptoUtil.encryptTxnId(
                            Long.parseLong(txnId.get(0).toString()),
                            Long.parseLong(sellerAccountNumber)));
        } catch (final Exception e) {
            throw new TestExecutionException("Encryption of Transaction ID failed", e);
        }

        try {
            String enc_act_id = this.cryptoUtil.encryptActivityId(PActivityTransMapConstants.LEDGER_TYPE_DEBIT, activityId);
            reimbursePaymentSenderRequest.setTransactionHandle(enc_act_id);
        } catch (Exception e) {
            throw new TestExecutionException("Encryption of Activity ID failed", e);
        }

        return reimbursePaymentSenderRequest;
    }

    @Override
    public ReimbursePaymentSenderResponse executePostPay(final ReimbursePaymentSenderRequest reimbursePaymentSenderRequest, final boolean call2PEX) {
        Assert.assertNotNull(reimbursePaymentSenderRequest, "ReimbursePayment Sender request should not be null");
        return this.postPaymentService.reimbursePaymentSenderService(reimbursePaymentSenderRequest, call2PEX);
    }
    


    @Override
    public void assertPostPayResponse(final ReimbursePaymentSenderResponse reimbursePaymentSenderResponse,
                                      final PostPaymentRequest postPayRequest, final Context context) {

        Assert.assertNotNull(reimbursePaymentSenderResponse, "ReimbursePayment Sender response should not be null");
        Assert.assertEquals(reimbursePaymentSenderResponse.getSuccess().toString(), postPayRequest.getReturnCode(),
            "ReimbursePayment assertPostPayResponse() failed while validating return code");

        LOGGER.info("ReimbursePayment Sender Operation Passed");

        if (postPayRequest.getDeclineReason() != null) {
            Assert.assertEquals(
                    reimbursePaymentSenderResponse.getDeclineDetails().getReasonAsEnum().getValue().toString(),
                    postPayRequest.getDeclineReason(),
                    "ReimbursePayment assertPostPayResponse() failed while validating decline reason");
            LOGGER.info("Validating decline reason = {} for the failure", postPayRequest.getDeclineReason());
        }
    }

    /**
     * Enumerations for Account Numbers
     */
    private enum ReimburseFunderAccountNumberType {
        BUYER(1),
        SELLER(2),
        FUNDER(3);

        private BigInteger value;

        ReimburseFunderAccountNumberType(final long value) {
            this.value = BigInteger.valueOf(value);
        }

        public BigInteger getValue() {
            return this.value;
        }
    }
}
