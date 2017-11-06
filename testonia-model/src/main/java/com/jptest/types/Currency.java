package com.jptest.types;

public class Currency {
    private String currencyCode;
    private Long amount;

    public Currency(String currencyCode, long amount) {

    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
