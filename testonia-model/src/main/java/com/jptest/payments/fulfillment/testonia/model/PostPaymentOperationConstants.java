package com.jptest.payments.fulfillment.testonia.model;

/**
 * PostPaymentOperationConstants defines the operation names to be given in test data, json
 *
 * @JP Inc.
 */
public class PostPaymentOperationConstants {

    public static final String REFUND_PAYMENT = "refund_payment";
    public static final String jptest_PAYOUT = "jptest_payout";
    public static final String REVERSE_PAYMENT = "reverse_payment";
    public static final String REVERSE_FEE = "reverse_fee";
    public static final String CREATE_PENDING_PAYMENT_REVERSAL = "create_pending_payment_reversal";
    public static final String CANCLE_PENDING_PAYMENT_REVERSAL = "cancel_pending_payment_reversal";
    public static final String COMPLETE_PENDING_PAYMENT_REVERSAL = "complete_pending_payment_reversal";
    public static final String POST_BANK_RETURN = "post_bank_return";
    public static final String CREATE_HOLD = "create_hold";
    public static final String RELEASE_HOLD = "release_hold";
    public static final String CREATE_RESERVE = "create_reserve";
    public static final String RELEASE_RESERVE = "release_reserve";
    public static final String TRANSITION_TO_HOLD = "transition_to_hold";
    public static final String TRANSITION_TO_RESERVE = "transition_to_reserve";
    public static final String REVERSE_jptest_PAYOUT = "reverse_jptest_payout";
    public static final String WITHDRAW = "withdraw";
    public static final String UPDATE_TRANSACTION_PROPERTY = "update_transaction_property";
    public static final String INITIATE_REFUND_NEGOTIATION = "initiate_refund_negotiation";
    public static final String CLOSE_REFUND_NEGOTIATION = "close_refund_negotiation";
    public static final String RECOVER_DISPUTED_FUNDS = "recover_disputed_funds";
    public static final String REIMBURSE_PAYMENT_RECEIVER = "reimburse_payment_receiver";
    public static final String REIMBURSE_PAYMENT_SENDER = "reimburse_payment_sender";
    public static final String RELEASE_PENDING_PAYMENTS_HOLDS = "release_pending_payments_holds";
    public static final String DO_COMPLETION = "do_completion";
    public static final String DISPUTE_PAYOUT = "dispute_payout";
    public static final String DISPUTE_PAYOUT_RECOUP = "dispute_payout_recoup";
    public static final String RECOVER_FUNDS = "recover_funds";
    public static final String FULFILL_DISBURSEMENT = "fulfill_disbursement";
    public static final String MLP_FINALIZE = "mlp_finalize";
    public static final String COMPLETE_UNILATERAL_PAYMENTS_FOR_ALIAS = "complete_unilateral_payments_for_alias";
    public static final String COMPLETE_TRANSFER = "complete_transfer";
    public static final String REVIEW_TRANSFER = "review_transfer";
    public static final String REVERSE_TRANSFER = "reverse_transfer";
    public static final String FULFILL_PAYOFF_TAB = "fulfill_payoff_tab";
}
