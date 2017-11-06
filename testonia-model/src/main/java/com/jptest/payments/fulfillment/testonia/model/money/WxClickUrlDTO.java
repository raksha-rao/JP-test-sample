package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WXCLICK_URL table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WxClickUrlDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "referral_url")
    private String referralUrl;

    @XmlElement(name = "return_url")
    private String returnUrl;

    @XmlElement(name = "trans_data_map_id")
    private BigInteger transDataMapId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getReferralUrl() {
        return referralUrl;
    }

    public void setReferralUrl(String referralUrl) {
        this.referralUrl = referralUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public BigInteger getTransDataMapId() {
        return transDataMapId;
    }

    public void setTransDataMapId(BigInteger transDataMapId) {
        this.transDataMapId = transDataMapId;
    }

}
