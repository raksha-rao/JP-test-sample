package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WMERCHANT_PULL_LOG table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WMerchantPullLogDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "amount")
    private Long amount;

    @XmlElement(name = "ba_id")
    private Long baId;

    @XmlElement(name = "bufs_id")
    private BigInteger bufsId;

    @XmlElement(name = "bufs_type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte bufsType;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "fs_id")
    private BigInteger fsId;

    @XmlElement(name = "fs_type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte fsType;

    @XmlElement(name = "invoice")
    private String invoice;

    @XmlElement(name = "merchant_account_number")
    private BigInteger merchantAccountNumber;

    @XmlElement(name = "merchant_transaction_id")
    private BigInteger merchantTransactionId;

    @XmlElement(name = "mp_id")
    private BigInteger mpId;

    @XmlElement(name = "reason")
    private String reason;

    @XmlElement(name = "reason_code")
    private Long reasonCode;

    @XmlElement(name = "status")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte status;

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

    public Long getBaId() {
        return baId;
    }

    public void setBaId(Long baId) {
        this.baId = baId;
    }

    public BigInteger getBufsId() {
        return bufsId;
    }

    public void setBufsId(BigInteger bufsId) {
        this.bufsId = bufsId;
    }

    public Byte getBufsType() {
        return bufsType;
    }

    public void setBufsType(Byte bufsType) {
        this.bufsType = bufsType;
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

    public BigInteger getFsId() {
        return fsId;
    }

    public void setFsId(BigInteger fsId) {
        this.fsId = fsId;
    }

    public Byte getFsType() {
        return fsType;
    }

    public void setFsType(Byte fsType) {
        this.fsType = fsType;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public BigInteger getMerchantAccountNumber() {
        return merchantAccountNumber;
    }

    public void setMerchantAccountNumber(BigInteger merchantAccountNumber) {
        this.merchantAccountNumber = merchantAccountNumber;
    }

    public BigInteger getMerchantTransactionId() {
        return merchantTransactionId;
    }

    public void setMerchantTransactionId(BigInteger merchantTransactionId) {
        this.merchantTransactionId = merchantTransactionId;
    }

    public BigInteger getMpId() {
        return mpId;
    }

    public void setMpId(BigInteger mpId) {
        this.mpId = mpId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(Long reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

}
