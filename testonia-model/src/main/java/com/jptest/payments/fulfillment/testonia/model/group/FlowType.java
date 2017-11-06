package com.jptest.payments.fulfillment.testonia.model.group;

/**
 * Defining FlowType with enum value (e.g. SALE_ENUM) and constant (SALE) Since groups attribute of @Test requires
 * constant string value, it will use constant SALE. At other places, we can use enum SALE_ENUM
 */
public enum FlowType {

    SALE_ENUM(Constants.SALE),
    AUTH_SETTLE_V1_ENUM(Constants.AUTH_SETTLE_V1),
    AUTH_SETTLE_V2_ENUM(Constants.AUTH_SETTLE_V2),
    REFERENCE_TRANSACTION_ENUM(Constants.REFERENCE_TRANSACTION),
    RECURRING_PAYMENT_ENUM(Constants.RECURRING_PAYMENT),
    SEND_MONEY_ENUM(Constants.SEND_MONEY),
    P2P_ENUM(Constants.P2P);

    private String value;

    public static final String SALE = Constants.SALE;
    public static final String AUTH_SETTLE_V1 = Constants.AUTH_SETTLE_V1;
    public static final String AUTH_SETTLE_V2 = Constants.AUTH_SETTLE_V2;
    public static final String REFERENCE_TRANSACTION = Constants.REFERENCE_TRANSACTION;
    public static final String RECURRING_PAYMENT = Constants.RECURRING_PAYMENT;
    public static final String SEND_MONEY = Constants.SEND_MONEY;
    public static final String P2P = Constants.P2P;

    private FlowType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static class Constants {
        private static final String SALE = "SALE";
        private static final String AUTH_SETTLE_V1 = "AUTH_SETTLE_V1";
        private static final String AUTH_SETTLE_V2 = "AUTH_SETTLE_V2";
        private static final String REFERENCE_TRANSACTION = "REFERENCE_TRANSACTION";
        private static final String RECURRING_PAYMENT = "RECURRING_PAYMENT";
        private static final String SEND_MONEY = "SEND_MONEY";
        private static final String P2P = "P2P";
    }
}
