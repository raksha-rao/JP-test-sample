package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents RECEIVABLE_JOURNAL_ENTRY table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceivableJournalEntryDTO {

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "amount")
    private Long amount;

    @XmlElement(name = "currency_cd")
    private String currencyCode;

    @XmlElement(name = "direction_cd")
    private Long directionCode;

    @XmlElement(name = "participant_transaction_id")
    private String participantTransactionId;

    @XmlElement(name = "participant_type_cd")
    private String participantTypeCode;

    @XmlElement(name = "payment_funding_method")
    private String paymentFundingMethod;

    @XmlElement(name = "payoff_reason")
    private String payoffReason;

    @XmlElement(name = "jptest_legal_entity_cd")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte jptestLegalEntityCode;

    @XmlElement(name = "reclassified_activity_id")
    private BigInteger reclassifiedActivityId;

    @XmlElement(name = "row_created_time")
    private Long rowCreatedTime;

    @XmlElement(name = "time_created_unix_secs")
    private Long timeCreatedUnixSecs;

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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(Long directionCode) {
        this.directionCode = directionCode;
    }

    public String getParticipantTransactionId() {
        return participantTransactionId;
    }

    public void setParticipantTransactionId(String participantTransactionId) {
        this.participantTransactionId = participantTransactionId;
    }

    public String getParticipantTypeCode() {
        return participantTypeCode;
    }

    public void setParticipantTypeCode(String participantTypeCode) {
        this.participantTypeCode = participantTypeCode;
    }

    public String getPaymentFundingMethod() {
        return paymentFundingMethod;
    }

    public void setPaymentFundingMethod(String paymentFundingMethod) {
        this.paymentFundingMethod = paymentFundingMethod;
    }

    public String getPayoffReason() {
        return payoffReason;
    }

    public void setPayoffReason(String payoffReason) {
        this.payoffReason = payoffReason;
    }

    public Byte getjptestLegalEntityCode() {
        return jptestLegalEntityCode;
    }

    public void setjptestLegalEntityCode(Byte jptestLegalEntityCode) {
        this.jptestLegalEntityCode = jptestLegalEntityCode;
    }

    public BigInteger getReclassifiedActivityId() {
        return reclassifiedActivityId;
    }

    public void setReclassifiedActivityId(BigInteger reclassifiedActivityId) {
        this.reclassifiedActivityId = reclassifiedActivityId;
    }

    public Long getRowCreatedTime() {
        return rowCreatedTime;
    }

    public void setRowCreatedTime(Long rowCreatedTime) {
        this.rowCreatedTime = rowCreatedTime;
    }

    public Long getTimeCreatedUnixSecs() {
        return timeCreatedUnixSecs;
    }

    public void setTimeCreatedUnixSecs(Long timeCreatedUnixSecs) {
        this.timeCreatedUnixSecs = timeCreatedUnixSecs;
    }

}
