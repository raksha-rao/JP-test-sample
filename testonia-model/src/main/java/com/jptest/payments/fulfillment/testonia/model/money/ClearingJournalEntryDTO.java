package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents CLEARING_JOURNAL_ENTRY table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClearingJournalEntryDTO {

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "aggregate_account_num")
    private BigInteger aggregateAccountNumber;

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

    @XmlElement(name = "trans_amount")
    private Long transAmount;

    @XmlElement(name = "trans_amount_currency_cd")
    private String transAmountCurrencyCode;

    @XmlElement(name = "user_account_type")
    private String userAccountType;

    @XmlElement(name = "user_country_cd")
    private String userCountryCode;

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

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransAmountCurrencyCode() {
        return transAmountCurrencyCode;
    }

    public void setTransAmountCurrencyCode(String transAmountCurrencyCode) {
        this.transAmountCurrencyCode = transAmountCurrencyCode;
    }

    public String getUserAccountType() {
        return userAccountType;
    }

    public void setUserAccountType(String userAccountType) {
        this.userAccountType = userAccountType;
    }

    public String getUserCountryCode() {
        return userCountryCode;
    }

    public void setUserCountryCode(String userCountryCode) {
        this.userCountryCode = userCountryCode;
    }

    public Long getUserGroupCode() {
        return userGroupCode;
    }

    public void setUserGroupCode(Long userGroupCode) {
        this.userGroupCode = userGroupCode;
    }

}
