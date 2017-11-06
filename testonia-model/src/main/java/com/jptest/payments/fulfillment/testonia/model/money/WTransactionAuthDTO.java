package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WTRANSACTION_AUTH table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WTransactionAuthDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "active_auth_id")
    private BigInteger activeAuthId;

    @XmlElement(name = "amount_authorized")
    private Long amountAuthorized;

    @XmlElement(name = "amount_authorized_usd")
    private Long amountAuthorizedUSD;

    @XmlElement(name = "amount_settled")
    private Long amountSettled;

    @XmlElement(name = "amount_settled_usd")
    private Long amountSettledUSD;

    @XmlElement(name = "auth_expiration_time")
    private Long authExpirationTime;

    @XmlElement(name = "balance_amount")
    private Long balanceAmount;

    @XmlElement(name = "balance_amount_usd")
    private Long balanceAmountUSD;

    @XmlElement(name = "bufs_amount")
    private Long bufsAmount;

    @XmlElement(name = "bufs_amount_usd")
    private Long bufsAmountUSD;

    @XmlElement(name = "bufs_currency_code")
    private String bufsCurrencyCode;

    @XmlElement(name = "bufs_id")
    private BigInteger bufsId;

    @XmlElement(name = "bufs_type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte bufsType;

    @XmlElement(name = "counterparty")
    private BigInteger counterparty;

    @XmlElement(name = "cparty_trans_id")
    private BigInteger counterpartyTransactionId;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "fs_amount")
    private Long fsAmount;

    @XmlElement(name = "fs_amount_usd")
    private Long fsAmountUSD;

    @XmlElement(name = "fs_currency_code")
    private String fsCurrencyCode;

    @XmlElement(name = "fs_id")
    private BigInteger fsId;

    @XmlElement(name = "fs_type")
    private Byte fsType;

    @XmlElement(name = "honor_expiration_time")
    private Long honorExpirationTime;

    @XmlElement(name = "nontarget_bal_amount")
    private Long nonTargetBalanceAmount;

    @XmlElement(name = "nontarget_bal_amount_usd")
    private Long nonTargetBalanceAmountUSD;

    @XmlElement(name = "nontarget_bal_currency_code")
    private String nonTargetBalanceCurrencyCode;

    @XmlElement(name = "number_of_settlements")
    private Integer numberOfSettlements;

    @XmlElement(name = "original_auth_id")
    private BigInteger originalAuthId;

    @XmlElement(name = "reauth_time")
    private Long reauthTime;

    @XmlElement(name = "status")
    private Byte status;

    @XmlElement(name = "time_created")
    private Long timeCreated;

    @XmlElement(name = "time_updated")
    private Long timeUpdated;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigInteger getActiveAuthId() {
        return activeAuthId;
    }

    public void setActiveAuthId(BigInteger activeAuthId) {
        this.activeAuthId = activeAuthId;
    }

    public Long getAmountAuthorized() {
        return amountAuthorized;
    }

    public void setAmountAuthorized(Long amountAuthorized) {
        this.amountAuthorized = amountAuthorized;
    }

    public Long getAmountAuthorizedUSD() {
        return amountAuthorizedUSD;
    }

    public void setAmountAuthorizedUSD(Long amountAuthorizedUSD) {
        this.amountAuthorizedUSD = amountAuthorizedUSD;
    }

    public Long getAmountSettled() {
        return amountSettled;
    }

    public void setAmountSettled(Long amountSettled) {
        this.amountSettled = amountSettled;
    }

    public Long getAmountSettledUSD() {
        return amountSettledUSD;
    }

    public void setAmountSettledUSD(Long amountSettledUSD) {
        this.amountSettledUSD = amountSettledUSD;
    }

    public Long getAuthExpirationTime() {
        return authExpirationTime;
    }

    public void setAuthExpirationTime(Long authExpirationTime) {
        this.authExpirationTime = authExpirationTime;
    }

    public Long getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Long balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Long getBalanceAmountUSD() {
        return balanceAmountUSD;
    }

    public void setBalanceAmountUSD(Long balanceAmountUSD) {
        this.balanceAmountUSD = balanceAmountUSD;
    }

    public Long getBufsAmount() {
        return bufsAmount;
    }

    public void setBufsAmount(Long bufsAmount) {
        this.bufsAmount = bufsAmount;
    }

    public Long getBufsAmountUSD() {
        return bufsAmountUSD;
    }

    public void setBufsAmountUSD(Long bufsAmountUSD) {
        this.bufsAmountUSD = bufsAmountUSD;
    }

    public String getBufsCurrencyCode() {
        return bufsCurrencyCode;
    }

    public void setBufsCurrencyCode(String bufsCurrencyCode) {
        this.bufsCurrencyCode = bufsCurrencyCode;
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

    public BigInteger getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(BigInteger counterparty) {
        this.counterparty = counterparty;
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

    public Long getFsAmount() {
        return fsAmount;
    }

    public void setFsAmount(Long fsAmount) {
        this.fsAmount = fsAmount;
    }

    public Long getFsAmountUSD() {
        return fsAmountUSD;
    }

    public void setFsAmountUSD(Long fsAmountUSD) {
        this.fsAmountUSD = fsAmountUSD;
    }

    public String getFsCurrencyCode() {
        return fsCurrencyCode;
    }

    public void setFsCurrencyCode(String fsCurrencyCode) {
        this.fsCurrencyCode = fsCurrencyCode;
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

    public Long getHonorExpirationTime() {
        return honorExpirationTime;
    }

    public void setHonorExpirationTime(Long honorExpirationTime) {
        this.honorExpirationTime = honorExpirationTime;
    }

    public Long getNonTargetBalanceAmount() {
        return nonTargetBalanceAmount;
    }

    public void setNonTargetBalanceAmount(Long nonTargetBalanceAmount) {
        this.nonTargetBalanceAmount = nonTargetBalanceAmount;
    }

    public Long getNonTargetBalanceAmountUSD() {
        return nonTargetBalanceAmountUSD;
    }

    public void setNonTargetBalanceAmountUSD(Long nonTargetBalanceAmountUSD) {
        this.nonTargetBalanceAmountUSD = nonTargetBalanceAmountUSD;
    }

    public String getNonTargetBalanceCurrencyCode() {
        return nonTargetBalanceCurrencyCode;
    }

    public void setNonTargetBalanceCurrencyCode(String nonTargetBalanceCurrencyCode) {
        this.nonTargetBalanceCurrencyCode = nonTargetBalanceCurrencyCode;
    }

    public Integer getNumberOfSettlements() {
        return numberOfSettlements;
    }

    public void setNumberOfSettlements(Integer numberOfSettlements) {
        this.numberOfSettlements = numberOfSettlements;
    }

    public BigInteger getOriginalAuthId() {
        return originalAuthId;
    }

    public void setOriginalAuthId(BigInteger originalAuthId) {
        this.originalAuthId = originalAuthId;
    }

    public Long getReauthTime() {
        return reauthTime;
    }

    public void setReauthTime(Long reauthTime) {
        this.reauthTime = reauthTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

}
