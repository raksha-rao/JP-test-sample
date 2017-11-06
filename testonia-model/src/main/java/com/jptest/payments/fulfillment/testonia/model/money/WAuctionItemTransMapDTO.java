package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents WAUCTION_ITEM_TRANS_MAP table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WAuctionItemTransMapDTO {

    @XmlElement(name = "auction_item_id")
    private BigInteger auctionItemId;

    @XmlElement(name = "payee_transaction_id")
    private BigInteger payeeTransactionId;

    @XmlElement(name = "payer_transaction_id")
    private BigInteger payerTransactionId;

    public BigInteger getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(BigInteger auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public BigInteger getPayeeTransactionId() {
        return payeeTransactionId;
    }

    public void setPayeeTransactionId(BigInteger payeeTransactionId) {
        this.payeeTransactionId = payeeTransactionId;
    }

    public BigInteger getPayerTransactionId() {
        return payerTransactionId;
    }

    public void setPayerTransactionId(BigInteger payerTransactionId) {
        this.payerTransactionId = payerTransactionId;
    }

}
