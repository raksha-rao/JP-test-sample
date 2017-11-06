package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents PACTIVITY_TRANS_MAP table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PActivityTransMapDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "ledger_type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte ledgerType;

    @XmlElement(name = "operation_type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte operationType;

    @XmlElement(name = "txn_id")
    private BigInteger transactionId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigInteger getActivityId() {
        return activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    public Byte getLedgerType() {
        return ledgerType;
    }

    public void setLedgerType(Byte ledgerType) {
        this.ledgerType = ledgerType;
    }

    public Byte getOperationType() {
        return operationType;
    }

    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

}
