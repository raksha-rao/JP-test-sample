package com.jptest.payments.fulfillment.testonia.model;

/*import com.jptest.money.CancelPendingPaymentReversalRequest;
import com.jptest.money.CloseRefundNegotiationRequest;
import com.jptest.money.CompletePendingPaymentReversalRequest;
import com.jptest.money.CompleteUnilateralPaymentsForAliasRequest;
import com.jptest.money.CreateHoldRequest;
import com.jptest.money.CreatePendingPaymentReversalRequest;
import com.jptest.money.CreateReserveRequest;
import com.jptest.money.DisputePayoutRequest;
import com.jptest.money.FulfillDisbursementRequest;
import com.jptest.money.InitiateRefundNegotiationRequest;
import com.jptest.money.MlpFinalizeRequest;
import com.jptest.money.jptestPayoutRequest;
import com.jptest.money.PostBankReturnRequest;
import com.jptest.money.RecoverDisputedFundsRequest;
import com.jptest.money.RecoverFundsRequest;
import com.jptest.money.RefundPaymentRequest;
import com.jptest.money.ReimbursePaymentReceiverRequest;
import com.jptest.money.ReimbursePaymentSenderRequest;
import com.jptest.money.ReleaseHoldRequest;
import com.jptest.money.ReleasePendingPaymentsHoldsRequest;
import com.jptest.money.ReleaseReserveRequest;
import com.jptest.money.ReverseFeeRequest;
import com.jptest.money.ReversePaymentRequest;
import com.jptest.money.ReversejptestPayoutRequest;
import com.jptest.money.TransitionToHoldRequest;
import com.jptest.money.TransitionToReserveRequest;
import com.jptest.money.UpdateTransactionPropertyRequest;
import com.jptest.money.WithdrawRequest;*/
/*import com.jptest.transfer.transfercompletion.model.CompleteTransferMessage;
import com.jptest.transfer.transfercompletion.model.ReverseTransferMessage;
import com.jptest.transfer.transfercompletion.model.completeTransferReviewMessage;*/


/**
 * PostPaymentRequest class takes care of mapping the json test data to corresponding Post Payment Operation request
 *
 * @JP Inc.
 */
public class PostPaymentRequest {

    private String returnCode = "0";
    private String declineReason = null;
    private Object request;
    private boolean call2PEX = false;
    private boolean disableInlineValidations = true;
    private boolean skipAsyncCompletionCheck = false;
    private PostOperationValidationsInput postOperationValidationsInput;
    private String operationName = null;
    private boolean disableComponentSpecificValidation;
    private ComponentSpecificValidationInput componentSpecificValidationInput;

    public void setOperationName(final String operationName) {
        this.operationName = operationName;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(final String returnCode) {
        this.returnCode = returnCode;
    }

    public String getDeclineReason() {
        return this.declineReason;
    }

    public void setDeclineReason(final String declineReason) {
        this.declineReason = declineReason;
    }

    public Object getRequest() {
        return this.request;
    }

    public PostOperationValidationsInput getPostOperationValidationsInput() {
        return this.postOperationValidationsInput;
    }

    public void setPostOperationValidationsInput(final PostOperationValidationsInput postOperationValidationsInput) {
        this.postOperationValidationsInput = postOperationValidationsInput;
    }

    public boolean isDisableInlineValidations() {
        return this.disableInlineValidations;
    }

    public void setDisableInlineValidations(final boolean disableInlineValidations) {
        this.disableInlineValidations = disableInlineValidations;
    }

    public boolean isSkipAsyncCompletionCheck() {
        return this.skipAsyncCompletionCheck;
    }

    public void setSkipAsyncCompletionTask(final boolean skipAsyncCompletionTask) {
        this.skipAsyncCompletionCheck = skipAsyncCompletionTask;
    }

   /* @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "operationName")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RefundPaymentRequest.class, name = PostPaymentOperationConstants.REFUND_PAYMENT),
            @JsonSubTypes.Type(value = CompletePendingPaymentReversalRequest.class, name = PostPaymentOperationConstants.COMPLETE_PENDING_PAYMENT_REVERSAL),
            @JsonSubTypes.Type(value = CreatePendingPaymentReversalRequest.class, name = PostPaymentOperationConstants.CREATE_PENDING_PAYMENT_REVERSAL),
            @JsonSubTypes.Type(value = ReversePaymentRequest.class, name = PostPaymentOperationConstants.REVERSE_PAYMENT),
            @JsonSubTypes.Type(value = ReverseFeeRequest.class, name = PostPaymentOperationConstants.REVERSE_FEE),
            @JsonSubTypes.Type(value = jptestPayoutRequest.class, name = PostPaymentOperationConstants.jptest_PAYOUT),
            @JsonSubTypes.Type(value = PostBankReturnRequest.class, name = PostPaymentOperationConstants.POST_BANK_RETURN),
            @JsonSubTypes.Type(value = ReversejptestPayoutRequest.class, name = PostPaymentOperationConstants.REVERSE_jptest_PAYOUT),
            @JsonSubTypes.Type(value = CreateHoldRequest.class, name = PostPaymentOperationConstants.CREATE_HOLD),
            @JsonSubTypes.Type(value = ReleaseHoldRequest.class, name = PostPaymentOperationConstants.RELEASE_HOLD),
            @JsonSubTypes.Type(value = TransitionToHoldRequest.class, name = PostPaymentOperationConstants.TRANSITION_TO_HOLD),
            @JsonSubTypes.Type(value = TransitionToReserveRequest.class, name = PostPaymentOperationConstants.TRANSITION_TO_RESERVE),
            @JsonSubTypes.Type(value = UpdateTransactionPropertyRequest.class, name = PostPaymentOperationConstants.UPDATE_TRANSACTION_PROPERTY),
            @JsonSubTypes.Type(value = WithdrawRequest.class, name = PostPaymentOperationConstants.WITHDRAW),
            @JsonSubTypes.Type(value = CancelPendingPaymentReversalRequest.class, name = PostPaymentOperationConstants.CANCLE_PENDING_PAYMENT_REVERSAL),
            @JsonSubTypes.Type(value = RecoverDisputedFundsRequest.class, name = PostPaymentOperationConstants.RECOVER_DISPUTED_FUNDS),
            @JsonSubTypes.Type(value = ReimbursePaymentReceiverRequest.class, name = PostPaymentOperationConstants.REIMBURSE_PAYMENT_RECEIVER),
            @JsonSubTypes.Type(value = ReimbursePaymentSenderRequest.class, name = PostPaymentOperationConstants.REIMBURSE_PAYMENT_SENDER),
            @JsonSubTypes.Type(value = ReleasePendingPaymentsHoldsRequest.class, name = PostPaymentOperationConstants.RELEASE_PENDING_PAYMENTS_HOLDS),
            @JsonSubTypes.Type(value = InitiateRefundNegotiationRequest.class, name = PostPaymentOperationConstants.INITIATE_REFUND_NEGOTIATION),
            @JsonSubTypes.Type(value = CloseRefundNegotiationRequest.class, name = PostPaymentOperationConstants.CLOSE_REFUND_NEGOTIATION),
            @JsonSubTypes.Type(value = DisputePayoutRequest.class, name = PostPaymentOperationConstants.DISPUTE_PAYOUT),
            @JsonSubTypes.Type(value = CreateReserveRequest.class, name = PostPaymentOperationConstants.CREATE_RESERVE),
            @JsonSubTypes.Type(value = ReleaseReserveRequest.class, name = PostPaymentOperationConstants.RELEASE_RESERVE),
            @JsonSubTypes.Type(value = RecoupRequest.class, name = PostPaymentOperationConstants.DISPUTE_PAYOUT_RECOUP),
            @JsonSubTypes.Type(value = RecoverFundsRequest.class, name = PostPaymentOperationConstants.RECOVER_FUNDS),
            @JsonSubTypes.Type(value = DoCompletionRequest.class, name = PostPaymentOperationConstants.DO_COMPLETION),
            @JsonSubTypes.Type(value = FulfillDisbursementRequest.class, name = PostPaymentOperationConstants.FULFILL_DISBURSEMENT),
            @JsonSubTypes.Type(value = MlpFinalizeRequest.class, name = PostPaymentOperationConstants.MLP_FINALIZE),
            @JsonSubTypes.Type(value = CompleteUnilateralPaymentsForAliasRequest.class, name = PostPaymentOperationConstants.COMPLETE_UNILATERAL_PAYMENTS_FOR_ALIAS),
            @JsonSubTypes.Type(value = CompleteTransferMessage.class, name = PostPaymentOperationConstants.COMPLETE_TRANSFER),
            @JsonSubTypes.Type(value = completeTransferReviewMessage.class, name = PostPaymentOperationConstants.REVIEW_TRANSFER),
            @JsonSubTypes.Type(value = ReverseTransferMessage.class, name = PostPaymentOperationConstants.REVERSE_TRANSFER),
            @JsonSubTypes.Type(value = FulfillPayoffTabWrapperRequest.class, name = PostPaymentOperationConstants.FULFILL_PAYOFF_TAB)

    })*/
    public void setRequest(final Object request)
            throws InstantiationException, IllegalAccessException {
        this.request = request;

    }

    public boolean isCall2PEX() {
        return this.call2PEX;
    }

    public void setCall2PEX(final boolean call2PEX) {
        this.call2PEX = call2PEX;
    }

    public boolean isDisableComponentSpecificValidation() {
        return this.disableComponentSpecificValidation;
    }

    public void setDisableComponentSpecificValidation(final boolean disableComponentSpecificValidation) {
        this.disableComponentSpecificValidation = disableComponentSpecificValidation;
    }

    public ComponentSpecificValidationInput getComponentSpecificValidationInput() {
        return this.componentSpecificValidationInput;
    }

    public void setComponentSpecificValidationInput(
            final ComponentSpecificValidationInput componentSpecificValidationInput) {
        this.componentSpecificValidationInput = componentSpecificValidationInput;
    }
}
