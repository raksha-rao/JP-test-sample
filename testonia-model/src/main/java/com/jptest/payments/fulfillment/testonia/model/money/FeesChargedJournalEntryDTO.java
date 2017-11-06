package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents FEES_CHARGED_JOURNAL_ENTRY table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FeesChargedJournalEntryDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "activity_event_sequence")
    private Long activityEventSequence;

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "aggregate_account_number")
    private BigInteger aggregateAccountNumber;

    @XmlElement(name = "data")
    private String data;

    @XmlElement(name = "direction_cd")
    private Long directionCode;

    @XmlElement(name = "fee_amount")
    private Long feeAmount;

    @XmlElement(name = "fee_amount_usd")
    private Long feeAmountUsd;

    @XmlElement(name = "fee_currency_cd")
    private String feeCurrencyCode;

    @XmlElement(name = "fee_reason_cd")
    private String feeReasonCode;

    @XmlElement(name = "individual_account_num")
    private BigInteger individualAccountNumber;

    @XmlElement(name = "individual_account_type")
    private Long individualAccountType;

    @XmlElement(name = "participant_transaction_id")
    private String participantTransactionId;

    @XmlElement(name = "participant_type_cd")
    private String participantTypeCode;

    @XmlElement(name = "jptest_legal_entity_cd")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte jptestLegalEntityCode;

    @XmlElement(name = "row_created_time")
    private Long rowCreatedTime;

    @XmlElement(name = "time_created_unix_secs")
    private Long timeCreatedUnixSecs;

    @XmlElement(name = "user_group_cd")
    private Long userGroupCode;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getActivityEventSequence() {
        return activityEventSequence;
    }

    public void setActivityEventSequence(Long activityEventSequence) {
        this.activityEventSequence = activityEventSequence;
    }

    public BigInteger getActivityId() {
        return activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    public BigInteger getAggregateAccountNumber() {
        return aggregateAccountNumber;
    }

    public void setAggregateAccountNumber(BigInteger aggregateAccountNumber) {
        this.aggregateAccountNumber = aggregateAccountNumber;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(Long directionCode) {
        this.directionCode = directionCode;
    }

    public Long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Long getFeeAmountUsd() {
        return feeAmountUsd;
    }

    public void setFeeAmountUsd(Long feeAmountUsd) {
        this.feeAmountUsd = feeAmountUsd;
    }

    public String getFeeCurrencyCode() {
        return feeCurrencyCode;
    }

    public void setFeeCurrencyCode(String feeCurrencyCode) {
        this.feeCurrencyCode = feeCurrencyCode;
    }

    public String getFeeReasonCode() {
        return feeReasonCode;
    }

    public void setFeeReasonCode(String feeReasonCode) {
        this.feeReasonCode = feeReasonCode;
    }

    public BigInteger getIndividualAccountNumber() {
        return individualAccountNumber;
    }

    public void setIndividualAccountNumber(BigInteger individualAccountNumber) {
        this.individualAccountNumber = individualAccountNumber;
    }

    public Long getIndividualAccountType() {
        return individualAccountType;
    }

    public void setIndividualAccountType(Long individualAccountType) {
        this.individualAccountType = individualAccountType;
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

    public Byte getjptestLegalEntityCode() {
        return jptestLegalEntityCode;
    }

    public void setjptestLegalEntityCode(Byte jptestLegalEntityCode) {
        this.jptestLegalEntityCode = jptestLegalEntityCode;
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

    public Long getUserGroupCode() {
        return userGroupCode;
    }

    public void setUserGroupCode(Long userGroupCode) {
        this.userGroupCode = userGroupCode;
    }

}
