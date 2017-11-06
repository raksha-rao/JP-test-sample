package com.jptest.payments.fulfillment.testonia.model.money;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WRETURNS_REVIEW table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WReturnsReviewDTO {

    @XmlElement(name = "amount")
    private Long amount;

    @XmlElement(name = "country_code")
    private String countryCode;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "status")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte status;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

}
