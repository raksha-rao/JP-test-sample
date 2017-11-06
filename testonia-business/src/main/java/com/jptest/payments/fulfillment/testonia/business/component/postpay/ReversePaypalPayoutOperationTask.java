package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.LegacyTable;
import com.jptest.money.ReversejptestPayoutResponseCode;
import com.jptest.money.ReversejptestPayoutRequest;
import com.jptest.money.ReversejptestPayoutResponse;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.ReadDepth;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.EngineActivityIdRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.service.PostPaymentExecService;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * ReversejptestPayoutOperationTask takes care of constructing the Reverse jptest payout Payment Request and executing
 * reverse jptest payout payment and validates the response
 *
 * @JP Inc.
 */
public class ReversejptestPayoutOperationTask
        extends BasePostPaymentOperationTask<ReversejptestPayoutRequest, ReversejptestPayoutResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReversejptestPayoutOperationTask.class);

    @Inject
    public CryptoUtil cryptoUtil;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private PostPaymentExecService postPaymentService;
    @Inject
    private PaymentReadServBridge plsBridge;

    @Override
    public ReversejptestPayoutRequest constructPostPayRequest(
            final ReversejptestPayoutRequest reversejptestPayoutRequest,
            final Context context) {

        final Optional<Object> buyer = Optional
                .ofNullable(this.getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName()));
        final User buyerData;
        buyerData = (User) buyer.get();
        final String buyerAccountNumber = buyerData.getAccountNumber();

        final List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndType(
                new BigInteger(buyerAccountNumber),
                String.valueOf(WTransactionConstants.Type.BONUS.getValue()));
        String encryptedId = null;
        try {
            encryptedId = this.cryptoUtil.encryptTxnId(txnId.get(0).longValue(),
                    Long.parseLong(buyerAccountNumber));
            reversejptestPayoutRequest.setReversePayoutHandle(
                    encryptedId);
        } catch (final Exception e) {
            LOGGER.error("Encyprtion of transaction id failed");
        }
        reversejptestPayoutRequest
                .setPayoutHandle(encryptedId);

        return reversejptestPayoutRequest;
    }

    @Override
    public ReversejptestPayoutResponse executePostPay(final ReversejptestPayoutRequest reversejptestPayoutRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(reversejptestPayoutRequest);

        return this.postPaymentService.reversejptestPayoutService(reversejptestPayoutRequest, call2PEX);
    }

    @Override
    public void assertPostPayResponse(final ReversejptestPayoutResponse reversejptestPayoutResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(reversejptestPayoutResponse,
                "reverse jptest payout response should not be null");
        Assert.assertEquals(reversejptestPayoutResponse.getErrorCodeAsEnum().getValue().toString(),
                postPayRequest.getReturnCode(), this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");


        if (reversejptestPayoutResponse.getTransactionHandle() != null)
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), reversejptestPayoutResponse.getTransactionHandle());

        LOGGER.info("Reverse jptest Payout - Encrypted Txn Id - {}", reversejptestPayoutResponse.getTransactionHandle());
        LOGGER.info("Reverse jptest Payout Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final ReversejptestPayoutResponse response) {
        if (response.getErrorCodeAsEnum().equals(ReversejptestPayoutResponseCode.SUCCESS)) {
            BigInteger transactionId = null;
            final GetLegacyEquivalentByPaymentReferenceRequest getLegacyEquivalentByPaymentReferenceRequest = new GetLegacyEquivalentByPaymentReferenceRequest();
            final GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse = this
                    .getLegacyEquivalentByPaymentReference(
                            context, getLegacyEquivalentByPaymentReferenceRequest);

            final List<WTransactionVO> wTransactionList = getLegacyEquivalentByPaymentReferenceResponse
                    .getLegacyEquivalent()
                    .getWtransactions();

            for (final WTransactionVO wTxn : wTransactionList) {
                if (wTxn.getType() == WTransactionConstants.Type.BONUS.getValue()
                        && wTxn.getAmount().getUnitAmount() < 0) {
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
            LOGGER.info("REVERSE_jptest_PAYOUT: Activity ID - {}, Transaction ID - {}", activityId, transactionId);
        }

    }

    private GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReference(final Context context,
            final GetLegacyEquivalentByPaymentReferenceRequest request) {

        final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
        paymentSideReferenceVO
                .setReferenceValue(
                        (String) this.getDataFromContext(context, ContextKeys.WTRANSACTION_ID_KEY.getName()));
        paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
        request.setPaymentReference(paymentSideReferenceVO);
        final List<LegacyTable> legacyTable = new ArrayList<LegacyTable>();
        legacyTable.add(LegacyTable.WTRANSACTION);
        request.setRequestedLegacyTablesAsEnum(legacyTable);
        request.setMinimumReadDepth(ReadDepth.WHOLE_PAYMENT_TREE);
        request.setRequireRiskDecision(false);
        return this.plsBridge.getLegacyEquivalentByPaymentReference(request);
    }
}
