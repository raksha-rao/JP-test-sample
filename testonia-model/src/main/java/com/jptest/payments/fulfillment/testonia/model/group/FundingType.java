package com.jptest.payments.fulfillment.testonia.model.group;

/**
 * Defining FundingType with enum value (e.g. IACH_ENUM) and constant (IACH)
 * Since groups attribute of @Test requires constant string value, it will use constant IACH.
 * At other places, we can use enum IACH_ENUM
 */
public enum FundingType {

    IACH_ENUM(Constants.IACH),
    CHARGE_ENUM(Constants.CHARGE),
    BALANCE_ENUM(Constants.BALANCE),
    ECHECK_ENUM(Constants.ECHECK),
    ELV_ENUM(Constants.ELV),
    INCENTIVE_ENUM(Constants.INCENTIVE),
    EXTERNAL_INCENTIVE_ENUM(Constants.EXTERNAL_INCENTIVE),
    VIRTUAL_LINE_ENUM(Constants.VIRTUAL_LINE),
    DUAL_CARD_ENUM(Constants.DUAL_CARD),
    PINLESS_DEBIT_ENUM(Constants.PINLESS_DEBIT);

    private String value;

    public static final String IACH = Constants.IACH;
    public static final String CHARGE = Constants.CHARGE;
    public static final String BALANCE = Constants.BALANCE;
    public static final String ECHECK = Constants.ECHECK;
    public static final String ELV = Constants.ELV;
    public static final String INCENTIVE = Constants.INCENTIVE;
    public static final String EXTERNAL_INCENTIVE = Constants.EXTERNAL_INCENTIVE;
    public static final String PINLESS_DEBIT = Constants.PINLESS_DEBIT;
    public static final String DUAL_CARD = Constants.DUAL_CARD;
    public static final String VIRTUAL_LINE = Constants.VIRTUAL_LINE;

    private FundingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static class Constants {
        private static final String IACH = "IACH";
        private static final String CHARGE = "CHARGE";
        private static final String BALANCE = "BALANCE";
        private static final String ECHECK = "ECHECK";
        private static final String ELV = "ELV";
        private static final String INCENTIVE = "INCENTIVE";
        private static final String EXTERNAL_INCENTIVE = "EXTERNAL_INCENTIVE";
        private static final String PINLESS_DEBIT = "PINLESS_DEBIT";
        private static final String VIRTUAL_LINE = "BC_VLINE";
        private static final String DUAL_CARD = "BC_DCARD";
    }
}
