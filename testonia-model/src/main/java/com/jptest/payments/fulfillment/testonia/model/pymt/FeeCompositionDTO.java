package com.jptest.payments.fulfillment.testonia.model.pymt;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents fee_composition table records
 * @JP Inc.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class FeeCompositionDTO {

    @XmlElement(name = "id")
    private BigInteger id;

    @XmlElement(name = "payment_side_id")
    private BigInteger paymentSideId;

    @XmlElement(name = "debit_credit_code")
    private String debitCreditCode;

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "type_code")
    private String typeCode;

    @XmlElement(name = "fee_amount")
    private Long feeAmount;

    @XmlElement(name = "fee_percentage")
    private BigDecimal feePercentage;

    @XmlElement(name = "fee_amount_fixed")
    private Long feeAmountFixed;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "calculation_factors")
    private String calculationFactors;

    @XmlElement(name = "initiation_time")
    private Long initiationTime;

    @XmlElement(name = "row_created_time")
    private Long rowCreatedTime;

    @XmlElement(name = "row_updated_time")
    private Long rowUpdatedTime;

    public BigInteger getPaymentSideId() {
        return paymentSideId;
    }

    public void setPaymentSideId(BigInteger paymentSideId) {
        this.paymentSideId = paymentSideId;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getFeePercentage() {
        return feePercentage;
    }

    public void setFeePercentage(BigDecimal feePercentage) {
        this.feePercentage = feePercentage;
    }

    public Long getFeeAmountFixed() {
        return feeAmountFixed;
    }

    public void setFeeAmountFixed(Long feeAmountFixed) {
        this.feeAmountFixed = feeAmountFixed;
    }

    public String getCalculationFactors() {
        return calculationFactors;
    }

    public void setCalculationFactors(String calculationFactors) {
        this.calculationFactors = calculationFactors;
    }

    public String getDebitCreditCode() {
        return debitCreditCode;
    }

    public void setDebitCreditCode(String debitCreditCode) {
        this.debitCreditCode = debitCreditCode;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getInitiationTime() {
        return initiationTime;
    }

    public void setInitiationTime(Long initiationTime) {
        this.initiationTime = initiationTime;
    }

    public Long getRowCreatedTime() {
        return rowCreatedTime;
    }

    public void setRowCreatedTime(Long rowCreatedTime) {
        this.rowCreatedTime = rowCreatedTime;
    }

    public Long getRowUpdatedTime() {
        return rowUpdatedTime;
    }

    public void setRowUpdatedTime(Long rowUpdatedTime) {
        this.rowUpdatedTime = rowUpdatedTime;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

}
