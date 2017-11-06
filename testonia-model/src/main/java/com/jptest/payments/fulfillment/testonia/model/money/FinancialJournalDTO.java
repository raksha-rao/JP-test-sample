package com.jptest.payments.fulfillment.testonia.model.money;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FinancialJournalDTO {

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "bonus_grant_entry")
    private List<BonusGrantEntryDTO> bonusGrantEntries;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "clearing_journal_entry")
    private List<ClearingJournalEntryDTO> clearingJournalEntries;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "fees_charged_journal_entry")
    private List<FeesChargedJournalEntryDTO> feesChargedJournalEntries;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "financial_journal_2")
    private List<FinancialJournal2DTO> financialJournal2Entries;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "payable_journal_entry")
    private List<PayableJournalEntryDTO> payableJournalEntries;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "receivable_journal_entry")
    private List<ReceivableJournalEntryDTO> receivableJournalEntries;

    public List<BonusGrantEntryDTO> getBonusGrantEntries() {
        return bonusGrantEntries;
    }

    public void setBonusGrantEntries(List<BonusGrantEntryDTO> bonusGrantEntries) {
        this.bonusGrantEntries = bonusGrantEntries;
    }

    public List<ClearingJournalEntryDTO> getClearingJournalEntries() {
        return clearingJournalEntries;
    }

    public void setClearingJournalEntries(List<ClearingJournalEntryDTO> clearingJournalEntries) {
        this.clearingJournalEntries = clearingJournalEntries;
    }

    public List<FeesChargedJournalEntryDTO> getFeesChargedJournalEntries() {
        return feesChargedJournalEntries;
    }

    public void setFeesChargedJournalEntries(List<FeesChargedJournalEntryDTO> feesChargedJournalEntries) {
        this.feesChargedJournalEntries = feesChargedJournalEntries;
    }

    public List<FinancialJournal2DTO> getFinancialJournal2Entries() {
        return financialJournal2Entries;
    }

    public void setFinancialJournal2Entries(List<FinancialJournal2DTO> financialJournal2Entries) {
        this.financialJournal2Entries = financialJournal2Entries;
    }

    public List<PayableJournalEntryDTO> getPayableJournalEntries() {
        return payableJournalEntries;
    }

    public void setPayableJournalEntries(List<PayableJournalEntryDTO> payableJournalEntries) {
        this.payableJournalEntries = payableJournalEntries;
    }

    public List<ReceivableJournalEntryDTO> getReceivableJournalEntries() {
        return receivableJournalEntries;
    }

    public void setReceivableJournalEntries(List<ReceivableJournalEntryDTO> receivableJournalEntries) {
        this.receivableJournalEntries = receivableJournalEntries;
    }

}
