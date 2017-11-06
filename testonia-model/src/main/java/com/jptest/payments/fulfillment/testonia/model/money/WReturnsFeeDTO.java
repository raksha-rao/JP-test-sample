package com.jptest.payments.fulfillment.testonia.model.money;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WRETURNS_FEE table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WReturnsFeeDTO {

    @XmlElement(name = "bene_fee_amount")
    private Long beneficiaryFeeAmount;

    @XmlElement(name = "bene_fee_currency_code")
    private String beneficiaryFeeCurrencyCode;

    @XmlElement(name = "markup_amount")
    private Long markupAmount;

    @XmlElement(name = "markup_currency_code")
    private String markupCurrencyCode;

    @XmlElement(name = "status")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte status;

    @XmlElement(name = "total_fee_amount")
    private Long totalFeeAmount;

    @XmlElement(name = "total_fee_currency_code")
    private String totalFeeCurrencyCode;

    @XmlElement(name = "vend_fee_amount")
    private Long vendorFeeAmount;

    @XmlElement(name = "vend_fee_currency_code")
    private String vendorFeeCurrencyCode;

    public Long getBeneficiaryFeeAmount() {
        return beneficiaryFeeAmount;
    }

    public void setBeneficiaryFeeAmount(Long beneficiaryFeeAmount) {
        this.beneficiaryFeeAmount = beneficiaryFeeAmount;
    }

    public String getBeneficiaryFeeCurrencyCode() {
        return beneficiaryFeeCurrencyCode;
    }

    public void setBeneficiaryFeeCurrencyCode(String beneficiaryFeeCurrencyCode) {
        this.beneficiaryFeeCurrencyCode = beneficiaryFeeCurrencyCode;
    }

    public Long getMarkupAmount() {
        return markupAmount;
    }

    public void setMarkupAmount(Long markupAmount) {
        this.markupAmount = markupAmount;
    }

    public String getMarkupCurrencyCode() {
        return markupCurrencyCode;
    }

    public void setMarkupCurrencyCode(String markupCurrencyCode) {
        this.markupCurrencyCode = markupCurrencyCode;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getTotalFeeAmount() {
        return totalFeeAmount;
    }

    public void setTotalFeeAmount(Long totalFeeAmount) {
        this.totalFeeAmount = totalFeeAmount;
    }

    public String getTotalFeeCurrencyCode() {
        return totalFeeCurrencyCode;
    }

    public void setTotalFeeCurrencyCode(String totalFeeCurrencyCode) {
        this.totalFeeCurrencyCode = totalFeeCurrencyCode;
    }

    public Long getVendorFeeAmount() {
        return vendorFeeAmount;
    }

    public void setVendorFeeAmount(Long vendorFeeAmount) {
        this.vendorFeeAmount = vendorFeeAmount;
    }

    public String getVendorFeeCurrencyCode() {
        return vendorFeeCurrencyCode;
    }

    public void setVendorFeeCurrencyCode(String vendorFeeCurrencyCode) {
        this.vendorFeeCurrencyCode = vendorFeeCurrencyCode;
    }

}
