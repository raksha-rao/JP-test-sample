package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Represents WTRANS_RETURN table record
 */
@XmlRootElement
public class WTransReturnDTO {

    private BigInteger accountNumber;
    private Long bouncedAmount;
    private Long bouncedAmountUsd;
    private String countryCode;
    private Long flags;
    private Integer nachaReturnType;
    private BigInteger transactionId;

    public BigInteger getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getBouncedAmount() {
        return this.bouncedAmount;
    }

    public void setBouncedAmount(Long bouncedAmount) {
        this.bouncedAmount = bouncedAmount;
    }

    public Long getBouncedAmountUsd() {
        return this.bouncedAmountUsd;
    }

    public void setBouncedAmountUsd(Long bouncedAmountUsd) {
        this.bouncedAmountUsd = bouncedAmountUsd;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Long getFlags() {
        return this.flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public Integer getNachaReturnType() {
        return this.nachaReturnType;
    }

    public void setNachaReturnType(Integer nachaReturnType) {
        this.nachaReturnType = nachaReturnType;
    }

    public BigInteger getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

}
