package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WTRANSACTION_BUFS table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WTransactionBufsDTO {

    @XmlElement(name = "source_id")
    private BigInteger sourceId;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte type;

    public void setSourceId(BigInteger sourceId) {
        this.sourceId = sourceId;
    }

    public BigInteger getSourceId() {
        return sourceId;
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
