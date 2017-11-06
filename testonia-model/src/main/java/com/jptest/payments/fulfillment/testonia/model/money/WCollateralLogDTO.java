package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WCOLLATERAL_LOG table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WCollateralLogDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "collateral_amount")
    private Long collateralAmount;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "net_change")
    private Long netChange;

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

    public Long getCollateralAmount() {
        return collateralAmount;
    }

    public void setCollateralAmount(Long collateralAmount) {
        this.collateralAmount = collateralAmount;
    }

    public Long getNetChange() {
        return netChange;
    }

    public void setNetChange(Long netChange) {
        this.netChange = netChange;
    }

}
