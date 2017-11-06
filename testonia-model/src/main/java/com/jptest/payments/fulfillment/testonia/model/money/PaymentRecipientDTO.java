package com.jptest.payments.fulfillment.testonia.model.money;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentRecipientDTO {

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "fee_history")
    private List<FeeHistoryDTO> feeHistory;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "fx_history")
    private List<FXHistoryDTO> fxHistory;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "pactivity_trans_map")
    private List<PActivityTransMapDTO> paymentActivityTransactionMap;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wcollateral_log")
    private List<WCollateralLogDTO> collateralLogs;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wrefund_negotiation")
    private List<WRefundNegotiationDTO> refundNegotiations;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wreturns_fee")
    private List<WReturnsFeeDTO> returnFees;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wreturns_review")
    private List<WReturnsReviewDTO> returnReviews;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wsubbalance_transaction")
    private List<WSubBalanceTransactionDTO> subBalanceTransactions;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction")
    private List<WTransactionDTO> transactions = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_details")
    private List<WTransactionDetailsDTO> transactionDetails;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_url")
    private List<WTransactionUrlDTO> transactionUrls;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtrans_refund_relation")
    private List<WTransRefundRelationDTO> transactionRefundRelations;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wuser_holding")
    private List<WUserHoldingDTO> userHoldings;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wuser_holding_subbalance")
    private List<WUserHoldingSubBalanceDTO> userHoldingSubBalances;

    public List<WCollateralLogDTO> getCollateralLogs() {
        return collateralLogs;
    }

    public void setCollateralLogs(List<WCollateralLogDTO> collateralLogs) {
        this.collateralLogs = collateralLogs;
    }

    public List<FeeHistoryDTO> getFeeHistory() {
        return feeHistory;
    }

    public void setFeeHistory(List<FeeHistoryDTO> feeHistory) {
        this.feeHistory = feeHistory;
    }

    public List<FXHistoryDTO> getFXHistory() {
        return fxHistory;
    }

    public void setFXHistory(List<FXHistoryDTO> fxHistory) {
        this.fxHistory = fxHistory;
    }

    public List<PActivityTransMapDTO> getPaymentActivityTransactionMap() {
        return paymentActivityTransactionMap;
    }

    public void setPaymentActivityTransactionMap(List<PActivityTransMapDTO> paymentActivityTransactionMap) {
        this.paymentActivityTransactionMap = paymentActivityTransactionMap;
    }

    public List<WRefundNegotiationDTO> getRefundNegotiations() {
        return refundNegotiations;
    }

    public void setRefundNegotiations(List<WRefundNegotiationDTO> refundNegotiations) {
        this.refundNegotiations = refundNegotiations;
    }

    public List<WReturnsFeeDTO> getReturnFees() {
        return returnFees;
    }

    public void setReturnFees(List<WReturnsFeeDTO> returnFees) {
        this.returnFees = returnFees;
    }

    public List<WReturnsReviewDTO> getReturnReviews() {
        return returnReviews;
    }

    public void setReturnReviews(List<WReturnsReviewDTO> returnReviews) {
        this.returnReviews = returnReviews;
    }

    public List<WSubBalanceTransactionDTO> getSubBalanceTransactions() {
        return subBalanceTransactions;
    }

    public void setSubBalanceTransactions(List<WSubBalanceTransactionDTO> subBalanceTransactions) {
        this.subBalanceTransactions = subBalanceTransactions;
    }

    public List<WTransactionDTO> getTransactions() {
        return transactions;
    }

    public void addTransaction(WTransactionDTO transaction) {
        transactions.add(transaction);
    }

    public void addTransactions(List<WTransactionDTO> transactions) {
        this.transactions.addAll(transactions);
    }

    public List<WTransactionDetailsDTO> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(List<WTransactionDetailsDTO> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public List<WTransactionUrlDTO> getTransactionUrls() {
        return transactionUrls;
    }

    public void setTransactionUrls(List<WTransactionUrlDTO> transactionUrls) {
        this.transactionUrls = transactionUrls;
    }

    public List<WTransRefundRelationDTO> getTransactionRefundRelations() {
        return transactionRefundRelations;
    }

    public void setTransactionRefundRelations(List<WTransRefundRelationDTO> transactionRefundRelations) {
        this.transactionRefundRelations = transactionRefundRelations;
    }

    public List<WUserHoldingDTO> getUserHoldings() {
        return userHoldings;
    }

    public void setUserHoldings(List<WUserHoldingDTO> userHoldings) {
        this.userHoldings = userHoldings;
    }

    public List<WUserHoldingSubBalanceDTO> getUserHoldingSubBalances() {
        return userHoldingSubBalances;
    }

    public void setUserHoldingSubBalances(List<WUserHoldingSubBalanceDTO> userHoldingSubBalances) {
        this.userHoldingSubBalances = userHoldingSubBalances;
    }

}
