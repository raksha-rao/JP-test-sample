package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WTRANSACTION_ADMIN table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WTransactionAdminDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "center_code")
    private Integer centerCode;

    @XmlElement(name = "company_code")
    private Integer companyCode;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "gl_account_number")
    private BigInteger glAccountNumber;

    @XmlElement(name = "internal_memo")
    private String internalMemo;

    @XmlElement(name = "original_trans_id")
    private BigInteger originalTransactionId;

    @XmlElement(name = "reason_id")
    private BigInteger reasonId;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(Integer centerCode) {
        this.centerCode = centerCode;
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public BigInteger getGlAccountNumber() {
        return glAccountNumber;
    }

    public void setGlAccountNumber(BigInteger glAccountNumber) {
        this.glAccountNumber = glAccountNumber;
    }

    public String getInternalMemo() {
        return internalMemo;
    }

    public void setInternalMemo(String internalMemo) {
        this.internalMemo = internalMemo;
    }

    public BigInteger getOriginalTransactionId() {
        return originalTransactionId;
    }

    public void setOriginalTransactionId(BigInteger originalTransactionId) {
        this.originalTransactionId = originalTransactionId;
    }

    public BigInteger getReasonId() {
        return reasonId;
    }

    public void setReasonId(BigInteger reasonId) {
        this.reasonId = reasonId;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

}
