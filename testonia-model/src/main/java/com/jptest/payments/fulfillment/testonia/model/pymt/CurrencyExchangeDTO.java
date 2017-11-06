package com.jptest.payments.fulfillment.testonia.model.pymt;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents currency_exchange table records
 * @JP Inc.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CurrencyExchangeDTO {

    @XmlElement(name = "id")
    private BigInteger id;

    @XmlElement(name = "payment_side_id")
    private BigInteger paymentSideId;

    @XmlElement(name = "debit_credit_code")
    private String debitCreditCode;

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "exchange_time")
    private Long exchangeTime;

    @XmlElement(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @XmlElement(name = "rate_type_code")
    private String rateTypeCode;

    @XmlElement(name = "reason_code")
    private String reasonCode;

    @XmlElement(name = "fees_waived_flag")
    private String feesWaivedFlag;

    @XmlElement(name = "from_money_amount")
    private Long fromMoneyAmount;

    @XmlElement(name = "from_currency_code")
    private String fromCurrencyCode;

    @XmlElement(name = "from_usd_money_amount")
    private Long fromUSDMoneyAmount;

    @XmlElement(name = "to_money_amount")
    private Long toMoneyAmount;

    @XmlElement(name = "to_currency_code")
    private String toCurrencyCode;

    @XmlElement(name = "to_usd_money_amount")
    private Long toUSDMoneyAmount;

    @XmlElement(name = "external_exchange_flag")
    private String externalExchangeFlag;

    @XmlElement(name = "legacy_from_id")
    private Long legacyFromId;

    @XmlElement(name = "legacy_to_id")
    private Long legacyToId;

    @XmlElement(name = "legacy_from_batc")
    private Long legacyfromBATC;

    @XmlElement(name = "calculation_factors")
    private String calculationFactors;

    @XmlElement(name = "legacy_to_batc")
    private Long legacyToBATC;

    @XmlElement(name = "legacy_shared_id")
    private Long legacySharedId;

    @XmlElement(name = "legacy_parent_id")
    private Long legacyParentId;

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

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
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

    public Long getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Long exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getRateTypeCode() {
        return rateTypeCode;
    }

    public void setRateTypeCode(String rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getFeesWaivedFlag() {
        return feesWaivedFlag;
    }

    public void setFeesWaivedFlag(String feesWaivedFlag) {
        this.feesWaivedFlag = feesWaivedFlag;
    }

    public Long getFromMoneyAmount() {
        return fromMoneyAmount;
    }

    public void setFromMoneyAmount(Long fromMoneyAmount) {
        this.fromMoneyAmount = fromMoneyAmount;
    }

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }

    public Long getFromUSDMoneyAmount() {
        return fromUSDMoneyAmount;
    }

    public void setFromUSDMoneyAmount(Long fromUSDMoneyAmount) {
        this.fromUSDMoneyAmount = fromUSDMoneyAmount;
    }

    public Long getToMoneyAmount() {
        return toMoneyAmount;
    }

    public void setToMoneyAmount(Long toMoneyAmount) {
        this.toMoneyAmount = toMoneyAmount;
    }

    public String getToCurrencyCode() {
        return toCurrencyCode;
    }

    public void setToCurrencyCode(String toCurrencyCode) {
        this.toCurrencyCode = toCurrencyCode;
    }

    public Long getToUSDMoneyAmount() {
        return toUSDMoneyAmount;
    }

    public void setToUSDMoneyAmount(Long toUSDMoneyAmount) {
        this.toUSDMoneyAmount = toUSDMoneyAmount;
    }

    public String getExternalExchangeFlag() {
        return externalExchangeFlag;
    }

    public void setExternalExchangeFlag(String externalExchangeFlag) {
        this.externalExchangeFlag = externalExchangeFlag;
    }

    public Long getLegacyFromId() {
        return legacyFromId;
    }

    public void setLegacyFromId(Long legacyFromId) {
        this.legacyFromId = legacyFromId;
    }

    public Long getLegacyToId() {
        return legacyToId;
    }

    public void setLegacyToId(Long legacyToId) {
        this.legacyToId = legacyToId;
    }

    public Long getLegacyfromBATC() {
        return legacyfromBATC;
    }

    public void setLegacyfromBATC(Long legacyfromBATC) {
        this.legacyfromBATC = legacyfromBATC;
    }

    public String getCalculationFactors() {
        return calculationFactors;
    }

    public void setCalculationFactors(String calculationFactors) {
        this.calculationFactors = calculationFactors;
    }

    public Long getLegacyToBATC() {
        return legacyToBATC;
    }

    public void setLegacyToBATC(Long legacyToBATC) {
        this.legacyToBATC = legacyToBATC;
    }

    public Long getLegacySharedId() {
        return legacySharedId;
    }

    public void setLegacySharedId(Long legacySharedId) {
        this.legacySharedId = legacySharedId;
    }

    public Long getLegacyParentId() {
        return legacyParentId;
    }

    public void setLegacyParentId(Long legacyParentId) {
        this.legacyParentId = legacyParentId;
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

    public Long getFinancialJournalAID() {
        return financialJournalAID;
    }

    public void setFinancialJournalAID(Long financialJournalAID) {
        this.financialJournalAID = financialJournalAID;
    }

    public Long getFjCreatedTime() {
        return fjCreatedTime;
    }

    public void setFjCreatedTime(Long fjCreatedTime) {
        this.fjCreatedTime = fjCreatedTime;
    }

    public Long getFjUpdatedTime() {
        return fjUpdatedTime;
    }

    public void setFjUpdatedTime(Long fjUpdatedTime) {
        this.fjUpdatedTime = fjUpdatedTime;
    }

    public Long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(Long rowVersion) {
        this.rowVersion = rowVersion;
    }

    @XmlElement(name = "row_updated_time")
    private Long rowUpdatedTime;

    @XmlElement(name = "financial_journal_aid")
    private Long financialJournalAID;

    @XmlElement(name = "fj_created_time")
    private Long fjCreatedTime;

    @XmlElement(name = "fj_updated_time")
    private Long fjUpdatedTime;

    @XmlElement(name = "row_version")
    private Long rowVersion;

}
