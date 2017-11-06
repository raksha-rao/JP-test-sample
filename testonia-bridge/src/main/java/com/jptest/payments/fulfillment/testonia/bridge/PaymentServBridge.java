package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.money.AuthRequest;
import com.jptest.money.AuthResponse;
import com.jptest.money.CancelPendingPaymentReversalRequest;
import com.jptest.money.CancelPendingPaymentReversalResponse;
import com.jptest.money.CloseRefundNegotiationRequest;
import com.jptest.money.CloseRefundNegotiationResponse;
import com.jptest.money.CompletePendingPaymentReversalRequest;
import com.jptest.money.CompletePendingPaymentReversalResponse;
import com.jptest.money.CompleteUnilateralPaymentsForAliasRequest;
import com.jptest.money.CompleteUnilateralPaymentsForAliasResponse;
import com.jptest.money.CreateHoldRequest;
import com.jptest.money.CreateHoldResponse;
import com.jptest.money.CreatePendingPaymentReversalRequest;
import com.jptest.money.CreatePendingPaymentReversalResponse;
import com.jptest.money.CreateReserveRequest;
import com.jptest.money.CreateReserveResponse;
import com.jptest.money.DisputePayoutRecoupRequest;
import com.jptest.money.DisputePayoutRecoupResponse;
import com.jptest.money.DisputePayoutRequest;
import com.jptest.money.DisputePayoutResponse;
import com.jptest.money.FulfillDisbursementRequest;
import com.jptest.money.FulfillDisbursementResponse;
import com.jptest.money.FulfillPayoffTabRequest;
import com.jptest.money.FulfillPayoffTabResponse;
import com.jptest.money.HoldsLifeCycle;
import com.jptest.money.InitiateRefundNegotiationRequest;
import com.jptest.money.InitiateRefundNegotiationResponse;
import com.jptest.money.NewActivityIdRequest;
import com.jptest.money.NewActivityIdResponse;
import com.jptest.money.PayRequest;
import com.jptest.money.PayResponse;
import com.jptest.money.PaymentServ;
import com.jptest.money.jptestPayoutRequest;
import com.jptest.money.jptestPayoutResponse;
import com.jptest.money.PostBankReturnRequest;
import com.jptest.money.PostBankReturnResponse;
import com.jptest.money.Posting;
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
import com.jptest.money.ReleaseHoldRequest;
import com.jptest.money.ReleaseHoldResponse;
import com.jptest.money.ReleasePendingPaymentsHoldsRequest;
import com.jptest.money.ReleasePendingPaymentsHoldsResponse;
import com.jptest.money.ReleaseReserveRequest;
import com.jptest.money.ReleaseReserveResponse;
import com.jptest.money.ReverseFeeRequest;
import com.jptest.money.ReverseFeeResponse;
import com.jptest.money.ReversePaymentRequest;
import com.jptest.money.ReversePaymentResponse;
import com.jptest.money.ReversejptestPayoutRequest;
import com.jptest.money.ReversejptestPayoutResponse;
import com.jptest.money.SinglePartyFulfillment;
import com.jptest.money.TransitionToHoldRequest;
import com.jptest.money.TransitionToHoldResponse;
import com.jptest.money.TransitionToReserveRequest;
import com.jptest.money.TransitionToReserveResponse;
import com.jptest.money.UpdateTransactionPropertyRequest;
import com.jptest.money.UpdateTransactionPropertyResponse;
import com.jptest.money.WithdrawRequest;
import com.jptest.money.WithdrawResponse;

/**
 * Represents bridge for paymentserv API calls
 */
@Singleton
public class PaymentServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServBridge.class);

    @Inject
    @Named("paymentserv")
    private PaymentServ paymentServ;

    @Inject
    @Named("paymentserv")
    private HoldsLifeCycle holdsLifeCycle;

    @Inject
    @Named("paymentserv")
    private Posting postingServ;

    @Inject
    @Named("paymentserv")
    private SinglePartyFulfillment singlepartyfulfillment;

    /**
     * Call to do withdrawal
     *
     * @param request
     * @return
     */

    public WithdrawResponse doWithdrawal(final WithdrawRequest request) {
        LOGGER.info("Do WithDrawal Request {}:", printValueObject(request));
        final WithdrawResponse response = this.singlepartyfulfillment.withdraw(request);
        LOGGER.info("Do WithDrawal Response {}:", printValueObject(response));
        return response;
    }

    /**
     * Call to create activityId
     *
     * @return
     */
    public BigInteger createActivityId() {
        final NewActivityIdRequest newActivityIdRequest = new NewActivityIdRequest();
        final NewActivityIdResponse response = this.paymentServ.new_activity_id(newActivityIdRequest);
        LOGGER.info("new_activity_id response {}:", printValueObject(response));
        return response.getActivityId();
    }

    /**
     * Call to pay() operation
     *
     * @param payRequest
     * @return
     */
    public PayResponse pay(final PayRequest request) {
        LOGGER.info("pay request: {}", printValueObject(request));
        final PayResponse response = this.paymentServ.pay(request);
        LOGGER.info("pay response: {}", printValueObject(response));
        return response;
    }

    /**
     * Calls auth() operation
     *
     * @param AuthRequest
     * @return AuthResponse
     * 
     */
    public AuthResponse auth(final AuthRequest request) {
        LOGGER.info("auth request: {}", printValueObject(request));
        final AuthResponse response = this.paymentServ.auth(request);
        LOGGER.info("auth response: {}", printValueObject(response));
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
        final ReleasePendingPaymentsHoldsResponse response = this.paymentServ.release_pending_payments_holds(request);
        LOGGER.info("ReleasePendingPaymentsHoldsResponse response: {}", printValueObject(response));
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
        final ReversePaymentResponse response = this.paymentServ.reverse_payment(request);
        LOGGER.info("ReversePaymentResponse response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to refund_payment operation
     *
     * @param RefundPaymentRequest
     * @return
     */
    public RefundPaymentResponse refundPayment(final RefundPaymentRequest request) {
        LOGGER.info("refund_payment request: {}", printValueObject(request));
        final RefundPaymentResponse response = this.paymentServ.refund_payment(request);
        LOGGER.info("refund_payment response: {}", printValueObject(response));
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
        final jptestPayoutResponse response = this.paymentServ.jptest_payout(request);
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
        final CreatePendingPaymentReversalResponse response = this.paymentServ.create_pending_payment_reversal(request);
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
        final CancelPendingPaymentReversalResponse response = this.paymentServ.cancel_pending_payment_reversal(request);
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
        final CompletePendingPaymentReversalResponse response = this.paymentServ
                .complete_pending_payment_reversal(request);
        LOGGER.info("complete_pending_payment_reversal response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to create_hold() operation
     *
     * @param CreateHoldRequest
     * @return
     */
    public CreateHoldResponse createHold(final CreateHoldRequest request) {
        LOGGER.info("CreateHold request: {}", printValueObject(request));
        final CreateHoldResponse response = this.holdsLifeCycle.create_hold(request);
        LOGGER.info("CreateHold response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to release_hold() operation
     *
     * @param ReleaseHoldRequest
     * @return
     */
    public ReleaseHoldResponse releaseHold(final ReleaseHoldRequest request) {
        LOGGER.info("ReleaseHold request: {}", printValueObject(request));
        final ReleaseHoldResponse response = this.holdsLifeCycle.release_hold(request);
        LOGGER.info("ReleaseHold response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to create_reserve() operation
     *
     * @param CreateReserveRequest
     * @return
     */
    public CreateReserveResponse createReserve(final CreateReserveRequest request) {
        LOGGER.info("CreateReserve request: {}", printValueObject(request));
        final CreateReserveResponse response = this.holdsLifeCycle.create_reserve(request);
        LOGGER.info("CreateReserve response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to release_reserve() operation
     *
     * @param ReleaseReserveRequest
     * @return
     */
    public ReleaseReserveResponse releaseReserve(final ReleaseReserveRequest request) {
        LOGGER.info("ReleaseReserve request: {}", printValueObject(request));
        final ReleaseReserveResponse response = this.holdsLifeCycle.release_reserve(request);
        LOGGER.info("ReleaseReserve response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to transition_to_hold() operation
     *
     * @param TransitionToHoldRequest
     * @return
     */
    public TransitionToHoldResponse transitionToHold(final TransitionToHoldRequest request) {
        LOGGER.info("TransitionToHold request: {}", printValueObject(request));
        final TransitionToHoldResponse response = this.holdsLifeCycle.transition_to_hold(request);
        LOGGER.info("TransitionToHold response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to transition_to_reserve() operation
     *
     * @param TransitionToReserveRequest
     * @return
     */
    public TransitionToReserveResponse transitionToReserve(final TransitionToReserveRequest request) {
        LOGGER.info("TransitionToReserve request: {}", printValueObject(request));
        final TransitionToReserveResponse response = this.holdsLifeCycle.transition_to_reserve(request);
        LOGGER.info("TransitionToReserve response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to post_bank_return Operation
     *
     * @param PostBankReturnRequest
     * @return PostBankReturnResponse
     */
    public PostBankReturnResponse postBankReturn(final PostBankReturnRequest request) {
        LOGGER.info("PostBankReturnRequest: {}", printValueObject(request));
        final PostBankReturnResponse response = this.postingServ.post_bank_return(request);
        LOGGER.info("PostBankReturnResponse: {}", printValueObject(response));
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
        final ReversejptestPayoutResponse response = this.paymentServ.reverse_jptest_payout(request);
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
        final UpdateTransactionPropertyResponse response = this.paymentServ.update_transaction_property(request);
        LOGGER.info("update_transaction_property response: {}", printValueObject(response));
        return response;
    }


    /**
     * Call to initiate refund negotiation API 
     *
     */
    public InitiateRefundNegotiationResponse initiateRefundNegotiation(final InitiateRefundNegotiationRequest request) {

        LOGGER.info("initiate refund negotiation request: {}", printValueObject(request));
        final InitiateRefundNegotiationResponse response = this.paymentServ.initiate_refund_negotiation(request);
        LOGGER.info("initiate refund negotiation response: {}", printValueObject(response));
        return response;
    }


    /**
     * Call to initiate close refund negotiation API
     *
     */
    public CloseRefundNegotiationResponse closeRefundNegotiation(final CloseRefundNegotiationRequest request) {

    	LOGGER.info("close refund negotiation request: {}", printValueObject(request));
    	final CloseRefundNegotiationResponse response = this.paymentServ.close_refund_negotiation(request);
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
        final RecoverDisputedFundsResponse response = this.paymentServ.recover_disputed_funds(request);
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
        final ReimbursePaymentReceiverResponse response = this.paymentServ.reimburse_payment_receiver(request);
        LOGGER.info("reimburse_payment_receiver response: {}", printValueObject(response));
        return response;
    }
    
    /**
     * Call to fulfill_disbursement() operation
     *
     * @param FulfillDisbursementRequest
     * @return FulfillDisbursementResponse
     */
    public FulfillDisbursementResponse fulfillDisbursement(final FulfillDisbursementRequest request) {
        LOGGER.info("fulfill_disbursement request: {}", printValueObject(request));
        final FulfillDisbursementResponse response = this.singlepartyfulfillment.fulfill_disbursement(request);
        LOGGER.info("fulfill_disbursement response: {}", printValueObject(response));
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
        final DisputePayoutResponse response = this.paymentServ.dispute_payout(request);
        LOGGER.info("dispute_payout response: {}", printValueObject(response));
        return response;
    }

    public ReverseFeeResponse reverseFee(ReverseFeeRequest request) {
        LOGGER.info("ReverseFeeRequest request: {}", printValueObject(request));
        final ReverseFeeResponse response = this.paymentServ.reverse_fee(request);
        LOGGER.info("ReverseFeeResponse response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to dipsute_payout_recoup() operation
     *
     * @param DisputePayoutRecoupRequest
     * @return DisputePayoutRecoupResponse
     */
    public DisputePayoutRecoupResponse disputePayoutRecoup(final DisputePayoutRecoupRequest request) {
        LOGGER.info("dipsute_payout_recoup request: {}", printValueObject(request));
        final DisputePayoutRecoupResponse response = this.paymentServ.dispute_payout_recoup(request);
        LOGGER.info("dipsute_payout_recoup response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to recover_funds() operation
     *
     * @param RecoverFundsRequest
     * @return RecoverFundsResponse
     */
    public RecoverFundsResponse recoverFunds(final RecoverFundsRequest request) {
        LOGGER.info("recover_funds request: {}", printValueObject(request));
        final RecoverFundsResponse response = this.singlepartyfulfillment.recover_funds(request);
        LOGGER.info("recover_funds response: {}", printValueObject(response));
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
        final ReimbursePaymentSenderResponse response = this.paymentServ.reimburse_payment_sender(request);
        LOGGER.info("reimburse_payment_sender response: {}", printValueObject(response));
        return response;
    }
    
    /**
     * Call to complete_unilateral_payments_for_alias operation
     * 
     * @param CompleteUnilateralPaymentsForAliasRequest
     * @return CompleteUnilateralPaymentsForAliasResponse
     */
    public CompleteUnilateralPaymentsForAliasResponse completeUnilateralPaymentsForAlias(
    		CompleteUnilateralPaymentsForAliasRequest request) {
        LOGGER.info("CompleteUnilateralPaymentsForAlias request: {}", printValueObject(request));
        final CompleteUnilateralPaymentsForAliasResponse response = this.paymentServ.
        		complete_unilateral_payments_for_alias(request);
        LOGGER.info("CompleteUnilateralPaymentsForAlias response: {}", printValueObject(response));
        return response;
    }

    /**
     * Call to fulfill_payoff_tab() operation
     *
     * @param FulfillPayoffTabRequest
     * @return FulfillPayoffTabResponse
     */
	public FulfillPayoffTabResponse fulfillPayoffTab(final FulfillPayoffTabRequest request) {
		LOGGER.info("fulfill_payoff_tab request: {}", printValueObject(request));
		final FulfillPayoffTabResponse response = this.singlepartyfulfillment.fulfill_payoff_tab(request);
		LOGGER.info("fulfill_payoff_tab response: {}", printValueObject(response));
		return response;
	}
}
