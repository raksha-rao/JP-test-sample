package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;


/**
 * Represents WTRANS_DATA_MAP table record
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WTransDataMapDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "time_created")
    private Long timeCreated;

    @XmlElement(name = "map_id")
    private BigInteger mapId;

    @XmlElement(name = "transactionlike_id")
    private BigInteger transactionLikeId;

    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    @XmlElement(name = "transactionlike_type")
    private Byte transactionLikeType;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public BigInteger getMapId() {
        return mapId;
    }

    public void setMapId(BigInteger mapId) {
        this.mapId = mapId;
    }

    public BigInteger getTransactionLikeId() {
        return transactionLikeId;
    }

    public void setTransactionLikeId(BigInteger transactionLikeId) {
        this.transactionLikeId = transactionLikeId;
    }

    public Byte getTransactionLikeType() {
        return transactionLikeType;
    }

    public void setTransactionLikeType(Byte transactionLikeType) {
        this.transactionLikeType = transactionLikeType;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }

}
