package com.jptest.payments.fulfillment.testonia.model.risk.fmf;

/**
 * Represents the input for RiskFMFActionType.Transaction_Amount FMF filter.
 */
public class TransactionAmountFMFData extends BaseFMFData {

    private long amount;

    private String currency;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public RiskFMFFilterType getFilterType() {
        return RiskFMFFilterType.Transaction_Amount;
    }

}
