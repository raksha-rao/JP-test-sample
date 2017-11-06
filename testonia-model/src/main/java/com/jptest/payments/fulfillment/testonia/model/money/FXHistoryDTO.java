package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.jptest.payments.fulfillment.testonia.model.util.FXDataTrimmer;

/**
 * Represents FX_HISTORY table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FXHistoryDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "amount_from")
    private Long amountFrom;

    @XmlElement(name = "amount_to")
    private Long amountTo;

    @XmlElement(name = "currency_code_from")
    private String currencyCodeFrom;

    @XmlElement(name = "currency_code_to")
    private String currencyCodeTo;

    @XmlTransient
    private String data;
    
    @XmlElement(name = "data")
    private String truncatedData;

    @XmlTransient
    private BigDecimal exchangeRate;
    
    @XmlElement(name = "exchange_rate")
    private String truncatedExchangeRate;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(Long amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Long getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(Long amountTo) {
        this.amountTo = amountTo;
    }

    public String getCurrencyCodeFrom() {
        return currencyCodeFrom;
    }

    public void setCurrencyCodeFrom(String currencyCodeFrom) {
        this.currencyCodeFrom = currencyCodeFrom;
    }

    public String getCurrencyCodeTo() {
        return currencyCodeTo;
    }

    public void setCurrencyCodeTo(String currencyCodeTo) {
        this.currencyCodeTo = currencyCodeTo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.truncatedData = FXDataTrimmer.trimAuditTrail(data);
    }
    
    public String getTruncatedData() {
		return truncatedData;
	}

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    
    public String getTruncatedExchangeRate() {
		return truncatedExchangeRate;
	}

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        this.truncatedExchangeRate = exchangeRate != null ? FXDataTrimmer.trimExchangeRate(exchangeRate.toPlainString()) : null;
        
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

}
