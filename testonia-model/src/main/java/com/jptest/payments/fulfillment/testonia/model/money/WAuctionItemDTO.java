package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Represents WAUCTION_ITEM table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WAuctionItemDTO {

    @XmlElement(name = "auction_time")
    private Long auctionTime;

    @XmlElement(name = "bid_amount")
    private Long bidAmount;

    @XmlElement(name = "buyer_protection")
    private BigInteger buyerProtection;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "flags")
    private Long flags;

    @XmlElement(name = "id")
    private BigInteger id;

    @XmlElement(name = "insurance_amount")
    private Long insuranceAmount;

    @XmlElement(name = "quantity")
    private Integer quantity;

    @XmlElement(name = "shipping_amount")
    private Long shippingAmount;

    @XmlElement(name = "tax_amount")
    private Long taxAmount;

    @XmlElement(name = "order_id")
    private String orderId;

    public Long getAuctionTime() {
        return auctionTime;
    }

    public void setAuctionTime(Long auctionTime) {
        this.auctionTime = auctionTime;
    }

    public Long getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Long bidAmount) {
        this.bidAmount = bidAmount;
    }

    public BigInteger getBuyerProtection() {
        return buyerProtection;
    }

    public void setBuyerProtection(BigInteger buyerProtection) {
        this.buyerProtection = buyerProtection;
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

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Long getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(Long insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(Long shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public Long getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Long taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
