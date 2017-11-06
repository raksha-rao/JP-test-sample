package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WTRANSACTION_DETAILS table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WTransactionDetailsDTO {

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "hosted_button_keys_text")
    private String hostedButtonKeysText;

    @XmlElement(name = "id")
    private BigInteger id;

    @XmlElement(name = "market")
    private Long market;

    @XmlElement(name = "trans_data_map_id")
    private BigInteger transDataMapId;

    @XmlElement(name = "wt_details_char1")
    private String wtDetailsChar1;

    @XmlElement(name = "wt_details_char2")
    private String wtDetailsChar2;

    @XmlElement(name = "wt_details_char3")
    private String wtDetailsChar3;

    @XmlElement(name = "wt_details_char4")
    private String wtDetailsChar4;

    @XmlElement(name = "wt_details_char5")
    private String wtDetailsChar5;

    @XmlElement(name = "wt_details_num1")
    private BigInteger wtDetailsNum1;

    @XmlElement(name = "wt_details_num2")
    private BigInteger wtDetailsNum2;

    @XmlElement(name = "wt_details_num3")
    private BigInteger wtDetailsNum3;

    @XmlElement(name = "wt_details_num4")
    private BigInteger wtDetailsNum4;

    @XmlElement(name = "wt_details_num5")
    private BigInteger wtDetailsNum5;

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public String getHostedButtonKeysText() {
        return hostedButtonKeysText;
    }

    public void setHostedButtonKeysText(String hostedButtonKeysText) {
        this.hostedButtonKeysText = hostedButtonKeysText;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Long getMarket() {
        return market;
    }

    public void setMarket(Long market) {
        this.market = market;
    }

    public BigInteger getTransDataMapId() {
        return transDataMapId;
    }

    public void setTransDataMapId(BigInteger transDataMapId) {
        this.transDataMapId = transDataMapId;
    }

    public String getWtDetailsChar1() {
        return wtDetailsChar1;
    }

    public void setWtDetailsChar1(String wtDetailsChar1) {
        this.wtDetailsChar1 = wtDetailsChar1;
    }

    public String getWtDetailsChar2() {
        return wtDetailsChar2;
    }

    public void setWtDetailsChar2(String wtDetailsChar2) {
        this.wtDetailsChar2 = wtDetailsChar2;
    }

    public String getWtDetailsChar3() {
        return wtDetailsChar3;
    }

    public void setWtDetailsChar3(String wtDetailsChar3) {
        this.wtDetailsChar3 = wtDetailsChar3;
    }

    public String getWtDetailsChar4() {
        return wtDetailsChar4;
    }

    public void setWtDetailsChar4(String wtDetailsChar4) {
        this.wtDetailsChar4 = wtDetailsChar4;
    }

    public String getWtDetailsChar5() {
        return wtDetailsChar5;
    }

    public void setWtDetailsChar5(String wtDetailsChar5) {
        this.wtDetailsChar5 = wtDetailsChar5;
    }

    public BigInteger getWtDetailsNum1() {
        return wtDetailsNum1;
    }

    public void setWtDetailsNum1(BigInteger wtDetailsNum1) {
        this.wtDetailsNum1 = wtDetailsNum1;
    }

    public BigInteger getWtDetailsNum2() {
        return wtDetailsNum2;
    }

    public void setWtDetailsNum2(BigInteger wtDetailsNum2) {
        this.wtDetailsNum2 = wtDetailsNum2;
    }

    public BigInteger getWtDetailsNum3() {
        return wtDetailsNum3;
    }

    public void setWtDetailsNum3(BigInteger wtDetailsNum3) {
        this.wtDetailsNum3 = wtDetailsNum3;
    }

    public BigInteger getWtDetailsNum4() {
        return wtDetailsNum4;
    }

    public void setWtDetailsNum4(BigInteger wtDetailsNum4) {
        this.wtDetailsNum4 = wtDetailsNum4;
    }

    public BigInteger getWtDetailsNum5() {
        return wtDetailsNum5;
    }

    public void setWtDetailsNum5(BigInteger wtDetailsNum5) {
        this.wtDetailsNum5 = wtDetailsNum5;
    }

}
