package com.jptest.payments.fulfillment.testonia.business.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.CancelPendingPaymentReversalRequest;
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
import com.jptest.money.WithdrawRequest;
import com.jptest.payments.fulfillment.testonia.business.component.paymentcompletion.CompleTransferMessageOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.paymentcompletion.CompleteTransferReviewMessageOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.paymentcompletion.ReverseTransferMessageOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.BasePostPaymentOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.CancelPendingReversalOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.CloseRefundNegotiationOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.CompleteP10UnilateralPaymentsTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.CompletePendingReversalOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.CreateHoldOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.CreatePendingReversalOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.CreateReserveOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.DisputePayoutOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.DisputePayoutRecoupOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.DoCompletionOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.FulFillDisbursementTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.InitiateRefundNegotiationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.MLPFinalizeTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.PayoffTabTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.jptestPayoutOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.PostBankReturnOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.RecoverDisputedFundsOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.RecoverFundsOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.RefundPaymentOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.ReimbursePaymentReceiverOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.ReimbursePaymentSenderOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.ReleaseHoldOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.ReleasePendingPaymentsHoldsOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.ReleaseReserveOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.ReverseFeeOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.ReversePaymentOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.ReversejptestPayoutOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.TransitionToHoldOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.TransitionToReserveOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.UpdateTransactionPropertyTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.WithdrawlsOperationTask;
import com.jptest.payments.fulfillment.testonia.model.DoCompletionRequest;
import com.jptest.payments.fulfillment.testonia.model.FulfillPayoffTabWrapperRequest;
import com.jptest.payments.fulfillment.testonia.model.money.RecoupRequest;
import com.jptest.transfer.transfercompletion.model.CompleteTransferMessage;
import com.jptest.transfer.transfercompletion.model.ReverseTransferMessage;
import com.jptest.transfer.transfercompletion.model.completeTransferReviewMessage;


/**
 * PosTPaymentOperaitons enum takes care of linking the corresponding post payment operation task for a given post
 * payment request
 *
 * @JP Inc.
 */
public enum PostPaymentOperations {

    REFUND_PAYMENT(RefundPaymentRequest.class, RefundPaymentOperationTask.class),
    REVERSE_PAYMENT(ReversePaymentRequest.class, ReversePaymentOperationTask.class),
    REVERSE_FEE(ReverseFeeRequest.class, ReverseFeeOperationTask.class),
    jptest_PAYOUT(jptestPayoutRequest.class, jptestPayoutOperationTask.class),
    REVERSE_jptest_PAYOUT(ReversejptestPayoutRequest.class, ReversejptestPayoutOperationTask.class),
    CREATE_PENDING_PAYMENT_REVERSAL_REQUEST(
            CreatePendingPaymentReversalRequest.class,
            CreatePendingReversalOperationTask.class),
    COMPLETE_PENDING_PAYMENT_REVERSAL_REQUEST(
            CompletePendingPaymentReversalRequest.class,
            CompletePendingReversalOperationTask.class),
    CANCEL_PENDING_PAYMENT_REVERSAL_REQUEST(
            CancelPendingPaymentReversalRequest.class,
            CancelPendingReversalOperationTask.class),
    CREATE_HOLD(CreateHoldRequest.class, CreateHoldOperationTask.class),
    RELEASE_HOLD(ReleaseHoldRequest.class, ReleaseHoldOperationTask.class),
    CREATE_RESERVE(CreateReserveRequest.class, CreateReserveOperationTask.class),
    RELEASE_RESERVE(ReleaseReserveRequest.class, ReleaseReserveOperationTask.class),
    TRANSITION_TO_HOLD(TransitionToHoldRequest.class, TransitionToHoldOperationTask.class),
    TRANSITION_TO_RESERVE(TransitionToReserveRequest.class, TransitionToReserveOperationTask.class),
    POST_BANK_RETURN(PostBankReturnRequest.class, PostBankReturnOperationTask.class),
    WITHDRAW(WithdrawRequest.class, WithdrawlsOperationTask.class),
    UPDATE_TRANSACTION_PROPERTY(UpdateTransactionPropertyRequest.class, UpdateTransactionPropertyTask.class),
    INITIATE_REFUND_NEGOTIATION(InitiateRefundNegotiationRequest.class, InitiateRefundNegotiationTask.class),
    CLOSE_REFUND_NEGOTIATION(CloseRefundNegotiationRequest.class, CloseRefundNegotiationOperationTask.class),
    RECOVER_DISPUTED_FUNDS(RecoverDisputedFundsRequest.class, RecoverDisputedFundsOperationTask.class),
    REIMBURSE_PAYMENT_RECEIVER(ReimbursePaymentReceiverRequest.class, ReimbursePaymentReceiverOperationTask.class),
    RELEASE_PENDING_PAYMENTS_HOLDS(
            ReleasePendingPaymentsHoldsRequest.class,
            ReleasePendingPaymentsHoldsOperationTask.class),
    DISPUTE_PAYOUT(DisputePayoutRequest.class, DisputePayoutOperationTask.class),
    DO_COMPLETEION(DoCompletionRequest.class, DoCompletionOperationTask.class),
    FULFILL_DISBURSEMENT(FulfillDisbursementRequest.class, FulFillDisbursementTask.class),
    DISPUTE_PAYOUT_RECOUP(RecoupRequest.class, DisputePayoutRecoupOperationTask.class),
    MLP_FINALIZE(MlpFinalizeRequest.class, MLPFinalizeTask.class),
    COMPLETE_UNILATERAL_PAYMENTS_FOR_ALIAS(
            CompleteUnilateralPaymentsForAliasRequest.class,
            CompleteP10UnilateralPaymentsTask.class),
    RECOVER_FUNDS(RecoverFundsRequest.class, RecoverFundsOperationTask.class),
    REIMBURSE_PAYMENT_SENDER(ReimbursePaymentSenderRequest.class, ReimbursePaymentSenderOperationTask.class),
    COMPLETE_TRANSFER(
            CompleteTransferMessage.class,
            CompleTransferMessageOperationTask.class),
    REVIEW_TRANSFER(completeTransferReviewMessage.class, CompleteTransferReviewMessageOperationTask.class),
    REVERSE_TRANSFER(ReverseTransferMessage.class, ReverseTransferMessageOperationTask.class),
    FULFILL_PAYOFF_TAB(FulfillPayoffTabWrapperRequest.class, PayoffTabTask.class);

    private Class<?> postPaymentRequest;
    private Class<? extends BasePostPaymentOperationTask<?, ?>> operationTask;
    private static final String ACTIVITY_ID = "_activityId";
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelPendingReversalOperationTask.class);

    private PostPaymentOperations(final Class<?> postPayRequest,
            final Class<? extends BasePostPaymentOperationTask<?, ?>> operationTask) {
        this.postPaymentRequest = postPayRequest;
        this.operationTask = operationTask;
    }

    public static BasePostPaymentOperationTask<?, ?> getOperationTask(final Object postPaymentRequest)
            throws UnsupportedOperationException {

        for (final PostPaymentOperations postPaymentOperation : PostPaymentOperations.values()) {

            if (postPaymentOperation.postPaymentRequest.isInstance(postPaymentRequest)) {

                try {
                    return postPaymentOperation.operationTask.newInstance();
                }
                catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.error("Instantiation Exception" + e);
                }

            }
        }
        throw new UnsupportedOperationException("Instantiation Exception");
    }

    public static String getActivityIdKey(Object postPaymentRequest) {
        return postPaymentRequest.getClass().getSimpleName() + ACTIVITY_ID;
    }

    public static String getActivityIdKey(Class<?> postPaymentRequest) {
        return postPaymentRequest.getSimpleName() + ACTIVITY_ID;
    }

    public Class<?> getPostPaymentRequest() {
        return this.postPaymentRequest;
    }

}
