package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WWAX_TRANSACTION table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WwaxTransactionDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "alias_id")
    private BigInteger aliasId;

    @XmlElement(name = "cc_login_account_number")
    private BigInteger ccLoginAccountNumber;

    @XmlElement(name = "email_login_account_number")
    private BigInteger emailLoginAccountNumber;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "receipt_id")
    private BigInteger receiptId;

    @XmlElement(name = "time_created")
    private Long timeCreated;

    @XmlElement(name = "trans_data_map_id")
    private BigInteger transDataMapId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigInteger getAliasId() {
        return aliasId;
    }

    public void setAliasId(BigInteger aliasId) {
        this.aliasId = aliasId;
    }

    public BigInteger getCcLoginAccountNumber() {
        return ccLoginAccountNumber;
    }

    public void setCcLoginAccountNumber(BigInteger ccLoginAccountNumber) {
        this.ccLoginAccountNumber = ccLoginAccountNumber;
    }

    public BigInteger getEmailLoginAccountNumber() {
        return emailLoginAccountNumber;
    }

    public void setEmailLoginAccountNumber(BigInteger emailLoginAccountNumber) {
        this.emailLoginAccountNumber = emailLoginAccountNumber;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public BigInteger getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(BigInteger receiptId) {
        this.receiptId = receiptId;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public BigInteger getTransDataMapId() {
        return transDataMapId;
    }

    public void setTransDataMapId(BigInteger transDataMapId) {
        this.transDataMapId = transDataMapId;
    }

}
