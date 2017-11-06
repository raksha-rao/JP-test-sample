package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WSUBBALANCE_TRANSACTION table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WSubBalanceTransactionDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "amount")
    private Long amount;

    @XmlElement(name = "available_balance")
    private Long availableBalance;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "reason_code")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte reasonCode;

    @XmlElement(name = "subbalance_type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte subBalanceType;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

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

    public Long getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Long availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Byte getSubBalanceType() {
        return subBalanceType;
    }

    public void setSubBalanceType(Byte subBalanceType) {
        this.subBalanceType = subBalanceType;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public Byte getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(Byte reasonCode) {
        this.reasonCode = reasonCode;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

}
