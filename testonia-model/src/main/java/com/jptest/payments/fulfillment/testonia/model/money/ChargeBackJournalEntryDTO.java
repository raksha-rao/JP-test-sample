package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChargeBackJournalEntryDTO {

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "participant_transaction_id")
    private String participantTransactionId;

    @XmlElement(name = "time_created_unix_secs")
    private Long timeCreatedUnixSecs;

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "amount")
    private Long amount;

    @XmlElement(name = "currency_cd")
    private String currencyCd;

    @XmlElement(name = "direction_cd")
    private String directionCd;
    
    @XmlElement(name = "payable_reason_cd")
    private String payableReasonCd;

    @XmlElement(name = "jptest_legal_entity_cd")
    private String jptestLegalEntityCd;

    @XmlElement(name = "vendor_settlement_id")
    private BigInteger vendorSettlementId;

    @XmlElement(name = "charge_back_factors")
    private String chargebackFactors;

    @XmlElement(name = "row_created_time")
    private Long rowCreatedTime;

    public BigInteger getActivityId() {
        return activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    public String getParticipantTransactionId() {
        return participantTransactionId;
    }

    public void setParticipantTransactionId(String participantTransactionId) {
        this.participantTransactionId = participantTransactionId;
    }

    public Long getTimeCreatedUnixSecs() {
        return timeCreatedUnixSecs;
    }

    public void setTimeCreatedUnixSecs(Long timeCreatedUnixSecs) {
        this.timeCreatedUnixSecs = timeCreatedUnixSecs;
    }

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

    public String getCurrencyCd() {
        return currencyCd;
    }

    public void setCurrencyCd(String currencyCd) {
        this.currencyCd = currencyCd;
    }

    public String getDirectionCd() {
        return directionCd;
    }

    public void setDirectionCd(String directionCd) {
        this.directionCd = directionCd;
    }

    public String getPayableReasonCd() {
        return payableReasonCd;
    }

    public void setPayableReasonCd(String payableReasonCd) {
        this.payableReasonCd = payableReasonCd;
    }

    public String getjptestLegalEntityCd() {
        return jptestLegalEntityCd;
    }

    public void setjptestLegalEntityCd(String jptestLegalEntityCd) {
        this.jptestLegalEntityCd = jptestLegalEntityCd;
    }

    public BigInteger getVendorSettlementId() {
        return vendorSettlementId;
    }

    public void setVendorSettlementId(BigInteger vendorSettlementId) {
        this.vendorSettlementId = vendorSettlementId;
    }

    public String getChargebackFactors() {
        return chargebackFactors;
    }

    public void setChargebackFactors(String chargebackFactors) {
        this.chargebackFactors = chargebackFactors;
    }

    public Long getRowCreatedTime() {
        return rowCreatedTime;
    }

    public void setRowCreatedTime(Long rowCreatedTime) {
        this.rowCreatedTime = rowCreatedTime;
    }

}
