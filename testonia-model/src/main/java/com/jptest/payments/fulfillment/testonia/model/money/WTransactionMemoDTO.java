package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WTRANSACTION_MEMO table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WTransactionMemoDTO {

    @XmlElement(name = "memo")
    private String memo;

    @XmlElement(name = "restriction_id")
    private BigInteger restrictionId;

    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte type;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigInteger getRestrictionId() {
        return restrictionId;
    }

    public void setRestrictionId(BigInteger restrictionId) {
        this.restrictionId = restrictionId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

}
