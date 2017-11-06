package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.DisputePayoutRequest;
import com.jptest.money.DisputePayoutResponse;
import com.jptest.money.LegacyTable;
import com.jptest.money.OperationIdempotencyVO;
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
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;

/**
@JP Inc.
 */
public class DisputePayoutOperationTask
extends BasePostPaymentOperationTask<DisputePayoutRequest, DisputePayoutResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(jptestPayoutOperationTask.class);

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private PostPaymentExecService postPaymentService;

    @Inject
    private WTransactionP20DaoImpl wTxnDAO;
    @Inject
    private CryptoUtil cryptoUtil;
    @Inject
    private PaymentReadServBridge plsBridge;

    @Override
    public DisputePayoutRequest constructPostPayRequest(final DisputePayoutRequest disputePayoutRequest,
            final Context context) {

        final BigInteger activityId = this.paymentServBridge.createActivityId();
        final User thirdParty = (User) this.getDataFromContext(context, ContextKeys.THIRD_PARTY_VO_KEY.getName());
        final User seller = (User) this.getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());
        final User buyer = (User) this.getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
        final String thirdPartyAccountNumber = thirdParty.getAccountNumber();
        final String sellerAccountNumber = seller.getAccountNumber();
        final String buyerAccountNumber = buyer.getAccountNumber();
        final List<BigInteger> buyerTxnId = this.wTxnDAO.getTransactionIdByAccountNumberAndTypeAndStatus(
                new BigInteger(buyerAccountNumber),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()),
                String.valueOf(WTransactionConstants.Status.SUCCESS.getValue()));
        disputePayoutRequest.setPayoutFundingAccount(new BigInteger(thirdPartyAccountNumber));
        disputePayoutRequest.setSellerAccountNumber(new BigInteger(sellerAccountNumber));
        final OperationIdempotencyVO operationIdempotencyVO = new OperationIdempotencyVO();
        operationIdempotencyVO.setActivityId(activityId);
        disputePayoutRequest.setOperationIdempotencyData(operationIdempotencyVO);
        try {
            disputePayoutRequest.setEncryptedPurchasePaymentId(this.cryptoUtil.encryptTxnId(
                    Long.parseLong(buyerTxnId.get(0).toString()),
                    Long.parseLong(buyerAccountNumber)));
        } catch (final Exception e) {
            LOGGER.error("Encryption of Transaction ID failed ", e);
            throw new TestExecutionException("Encryption of Transaction ID failed");
        }

        return disputePayoutRequest;
    }

    @Override
    public DisputePayoutResponse executePostPay(final DisputePayoutRequest disputePayoutRequest,
            final boolean call2PEX) {
        Assert.assertNotNull(disputePayoutRequest);
        final DisputePayoutResponse response = this.postPaymentService.disputePayoutService(disputePayoutRequest, call2PEX);
        return response;
    }

    @Override
    public void assertPostPayResponse(final DisputePayoutResponse disputePayoutResponse,
            final PostPaymentRequest postPayRequest, final Context context) {
        Assert.assertNotNull(disputePayoutResponse,
                "disputePayoutResponse should not be null");

        Assert.assertEquals(disputePayoutResponse.getResult().toString(), postPayRequest.getReturnCode(),
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");
        if (disputePayoutResponse.getResult() == 0) {
            context.setData(ContextKeys.ENCRYPTED_PAYOUT_TRANSACTION_ID.getName(),
                    disputePayoutResponse.getEncryptedPayoutTransactionId());

            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    disputePayoutResponse.getEncryptedPayoutTransactionId());
        }

        LOGGER.info("Dispute Payout Operation Passed");
    }

    @Override
    protected void populateActivityId(final Context context, final DisputePayoutResponse response) {
        if (response.getResult() == 0) {
            BigInteger transactionId = null;
            final GetLegacyEquivalentByPaymentReferenceRequest getLegacyEquivalentByPaymentReferenceRequest = new GetLegacyEquivalentByPaymentReferenceRequest();
            final GetLegacyEquivalentByPaymentReferenceResponse getLegacyEquivalentByPaymentReferenceResponse = this
                    .getLegacyEquivalentByPaymentReference(
                            context, getLegacyEquivalentByPaymentReferenceRequest);

            final List<WTransactionVO> wTransactionList = getLegacyEquivalentByPaymentReferenceResponse
                    .getLegacyEquivalent()
                    .getWtransactions();

            for (final WTransactionVO wTxn : wTransactionList) {
                if (wTxn.getType() == WTransactionConstants.Type.DISPLAY_ONLY.getValue()
                        && wTxn.getAmount().getUnitAmount() > 0) {
                    transactionId = wTxn.getId();
                    break;
                }
            }
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    transactionId);
            Assert.assertNotNull(transactionId, this.getClass().getSimpleName() 
                    + ". populateActivityId() - Found null transactionId");
            final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(transactionId);
            final BigInteger activityId = activityIdRetriever.process(context);
            Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
                    + ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(), activityId);
            LOGGER.info("DISPUTE_PAYOUT: Activity ID - {}, Transaction ID - {}", activityId, transactionId);
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
