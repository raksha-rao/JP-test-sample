package com.jptest.payments.fulfillment.testonia.model.pymt;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class MoneyMovementDTO {

	@XmlElement(name = "money_movement_id")
    private BigInteger id;

    @XmlElement(name = "payment_side_id")
    private BigInteger paymentSideId;

    @XmlElement(name = "debit_credit_code")
	private String debitCreditCode;

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "wallet_instrument_id")
    private String walletInstrumentId;

    @XmlElement(name = "wallet_instrument_type_code")
    private String walletInstrumentTypeCode;

    @XmlElement(name = "type_code")
    private String typeCode;

    @XmlElement(name = "subtype_code")
    private String subtypeCode;

    @XmlElement(name = "reason_code")
    private String reasonCode;

    @XmlElement(name = "status_code")
    private String statusCode;

    @XmlElement(name = "money_amount")
    private Long moneyAmount;

    @XmlElement(name = "currency_code")
    private String currencyCode;

	@XmlElement(name = "currency_exchange_id")
    private BigInteger currencyExchangeId;
	
    @XmlElement(name = "usd_money_amount")
    private Long usdMoneyAmount;
    
    @XmlElement(name = "expected_clearing_time")
	private Long expectedClearingTime;

    @XmlElement(name = "at_posting_balance")
    private Long atPostingBalance;

    @XmlElement(name = "at_posting_withheld_balance")
    private Long atPostingWithheldBalance;
	
    @XmlElement(name = "balance_posting_time")
	private Long balancePostingTime;

    @XmlElement(name = "initiation_time")
    private Long initiationTime;

    @XmlElement(name = "last_updated_time")
    private Long lastUpdatedTime;	
	
    @XmlElement(name = "legacy_instrument_type_code")
    private String legacyInstrumentTypeCode;

    @XmlElement(name = "legacy_instrument_id")
    private Long legacyInstrumentId;	

    @XmlElement(name = "legacy_parent_id")
    private BigInteger legacyParentId;
	
    @XmlElement(name = "backup_for_money_movement_id")
    private Long backupForMoneyMovementId;

    @XmlElement(name = "replaced_money_movement_id")
    private Long replacedMoneyMovementId;	
	
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

    @XmlElement(name = "financial_journal_aid")
    private Long financialJournalAid;

    @XmlElement(name = "fj_created_time")
	private Long fjCreatedTime;

    @XmlElement(name = "fj_updated_time")
    private Long fjUpdatedTime;

    @XmlElement(name = "memo")
    private String memo;
	
    @XmlElement(name = "legacy_shared_id")
    private BigInteger legacySharedId;

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

    public String getWalletInstrumentId() {
        return walletInstrumentId;
    }

    public void setWalletInstrumentId(String walletInstrumentId) {
        this.walletInstrumentId = walletInstrumentId;
    }

    public String getWalletInstrumentTypeCode() {
        return walletInstrumentTypeCode;
    }

    public void setWalletInstrumentTypeCode(String walletInstrumentTypeCode) {
        this.walletInstrumentTypeCode = walletInstrumentTypeCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
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

    public Long getUsdMoneyAmount() {
        return usdMoneyAmount;
    }

    public void setUsdMoneyAmount(Long usdMoneyAmount) {
        this.usdMoneyAmount = usdMoneyAmount;
    }

    public Long getInitiationTime() {
        return initiationTime;
    }

    public void setInitiationTime(Long initiationTime) {
        this.initiationTime = initiationTime;
    }

    public Long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getLegacyInstrumentTypeCode() {
        return legacyInstrumentTypeCode;
    }

    public void setLegacyInstrumentTypeCode(String legacyInstrumentTypeCode) {
        this.legacyInstrumentTypeCode = legacyInstrumentTypeCode;
    }

    public Long getLegacyInstrumentId() {
        return legacyInstrumentId;
    }

    public void setLegacyInstrumentId(Long legacyInstrumentId) {
        this.legacyInstrumentId = legacyInstrumentId;
    }

    public Long getBalancePostingTime() {
        return balancePostingTime;
    }

    public void setBalancePostingTime(Long balancePostingTime) {
        this.balancePostingTime = balancePostingTime;
    }

    public Long getBackupForMoneyMovementId() {
        return backupForMoneyMovementId;
    }

    public void setBackupForMoneyMovementId(Long backupForMoneyMovementId) {
        this.backupForMoneyMovementId = backupForMoneyMovementId;
    }

    public Long getReplacedMoneyMovementId() {
        return replacedMoneyMovementId;
    }

    public void setReplacedMoneyMovementId(Long replacedMoneyMovementId) {
        this.replacedMoneyMovementId = replacedMoneyMovementId;
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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getSubtypeCode() {
        return subtypeCode;
    }

    public void setSubtypeCode(String subtypeCode) {
        this.subtypeCode = subtypeCode;
    }

    public Long getExpectedClearingTime() {
        return expectedClearingTime;
    }

    public void setExpectedClearingTime(Long expectedClearingTime) {
        this.expectedClearingTime = expectedClearingTime;
    }

    public Long getAtPostingBalance() {
        return atPostingBalance;
    }

    public void setAtPostingBalance(Long atPostingBalance) {
        this.atPostingBalance = atPostingBalance;
    }

    public Long getAtPostingWithheldBalance() {
        return atPostingWithheldBalance;
    }

    public void setAtPostingWithheldBalance(Long atPostingWithheldBalance) {
        this.atPostingWithheldBalance = atPostingWithheldBalance;
    }

    public BigInteger getLegacyParentId() {
        return legacyParentId;
    }

    public void setLegacyParentId(BigInteger legacyParentId) {
        this.legacyParentId = legacyParentId;
    }

    public BigInteger getLegacySharedId() {
        return legacySharedId;
    }

    public void setLegacySharedId(BigInteger legacySharedId) {
        this.legacySharedId = legacySharedId;
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

    public Long getFinancialJournalAid() {
        return financialJournalAid;
    }

    public void setFinancialJournalAid(Long financialJournalAid) {
        this.financialJournalAid = financialJournalAid;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    

}
