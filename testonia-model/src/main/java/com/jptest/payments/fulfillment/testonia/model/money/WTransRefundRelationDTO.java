package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WTRANS_REFUND_RELATION table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WTransRefundRelationDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "bc_credit_id")
    private BigInteger bcCreditId;

    @XmlElement(name = "cc_credit_id")
    private BigInteger ccCreditId;

    @XmlElement(name = "currency_conversion_id")
    private BigInteger currencyConversionId;

    @XmlElement(name = "fee_refund_id")
    private BigInteger feeRefundId;

    @XmlElement(name = "refund_id")
    private BigInteger refundId;

    @XmlElement(name = "temp_hold_id")
    private BigInteger tempHoldId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigInteger getBcCreditId() {
        return bcCreditId;
    }

    public void setBcCreditId(BigInteger bcCreditId) {
        this.bcCreditId = bcCreditId;
    }

    public BigInteger getCcCreditId() {
        return ccCreditId;
    }

    public void setCcCreditId(BigInteger ccCreditId) {
        this.ccCreditId = ccCreditId;
    }

    public BigInteger getCurrencyConversionId() {
        return currencyConversionId;
    }

    public void setCurrencyConversionId(BigInteger currencyConversionId) {
        this.currencyConversionId = currencyConversionId;
    }

    public BigInteger getFeeRefundId() {
        return feeRefundId;
    }

    public void setFeeRefundId(BigInteger feeRefundId) {
        this.feeRefundId = feeRefundId;
    }

    public BigInteger getRefundId() {
        return refundId;
    }

    public void setRefundId(BigInteger refundId) {
        this.refundId = refundId;
    }

    public BigInteger getTempHoldId() {
        return tempHoldId;
    }

    public void setTempHoldId(BigInteger tempHoldId) {
        this.tempHoldId = tempHoldId;
    }

}
