package com.jptest.payments.fulfillment.testonia.model.dynamicvalue;

public abstract class AbstractFXPlaceHolder extends PlaceHolder {

    public static final String SENDER_CONVERSION_TYPE = "CONVERT_SEND";
    public static final String RECIPIENT_CONVERSION_TYPE = "CONVERT_RECEIVE";

    public AbstractFXPlaceHolder(String originalPlaceholder) {
        super(originalPlaceholder);
    }

    public abstract String buildKey(String fieldName);

    public abstract String getCurrencyFrom();

    public abstract String getFxConversionType();

    public boolean isSenderSideConversion() {
        return isSenderSideConversion(getFxConversionType());
    }

    public static boolean isSenderSideConversion(String conversionType) {
        return SENDER_CONVERSION_TYPE.equals(conversionType);
    }

}
