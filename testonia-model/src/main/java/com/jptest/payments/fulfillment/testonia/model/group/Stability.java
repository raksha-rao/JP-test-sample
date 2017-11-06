package com.jptest.payments.fulfillment.testonia.model.group;

/**
 * Defining various degrees of stability of test cases. This is to distinguish bet'n stable, somewhat unstable and
 * completely uncooked (not ready) test cases. The tests depending on their execution trends will vary among these
 * stability values.
 */
public enum Stability {

    STABLE_ENUM(Constants.STABLE),
    UNSTABLE_ENUM(Constants.UNSTABLE),
    NOTREADY_FOR_ECI_ENUM(Constants.NOTREADY_FOR_ECI),
    NOTREADY_ENUM(Constants.NOTREADY),
    NOTREADY_FOR_PLACEHOLDER_ENUM(Constants.NOTREADY_FOR_PLACEHOLDER),
    NOTREADY_FOR_IPN_ENUM(Constants.NOTREADY_FOR_IPN),

    NOT_YET_LIVE_ENUM(Constants.NOT_YET_LIVE);

    private String value;

    public static final String STABLE = Constants.STABLE;
    public static final String UNSTABLE = Constants.UNSTABLE;
    public static final String NOTREADY = Constants.NOTREADY;
    public static final String NOTREADY_FOR_ECI = Constants.NOTREADY_FOR_ECI;
    public static final String NOTREADY_FOR_PLACEHOLDER = Constants.NOTREADY_FOR_PLACEHOLDER;
    public static final String NOTREADY_FOR_IPN = Constants.NOTREADY_FOR_IPN;

    public static final String NOT_YET_LIVE = Constants.NOT_YET_LIVE;

    private Stability(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static class Constants {
        private static final String STABLE = "STABLE";
        private static final String UNSTABLE = "UNSTABLE";
        private static final String NOTREADY = "NOTREADY";
        private static final String NOTREADY_FOR_ECI = "NOTREADY_FOR_ECI";
        private static final String NOTREADY_FOR_PLACEHOLDER = "NOTREADY_FOR_PLACEHOLDER";
        private static final String NOTREADY_FOR_IPN = "NOTREADY_FOR_IPN";

        private static final String NOT_YET_LIVE = "NOT_YET_LIVE";
    }
}
