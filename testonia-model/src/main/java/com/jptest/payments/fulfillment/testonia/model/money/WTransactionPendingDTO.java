package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WTRANSACTION_PENDING table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WTransactionPendingDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "status")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte status;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte type;

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getStatus() {
        return status;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getType() {
        return type;
    }

}
