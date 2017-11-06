package com.jptest.payments.fulfillment.testonia.model.pymt;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PaymentSideReferenceDTO {

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "reference_value")    
    private String referenceValue;

	@XmlElement(name = "item")
    @XmlElementWrapper(name = "reference_type_code")
    private String referenceTypeCode;

	@XmlElement(name = "item")
    @XmlElementWrapper(name = "payment_side_id")
    private BigInteger paymentSideId;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "row_created_time")
    private Long rowCreatedTime;

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    public String getReferenceTypeCode() {
        return referenceTypeCode;
    }

    public void setReferenceTypeCode(String referenceTypeCode) {
        this.referenceTypeCode = referenceTypeCode;
    }

    public BigInteger getPaymentSideId() {
        return paymentSideId;
    }

    public void setPaymentSideId(BigInteger paymentSideId) {
        this.paymentSideId = paymentSideId;
    }

    public Long getRowCreatedTime() {
        return rowCreatedTime;
    }

    public void setRowCreatedTime(Long rowCreatedTime) {
        this.rowCreatedTime = rowCreatedTime;
    }
}
