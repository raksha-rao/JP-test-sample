package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBBase64Adapter;

/**
 * Represents WPAYMENT_EXTENSION_DATA table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WPaymentExtensionDataDTO {

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "extension_data")
    @XmlJavaTypeAdapter(JAXBBase64Adapter.class)
    private String extensionData;

    @XmlElement(name = "payer_account_number")
    private BigInteger payerAccountNumber;

    public BigInteger getActivityId() {
        return activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    public String getExtensionData() {
        return extensionData;
    }

    public void setExtensionData(String extensionData) {
        this.extensionData = extensionData;
    }

    public BigInteger getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public void setPayerAccountNumber(BigInteger payerAccountNumber) {
        this.payerAccountNumber = payerAccountNumber;
    }

}
