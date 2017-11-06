package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.WTransactionFlagsHelper;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBMapToListAdapter;

/**
 * Represents PAYMENT_CHECKPOINT table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentCheckpointDTO {

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "alias_type_code")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte aliasTypeCode;

    @XmlElement(name = "flags1")
    private Long flags1;

    @XmlElement(name = "flags2")
    private Long flags2;

    @XmlElement(name = "flags3")
    private Long flags3;

    @XmlElement(name = "flags4")
    private Long flags4;

    @XmlElement(name = "flags5")
    private BigInteger flags5;

    @XmlElement(name = "flags6")
    private BigInteger flags6;

    @XmlElement(name = "flags")
    @XmlJavaTypeAdapter(JAXBMapToListAdapter.class)
    public Map<String, String> getFlags() {
        Map<String, String> allFlags = new HashMap<>();
        allFlags.putAll(WTransactionFlagsHelper.getFlags1(flags1));
        allFlags.putAll(WTransactionFlagsHelper.getFlags2(flags2));
        allFlags.putAll(WTransactionFlagsHelper.getFlags3(flags3));
        allFlags.putAll(WTransactionFlagsHelper.getFlags4(flags4));
        allFlags.putAll(WTransactionFlagsHelper.getFlags5(flags5));
        allFlags.putAll(WTransactionFlagsHelper.getFlags6(flags6));
        return allFlags;
    }

    @XmlElement(name = "funding_source_name")
    private String fundingSourceName;

    @XmlElement(name = "reason_code")
    private String reasonCode;

    @XmlElement(name = "status_code")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte statusCode;

    @XmlElement(name = "subtype_code")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte subtypeCode;

    @XmlElement(name = "type_code")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte typeCode;

    public BigInteger getActivityId() {
        return activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    public Byte getAliasTypeCode() {
        return aliasTypeCode;
    }

    public void setAliasTypeCode(Byte aliasTypeCode) {
        this.aliasTypeCode = aliasTypeCode;
    }

    public Long getFlags1() {
        return flags1;
    }

    public void setFlags1(Long flags1) {
        this.flags1 = flags1;
    }

    public Long getFlags2() {
        return flags2;
    }

    public void setFlags2(Long flags2) {
        this.flags2 = flags2;
    }

    public Long getFlags3() {
        return flags3;
    }

    public void setFlags3(Long flags3) {
        this.flags3 = flags3;
    }

    public Long getFlags4() {
        return flags4;
    }

    public void setFlags4(Long flags4) {
        this.flags4 = flags4;
    }

    public BigInteger getFlags5() {
        return flags5;
    }

    public void setFlags5(BigInteger flags5) {
        this.flags5 = flags5;
    }

    public BigInteger getFlags6() {
        return flags6;
    }

    public void setFlags6(BigInteger flags6) {
        this.flags6 = flags6;
    }

    public String getFundingSourceName() {
        return fundingSourceName;
    }

    public void setFundingSourceName(String fundingSourceName) {
        this.fundingSourceName = fundingSourceName;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Byte getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Byte statusCode) {
        this.statusCode = statusCode;
    }

    public Byte getSubtypeCode() {
        return subtypeCode;
    }

    public void setSubtypeCode(Byte subtypeCode) {
        this.subtypeCode = subtypeCode;
    }

    public Byte getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Byte typeCode) {
        this.typeCode = typeCode;
    }

}
