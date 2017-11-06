package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.LegacyTable;
import com.jptest.money.jptestPayoutResponseCode;
import com.jptest.money.jptestPayoutRequest;
import com.jptest.money.jptestPayoutResponse;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.ReadDepth;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.EngineActivityIdRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * jptestPayoutOperationTask takes care of constructing the jptestPayoutRequest and executing jptest_payout and
 * validates the response
 *
 * @JP Inc.
 */
public class jptestPayoutOperationTask extends BasePostPaymentOperationTask<jptestPayoutRequest, jptestPayoutResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(jptestPayoutOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private CryptoUtil cryptoUtil;
    @Inject
    private PostPaymentExecService postPaymentService;
    @Inject
    private PaymentReadServBridge plsBridge;
    @Inject
    private TransactionHelper transactionHelper;

    private ContextKeys userContextKey = ContextKeys.BUYER_VO_KEY;

    public jptestPayoutOperationTask() {

    }

    public jptestPayoutOperationTask(final ContextKeys userContextKey) {
        this.userContextKey = userContextKey;
    }

    @Override
    public jptestPayoutRequest constructPostPayRequest(final jptestPayoutRequest jptestPayoutRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());

        jptestPayoutRequest.setIdempotencyString(activityId.toString());

        if (jptestPayoutRequest.getAccountNumber() != null
                && jptestPayoutRequest.getAccountNumber().equals(AccountNumberType.SELLER.value)) {
            this.fillRequestdetails(seller, jptestPayoutRequest, context);
        }
        else if (this.userContextKey != null && this.userContextKey == ContextKeys.SELLER_VO_KEY) {
            this.fillRequestdetails(seller, jptestPayoutRequest, context);
        }
        else {
            this.fillRequestdetails(buyer, jptestPayoutRequest, context);
        }

        return jptestPayoutRequest;

    }

    private void fillRequestdetails(final User user, final jptestPayoutRequest jptestPayoutRequest,
            final Context context) {

        String accountNumber = user.getAccountNumber();
        List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndTypeAndStatus(
                new BigInteger(accountNumber), String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
                String.valueOf(WTransactionConstants.Status.SUCCESS.getValue()));

        // If found zero txns for above criteria, check if its unilateral unclaimed txn
        if (txnId.isEmpty()) {
            @SuppressWarnings("unchecked")
            final List<WTransactionVO> wTransactionList = (List<WTransactionVO>) this.getDataFromContext(context,
                    ContextKeys.WTRANSACTION_LIST_KEY.getName());
            final WTransactionVO senderTransaction = this.transactionHelper.getSenderTransaction(wTransactionList);
            LOGGER.info("Found Sender Transaction Id: {}", senderTransaction.getId());

            if (this.transactionHelper.isUnilateral(senderTransaction)) {
                final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
                accountNumber = buyer.getAccountNumber();
                txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndTypeAndStatus(
                        new BigInteger(accountNumber), String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
                        String.valueOf(WTransactionConstants.Status.PENDING.getValue()));
            }
        }

        try {
            jptestPayoutRequest.setPaymentHandle(this.cryptoUtil.encryptTxnId(Long.parseLong(txnId.get(0).toString()),
                    Long.parseLong(accountNumber)));
        }
        catch (final Exception e) {
            LOGGER.error("Encryption of Transaction ID failed", e);
            throw new TestExecutionException("Encryption of Transaction ID failed");
        }
        jptestPayoutRequest.setAccountNumber(new BigInteger(accountNumber));
    }

    @Override
    public jptestPayoutResponse executePostPay(final jptestPayoutRequest jptestPayoutRequest, final boolean call2PEX) {
        Assert.assertNotNull(jptestPayoutRequest);

        return this.postPaymentService.jptestPayoutService(jptestPayoutRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final jptestPayoutResponse jptestPayoutResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(jptestPayoutResponse, "jptestPayoutResponse should not be null");

        Assert.assertEquals(jptestPayoutResponse.getErrorCodeAsEnum().getValue().toString(),
                postPayRequest.getReturnCode(),
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");

        if (jptestPayoutResponse.getTransactionHandle() != null) {
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), jptestPayoutResponse.getTransactionHandle());
        }

        LOGGER.info("jptest Payout - Encrypted Txn Id - {}", jptestPayoutResponse.getTransactionHandle());
        LOGGER.info("jptest Payout Operation Passed");

    }

    /**
     * Enumerations for Account Numbers
     */

    public enum AccountNumberType {
        BUYER(1),
        SELLER(2);

        private final BigInteger value;

        private AccountNumberType(final long value) {
            this.value = BigInteger.valueOf(value);
        }

        public BigInteger getValue() {
            return this.value;
        }
    }

    @Override
    protected void populateActivityId(final Context context, final jptestPayoutResponse response) {
        if (response.getErrorCodeAsEnum().equals(jptestPayoutResponseCode.SUCCESS)) {
            BigInteger transactionId = null;
            final GetLegacyEquivalentByPaymentReferenceRequest getLegacyEquivalentByPaymentReferenceRequest = new GetLegacyEquivalentByPaymentReferenceRequest();
            final GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse = this
                    .getLegacyEquivalentByPaymentReference(context, getLegacyEquivalentByPaymentReferenceRequest);

            final List<WTransactionVO> wTransactionList = getLegacyEquivalentByPaymentReferenceResponse
                    .getLegacyEquivalent().getWtransactions();

            for (final WTransactionVO wTxn : wTransactionList) {
                if (wTxn.getType() == WTransactionConstants.Type.BONUS.getValue()
                        && wTxn.getAmount().getUnitAmount() > 0) {
                    transactionId = wTxn.getId();
                    break;
                }
            }
            Assert.assertNotNull(transactionId, this.getClass().getSimpleName()
                    + ". populateActivityId() - Found null transactionId");
            final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(transactionId);
            final BigInteger activityId = activityIdRetriever.process(context);
            Assert.assertNotNull(activityId, this.getClass().getSimpleName()
                    + ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(), activityId);
            LOGGER.info("jptest_PAYOUT: Activity ID - {}, Transaction ID - {}", activityId, transactionId);
        }

    }

    private GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReference(final Context context,
            final GetLegacyEquivalentByPaymentReferenceRequest request) {

        final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
        final String wtransactionID = (String) this.getDataFromContext(context,
                ContextKeys.WTRANSACTION_ID_KEY.getName());
        paymentSideReferenceVO.setReferenceValue(wtransactionID);
        if (StringUtils.isNumeric(wtransactionID)) {
            paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.LEGACY_BASE_ID);
        }
        else {
            paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
        }
        request.setPaymentReference(paymentSideReferenceVO);
        final List<LegacyTable> legacyTable = new ArrayList<LegacyTable>();
        legacyTable.add(LegacyTable.WTRANSACTION);
        request.setRequestedLegacyTablesAsEnum(legacyTable);
        request.setMinimumReadDepth(ReadDepth.WHOLE_PAYMENT_TREE);
        request.setRequireRiskDecision(false);
        return this.plsBridge.getLegacyEquivalentByPaymentReference(request);
    }
}
