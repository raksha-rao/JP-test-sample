package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.payments.fulfillment.testonia.model.util.WUserHoldingFlagsHelper;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBMapToListAdapter;

/**
 * Represents WUSER_HOLDING table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WUserHoldingDTO {

    @XmlElement(name = "id")
    private BigInteger id;

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "balance")
    private Long balance;

    @XmlElement(name = "collateral_amount")
    private Long collateralAmount;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "flags-values")
    @XmlJavaTypeAdapter(JAXBMapToListAdapter.class)
    private Map<String, String> allFlags = new HashMap<>();

    @XmlElement(name = "pending_reversals")
    private Long pendingReversals;

    public BigInteger getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(final BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getBalance() {
        return this.balance;
    }

    public void setBalance(final Long balance) {
        this.balance = balance;
    }

    public Long getCollateralAmount() {
        return this.collateralAmount;
    }

    public void setCollateralAmount(final Long collateralAmount) {
        this.collateralAmount = collateralAmount;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(final String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getFlags() {
        return this.flags;
    }

    public void setFlags(final Long flags) {
        this.flags = flags;
        this.allFlags.clear();
        this.allFlags.putAll(WUserHoldingFlagsHelper.getFlags(flags));
    }

    public Long getPendingReversals() {
        return this.pendingReversals;
    }

    public void setPendingReversals(final Long pendingReversals) {
        this.pendingReversals = pendingReversals;
    }

    public BigInteger getId() {
        return this.id;
    }

    public void setId(final BigInteger id) {
        this.id = id;
    }
}
