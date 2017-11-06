package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WXCLICK table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WxClickDTO {

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "adjustment_amount")
    private Long adjustmentAmount;

    @XmlElement(name = "counterparty")
    private BigInteger counterparty;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "gift_message")
    private String giftMessage;

    @XmlElement(name = "gift_wrap_name")
    private String giftWrapName;

    @XmlElement(name = "handling_amount")
    private Long handlingAmount;

    @XmlElement(name = "invoice")
    private String invoice;

    @XmlElement(name = "item_amount")
    private Long itemAmount;

    @XmlElement(name = "item_name")
    private String itemName;

    @XmlElement(name = "item_number")
    private String itemNumber;

    @XmlElement(name = "promotional_email_address")
    private String promotionalEmailAddress;

    @XmlElement(name = "quantity")
    private Integer quantity;

    @XmlElement(name = "sales_tax")
    private BigDecimal salesTax;

    @XmlElement(name = "shipping_amount")
    private Long shippingAmount;

    @XmlElement(name = "survey_answer")
    private String surveyAnswer;

    @XmlElement(name = "survey_question")
    private String surveyQuestion;

    @XmlElement(name = "trans_data_map_id")
    private BigInteger transDataMapId;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(Long adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public BigInteger getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(BigInteger counterparty) {
        this.counterparty = counterparty;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }

    public String getGiftWrapName() {
        return giftWrapName;
    }

    public void setGiftWrapName(String giftWrapName) {
        this.giftWrapName = giftWrapName;
    }

    public Long getHandlingAmount() {
        return handlingAmount;
    }

    public void setHandlingAmount(Long handlingAmount) {
        this.handlingAmount = handlingAmount;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Long getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(Long itemAmount) {
        this.itemAmount = itemAmount;
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

    public String getPromotionalEmailAddress() {
        return promotionalEmailAddress;
    }

    public void setPromotionalEmailAddress(String promotionalEmailAddress) {
        this.promotionalEmailAddress = promotionalEmailAddress;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(BigDecimal salesTax) {
        this.salesTax = salesTax;
    }

    public Long getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(Long shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public String getSurveyAnswer() {
        return surveyAnswer;
    }

    public void setSurveyAnswer(String surveyAnswer) {
        this.surveyAnswer = surveyAnswer;
    }

    public String getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(String surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    public BigInteger getTransDataMapId() {
        return transDataMapId;
    }

    public void setTransDataMapId(BigInteger transDataMapId) {
        this.transDataMapId = transDataMapId;
    }

}
