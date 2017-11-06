package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WAUCTION_ITEM_ADDL_ATTR table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WAuctionItemAddlAttrDTO {

    @XmlElement(name = "addl_shipping")
    private Long additionalShipping;

    @XmlElement(name = "auction_item_id")
    private BigInteger auctionItemId;

    @XmlElement(name = "base_shipping")
    private Long baseShipping;

    @XmlElement(name = "handling")
    private Long handling;

    @XmlElement(name = "options")
    private String options;

    @XmlElement(name = "tax")
    private Long tax;

    public Long getAdditionalShipping() {
        return additionalShipping;
    }

    public void setAdditionalShipping(Long additionalShipping) {
        this.additionalShipping = additionalShipping;
    }

    public BigInteger getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(BigInteger auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public Long getBaseShipping() {
        return baseShipping;
    }

    public void setBaseShipping(Long baseShipping) {
        this.baseShipping = baseShipping;
    }

    public Long getHandling() {
        return handling;
    }

    public void setHandling(Long handling) {
        this.handling = handling;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

}
