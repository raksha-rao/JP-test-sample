package com.jptest.payments.fulfillment.testonia.model.group;

/*
 * This is a copy of com.jptest.money.FundingMethodType
 */
public enum FundingMethodType {

    /**
     * IACH - instrument_type = BANK funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = FALSE disbursement_funds_availability = INSTANT
     */
    IACH("IACH", "IACH"),
    /**
     * BONUS - instrument_type = BANK funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = FALSE disbursement_funds_availability = INSTANT
     */
    BONUS("BONUS", "BONUS"),
    /**
     * ECHECK - instrument_type = BANK funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = TRUE disbursement_funds_availability = DELAYED
     */
    ECHECK("ECHECK", "ECHEK"),
    /**
     * MEFT - instrument_type = BANK funds_transfer_initiator = CUSTOMER funds_transfer_initiation_time = DELAYED
     * good_funds_before_disbursement = TRUE disbursement_funds_availability = DELAYED
     */
    MEFT("MEFT", "MEFT"),
    /**
     * IEFT - instrument_type = BANK funds_transfer_initiator = CUSTOMER funds_transfer_initiation_time = DELAYED
     * good_funds_before_disbursement = TRUE disbursement_funds_availability = DELAYED
     */
    IEFT("IEFT", "IEFT"),
    /**
     * UACH - @@Deprecated.Going forward Funding Method IACH with unverified instrument will be used for UACH /
     * UACHWITHOUTBUFS Payment method. instrument_type = BANK funds_transfer_initiator = jptest
     * funds_transfer_initiation_time = WITH_PAYMENT good_funds_before_disbursement = FALSE
     * disbursement_funds_availability = INSTANT
     */
    UACH("UACH", "UACH"),
    /**
     * CHARGE - instrument_type = CARD funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = TRUE disbursement_funds_availability = INSTANT
     */
    CHARGE("CHARGE", "CHARG"),
    /**
     * DEFERRED_CHARGE - instrument_type = CARD funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability = DELAYED
     */
    DEFERRED_CHARGE("DEFERRED_CHARGE", "DCHARG"),
    /**
     * PINLESS_DEBIT - instrument_type = CARD funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability = INSTANT
     * PROCESSING_CAPABILITY = SMS
     */
    PINLESS_DEBIT("PINLESS_DEBIT", "PLDEB"),
    /**
     * ELV - instrument_type = BANK funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = FALSE disbursement_funds_availability = INSTANT
     */
    ELV("ELV", "ELV"),
    /**
     * UELV - instrument_type = BANK funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = FALSE disbursement_funds_availability = INSTANT
     */
    UELV("UELV", "UELV"),
    /**
     * VIRTUAL_LINE - instrument_type = PPCREDIT funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability = INSTANT
     */
    VIRTUAL_LINE("VIRTUAL_LINE", "VLINE"),
    /**
     * DUAL_CARD - instrument_type = PPCREDIT funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability = INSTANT
     */
    DUAL_CARD("DUAL_CARD", "DCARD"),
    /**
     * TRANSACTIONAL_CREDIT - instrument_type = PPCREDIT funds_transfer_initiator = jptest
     * funds_transfer_initiation_time = WITH_PAYMENT good_funds_before_disbursement = TRUE
     * disbursement_funds_availability = INSTANT
     */
    TRANSACTIONAL_CREDIT("TRANSACTIONAL_CREDIT", "TCRED"),
    /**
     * HOLDING - instrument_type = HOLDING funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability = INSTANT
     */
    HOLDING("HOLDING", "HOLD"),
    /**
     * UNVERIFIED_ECHECK - instrument_type = BANK funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability = DELAYED confirmed_instrument
     * = FALSE
     */
    UNVERIFIED_ECHECK("UNVERIFIED_ECHECK", "UVCHK"),
    /**
     * IACH_HOP - instrument_type = BANK funds_transfer_initiator = jptest funds_transfer_initiation_time = DELAYED
     * good_funds_before_disbursement = FALSE disbursement_funds_availability_time = INSTANT
     */
    IACH_HOP("IACH_HOP", "IACHP"),
    /**
     * CUP - instrument_type = BANK (CARD) funds_transfer_initiator = CUSTOMER funds_transfer_initiation_time =
     * BEFORE_PAYMENT/AFTER_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability_time =
     * INSTANT/DELAYED
     */
    CUP("CUP", "CUP"),
    /**
     * CHINA_PAY - instrument_type = BANK funds_transfer_initiator = CUSTOMER funds_transfer_initiation_time =
     * good_funds_before_disbursement = YES disbursement_funds_availability_time = INSTANT
     */
    CHINA_PAY("CHINA_PAY", "CHPAY"),
    /**
     * ELV_ECHECK - instrument_type = BANK funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = YES disbursement_funds_availability_time = DELAYED
     */
    ELV_ECHECK("ELV_ECHECK", "ELVEC"),
    /**
     * TAB - instrument_type = TAB funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = TRUE disbursement_funds_availability_time = INSTANT
     */
    TAB("TAB", "TAB"),
    /**
     * INCENTIVE - instrument_type = INCENTIVE funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = YES disbursement_funds_availability_time = INSTANT
     */
    INCENTIVE("INCENTIVE", "INCEN"),
    /**
     * PAP_TAB - instrument_type = PAP_TAB funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability_time = INSTANT
     */
    PAP_TAB("PAP_TAB", "PAPTB"),
    /**
     * PUI_TAB - instrument_type = PUI_TAB funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability_time = INSTANT
     */
    PUI_TAB("PUI_TAB", "PUITB"),
    /**
     * PAD_TAB - instrument_type = PAD_TAB funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability_time = INSTANT
     */
    PAD_TAB("PAD_TAB", "PADTB"),
    /**
     * MERCHANT_SPECIFIC_BALANCE - instrument_type = INCENTIVE funds_transfer_initiator = jptest
     * funds_transfer_initiation_time = WITH_PAYMENT good_funds_before_disbursement = YES
     * disbursement_funds_availability_time = INSTANT
     */
    MERCHANT_SPECIFIC_BALANCE("MERCHANT_SPECIFIC_BALANCE", "MSB"),
    /**
     * FLOAT_TAB - instrument_type = FLOAT_TAB funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability_time = INSTANT
     */
    FLOAT_TAB("FLOAT_TAB", "FLTTB"),
    /**
     * INSTANT_GENERIC_INSTRUMENT - instrument_type = GENERIC_INSTRUMENT funds_transfer_initiator = jptest
     * funds_transfer_initiation_time = WITH_PAYMENT good_funds_before_disbursement = TRUE
     * disbursement_funds_availability_time = INSTANT
     */
    INSTANT_GENERIC_INSTRUMENT("INSTANT_GENERIC_INSTRUMENT", "INSGI"),
    /**
     * DEFERRED_GENERIC_INSTRUMENT - instrument_type = GENERIC_INSTRUMENT funds_transfer_initiator = jptest
     * funds_transfer_initiation_time = WITH_PAYMENT good_funds_before_disbursement = TRUE
     * disbursement_funds_availability_time = INSTANT
     */
    DEFERRED_GENERIC_INSTRUMENT("DEFERRED_GENERIC_INSTRUMENT", "DEFGI"),
    /**
     * CUP_UPOP - instrument_type = CARD funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = TRUE disbursement_funds_availability_time = INSTANT
     */
    CUP_UPOP("CUP_UPOP", "CUP_UPOP"),
    /**
     * RECEIVABLE - instrument_type = NONE funds_transfer_initiator = jptest funds_transfer_initiation_time =
     * WITH_PAYMENT good_funds_before_disbursement = TRUE disbursement_funds_availability_time = INSTANT
     */
    RECEIVABLE("RECEIVABLE", "RECBL"),
    /**
     * EXTERNAL_WALLET - instrument_type = GENERIC_INSTRUMENT funds_transfer_initiator = jptest
     * funds_transfer_initiation_time = WITH_PAYMENT good_funds_before_disbursement = TRUE
     * disbursement_funds_availability_time = INSTANT
     */
    EXTERNAL_WALLET("EXTERNAL_WALLET", "EXTWALLET"),
    /**
     * PAYABLES - instrument_type = NONE funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = TRUE disbursement_funds_availability_time = DELAY
     */
    PAYABLES("PAYABLES", "PAYBL"),
    /**
     * DELAYED - instrument_type = NONE funds_transfer_initiator = jptest funds_transfer_initiation_time = WITH_PAYMENT
     * good_funds_before_disbursement = TRUE disbursement_funds_availability_time = DELAY
     */
    DELAYED("DELAYED", "DLYD");

    private final String name;
    private final String value;

    private FundingMethodType(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

}
