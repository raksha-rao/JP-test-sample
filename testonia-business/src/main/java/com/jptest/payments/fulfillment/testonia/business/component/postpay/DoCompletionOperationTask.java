package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.common.CommonPartyPb.Party;
import com.jptest.payments.common.CommonPartyPb.UserIdentifier;
import com.jptest.payments.fulfillment.testonia.bridge.TxnCompletionServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentReadServRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.DoCompletionRequest;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.payments.payment_message.PaymentInstructionPb.HoldDetails;
import com.jptest.payments.payment_message.PaymentInstructionPb.PaymentInstruction;
import com.jptest.payments.payment_message.PaymentMessagePb;
import com.jptest.payments.payment_message.PaymentMessagePb.Capture;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;
import com.jptest.payments.payment_message.TransactionPb.Transaction;
import com.jptest.payments.payments.CaptureOperationPb.CaptureOperation;
import com.jptest.payments.payments.StatusPb;
import com.jptest.payments.te.HoldTypePb.HoldType;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.test.money.constants.WTransactionConstants.Type;

/**
 * Completion Task to move the transaction from pending state to success.
 * Ex : Unilateral transaction will be in pending state until the user signs up.
 */
public class DoCompletionOperationTask extends BasePostPaymentOperationTask<DoCompletionRequest, PaymentMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(DoCompletionOperationTask.class);

    @Inject
    private TxnCompletionServBridge tcsBridge;

    private PaymentMessage request;

    @Inject
    private CryptoUtil cryptoUtil;

    @Inject
    private PaymentReadServRetrieverTask paymentReadServRetrieverTask;
    
    @Inject
    private PopulateActivityIdHelper populateActivityIdHelper;

    private String fulfillmentHandle;

    @Override
    public DoCompletionRequest constructPostPayRequest(DoCompletionRequest postPaymentRequest, Context context) {

        HoldDetails holdDetails = HoldDetails.newBuilder()
                .setHoldType(HoldType.forNumber(postPaymentRequest.getHoldType()))
                .build();

        PaymentInstruction paymentInstruction = PaymentInstruction.newBuilder().setHoldDetails(holdDetails).build();

        Transaction.Builder transactionBuilder = Transaction.newBuilder();
        FulfillPaymentResponse fulfillPaymentResponse = (FulfillPaymentResponse) this.getDataFromContext(context,
                ContextKeys.FULFILL_PAYMENT_RESPONSE_KEY.getName());

        if(fulfillPaymentResponse != null){
            transactionBuilder = getTransactionFromFulfillPaymentResponse(postPaymentRequest, context,
                    fulfillPaymentResponse);
        } else {
            transactionBuilder = getTransactionFromPaymentMessageResponse(context);
        }

        transactionBuilder.addPaymentInstructions(paymentInstruction);
        Capture capture = Capture.newBuilder().addTransactions(transactionBuilder.build())
                .setCaptureOperation(CaptureOperation.RELEASE_HOLD).build();

        this.request = PaymentMessagePb.PaymentMessage.newBuilder().setCapture(capture).build();

        return null;
    }

    private Transaction.Builder getTransactionFromFulfillPaymentResponse(DoCompletionRequest postPaymentRequest,
            Context context, FulfillPaymentResponse fulfillPaymentResponse) {

        fulfillmentHandle = fulfillPaymentResponse.getFulfillmentHandle();

        Transaction.Builder transactionBuilder;
        if (postPaymentRequest.getHoldType() == HoldType.PUBLIC_CREDENTIAL_CONFIRM_VALUE) {
            User seller = (User) this.getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());
            UserIdentifier userID = UserIdentifier.newBuilder().setAccountNumber(seller.getAccountNumber()).build();
            Party receiverParty = Party.newBuilder().setUserId(userID).build();

            transactionBuilder = Transaction.newBuilder()
                    .setTransactionHandle(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails()
                            .getDebitSideHandle())
                    .setReceiverParty(receiverParty);
        }
        else {
            transactionBuilder = Transaction.newBuilder()
                    .setTransactionHandle(fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails()
                            .getDebitSideHandle());
        }
        return transactionBuilder;
    }

    private Transaction.Builder getTransactionFromPaymentMessageResponse(Context context) {

        User seller = (User) this.getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());
        User buyer = (User) this.getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
        UserIdentifier userID = UserIdentifier.newBuilder().setAccountNumber(seller.getAccountNumber()).build();
        Party receiverParty = Party.newBuilder().setUserId(userID).build();

        PaymentReadServRetrieverTask plsTask = new PaymentReadServRetrieverTask(
                PaymentReferenceTypeCode.TRANSACTION_UNIT_HANDLE);
        final GetLegacyEquivalentByPaymentReferenceResponse plsResponse = plsTask.process(context);
        List<WTransactionVO> wtransactionList = plsResponse.getLegacyEquivalent().getWtransactions();
        WTransactionVO buyerTransRecord = null;

        boolean isUnilateral = false;

        for (WTransactionVO wTransactionVO : wtransactionList) {
            if (wTransactionVO.getType() == Type.USERUSER.getByte()) {
                if (wTransactionVO.getAccountNumber().toString().equals(buyer.getAccountNumber())) {
                    buyerTransRecord = wTransactionVO;
                    if (buyerTransRecord.getFlags6()
                            .equals(WTransactionConstants.Flag6.HAS_UNILATERAL_HOLD.getValue())) {
                        isUnilateral = true;
                    }
                }
                else if (wTransactionVO.getAccountNumber().toString().equals(seller.getAccountNumber())) {
                    fulfillmentHandle = wTransactionVO.getEncryptedId();
                }
            }
        }

        if (isUnilateral) {
            fulfillmentHandle = plsResponse.getLegacyEquivalent().getUnilateralCreditSideHandle();
        }

        Assert.assertNotNull(buyerTransRecord, this.getClass().getSimpleName()
                + ".getTransactionFromPaymentMessageResponse() - buyerTransRecord should not be Null");
        String encryptedTxnId = null;

        try {
            encryptedTxnId = cryptoUtil.encryptTxnId(buyerTransRecord.getId().longValue(),
                    buyerTransRecord.getAccountNumber().longValue());
        }
        catch (Exception ex) {
            LOG.error("Error trying to encrypt buyer txn id ", ex);
            throw new TestExecutionException("Encryption of Buyer Transaction ID failed");
        }

        Transaction.Builder transactionBuilder = Transaction.newBuilder()
                .setTransactionHandle(encryptedTxnId)
                .setReceiverParty(receiverParty);
        return transactionBuilder;
    }

    @Override
    public PaymentMessage executePostPay(DoCompletionRequest postPayRequest, boolean call2pex) {
        PaymentMessage response = tcsBridge.processPaymentCompletion(request);
        LOG.info("Payment Completion Response Status {}",response.getStatus().name());
        return response;
    }

    @Override
    public void assertPostPayResponse(PaymentMessage postPayResponse, PostPaymentRequest postPayRequest,
                                      Context context) {
        Assert.assertEquals(postPayResponse.getStatus(), StatusPb.Status.SUCCESS,
                this.getClass().getSimpleName() + ".assertPostPayResponse() failed:");

        // Right now DoCompletion response is of type Capture. Can support other operation in the future.
        // Modify/Add appropriate response type as below
        if(postPayResponse.getCapture() != null) {
            context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(),
                    postPayResponse.getCapture().getTransactions(0).getTransactionHandle());
        }
    }
    
    @Override
    protected void populateActivityId(final Context context, final PaymentMessage postPayResponse) {
        if (postPayResponse.getStatus().equals(StatusPb.Status.SUCCESS)) {
            GetLegacyEquivalentByPaymentReferenceResponse prsResponse = paymentReadServRetrieverTask.execute(context);
            List<WTransactionVO> wTransactionVOList = prsResponse.getLegacyEquivalent().getWtransactions();

            BigInteger transactionID = BigInteger.ZERO;
            if (this.request.getCapture().getTransactions(0).getPaymentInstructions(0).getHoldDetails()
                    .getHoldTypeValue() == HoldType.PUBLIC_CREDENTIAL_CONFIRM_VALUE) {
                for (WTransactionVO wTransactionVO : wTransactionVOList) {
                    if (wTransactionVO.getType() == WTransactionConstants.Type.USERUSER.getValue()
                            && wTransactionVO.getAmount().getAmount() > 0) {
                        transactionID = wTransactionVO.getId();
                        break;
                    }
                }
            }
            else if (this.request.getCapture().getTransactions(0).getPaymentInstructions(0).getHoldDetails()
                    .getHoldTypeValue() == HoldType.PROGRESSIVE_ONBOARDING_VALUE) {
                for (WTransactionVO wTransactionVO : wTransactionVOList) {
                    if (wTransactionVO.getType() == WTransactionConstants.Type.FEE.getValue()) {
                        transactionID = wTransactionVO.getId();
                        break;
                    }
                }
            }
            Assert.assertNotNull(transactionID, this.getClass().getSimpleName() 
            		+ ". populateActivityId() - Found null transactionID");
            final BigInteger activityId = populateActivityIdHelper.getLatestActivityIdForReferenceValue(
                    transactionID.toString());
            Assert.assertNotNull(activityId, this.getClass().getSimpleName() 
            		+ ". populateActivityId() - Found null activityId");
            context.setData(this.getActivityIdKey(), activityId);
            LOG.info("DoCompletion Operation: Activity ID - {}, Transaction ID - {} - {}", activityId, transactionID,
                    this.getActivityIdKey());
        }
    }
    
    @Override
    protected boolean isSuccess(Context context, PaymentMessage response) {
        if (response.getStatus() == StatusPb.Status.SUCCESS) {
            return true;
        }
        return false;
    }

    @Override
    protected void populateIpnEncryptedId(Context context, PaymentMessage response) {
        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        if (fulfillmentHandle != null)
            context.setData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName(), fulfillmentHandle);
    }
}
