package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;


/**
 * Represents WITHDRAWAL_REVIEW_DETAILS table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WithdrawalReveiwDetailsDTO {

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    public BigInteger getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(final BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    @XmlElement(name = "hold_duration")
    private BigInteger holdDuration;

    public BigInteger getHoldDuration() {
        return this.holdDuration;
    }

    public void setHoldDuration(final BigInteger holdDuration) {
        this.holdDuration = holdDuration;
    }

    @XmlElement(name = "reason_code")
    private BigInteger reasonCode;

    public BigInteger getReasonCode() {
        return this.reasonCode;
    }

    public void setReasonCode(final BigInteger reasonCode) {
        this.reasonCode = reasonCode;
    }

    @XmlElement(name = "status")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte status;

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(final Byte status) {
        this.status = status;
    }

}
