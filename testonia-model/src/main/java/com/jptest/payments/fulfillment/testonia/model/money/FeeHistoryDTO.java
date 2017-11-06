package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.FXDataTrimmer;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents FEE_HISTORY table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FeeHistoryDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "actual_fixed_fee")
    private Long actualFixedFee;

    @XmlElement(name = "actual_percent_fee")
    private BigDecimal actualPercentFee;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlTransient
    private String data;
    
    @XmlElement(name = "data")
    private String truncatedData;

    @XmlElement(name = "total_fee_amount")
    private Long totalFeeAmount;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    @XmlElement(name = "transaction_type")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte transactionType;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getActualFixedFee() {
        return actualFixedFee;
    }

    public void setActualFixedFee(Long actualFixedFee) {
        this.actualFixedFee = actualFixedFee;
    }

    public BigDecimal getActualPercentFee() {
        return actualPercentFee;
    }

    public void setActualPercentFee(BigDecimal actualPercentFee) {
        this.actualPercentFee = actualPercentFee;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.truncatedData = FXDataTrimmer.trimFeeAuditTrail(data);
    }
    
    public String getTruncatedData() {
		return truncatedData;
	}

    public Long getTotalFeeAmount() {
        return totalFeeAmount;
    }

    public void setTotalFeeAmount(Long totalFeeAmount) {
        this.totalFeeAmount = totalFeeAmount;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    public Byte getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Byte transactionType) {
        this.transactionType = transactionType;
    }

}
