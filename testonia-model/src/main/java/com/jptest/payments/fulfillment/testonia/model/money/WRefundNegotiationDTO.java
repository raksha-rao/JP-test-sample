package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;

/**
 * Represents WREFUND_NEGOTIATION table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WRefundNegotiationDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "ask_amount")
    private Long askAmount;

    @XmlElement(name = "ask_amount_usd")
    private Long askAmountUSD;

    @XmlElement(name = "bid_amount")
    private Long bidAmount;

    @XmlElement(name = "bid_amount_usd")
    private Long bidAmountUSD;

    @XmlElement(name = "counterparty")
    private BigInteger counterparty;

    @XmlElement(name = "fee_amount")
    private Long feeAmount;

    @XmlElement(name = "fee_amount_usd")
    private Long feeAmountUSD;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "status")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte status;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAskAmount() {
        return askAmount;
    }

    public void setAskAmount(Long askAmount) {
        this.askAmount = askAmount;
    }

    public Long getAskAmountUSD() {
        return askAmountUSD;
    }

    public void setAskAmountUSD(Long askAmountUSD) {
        this.askAmountUSD = askAmountUSD;
    }

    public Long getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Long bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Long getBidAmountUSD() {
        return bidAmountUSD;
    }

    public void setBidAmountUSD(Long bidAmountUSD) {
        this.bidAmountUSD = bidAmountUSD;
    }

    public BigInteger getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(BigInteger counterparty) {
        this.counterparty = counterparty;
    }

    public Long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Long getFeeAmountUSD() {
        return feeAmountUSD;
    }

    public void setFeeAmountUSD(Long feeAmountUSD) {
        this.feeAmountUSD = feeAmountUSD;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

}
