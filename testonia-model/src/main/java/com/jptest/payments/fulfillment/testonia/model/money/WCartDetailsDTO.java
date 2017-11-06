package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WCART_DETAILS table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WCartDetailsDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "charset")
    private String charset;

    @XmlElement(name = "cost")
    private BigInteger cost;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "extra_shipping")
    private BigInteger extraShipping;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "handling")
    private BigInteger handling;

    @XmlElement(name = "hosted_button_id")
    private BigInteger hostedButtonId;

    @XmlElement(name = "hosted_button_keys_text")
    private String hostedButtonKeysText;

    @XmlElement(name = "incentive_type")
    private BigInteger incentiveType;

    @XmlElement(name = "insurance")
    private BigInteger insurance;

    @XmlElement(name = "item_amount")
    private BigInteger itemAmount;

    @XmlElement(name = "item_description")
    private String itemDescription;

    @XmlElement(name = "item_discount")
    private BigInteger itemDiscount;

    @XmlElement(name = "item_name")
    private String itemName;

    @XmlElement(name = "item_number")
    private String itemNumber;

    @XmlElement(name = "item_upc")
    private String itemUpc;

    @XmlElement(name = "option_name1")
    private String optionName1;

    @XmlElement(name = "option_selection1")
    private String optionSelection1;

    @XmlElement(name = "quantity")
    private BigInteger quantity;

    @XmlElement(name = "quantity_unit")
    private BigInteger quantityUnit;

    @XmlElement(name = "return_policy_identifier")
    private String returnPolicyIdentifier;

    @XmlElement(name = "shipping")
    private BigInteger shipping;

    @XmlElement(name = "tax_amount")
    private BigInteger taxAmount;

    @XmlElement(name = "tax_rate")
    private BigInteger taxRate;

    @XmlElement(name = "total_amount")
    private BigInteger totalAmount;

    @XmlElement(name = "trans_data_map_id")
    private BigInteger transDataMapId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public BigInteger getCost() {
        return cost;
    }

    public void setCost(BigInteger cost) {
        this.cost = cost;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigInteger getExtraShipping() {
        return extraShipping;
    }

    public void setExtraShipping(BigInteger extraShipping) {
        this.extraShipping = extraShipping;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public BigInteger getHandling() {
        return handling;
    }

    public void setHandling(BigInteger handling) {
        this.handling = handling;
    }

    public BigInteger getHostedButtonId() {
        return hostedButtonId;
    }

    public void setHostedButtonId(BigInteger hostedButtonId) {
        this.hostedButtonId = hostedButtonId;
    }

    public String getHostedButtonKeysText() {
        return hostedButtonKeysText;
    }

    public void setHostedButtonKeysText(String hostedButtonKeysText) {
        this.hostedButtonKeysText = hostedButtonKeysText;
    }

    public BigInteger getIncentiveType() {
        return incentiveType;
    }

    public void setIncentiveType(BigInteger incentiveType) {
        this.incentiveType = incentiveType;
    }

    public BigInteger getInsurance() {
        return insurance;
    }

    public void setInsurance(BigInteger insurance) {
        this.insurance = insurance;
    }

    public BigInteger getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(BigInteger itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public BigInteger getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(BigInteger itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemUpc() {
        return itemUpc;
    }

    public void setItemUpc(String itemUpc) {
        this.itemUpc = itemUpc;
    }

    public String getOptionName1() {
        return optionName1;
    }

    public void setOptionName1(String optionName1) {
        this.optionName1 = optionName1;
    }

    public String getOptionSelection1() {
        return optionSelection1;
    }

    public void setOptionSelection1(String optionSelection1) {
        this.optionSelection1 = optionSelection1;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

    public BigInteger getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(BigInteger quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getReturnPolicyIdentifier() {
        return returnPolicyIdentifier;
    }

    public void setReturnPolicyIdentifier(String returnPolicyIdentifier) {
        this.returnPolicyIdentifier = returnPolicyIdentifier;
    }

    public BigInteger getShipping() {
        return shipping;
    }

    public void setShipping(BigInteger shipping) {
        this.shipping = shipping;
    }

    public BigInteger getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigInteger taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigInteger getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigInteger taxRate) {
        this.taxRate = taxRate;
    }

    public BigInteger getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigInteger totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigInteger getTransDataMapId() {
        return transDataMapId;
    }

    public void setTransDataMapId(BigInteger transDataMapId) {
        this.transDataMapId = transDataMapId;
    }

}
