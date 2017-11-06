package com.jptest.payments.fulfillment.testonia.business.service;

import javax.inject.Inject;
import javax.inject.Singleton;

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
import com.jptest.money.jptestPayoutRequest;
import com.jptest.money.jptestPayoutResponse;
import com.jptest.money.RecoverDisputedFundsRequest;
import com.jptest.money.RecoverDisputedFundsResponse;
import com.jptest.money.RecoverFundsRequest;
import com.jptest.money.RecoverFundsResponse;
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
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.PostPaymentExecServBridge;


/**
 * Represents wrapper class to route the txn to paymentserv or 2PEX
 */
@Singleton
public class PostPaymentExecService {

    @Inject
    private PostPaymentExecServBridge paymentServExecBridge;
    @Inject
    private PaymentServBridge paymentServBridge;

    public CancelPendingPaymentReversalResponse cancelPendingReversalService(
            final CancelPendingPaymentReversalRequest cancelPendingPaymentReversalRequest, final boolean call2PEX) {

        final CancelPendingPaymentReversalResponse cancelPendingPaymentReversalResponse;
        if (call2PEX) {
            cancelPendingPaymentReversalResponse = this.paymentServExecBridge
                    .cancelPendingPaymentReversal(cancelPendingPaymentReversalRequest);
        }
        else {
            cancelPendingPaymentReversalResponse = this.paymentServBridge
                    .cancelPendingPaymentReversal(cancelPendingPaymentReversalRequest);
        }
        return cancelPendingPaymentReversalResponse;
    }

    public CloseRefundNegotiationResponse closeRefundNegotiationService(
            final CloseRefundNegotiationRequest closeRefundNegotiationRequest, final boolean call2PEX) {

        CloseRefundNegotiationResponse closeRefundNegotiationResponse;
        if (call2PEX) {
            closeRefundNegotiationResponse = this.paymentServExecBridge
                    .closeRefundNegotiation(closeRefundNegotiationRequest);
        }
        else {
            closeRefundNegotiationResponse = this.paymentServBridge
                    .closeRefundNegotiation(closeRefundNegotiationRequest);
        }
        return closeRefundNegotiationResponse;
    }

    public CompletePendingPaymentReversalResponse completePendingPaymentReversalService(
            final CompletePendingPaymentReversalRequest completePendingPaymentReversalRequest, final boolean call2PEX) {

        final CompletePendingPaymentReversalResponse completePendingPaymentReversalResponse;
        if (call2PEX) {
            completePendingPaymentReversalResponse = this.paymentServExecBridge
                    .completePendingPaymentReversal(completePendingPaymentReversalRequest);
        }
        else {
            completePendingPaymentReversalResponse = this.paymentServBridge
                    .completePendingPaymentReversal(completePendingPaymentReversalRequest);
        }
        return completePendingPaymentReversalResponse;
    }

    public CreatePendingPaymentReversalResponse createPendingPaymentReversalService(
            final CreatePendingPaymentReversalRequest createPendingPaymentReversalRequest, final boolean call2PEX) {

        final CreatePendingPaymentReversalResponse createPendingPaymentReversalResponse;
        if (call2PEX) {
            createPendingPaymentReversalResponse = this.paymentServExecBridge
                    .createPendingPaymentReversal(createPendingPaymentReversalRequest);
        }
        else {
            createPendingPaymentReversalResponse = this.paymentServBridge
                    .createPendingPaymentReversal(createPendingPaymentReversalRequest);
        }
        return createPendingPaymentReversalResponse;
    }

    public InitiateRefundNegotiationResponse initiateRefundNegotiationService(
            final InitiateRefundNegotiationRequest initiateRefundNegotiationRequest, final boolean call2PEX) {

        final InitiateRefundNegotiationResponse response;
        if (call2PEX) {
            response = this.paymentServExecBridge
                    .initiateRefundNegotiation(initiateRefundNegotiationRequest);
        }
        else {
            response = this.paymentServBridge
                    .initiateRefundNegotiation(initiateRefundNegotiationRequest);
        }
        return response;
    }

    public jptestPayoutResponse jptestPayoutService(final jptestPayoutRequest jptestPayoutRequest,
            final boolean call2PEX) {

        final jptestPayoutResponse jptestPayoutResponse;
        if (call2PEX) {
            jptestPayoutResponse = this.paymentServExecBridge.jptestPayout(jptestPayoutRequest);
        }
        else {
            jptestPayoutResponse = this.paymentServBridge.jptestPayout(jptestPayoutRequest);
        }
        return jptestPayoutResponse;
    }

    public RecoverDisputedFundsResponse recoverDisputedFundsService(
            final RecoverDisputedFundsRequest recoverDisputedFundsRequest,
            final boolean call2PEX) {

        final RecoverDisputedFundsResponse response;
        if (call2PEX) {
            response = this.paymentServExecBridge
                    .recoverDisputedFunds(recoverDisputedFundsRequest);
        }
        else {
            response = this.paymentServBridge
                    .recoverDisputedFunds(recoverDisputedFundsRequest);
        }
        return response;
    }

    public RefundPaymentResponse refundPaymentService(final RefundPaymentRequest refundPaymentRequest,
            final boolean call2PEX) {

        final RefundPaymentResponse response;
        if (call2PEX) {
            response = this.paymentServExecBridge.refundPayment(refundPaymentRequest);
        }
        else {
            response = this.paymentServBridge.refundPayment(refundPaymentRequest);
        }
        return response;
    }

    public ReimbursePaymentReceiverResponse reimbursePaymentReceiverService(
            final ReimbursePaymentReceiverRequest reimbursePaymentReceiverRequest, final boolean call2PEX) {

        final ReimbursePaymentReceiverResponse response;
        if (call2PEX) {
            response = this.paymentServExecBridge
                    .reimbursePaymentReceiver(reimbursePaymentReceiverRequest);
        }
        else {
            response = this.paymentServBridge
                    .reimbursePaymentReceiver(reimbursePaymentReceiverRequest);
        }
        return response;
    }

    public ReleasePendingPaymentsHoldsResponse releasePendingPaymentsHoldsService(
            final ReleasePendingPaymentsHoldsRequest releasePendingPaymentsHoldsRequest, final boolean call2PEX) {

        final ReleasePendingPaymentsHoldsResponse releasePendingPaymentsHoldsResponse;
        if (call2PEX) {
            releasePendingPaymentsHoldsResponse = this.paymentServExecBridge
                    .releasePendingPaymentsHolds(releasePendingPaymentsHoldsRequest);
        }
        else {
            releasePendingPaymentsHoldsResponse = this.paymentServBridge
                    .releasePendingPaymentsHolds(releasePendingPaymentsHoldsRequest);
        }

        return releasePendingPaymentsHoldsResponse;
    }

    public ReversePaymentResponse reversePaymentService(final ReversePaymentRequest reversePaymentRequest,
            final boolean call2PEX) {

        final ReversePaymentResponse response;
        if (call2PEX) {
            response = this.paymentServExecBridge.reversePayment(reversePaymentRequest);
        }
        else {
            response = this.paymentServBridge.reversePayment(reversePaymentRequest);
        }
        return response;
    }

    public ReversejptestPayoutResponse reversejptestPayoutService(
            final ReversejptestPayoutRequest reversejptestPayoutRequest,
            final boolean call2PEX) {

        final ReversejptestPayoutResponse reversejptestPayoutResponse;
        if (call2PEX) {
            reversejptestPayoutResponse = this.paymentServExecBridge
                    .reversejptestPayout(reversejptestPayoutRequest);
        }
        else {
            reversejptestPayoutResponse = this.paymentServBridge
                    .reversejptestPayout(reversejptestPayoutRequest);
        }
        return reversejptestPayoutResponse;
    }

    public UpdateTransactionPropertyResponse updateTransactionPropertyService(
            final UpdateTransactionPropertyRequest updateTransactionPropertyRequest, final boolean call2PEX) {

        final UpdateTransactionPropertyResponse response;
        if (call2PEX) {
            response = this.paymentServExecBridge
                    .updateTransactionProperty(updateTransactionPropertyRequest);
        }
        else {
            response = this.paymentServBridge
                    .updateTransactionProperty(updateTransactionPropertyRequest);
        }
        return response;
    }

    public ReverseFeeResponse reverseFeeService(ReverseFeeRequest reverseFeeRequest, boolean call2PEX) {
        if (call2PEX) {
            return this.paymentServExecBridge.reverseFee(reverseFeeRequest);
        }
        else {
            return this.paymentServBridge.reverseFee(reverseFeeRequest);
        }
    }

    public DisputePayoutResponse disputePayoutService(final DisputePayoutRequest disputePayoutRequest, boolean call2PEX) {
        if (call2PEX) {
            return this.paymentServExecBridge.disputePayout(disputePayoutRequest);
        }
        else {
            return this.paymentServBridge.disputePayout(disputePayoutRequest);
        }
    }

    public DisputePayoutRecoupResponse disputePayoutRecoupService(final DisputePayoutRecoupRequest disputePayoutRecoupRequest, boolean call2PEX) {
        if (call2PEX) {
            return this.paymentServExecBridge.disputePayoutRecoup(disputePayoutRecoupRequest);
        }
        else {
            return this.paymentServBridge.disputePayoutRecoup(disputePayoutRecoupRequest);
        }
    }

    public RecoverFundsResponse recoverFundsSerice(final RecoverFundsRequest request, boolean call2PEX) {
        return this.paymentServBridge.recoverFunds(request);
    }

    public ReimbursePaymentSenderResponse reimbursePaymentSenderService(
            final ReimbursePaymentSenderRequest reimbursePaymentSenderRequest, final boolean call2PEX) {

        final ReimbursePaymentSenderResponse response;
        if (call2PEX) {
            response = this.paymentServExecBridge
                    .reimbursePaymentSender(reimbursePaymentSenderRequest);
        }
        else {
            response = this.paymentServBridge
                    .reimbursePaymentSender(reimbursePaymentSenderRequest);
        }
        return response;
    }
}
