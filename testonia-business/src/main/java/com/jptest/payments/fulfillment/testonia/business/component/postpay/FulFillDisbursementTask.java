package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import com.jptest.money.*;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.fulfillment.testonia.bridge.MoneyplanningServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.EngineActivityIdRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.business.vo.money.ActorInfoVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.DisbursementStrategyVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.PlanDisbursementRequestBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.money.PlanningContextVOBuilder;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.test.money.util.servicehelper.payment.PaymentServHelper;
import com.jptest.types.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.jptest.payments.OperationStatus.COMPLETED_OK;
import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

/**
 * Task to disburse the money from Delayed Q (Type 5) to disbursement account(either seller or partner like argentum)
 * Calls mps' plan_disbursement to get the disbursement plan based on the payment data (amount and txn handle).
 * Calls paymentserv's fulfill_disbursement API to disburse the money.
 */
public class FulFillDisbursementTask extends BasePostPaymentOperationTask<FulfillDisbursementRequest,
        FulfillDisbursementResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FulFillDisbursementTask.class);
    String txnHandle;

    @Inject
    PaymentReadServBridge paymentReadServBridge;
    GetLegacyEquivalentByPaymentReferenceResponse plsResponse = null;

    @Inject
    MoneyplanningServBridge mpsBridge;

    @Inject
    private PaymentServHelper paymentServHelper;

    @Inject
    private PaymentServBridge paymentServBridge;

    @Inject
    private CryptoUtil cryptoUtil;

    private User sender;
    private User recipient;

    @Override
    public FulfillDisbursementRequest constructPostPayRequest(FulfillDisbursementRequest postPaymentRequest,
                                                              Context context) {
        this.sender = (User) getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
        this.recipient = (User) getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());

        boolean isDisbursementToPartner = (boolean) getDataFromContext(context,
                ContextKeys.PARTNER_DISBURSEMENT.getName());

        FulfillPaymentResponse fulfillPaymentResponse = (FulfillPaymentResponse) getDataFromContext(context,
                ContextKeys.FULFILL_PAYMENT_RESPONSE_KEY.getName());
        txnHandle = fulfillPaymentResponse.getFulfillmentHandle();

        loadPersistedDataUsingPls(txnHandle);

        WTransactionVO disbursementRow = getDisbursementRow();
        if (disbursementRow == null) {
            LOGGER.error("Could not find Disbursement row for payment handle :{}", txnHandle);
            return null;
        }

        Currency disbursementAmount = getDisbursementRow().getAmount();
        disbursementAmount.setAmount(-disbursementAmount.getAmount()); //disb amount loaded will be -ve, change to +ve

        PlanDisbursementResponse planDisbursementResponse = getDisbursementPlan(context, isDisbursementToPartner,
                disbursementAmount);

        FulfillDisbursementRequest fulfillDisbursementRequest = new FulfillDisbursementRequest();
        try {
            fulfillDisbursementRequest.setIdempotenceId(paymentServHelper.createActivityId().toString());
        } catch (IOException ex) {
            LOGGER.error("Could not create activity id for idempotence" + ex);
            return null;
        }

        ActorInfoVOBuilder actorInfo = ActorInfoVOBuilder.newBuilder(new BigInteger(recipient.getAccountNumber()));
        fulfillDisbursementRequest.setActorInfo(actorInfo.build());
        fulfillDisbursementRequest.setDisbursementPlan(planDisbursementResponse.getDisbursementPlan());
        DisbursementFulfillmentContextVO disbContext = new DisbursementFulfillmentContextVO();
        fulfillDisbursementRequest.setDisbursementContext(disbContext);
        return fulfillDisbursementRequest;
    }

    private PlanDisbursementResponse getDisbursementPlan(Context context, boolean isDisbursementToPartner, Currency
            disbursementAmount) {

        FeePolicyVO feePolicyVO = new FeePolicyVO();
        feePolicyVO.setPayerPolicy(FeePayerPolicyType.NONE);

        PlanningContextVOBuilder planningContext = PlanningContextVOBuilder.newBuilder()
                .productFamily(ProductFamilyType.EXPRESS_CHECKOUT)
                .productType(ProductType.EXPRESS_CHECKOUT_SHORTCUT);

        DisbursementStrategyVOBuilder disbursementStrategy = DisbursementStrategyVOBuilder.newBuilder()
                .planningContext(planningContext).disbursementAmount(disbursementAmount)
                .fulfillmentType(FulfillmentType.DISBURSEMENT).feePolicy(feePolicyVO)
                .transactionHandle(txnHandle);

        if (isDisbursementToPartner) {
            // disbursement is done only to primary partner, and done for secondary only in case of instant disbursement
            User disbursementUser = (User) context.getData(ContextKeys.PRIMARY_PARTNER.getName());
            disbursementStrategy.settlementAccountNumber(new BigInteger(disbursementUser.getAccountNumber()));
            LOGGER.info("Disbursement to Partner : " + disbursementUser.getAccountNumber());
        }

        PlanDisbursementRequestBuilder planDisbursement = PlanDisbursementRequestBuilder.newBuilder()
                .disbursementStrategyBuilder(disbursementStrategy)
                .actorInfoVObuilder(ActorInfoVOBuilder.newBuilder(new BigInteger(sender.getAccountNumber())));

        PlanDisbursementRequest planDisbursementRequest = planDisbursement.build();
        return mpsBridge.planDisbursement(planDisbursementRequest);
    }

    @Override
    public FulfillDisbursementResponse executePostPay(FulfillDisbursementRequest fulfillDisbursementRequest, boolean
            call2pex) {
        FulfillDisbursementResponse fulfilldisbursementResponse = paymentServBridge.fulfillDisbursement
                (fulfillDisbursementRequest);
        return fulfilldisbursementResponse;
    }


    @Override
    public void assertPostPayResponse(FulfillDisbursementResponse disbursementResponse, PostPaymentRequest
            postPayRequest, Context context) {
        Assert.assertNotNull(disbursementResponse, "FulfillDisbursementResponse should not be null");
        Assert.assertEquals(disbursementResponse.getResultAsEnum().getValue() + "", postPayRequest.getReturnCode(), 
        		this.getClass().getSimpleName() + ".assertPostPayResponse() failed for return code:");
        String txnId = String.valueOf(disbursementResponse.getDisbursementTransactionId());

        try {
            String encryptedTxnId = cryptoUtil.encryptTxnId(Long.parseLong(txnId), Long.parseLong(recipient
                    .getAccountNumber()));
            LOGGER.info("\n Encrypted Disbursement Txn Id {} \n", encryptedTxnId);
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), encryptedTxnId); // add txn id to context
        } catch (Exception ex) {
            LOGGER.error("Error trying to encrypt disbursement txn id{} {}", txnId, ex);
            // Set it to null to fail validation, and avoid validating for previous ID.
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), null);
        }
        LOGGER.info("FulfillDisbursement Operation Decrypted Id : {}", txnId);
    }

    private void loadPersistedDataUsingPls(String transactionHandle) {

        GetLegacyEquivalentByPaymentReferenceRequest request = new GetLegacyEquivalentByPaymentReferenceRequest();
        request.setRequestedLegacyTablesAsEnum(new ArrayList<>());
        PaymentSideReferenceVO reference = new PaymentSideReferenceVO();
        reference.setReferenceType(PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
        reference.setReferenceValue(transactionHandle);
        request.setPaymentReference(reference);
        try {
            plsResponse = paymentReadServBridge.getLegacyEquivalentByPaymentReference(request);
            LOGGER.info(printValueObject(plsResponse));
            if (plsResponse.getStatusAsEnum() == COMPLETED_OK) return;
            else {
                LOGGER.error("ERROR in pls response {}", plsResponse.getStatusAsEnum().getName());
            }
        } catch (Exception ex) {
            LOGGER.error("Could not load PLS data {}", ex);
        }
    }

    private WTransactionVO getDisbursementRow() {
        List<WTransactionVO> wtxns = plsResponse.getLegacyEquivalent().getWtransactions();
        for (WTransactionVO wtxn : wtxns) {
            if (wtxn.getType() == WTransactionConstants.Type.PAYABLE.getByte()
                    && wtxn.getSubtype() == WTransactionConstants.Subtype.PAYABLES_TXN_LEVEL_DISBURSEMENT.getByte()
                    && wtxn.getReason() == WTransactionConstants.Reason.DELAYED_DISBURSEMENT.getByte()) {
                return wtxn;
            }
        }
        return null;
    }
    
    @Override
    protected void populateActivityId(Context context, FulfillDisbursementResponse response) {

       final BigInteger transactionId = response.getDisbursementTransactionId();
        if (transactionId == null) {
            LOGGER.warn("Transaction ID is not present in FulfillDisbursementResponse");
            return;
        }
        final EngineActivityIdRetrieverTask activityIdRetriever = new EngineActivityIdRetrieverTask(transactionId);
        final BigInteger activityId = activityIdRetriever.process(context);
	    Assert.assertNotNull(activityId, this.getClass().getSimpleName() + ". populateActivityId() - Found null activityId");

        context.setData(this.getActivityIdKey(), activityId);
        LOGGER.info("FulfillDisbursementResponse: Activity ID - {}, Transaction ID - {}", activityId,
                transactionId);
    }
}
