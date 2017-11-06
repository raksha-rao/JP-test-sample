package com.jptest.payments.fulfillment.testonia.model.risk;

public enum RiskHoldType {

    S2F("S2F"),
    ALH("ALH"),
    IPR("IPR");

    private String name;

    private RiskHoldType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
