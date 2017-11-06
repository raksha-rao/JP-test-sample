package com.jptest.payments.fulfillment.testonia.model.group;

/**
 * Defining Operation with enum value (e.g. FULFILL_PAYMENT_ENUM) and constant (FULFILL_PAYMENT) Since groups attribute
 * of @Test requires constant string value, it will use constant FULFILL_PAYMENT. At other places, we can use enum
 * FULFILL_PAYMENT_ENUM
 */
public enum Operation {

    FULFILL_PAYMENT_ENUM(Constants.FULFILL_PAYMENT),
    TRANSFER_ENUM(Constants.TRANSFER),
    TOPUP_ENUM(Constants.TOPUP),
    DEPOSIT_ENUM(Constants.DEPOSIT),
    REFUND_PAYMENT(Constants.REFUND),
    REVERSE_PAYMENT(Constants.REVERSE),
    CREATE_PENDING_REVERSAL_ENUM(Constants.CREATE_PENDING_REVERSAL),
    COMPLETE_PENDING_REVERSAL_ENUM(Constants.COMPLETE_PENDING_REVERSAL),
    CANCEL_PENDING_REVERSAL_ENUM(Constants.CANCEL_PENDING_REVERSAL),
    PAYOUT_ENUM(Constants.PAYOUT),
    INITIATE_REFUND_NEGOTIATION_ENUM(Constants.INITIATE_REFUND_NEGOTIATION),
    CLOSE_REFUND_NEGOTIATION_ENUM(Constants.CLOSE_REFUND_NEGOTIATION),
    CHARGEBACK_ENUM(Constants.CHARGEBACK),
	REPRESENTMENT_ENUM(Constants.REPRESENTMENT),
	RECOUP_ENUM(Constants.RECOUP),
	RESERVE_FUNDS_ENUM(Constants.RESERVE_FUNDS),
	RELEASE_FUNDS_ENUM(Constants.RELEASE_FUNDS),
	DISBURSE_FUNDS_ENUM(Constants.DISBURSE_FUNDS),
	PAYMENT_COMPLETION_ENUM(Constants.PAYMENT_COMPLETION);

    private String value;

    public static final String FULFILL_PAYMENT = Constants.FULFILL_PAYMENT;
    public static final String TRANSFER = Constants.TRANSFER;
    public static final String TOPUP = Constants.TOPUP;
    public static final String DEPOSIT = Constants.DEPOSIT;
    public static final String REFUND = Constants.REFUND;
    public static final String REVERSE = Constants.REVERSE;
    public static final String PAYOUT = Constants.PAYOUT;
    public static final String COMPLETE_PENDING_REVERSAL = Constants.COMPLETE_PENDING_REVERSAL;
    public static final String CREATE_PENDING_REVERSAL = Constants.CREATE_PENDING_REVERSAL;
    public static final String CANCEL_PENDING_REVERSAL = Constants.CANCEL_PENDING_REVERSAL;
    public static final String INITIATE_REFUND_NEGOTIATION = Constants.INITIATE_REFUND_NEGOTIATION;
    public static final String CLOSE_REFUND_NEGOTIATION = Constants.CLOSE_REFUND_NEGOTIATION;
    public static final String CHARGEBACK = Constants.CHARGEBACK;
    public static final String POST_BANK_RETURN = Constants.POST_BANK_RETURN;
    public static final String REPRESENTMENT = Constants.REPRESENTMENT;
    public static final String RECOUP = Constants.RECOUP;
    public static final String RESERVE_FUNDS = Constants.RESERVE_FUNDS;
    public static final String RELEASE_FUNDS = Constants.RELEASE_FUNDS;
    public static final String DISBURSE_FUNDS = Constants.DISBURSE_FUNDS;
    public static final String PAYMENT_COMPLETION = Constants.PAYMENT_COMPLETION;

    private Operation(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    private static class Constants {
        private static final String FULFILL_PAYMENT = "FULFILL_PAYMENT";
        private static final String TRANSFER = "TRANSFER";
        private static final String TOPUP = "TOPUP";
        private static final String DEPOSIT = "DEPOSIT";
        private static final String REFUND = "REFUND";
        private static final String REVERSE = "REVERSE";
        private static final String PAYOUT = "PAYOUT";
        private static final String COMPLETE_PENDING_REVERSAL = "COMPLETE_PENDING_REVERSAL";
        private static final String CREATE_PENDING_REVERSAL = "CREATE_PENDING_REVERSAL";
        private static final String CANCEL_PENDING_REVERSAL = "CANCEL_PENDING_REVERSAL";
        private static final String INITIATE_REFUND_NEGOTIATION = "INITIATE_REFUND_NEGOTIATION";
        private static final String CLOSE_REFUND_NEGOTIATION = "CLOSE_REFUND_NEGOTIATION";
        private static final String CHARGEBACK = "CHARGEBACK";
        private static final String POST_BANK_RETURN = "POST_BANK_RETURN";
        private static final String REPRESENTMENT = "REPRESENTMENT";
        private static final String RECOUP = "RECOUP";
        private static final String RESERVE_FUNDS = "RESERVE_FUNDS";
        private static final String RELEASE_FUNDS = "RELEASE_FUNDS";
        private static final String DISBURSE_FUNDS = "DISBURSE_FUNDS";
        private static final String PAYMENT_COMPLETION = "PAYMENT_COMPLETION";
    }
}
