package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WEFT_EXTERNAL_RECORD table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WEFTransactionDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "external_id")
    private String externalId;

    @XmlElement(name = "generic_id")
    private Long genericId;

    @XmlElement(name = "status")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte status;

    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte type;

    @XmlElement(name = "vendor")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte vendor;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Long getGenericId() {
        return genericId;
    }

    public void setGenericId(Long genericId) {
        this.genericId = genericId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getVendor() {
        return vendor;
    }

    public void setVendor(Byte vendor) {
        this.vendor = vendor;
    }
}
