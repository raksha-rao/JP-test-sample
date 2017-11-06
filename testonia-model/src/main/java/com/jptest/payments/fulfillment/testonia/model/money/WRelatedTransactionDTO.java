package com.jptest.payments.fulfillment.testonia.model.money;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

/**
 * Represents WRELATED_TRANSACTION table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WRelatedTransactionDTO {
    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    @XmlElement(name = "related_transaction_id")
    private BigInteger relatedTransactionId;

    @XmlElement(name = "association_type")
    private String associationType;

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    public BigInteger getRelatedTransactionId() {
        return relatedTransactionId;
    }

    public void setRelatedTransactionId(BigInteger relatedTransactionId) {
        this.relatedTransactionId = relatedTransactionId;
    }

    public String getAssociationType() {
        return associationType;
    }

    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }
}
