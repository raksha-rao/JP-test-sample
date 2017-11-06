package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.RecoverDisputedFundsRequest;
import com.jptest.money.RecoverDisputedFundsResponse;
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
 * RecoverDisputedFundsOperationTask takes care of constructing the recover_disputed_funds request , executes and
 * validates the response
 *
 * @JP Inc.
 */
public class RecoverDisputedFundsOperationTask
        extends BasePostPaymentOperationTask<RecoverDisputedFundsRequest, RecoverDisputedFundsResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverDisputedFundsOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private CryptoUtil cryptoUtil;
    @Inject
    private PostPaymentExecService postPaymentService;

    @Override
    public RecoverDisputedFundsRequest constructPostPayRequest(
            final RecoverDisputedFundsRequest recoverDisputedFundsRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final String idempotencyString = "Idempotency-String" + activityId;
        final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
        final String buyerAccountNumber = buyer.getAccountNumber();
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final String sellerAccountNumber = seller.getAccountNumber();
        final List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndTypeAndStatus(
                new BigInteger(sellerAccountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
                String.valueOf(WTransactionConstants.Status.SUCCESS.getValue()));
        if (recoverDisputedFundsRequest.getRecoverySinkAccountNumber() != null) {
            if (recoverDisputedFundsRequest.getRecoverySinkAccountNumber()
                    .equals(RecoverySinkAccountNumberType.BUYER.value)) {
                LOGGER.info("Setting recovery_sink_account_number parameter with Buyer Account Number {}",
                        buyerAccountNumber);
                recoverDisputedFundsRequest.setRecoverySinkAccountNumber(new BigInteger(buyerAccountNumber));

            }
            else if (recoverDisputedFundsRequest.getRecoverySinkAccountNumber()
                    .equals(RecoverySinkAccountNumberType.SELLER.value)) {
                LOGGER.info("Setting recovery_sink_account_number parameter with Seller Account Number {}",
                        sellerAccountNumber);
                recoverDisputedFundsRequest.setRecoverySinkAccountNumber(new BigInteger(sellerAccountNumber));
            }
            else if (recoverDisputedFundsRequest.getRecoverySinkAccountNumber()
                    .equals(RecoverySinkAccountNumberType.FUNDER.value)) {
                final User funder = (User) context.getData(ContextKeys.FUNDER_VO_KEY.getName());
                final String funderAccountNumber = funder.getAccountNumber();

                LOGGER.info("Setting recovery_sink_account_number parameter with Funder Account Number {}",
                        funderAccountNumber);
                recoverDisputedFundsRequest.setRecoverySinkAccountNumber(new BigInteger(funderAccountNumber));
                recoverDisputedFundsRequest.setRecoverySourceAccountNumber(new BigInteger(buyerAccountNumber));
                recoverDisputedFundsRequest.setChannelSettlementId(new BigInteger(10, new Random()).toString());
            }
        }
        try {
            recoverDisputedFundsRequest.setPaymentHandle(this.cryptoUtil.encryptTxnId(
                    Long.parseLong(txnId.get(0).toString()),
                    Long.parseLong(sellerAccountNumber)));
        }
        catch (final Exception e) {
            throw new TestExecutionException(
                    "Encryption of Transaction ID failed", e);
        }

        recoverDisputedFundsRequest.setIdempotencyString(idempotencyString);
        return recoverDisputedFundsRequest;
    }

    @Override
    public RecoverDisputedFundsResponse executePostPay(final RecoverDisputedFundsRequest recoverDisputedFundsRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(recoverDisputedFundsRequest);

        return this.postPaymentService.recoverDisputedFundsService(recoverDisputedFundsRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final RecoverDisputedFundsResponse recoverDisputedFundsResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        final RecoverDisputedFundsResponse response = recoverDisputedFundsResponse;
        Assert.assertNotNull(response,
                "Recover Disputed Funds response should not be null");

        Assert.assertEquals(response.getSuccess().toString(), postPayRequest.getReturnCode(),
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (recoverDisputedFundsResponse.getEncryptedTransactionId() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    recoverDisputedFundsResponse.getEncryptedTransactionId());

        LOGGER.info("Recover Disputed Funds - Encrypted Txn Id - {}",
                recoverDisputedFundsResponse.getEncryptedTransactionId());
        LOGGER.info("Recover Disputed Funds Operation Passed");
    }

    /**
     * Enumerations for Account Numbers
     */

    public enum RecoverySinkAccountNumberType {
        BUYER(1),
        SELLER(2),
        FUNDER(3);
        private final BigInteger value;

        private RecoverySinkAccountNumberType(final long value) {
            this.value = BigInteger.valueOf(value);
        }

        public BigInteger getValue() {
            return this.value;
        }
    }

    @Override
    protected void populateActivityId(final Context context, final RecoverDisputedFundsResponse response) {
        Assert.assertNotNull(response,
                "Recover Disputed Funds response should not be null");

        final String encTransactionId = response.getEncryptedTransactionId();
        long transactionId;
        try {
            transactionId = this.cryptoUtil.decryptTxnId(encTransactionId);
        }
        catch (final Exception e) {
            throw new TestExecutionException("Decryption of Transaction ID failed", e);
        }
        Assert.assertNotNull(transactionId, this.getClass().getSimpleName() 
        		+ ". populateActivityId() - Found null transactionId");
        final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(
                BigInteger.valueOf(transactionId));
        final BigInteger activityId = activityIdRetriever.process(context);
        Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
        		+ ". populateActivityId() - Found null activityId");

        context.setData(this.getActivityIdKey(), activityId);
        LOGGER.info("RDF_ACTIVITY_ID: Activity ID - {}, Transaction ID - {}", activityId,
                transactionId);

    }

    @Override
    protected void populateIpnEncryptedId(Context context, RecoverDisputedFundsResponse response) {

        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        if (response.getRecoverTransactionHandle() != null) {
            context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(), response.getRecoverTransactionHandle());
        }
    }
}
