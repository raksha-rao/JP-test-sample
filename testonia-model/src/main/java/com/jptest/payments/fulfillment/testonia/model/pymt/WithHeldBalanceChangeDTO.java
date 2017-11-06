package com.jptest.payments.fulfillment.testonia.model.pymt;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Represents withheld_balance_change table records
 * @JP Inc.
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class WithHeldBalanceChangeDTO {

    @XmlElement(name = "id")
    private BigInteger id;

    @XmlElement(name = "money_movement_id")
    private BigInteger moneyMovementId;

    @XmlElement(name = "payment_side_id")
    private BigInteger paymentSideId;

    @XmlElement(name = "debit_credit_code")
    private String debitCreditCode;

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "balance_type_code")
    private String balanceTypeCode;

    @XmlElement(name = "change_type_code")
    private String changeTypeCode;

    @XmlElement(name = "change_time")
    private Long changeTime;

    @XmlElement(name = "status_code")
    private String statusCode;

    @XmlElement(name = "money_amount")
    private Long moneyAmount;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "currency_exchange_id")
    private BigInteger currencyExchangeId;

    @XmlElement(name = "at_posting_holding_balance")
    private Long atPostingHoldingBalance;

    @XmlElement(name = "at_posting_withheld_balance")
    private Long atPostingWithheldBalance;

    @XmlElement(name = "balance_posting_time")
    private Long balancePostingTime;

    @XmlElement(name = "reason_code")
    private String reasonCode;

    @XmlElement(name = "row_version")
    private Long rowVersion;

    @XmlElement(name = "flags01")
    private BigInteger flags01;

    @XmlElement(name = "flags02")
    private BigInteger flags02;

    @XmlElement(name = "flags03")
    private BigInteger flags03;

    @XmlElement(name = "flags04")
    private BigInteger flags04;

    @XmlElement(name = "flags05")
    private BigInteger flags05;

    @XmlElement(name = "flags06")
    private BigInteger flags06;

    @XmlElement(name = "flags07")
    private BigInteger flags07;

    @XmlElement(name = "flags08")
    private BigInteger flags08;

    @XmlElement(name = "flags09")
    private BigInteger flags09;

    @XmlElement(name = "flags10")
    private BigInteger flags10;

    @XmlElement(name = "row_created_time")
    private Long rowCreatedTime;

    @XmlElement(name = "row_updated_time")
    private Long rowUpdatedTime;

    @XmlElement(name = "voids_balance_change_id")
    private Long voidsBalanceChangeId;

    @XmlElement(name = "voids_balance_change_time")
    private Long voidsBalanceChangeTime;

    @XmlElement(name = "derived_available_balance")
    private Long derivedAvailableBalance;
    
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getMoneyMovementId() {
        return moneyMovementId;
    }

    public void setMoneyMovementId(BigInteger moneyMovementId) {
        this.moneyMovementId = moneyMovementId;
    }

    public BigInteger getPaymentSideId() {
        return paymentSideId;
    }

    public void setPaymentSideId(BigInteger paymentSideId) {
        this.paymentSideId = paymentSideId;
    }

    public String getDebitCreditCode() {
        return debitCreditCode;
    }

    public void setDebitCreditCode(String debitCreditCode) {
        this.debitCreditCode = debitCreditCode;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBalanceTypeCode() {
        return balanceTypeCode;
    }

    public void setBalanceTypeCode(String balanceTypeCode) {
        this.balanceTypeCode = balanceTypeCode;
    }

    public String getChangeTypeCode() {
        return changeTypeCode;
    }

    public void setChangeTypeCode(String changeTypeCode) {
        this.changeTypeCode = changeTypeCode;
    }

    public Long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Long changeTime) {
        this.changeTime = changeTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Long getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigInteger getCurrencyExchangeId() {
        return currencyExchangeId;
    }

    public void setCurrencyExchangeId(BigInteger currencyExchangeId) {
        this.currencyExchangeId = currencyExchangeId;
    }

    public Long getAtPostingHoldingBalance() {
        return atPostingHoldingBalance;
    }

    public void setAtPostingHoldingBalance(Long atPostingHoldingBalance) {
        this.atPostingHoldingBalance = atPostingHoldingBalance;
    }

    public Long getAtPostingWithheldBalance() {
        return atPostingWithheldBalance;
    }

    public void setAtPostingWithheldBalance(Long atPostingWithheldBalance) {
        this.atPostingWithheldBalance = atPostingWithheldBalance;
    }

    public Long getBalancePostingTime() {
        return balancePostingTime;
    }

    public void setBalancePostingTime(Long balancePostingTime) {
        this.balancePostingTime = balancePostingTime;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(Long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public BigInteger getFlags01() {
        return flags01;
    }

    public void setFlags01(BigInteger flags01) {
        this.flags01 = flags01;
    }

    public BigInteger getFlags02() {
        return flags02;
    }

    public void setFlags02(BigInteger flags02) {
        this.flags02 = flags02;
    }

    public BigInteger getFlags03() {
        return flags03;
    }

    public void setFlags03(BigInteger flags03) {
        this.flags03 = flags03;
    }

    public BigInteger getFlags04() {
        return flags04;
    }

    public void setFlags04(BigInteger flags04) {
        this.flags04 = flags04;
    }

    public BigInteger getFlags05() {
        return flags05;
    }

    public void setFlags05(BigInteger flags05) {
        this.flags05 = flags05;
    }

    public BigInteger getFlags06() {
        return flags06;
    }

    public void setFlags06(BigInteger flags06) {
        this.flags06 = flags06;
    }

    public BigInteger getFlags07() {
        return flags07;
    }

    public void setFlags07(BigInteger flags07) {
        this.flags07 = flags07;
    }

    public BigInteger getFlags08() {
        return flags08;
    }

    public void setFlags08(BigInteger flags08) {
        this.flags08 = flags08;
    }

    public BigInteger getFlags09() {
        return flags09;
    }

    public void setFlags09(BigInteger flags09) {
        this.flags09 = flags09;
    }

    public BigInteger getFlags10() {
        return flags10;
    }

    public void setFlags10(BigInteger flags10) {
        this.flags10 = flags10;
    }

    public Long getRowCreatedTime() {
        return rowCreatedTime;
    }

    public void setRowCreatedTime(Long rowCreatedTime) {
        this.rowCreatedTime = rowCreatedTime;
    }

    public Long getRowUpdatedTime() {
        return rowUpdatedTime;
    }

    public void setRowUpdatedTime(Long rowUpdatedTime) {
        this.rowUpdatedTime = rowUpdatedTime;
    }

    public Long getVoidsBalanceChangeId() {
        return voidsBalanceChangeId;
    }

    public void setVoidsBalanceChangeId(Long voidsBalanceChangeId) {
        this.voidsBalanceChangeId = voidsBalanceChangeId;
    }

    public Long getVoidsBalanceChangeTime() {
        return voidsBalanceChangeTime;
    }

    public void setVoidsBalanceChangeTime(Long voidsBalanceChangeTime) {
        this.voidsBalanceChangeTime = voidsBalanceChangeTime;
    }

    public Long getDerivedAvailableBalance() {
        return derivedAvailableBalance;
    }

    public void setDerivedAvailableBalance(Long derivedAvailableBalance) {
        this.derivedAvailableBalance = derivedAvailableBalance;
    }

   

}
