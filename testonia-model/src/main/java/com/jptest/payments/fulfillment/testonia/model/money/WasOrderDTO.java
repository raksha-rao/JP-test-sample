package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WAS_ORDER_P2 table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class WasOrderDTO {

    private BigInteger Id;
    private String status;
    private Long flags;
    private String currencycode;
    private Long amountAuthorized;
    private Long amountSettled;
    private Long amount;
    private BigInteger numberOfAuthorizations;
    private BigInteger numberOfSettlements;
    private BigInteger accountNumber;
    private BigInteger counterparty;
    private Long timeCreated;
    private Long timeUpdated;
    private Long expirationTime;
    private String fsType;
    private BigInteger fsId;
    private String bufsType;
    private BigInteger bufsId;
    private String type;
    private BigInteger messageId;
    private BigInteger addressId;
    private BigInteger parentId;
    private BigInteger paymentFlowId;
    private BigInteger activityId;
   

    @XmlElement(name = "id")
    public BigInteger getId() {
        return Id;
    }

    public void setId(BigInteger Id) {
        this.Id = Id;
    }
    
    @XmlElement(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @XmlElement(name = "flags")
    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }
    
    @XmlElement(name = "amount_authorized")
    public String getCurrencyCode() {
        return currencycode;
    }

    public void setCurrencyCode(String currencycode) {
        this.currencycode = currencycode;
    }
    
    @XmlElement(name = "currency_code")
    public Long getAmountAuthorized() {
        return amountAuthorized;
    }

    public void setAmountAuthorized(Long amountAuthorized) {
        this.amountAuthorized = amountAuthorized;
    }
    
    @XmlElement(name = "amount_settled")
    public Long getAmountSettled() {
    	return amountSettled;
    }
    
    public void setAmountSettled(Long amountSettled) {
    	this.amountSettled = amountSettled;
    }
    
    @XmlElement(name = "amount")
    public Long getamount() {
    	return amount;
    }
    
    public void setAmount(Long amount) {
    	this.amount = amount;
    }
    
    @XmlElement(name = "number_of_authorizations")
    public BigInteger getNumberOfAuthorizations() {
    	return numberOfAuthorizations;
    }
    
    public void setNumberOfAuthorizations(BigInteger numberOfAuthorizations) {
    	this.numberOfAuthorizations = numberOfAuthorizations;
    }
    
    @XmlElement(name = "number_of_settlements")
    public BigInteger getNumberOfSettlements() {
    	return numberOfSettlements;
    }
    
    public void setNumberOfSettlements(BigInteger numberOfSettlements) {
    	this.numberOfSettlements = numberOfSettlements;
    }
    
    @XmlElement(name = "account_number") 
    public BigInteger getAccountNumber() {
    	return accountNumber;
    }
    
    public void setAccountNumber(BigInteger accountNumber) {
    	this.accountNumber = accountNumber;
    }
    
    @XmlElement(name = "counterparty")
    public BigInteger getCounterParty() {
    	return counterparty;
    }
    
    public void setCounterParty(BigInteger counterparty) {
    	this.counterparty = counterparty;
    }
    
    @XmlElement(name = "time_created") 
    public Long getTimeCreated() {
    	return timeCreated;
    }
    
    public void setTimeCreated(Long timeCreated) {
    	this.timeCreated = timeCreated;
    }

    @XmlElement(name = "time_updated")
    public Long getTimeUpdated() {
    	return timeUpdated;
    }
    
    public void setTimeUpdated(Long timeUpdated) {
    	this.timeUpdated = timeUpdated;
    }
    
    @XmlElement(name = "expiration_time") 
    public Long getExpirationTime() {
    	return expirationTime;
    }
    
    public void setExpirationTime (Long expirationTime) {
    	this.expirationTime = expirationTime;
    }
    
    @XmlElement(name = "fs_type")
    public String getFsType() {
    	return fsType;
    }
    
    public void setFsType(String fsType) {
    	this.fsType = fsType;
    }
    
    @XmlElement(name = "fs_id")
    public BigInteger getFsId() {
    	return fsId;
    }
    
    public void setFsId(BigInteger fsId) {
    	this.fsId = fsId;
    }
    
    @XmlElement(name = "bufs_type") 
    public String getBufsType() {
    	return bufsType;
    }
    
    public void setBufsType(String bufsType) {
    	this.bufsType = bufsType;
    }
    
    @XmlElement(name = "bufs_id")
    public BigInteger getBufsId() {
    	return bufsId;
    }
    
    public void setBufsId(BigInteger bufsId) {
    	this.bufsId = bufsId;
    }
    
    @XmlElement(name = "type")
    public String getType() {
    	return type;
    }
    
    public void setType(String type) {
    	this.type = type;
    }
    
    @XmlElement(name = "message_id")
    public BigInteger getMessageId() {
    	return messageId;
    }
    
    public void setMessageId(BigInteger messageId) {
    	this.messageId = messageId;
    }
    
    @XmlElement(name = "address_id")
    public BigInteger getAddressId() {
    	return addressId;
    }
    
    public void setAddressId(BigInteger addressId) {
    	this.addressId = addressId;
    }
    
    @XmlElement(name = "parent_id") 
    public BigInteger getParentId() {
    	return parentId;
    }
    
    public void setParentId (BigInteger parentId) {
    	this.parentId = parentId;
    }
    
    @XmlElement(name = "payment_flow_id")
    public BigInteger getPaymentFlowId() {
    	return paymentFlowId;
    }
    
    public void setPaymentFlowId(BigInteger paymentFlowId) {
    	this.paymentFlowId = paymentFlowId;
    }
    
    @XmlElement(name = "activity_id")
    public BigInteger getActivityId() {
    	return activityId;
    }
    
    public void setActivityId(BigInteger activityId) {
    	this.activityId = activityId;
    }
}
