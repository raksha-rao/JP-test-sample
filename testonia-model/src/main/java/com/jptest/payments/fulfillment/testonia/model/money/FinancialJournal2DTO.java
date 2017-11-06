package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents FINANCIAL_JOURNAL_2 table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FinancialJournal2DTO {

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "amount")
    private Long amount;

    @XmlElement(name = "credit_activity_event_id")
    private String creditActivityEventId;

    @XmlElement(name = "credit_legal_entity")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte creditLegalEntity;

    @XmlElement(name = "credit_pt_id")
    private String creditPtId;

    @XmlElement(name = "credit_type")
    private String creditType;

    @XmlElement(name = "credit_user_country")
    private String creditUserCountry;

    @XmlElement(name = "credit_user_pp_account_number")
    private BigInteger creditUserPpAccountNumber;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "db_id")
    private BigInteger dbId;

    @XmlElement(name = "debit_activity_event_id")
    private String debitActivityEventId;

    @XmlElement(name = "debit_legal_entity")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte debitLegalEntity;

    @XmlElement(name = "debit_pt_id")
    private String debitPtId;

    @XmlElement(name = "debit_type")
    private String debitType;

    @XmlElement(name = "debit_user_country")
    private String debitUserCountry;

    @XmlElement(name = "debit_user_pp_account_number")
    private BigInteger debitUserPpAccountNumber;

    @XmlElement(name = "journal_aid")
    private BigInteger journalAid;

    @XmlElement(name = "journal_entry_type")
    private Integer journalEntryType;

    @XmlElement(name = "void_journal_id")
    private BigInteger voidJournalId;

    public BigInteger getActivityId() {
        return activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCreditActivityEventId() {
        return creditActivityEventId;
    }

    public void setCreditActivityEventId(String creditActivityEventId) {
        this.creditActivityEventId = creditActivityEventId;
    }

    public Byte getCreditLegalEntity() {
        return creditLegalEntity;
    }

    public void setCreditLegalEntity(Byte creditLegalEntity) {
        this.creditLegalEntity = creditLegalEntity;
    }

    public String getCreditPtId() {
        return creditPtId;
    }

    public void setCreditPtId(String creditPtId) {
        this.creditPtId = creditPtId;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public String getCreditUserCountry() {
        return creditUserCountry;
    }

    public void setCreditUserCountry(String creditUserCountry) {
        this.creditUserCountry = creditUserCountry;
    }

    public BigInteger getCreditUserPpAccountNumber() {
        return creditUserPpAccountNumber;
    }

    public void setCreditUserPpAccountNumber(BigInteger creditUserPpAccountNumber) {
        this.creditUserPpAccountNumber = creditUserPpAccountNumber;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigInteger getDbId() {
        return dbId;
    }

    public void setDbId(BigInteger dbId) {
        this.dbId = dbId;
    }

    public String getDebitActivityEventId() {
        return debitActivityEventId;
    }

    public void setDebitActivityEventId(String debitActivityEventId) {
        this.debitActivityEventId = debitActivityEventId;
    }

    public Byte getDebitLegalEntity() {
        return debitLegalEntity;
    }

    public void setDebitLegalEntity(Byte debitLegalEntity) {
        this.debitLegalEntity = debitLegalEntity;
    }

    public String getDebitPtId() {
        return debitPtId;
    }

    public void setDebitPtId(String debitPtId) {
        this.debitPtId = debitPtId;
    }

    public String getDebitType() {
        return debitType;
    }

    public void setDebitType(String debitType) {
        this.debitType = debitType;
    }

    public String getDebitUserCountry() {
        return debitUserCountry;
    }

    public void setDebitUserCountry(String debitUserCountry) {
        this.debitUserCountry = debitUserCountry;
    }

    public BigInteger getDebitUserPpAccountNumber() {
        return debitUserPpAccountNumber;
    }

    public void setDebitUserPpAccountNumber(BigInteger debitUserPpAccountNumber) {
        this.debitUserPpAccountNumber = debitUserPpAccountNumber;
    }

    public BigInteger getJournalAid() {
        return journalAid;
    }

    public void setJournalAid(BigInteger journalAid) {
        this.journalAid = journalAid;
    }

    public Integer getJournalEntryType() {
        return journalEntryType;
    }

    public void setJournalEntryType(Integer journalEntryType) {
        this.journalEntryType = journalEntryType;
    }

    public BigInteger getVoidJournalId() {
        return voidJournalId;
    }

    public void setVoidJournalId(BigInteger voidJournalId) {
        this.voidJournalId = voidJournalId;
    }

}
