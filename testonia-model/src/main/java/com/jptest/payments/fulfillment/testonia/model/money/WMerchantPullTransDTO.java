package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WMERCHANT_PULL_TRANS table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WMerchantPullTransDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "amount")
    private Long amount;

    @XmlElement(name = "ba_id")
    private Long baId;

    @XmlElement(name = "item")
    private BigInteger counterpartyTransactionId;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "merchant_account_number")
    private BigInteger merchantAccountNumber;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    @XmlElement(name = "trans_data_map_id")
    private BigInteger transDataMapId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBaId() {
        return baId;
    }

    public void setBaId(Long baId) {
        this.baId = baId;
    }

    public BigInteger getCounterpartyTransactionId() {
        return counterpartyTransactionId;
    }

    public void setCounterpartyTransactionId(BigInteger counterpartyTransactionId) {
        this.counterpartyTransactionId = counterpartyTransactionId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public BigInteger getMerchantAccountNumber() {
        return merchantAccountNumber;
    }

    public void setMerchantAccountNumber(BigInteger merchantAccountNumber) {
        this.merchantAccountNumber = merchantAccountNumber;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    public BigInteger getTransDataMapId() {
        return transDataMapId;
    }

    public void setTransDataMapId(BigInteger transDataMapId) {
        this.transDataMapId = transDataMapId;
    }

}
