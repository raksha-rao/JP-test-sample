package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.CancelPendingPaymentReversalRequest;
import com.jptest.money.CancelPendingPaymentReversalResponse;
import com.jptest.money.CloseRefundNegotiationRequest;
import com.jptest.money.CloseRefundNegotiationResponse;
import com.jptest.money.CompletePendingPaymentReversalRequest;
import com.jptest.money.CompletePendingPaymentReversalResponse;
import com.jptest.money.CreatePendingPaymentReversalRequest;
import com.jptest.money.CreatePendingPaymentReversalResponse;
import com.jptest.money.DisputePayoutRecoupRequest;
import com.jptest.money.DisputePayoutRecoupResponse;
import com.jptest.money.DisputePayoutRequest;
import com.jptest.money.DisputePayoutResponse;
import com.jptest.money.InitiateRefundNegotiationRequest;
import com.jptest.money.InitiateRefundNegotiationResponse;
import com.jptest.money.PaymentServ;
import com.jptest.money.jptestPayoutRequest;
import com.jptest.money.jptestPayoutResponse;
import com.jptest.money.RecoverDisputedFundsRequest;
import com.jptest.money.RecoverDisputedFundsResponse;
import com.jptest.money.RefundPaymentRequest;
import com.jptest.money.RefundPaymentResponse;
import com.jptest.money.ReimbursePaymentReceiverRequest;
import com.jptest.money.ReimbursePaymentReceiverResponse;
import com.jptest.money.ReimbursePaymentSenderRequest;
import com.jptest.money.ReimbursePaymentSenderResponse;
import com.jptest.money.ReleasePendingPaymentsHoldsRequest;
import com.jptest.money.ReleasePendingPaymentsHoldsResponse;
import com.jptest.money.ReverseFeeRequest;
import com.jptest.money.ReverseFeeResponse;
import com.jptest.money.ReversePaymentRequest;
import com.jptest.money.ReversePaymentResponse;
import com.jptest.money.ReversejptestPayoutRequest;
import com.jptest.money.ReversejptestPayoutResponse;
import com.jptest.money.UpdateTransactionPropertyRequest;
import com.jptest.money.UpdateTransactionPropertyResponse;


/**
 * Represents bridge for postpaymentexecserv API calls
 */
@Singleton
public class PostPaymentExecServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostPaymentExecServBridge.class);

    @Inject
    @Named("postpaymentexecserv")
    private PaymentServ client;

    /**
     * Call to refund_payment operation
     *
     * @param RefundPaymentRequest
     * @return
     */
    public RefundPaymentResponse refundPayment(final RefundPaymentRequest request) {
        LOGGER.info("refund_payment request: {}", printValueObject(request));
        final RefundPaymentResponse response = this.client.refund_payment(request);
        LOGGER.info("refund_payment response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to reverse_payment() operation
     *
     * @param ReversePaymentRequest
     * @return
     */
    public ReversePaymentResponse reversePayment(final ReversePaymentRequest request) {
        LOGGER.info("ReversePaymentRequest request: {}", printValueObject(request));
        final ReversePaymentResponse response = this.client.reverse_payment(request);
        LOGGER.info("ReversePaymentResponse response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to release_pending_payments_holds() operation
     *
     * @param ReleasePendingPaymentsHoldsRequest
     * @return
     */
    public ReleasePendingPaymentsHoldsResponse releasePendingPaymentsHolds(
            final ReleasePendingPaymentsHoldsRequest request) {
        LOGGER.info("ReleasePendingPaymentsHoldsRequest request: {}", printValueObject(request));
        final ReleasePendingPaymentsHoldsResponse response = this.client.release_pending_payments_holds(request);
        LOGGER.info("ReleasePendingPaymentsHoldsResponse response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to jptestPayout operation
     *
     * @param jptestPayoutRequest
     * @return
     */
    public jptestPayoutResponse jptestPayout(final jptestPayoutRequest request) {
        LOGGER.info("jptest_payout request: {}", printValueObject(request));
        final jptestPayoutResponse response = this.client.jptest_payout(request);
        LOGGER.info("jptest_payout response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to create_pending_payment_reversal operation
     *
     * @param CreatePendingPaymentReversalRequest
     * @return
     */
    public CreatePendingPaymentReversalResponse createPendingPaymentReversal(
            final CreatePendingPaymentReversalRequest request) {
        LOGGER.info("create_pending_payment_reversal request: {}", printValueObject(request));
        final CreatePendingPaymentReversalResponse response = this.client.create_pending_payment_reversal(request);
        LOGGER.info("create_pending_payment_reversal response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to cancel_pending_payment_reversal operation
     *
     * @param CancelPendingPaymentReversalRequest
     * @return
     */
    public CancelPendingPaymentReversalResponse cancelPendingPaymentReversal(
            final CancelPendingPaymentReversalRequest request) {
        LOGGER.info("cancel_pending_payment_reversal request: {}", printValueObject(request));
        final CancelPendingPaymentReversalResponse response = this.client.cancel_pending_payment_reversal(request);
        LOGGER.info("cancel_pending_payment_reversal response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to complete_pending_payment_reversal operation
     *
     * @param CompletePendingPaymentReversalRequest
     * @return
     */
    public CompletePendingPaymentReversalResponse completePendingPaymentReversal(
            final CompletePendingPaymentReversalRequest request) {
        LOGGER.info("complete_pending_payment_reversal request: {}", printValueObject(request));
        final CompletePendingPaymentReversalResponse response = this.client
                .complete_pending_payment_reversal(request);
        LOGGER.info("complete_pending_payment_reversal response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to reverse_jptest_payout
     *
     * @param ReversejptestPayoutRequest
     * @return
     */
    public ReversejptestPayoutResponse reversejptestPayout(final ReversejptestPayoutRequest request) {
        LOGGER.info("reverse_jptest_payout request: {}", printValueObject(request));
        final ReversejptestPayoutResponse response = this.client.reverse_jptest_payout(request);
        LOGGER.info("reverse_jptest_payout response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to update_transaction_property() operation
     *
     * @param UpdateTransactionPropertyRequest
     * @return
     */
    public UpdateTransactionPropertyResponse updateTransactionProperty(final UpdateTransactionPropertyRequest request) {
        LOGGER.info("update_transaction_property request: {}", printValueObject(request));
        final UpdateTransactionPropertyResponse response = this.client.update_transaction_property(request);
        LOGGER.info("update_transaction_property response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to initiate refund negotiation API
     */
    public InitiateRefundNegotiationResponse initiateRefundNegotiation(final InitiateRefundNegotiationRequest request) {

        LOGGER.info("initiate refund negotiation request: {}", printValueObject(request));
        final InitiateRefundNegotiationResponse response = this.client.initiate_refund_negotiation(request);
        LOGGER.info("initiate refund negotiation response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to initiate close refund negotiation API
     */
    public CloseRefundNegotiationResponse closeRefundNegotiation(final CloseRefundNegotiationRequest request) {

        LOGGER.info("initiate refund negotiation request: {}", printValueObject(request));
        final CloseRefundNegotiationResponse response = this.client.close_refund_negotiation(request);
        LOGGER.info("close refund negotiation response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to recover_disputed_funds() operation
     *
     * @param RecoverDisputedFundsRequest
     * @return RecoverDisputedFundsResponse
     */
    public RecoverDisputedFundsResponse recoverDisputedFunds(final RecoverDisputedFundsRequest request) {
        LOGGER.info("recover_disputed_funds request: {}", printValueObject(request));
        final RecoverDisputedFundsResponse response = this.client.recover_disputed_funds(request);
        LOGGER.info("recover_disputed_funds response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to reimburse_payment_receiver() operation
     *
     * @param ReimbursePaymentReceiverRequest
     * @return ReimbursePaymentReceiverResponse
     */
    public ReimbursePaymentReceiverResponse reimbursePaymentReceiver(final ReimbursePaymentReceiverRequest request) {
        LOGGER.info("reimburse_payment_receiver request: {}", printValueObject(request));
        final ReimbursePaymentReceiverResponse response = this.client.reimburse_payment_receiver(request);
        LOGGER.info("reimburse_payment_receiver response: {}", printValueObject(response));
        return response;
    }

    public ReverseFeeResponse reverseFee(ReverseFeeRequest request) {
        LOGGER.info("ReverseFeeRequest request: {}", printValueObject(request));
        final ReverseFeeResponse response = this.client.reverse_fee(request);
        LOGGER.info("ReverseFeeResponse response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to dispute_payout() operation
     *
     * @param DisputePayoutRequest
     * @return DisputePayoutResponse
     */
    public DisputePayoutResponse disputePayout(final DisputePayoutRequest request) {
        LOGGER.info("dispute_payout request: {}", printValueObject(request));
        final DisputePayoutResponse response = this.client.dispute_payout(request);
        LOGGER.info("dispute_payout response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to dipsute_payout_recoup() operation
     *
     * @param DisputePayoutRecoupResponse
     * @return DisputePayoutRecoupResponse
     */
    public DisputePayoutRecoupResponse disputePayoutRecoup(final DisputePayoutRecoupRequest request) {
        LOGGER.info("dipsute_payout_recoup request: {}", printValueObject(request));
        final DisputePayoutRecoupResponse response = this.client.dispute_payout_recoup(request);
        LOGGER.info("dipsute_payout_recoup response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to reimburse_payment_sender() operation
     *
     * @param ReimbursePaymentSenderRequest
     * @return ReimbursePaymentSenderResponse
     */
    public ReimbursePaymentSenderResponse reimbursePaymentSender(final ReimbursePaymentSenderRequest request) {
        LOGGER.info("reimburse_payment_sender request: {}", printValueObject(request));
        final ReimbursePaymentSenderResponse response = this.client.reimburse_payment_sender(request);
        LOGGER.info("reimburse_payment_sender response: {}", printValueObject(response));
        return response;
    }
}
