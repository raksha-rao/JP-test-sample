package com.jptest.payments.fulfillment.testonia.model.group;

/**
 * Defining Priority with enum value (e.g. P1_ENUM) and constant (P1)
 * Since groups attribute of @Test requires constant string value, it will use constant P1.
 * At other places, we can use enum P1_ENUM
 */
public enum Priority {

    P1_ENUM(Constants.P1),
    P2_ENUM(Constants.P2),
    P3_ENUM(Constants.P3),
    P4_ENUM(Constants.P4);

    private String value;

    public static final String P1 = Constants.P1;
    public static final String P2 = Constants.P2;
    public static final String P3 = Constants.P3;
    public static final String P4 = Constants.P4;

    private Priority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static class Constants {
        private static final String P1 = "P1";
        private static final String P2 = "P2";
        private static final String P3 = "P3";
        private static final String P4 = "P4";
    }
}
