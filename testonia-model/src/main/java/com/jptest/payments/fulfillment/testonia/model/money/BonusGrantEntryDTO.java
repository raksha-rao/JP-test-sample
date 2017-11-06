package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents BONUS_GRANT_ENTRY table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BonusGrantEntryDTO {

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "aggregate_account_num")
    private BigInteger aggregateAccountNumber;

    @XmlElement(name = "amount")
    private Long amount;

    @XmlElement(name = "amount_currency_cd")
    private String amountCurrencyCode;

    @XmlElement(name = "bonus_reason_cd")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte bonusReasonCode;

    @XmlElement(name = "direction_cd")
    private Long directionCode;

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

    @XmlElement(name = "pp_customer_acct_num")
    private BigInteger ppCustomerAccountNumber;

    @XmlElement(name = "row_created_time")
    private Long rowCreatedTime;

    @XmlElement(name = "time_created_unix_secs")
    private Long timeCreatedUnixSecs;

    @XmlElement(name = "user_group_cd")
    private Long userGroupCode;

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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getAmountCurrencyCode() {
        return amountCurrencyCode;
    }

    public void setAmountCurrencyCode(String amountCurrencyCode) {
        this.amountCurrencyCode = amountCurrencyCode;
    }

    public Byte getBonusReasonCode() {
        return bonusReasonCode;
    }

    public void setBonusReasonCode(Byte bonusReasonCode) {
        this.bonusReasonCode = bonusReasonCode;
    }

    public Long getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(Long directionCode) {
        this.directionCode = directionCode;
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

    public BigInteger getPpCustomerAccountNumber() {
        return ppCustomerAccountNumber;
    }

    public void setPpCustomerAccountNumber(BigInteger ppCustomerAccountNumber) {
        this.ppCustomerAccountNumber = ppCustomerAccountNumber;
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
