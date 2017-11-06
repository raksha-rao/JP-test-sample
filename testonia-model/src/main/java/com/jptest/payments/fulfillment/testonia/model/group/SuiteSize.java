package com.jptest.payments.fulfillment.testonia.model.group;

public enum SuiteSize {

    SMALL_ENUM(Constants.SMALL),
    MEDIUM_ENUM(Constants.MEDIUM),
    LARGE_ENUM(Constants.LARGE);

    private String value;

    public static final String SMALL = Constants.SMALL;
    public static final String MEDIUM = Constants.MEDIUM;
    public static final String LARGE = Constants.LARGE;

    private SuiteSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static class Constants {
        private static final String SMALL = "SMALL";
        private static final String MEDIUM = "MEDIUM";
        private static final String LARGE = "LARGE";
    }
}
