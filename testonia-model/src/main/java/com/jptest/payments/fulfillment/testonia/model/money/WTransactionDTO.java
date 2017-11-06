package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants.Status;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants.Type;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionFlagsHelper;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBMapToListAdapter;

/**
 * Represents WTRANSACTION_P2 table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class WTransactionDTO {

    private BigInteger accountNumber;
    private BigInteger achId;
    private BigInteger activityId;
    private String actualFixedFee;
    private BigDecimal actualPercentFee;
    private BigInteger addressId;
    private Long amount;
    private Long balanceAtTimeCreated;
    private BigInteger cctransId;
    private BigInteger counterparty;
    private String counterpartyAlias;
    private Byte counterpartyAliasType;
    private String counterpartyLastLoginIp;
    private String currencyCode;
    private Long flags1;
    private Long flags2;
    private Long flags3;
    private Long flags4;
    private BigInteger flags5;
    private BigInteger flags6;
    private BigInteger flags7;
    private BigInteger id;
    private String memo;
    private BigInteger messageId;
    private BigInteger parentId;
    private Byte reason;
    private Byte status;
    private Byte type;
    private BigInteger sharedId;
    private Byte subtype;
    private BigInteger targetAliasId;
    private Long timeCreated;
    private Long timeProcessed;
    private Long timeUpdated;
    private Long timeUser;
    private Byte transition;
    private Long usdAmount;

    @XmlElement(name = "account_number")
    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    @XmlElement(name = "ach_id")
    public BigInteger getAchId() {
        return achId;
    }

    public void setAchId(BigInteger achId) {
        this.achId = achId;
    }

    public BigInteger getActivityId() {
        return activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    @XmlElement(name = "actual_fixed_fee")
    public String getActualFixedFee() {
        return actualFixedFee;
    }

    public void setActualFixedFee(String actualFixedFee) {
        this.actualFixedFee = actualFixedFee;
    }

    @XmlElement(name = "actual_percent_fee")
    public BigDecimal getActualPercentFee() {
        return actualPercentFee;
    }

    public void setActualPercentFee(BigDecimal actualPercentFee) {
        this.actualPercentFee = actualPercentFee;
    }

    @XmlElement(name = "address_id")
    public BigInteger getAddressId() {
        return addressId;
    }

    public void setAddressId(BigInteger addressId) {
        this.addressId = addressId;
    }

    @XmlElement(name = "amount")
    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @XmlElement(name = "balance_at_time_created")
    public Long getBalanceAtTimeCreated() {
        return balanceAtTimeCreated;
    }

    public void setBalanceAtTimeCreated(Long balanceAtTimeCreated) {
        this.balanceAtTimeCreated = balanceAtTimeCreated;
    }

    public BigInteger getCctransId() {
        return cctransId;
    }

    public void setCctransId(BigInteger cctransId) {
        this.cctransId = cctransId;
    }

    @XmlElement(name = "counterparty")
    public BigInteger getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(BigInteger counterparty) {
        this.counterparty = counterparty;
    }

    public String getCounterpartyAlias() {
        return counterpartyAlias;
    }

    public void setCounterpartyAlias(String counterpartyAlias) {
        this.counterpartyAlias = counterpartyAlias;
    }

    @XmlElement(name = "counterparty_alias_type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    public Byte getCounterpartyAliasType() {
        return counterpartyAliasType;
    }

    public void setCounterpartyAliasType(Byte counterpartyAliasType) {
        this.counterpartyAliasType = counterpartyAliasType;
    }

    public String getCounterpartyLastLoginIp() {
        return counterpartyLastLoginIp;
    }

    public void setCounterpartyLastLoginIp(String counterpartyLastLoginIp) {
        this.counterpartyLastLoginIp = counterpartyLastLoginIp;
    }

    @XmlElement(name = "currency_code")
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

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

    @XmlElement(name = "flags1")
    public Long getFlags1() {
        return flags1;
    }

    public void setFlags1(Long flags1) {
        this.flags1 = flags1;
    }

    @XmlElement(name = "flags2")
    public Long getFlags2() {
        return flags2;
    }

    public void setFlags2(Long flags2) {
        this.flags2 = flags2;
    }

    @XmlElement(name = "flags3")
    public Long getFlags3() {
        return flags3;
    }

    public void setFlags3(Long flags3) {
        this.flags3 = flags3;
    }

    @XmlElement(name = "flags4")
    public Long getFlags4() {
        return flags4;
    }

    public void setFlags4(Long flags4) {
        this.flags4 = flags4;
    }

    @XmlElement(name = "flags5")
    public BigInteger getFlags5() {
        return flags5;
    }

    public void setFlags5(BigInteger flags5) {
        this.flags5 = flags5;
    }

    @XmlElement(name = "flags6")
    public BigInteger getFlags6() {
        return flags6;
    }

    public void setFlags6(BigInteger flags6) {
        this.flags6 = flags6;
    }

    @XmlElement(name = "flags7")
    public BigInteger getFlags7() {
        return flags7;
    }

    public void setFlags7(BigInteger flags7) {
        this.flags7 = flags7;
    }

    @XmlElement(name = "id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @XmlElement(name = "message_id")
    public BigInteger getMessageId() {
        return messageId;
    }

    public void setMessageId(BigInteger messageId) {
        this.messageId = messageId;
    }

    @XmlElement(name = "parent_id")
    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    @XmlElement(name = "reason")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    public Byte getReason() {
        return reason;
    }

    public void setReason(Byte reason) {
        this.reason = reason;
    }

    public BigInteger getSharedId() {
        return sharedId;
    }

    public void setSharedId(BigInteger sharedId) {
        this.sharedId = sharedId;
    }

    @XmlElement(name = "status")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @XmlElement(name = "subtype")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    public Byte getSubtype() {
        return subtype;
    }

    public void setSubtype(Byte subtype) {
        this.subtype = subtype;
    }

    @XmlElement(name = "target_alias_id")
    public BigInteger getTargetAliasId() {
        return targetAliasId;
    }

    public void setTargetAliasId(BigInteger targetAliasId) {
        this.targetAliasId = targetAliasId;
    }

    @XmlElement(name = "time_created")
    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    @XmlElement(name = "time_processed")
    public Long getTimeProcessed() {
        return timeProcessed;
    }

    public void setTimeProcessed(Long timeProcessed) {
        this.timeProcessed = timeProcessed;
    }

    @XmlElement(name = "time_updated")
    public Long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    @XmlElement(name = "time_user")
    public Long getTimeUser() {
        return timeUser;
    }

    public void setTimeUser(Long timeUser) {
        this.timeUser = timeUser;
    }

    @XmlElement(name = "transition")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    public Byte getTransition() {
        return transition;
    }

    public void setTransition(Byte transition) {
        this.transition = transition;
    }

    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @XmlElement(name = "usd_amount")
    public Long getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(Long usdAmount) {
        this.usdAmount = usdAmount;
    }

    /* START: UTILITY METHODS */
    public boolean isUsingBC() {
        return getType() == Type.BUYER_CREDIT_CHARGE.getByte() || getType() == Type.BUYER_CREDIT_CREDIT.getByte()
                || getType() == Type.BUYER_CREDIT_PAYMENT.getByte();
    }

    public boolean isCharge() {

        return getType() == Type.CHARGE.getByte() || (getParentId() == null && getStatus() == Status.DENIED.getByte()
                && getCctransId() != null && getCctransId().compareTo(BigInteger.ZERO) > 0);
    }

    public boolean isEFT() {
        return getType() == Type.EXT_INIT_DEPOSIT.getByte();
    }

    public boolean isMerchantPull() {
        return (getFlags3() & 0x00800000) > 0;
    }
    /* END: UTILITY METHODS */
}
