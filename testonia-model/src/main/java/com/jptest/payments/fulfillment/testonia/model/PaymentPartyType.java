package com.jptest.payments.fulfillment.testonia.model;

public enum PaymentPartyType {

    BUYER("buyer"),
    SELLER("seller");

    private String name;

    private PaymentPartyType(String paymentPartyType) {
        this.name = paymentPartyType;
    }

    public String getName() {
        return name;
    }

}
