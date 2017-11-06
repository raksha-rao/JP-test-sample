package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WTRANSACTION_WORLD table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WTransactionWorldDTO {

    @XmlElement(name = "amount_in_local_currency")
    private BigInteger amountInLocalCurrency;

    @XmlElement(name = "country")
    private String country;

    @XmlElement(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @XmlElement(name = "exchange_rate_id")
    private Integer exchangeRateId;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "interbank_rate")
    private BigDecimal interBankRate;

    @XmlElement(name = "local_currency_code")
    private String localCurrencyCode;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    public void setAmountInLocalCurrency(BigInteger amountInLocalCurrency) {
        this.amountInLocalCurrency = amountInLocalCurrency;
    }

    public BigInteger getAmountInLocalCurrency() {
        return amountInLocalCurrency;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setLocalCurrencyCode(String localCurrencyCode) {
        this.localCurrencyCode = localCurrencyCode;
    }

    public String getLocalCurrencyCode() {
        return localCurrencyCode;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRateId(Integer exchangeRateId) {
        this.exchangeRateId = exchangeRateId;
    }

    public Integer getExchangeRateId() {
        return exchangeRateId;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public Long getFlags() {
        return flags;
    }

    public Map<String, Long> getFlagDetails() {
        // TODO: convert flag to map
        return null;
    }

    public void setInterBankRate(BigDecimal interBankRate) {
        this.interBankRate = interBankRate;
    }

    public BigDecimal getInterBankRate() {
        return interBankRate;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }
}
