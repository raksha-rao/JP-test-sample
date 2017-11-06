package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBByteToStringAdapter;


/**
 * Represents WTRANSACTION_LOCKED_WDRL table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WtransactionLockedWdrlDTO {

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    public BigInteger getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(final BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    public BigInteger getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(final BigInteger accountNumber) {
        this.accountNumber = accountNumber;
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

    @XmlElement(name = "withdrawal_score")
    private BigInteger withdrawalScore;

    public BigInteger getWithdrawalScore() {
        return this.withdrawalScore;
    }

    public void setWithdrawalScore(final BigInteger withdrawalScore) {
        this.withdrawalScore = withdrawalScore;
    }

    @XmlElement(name = "is_fraudserv_processed")
    @XmlJavaTypeAdapter(JAXBByteToStringAdapter.class)
    private Byte isfraudservprocessed;

    public Byte getIsfraudservprocessed() {
        return this.isfraudservprocessed;
    }

    public void setIsfraudservprocessed(final Byte isfraudservprocessed) {
        this.isfraudservprocessed = isfraudservprocessed;
    }

    @XmlElement(name = "hold_duration_seconds")
    private BigInteger holdDurationSeconds;

    public BigInteger getHoldDurationSeconds() {
        return this.holdDurationSeconds;
    }

    public void setHoldDurationSeconds(final BigInteger holdDurationSeconds) {
        this.holdDurationSeconds = holdDurationSeconds;
    }

    @XmlElement(name = "rule_id")
    private BigInteger ruleId;

    public BigInteger getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(final BigInteger ruleId) {
        this.ruleId = ruleId;
    }


}
