package com.jptest.payments.fulfillment.testonia.model.money;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.jptest.payments.fulfillment.testonia.model.pymt.CounterPartyAliasDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.MoneyMovementDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.PaymentSideDTO;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentSenderDTO {

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
    @XmlElementWrapper(name = "wauction_item")
    private List<WAuctionItemDTO> auctionItems;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wauction_item_addl_attr")
    private List<WAuctionItemAddlAttrDTO> auctionItemAdditionalAttributes;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wauction_item_trans_map")
    private List<WAuctionItemTransMapDTO> auctionItemTransMap;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wcart_details")
    private List<WCartDetailsDTO> cartDetails = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wcollateral_log")
    private List<WCollateralLogDTO> collateralLogs;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "weft_external_record")
    private List<WEFTransactionDTO> wefTransactions;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wmerchant_pull_trans")
    private List<WMerchantPullTransDTO> merchantPullTransactions;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wmerchant_pull_log")
    private List<WMerchantPullLogDTO> merchantPullLogs;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wpayment_extension_data")
    private List<WPaymentExtensionDataDTO> paymentExtensionData;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtrans_data_map")
    private List<WTransDataMapDTO> transDataMap;

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
    @XmlElementWrapper(name = "wtransaction_admin")
    private List<WTransactionAdminDTO> transactionAdmin;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_auth")
    private List<WTransactionAuthDTO> transactionAuths;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_bufs")
    private List<WTransactionBufsDTO> transactionsWithBufs;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_details")
    private List<WTransactionDetailsDTO> transactionDetails;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_pending")
    private List<WTransactionPendingDTO> pendingTransactions;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_memo")
    private List<WTransactionMemoDTO> transactionMemos;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_url")
    private List<WTransactionUrlDTO> transactionUrls;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_world")
    private List<WTransactionWorldDTO> transactionsWorld;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtrans_refund_relation")
    private List<WTransRefundRelationDTO> transactionRefundRelations;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtrans_return")
    private List<WTransReturnDTO> transactionReturns;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wuser_holding")
    private List<WUserHoldingDTO> userHoldings;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wwax_transaction")
    private List<WwaxTransactionDTO> waxTransactions;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wxclick")
    private List<WxClickDTO> wxClicks;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "payment_side")
    private List<PaymentSideDTO> paymentSideDTO = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "money_movement")
    private List<MoneyMovementDTO> moneyMovementDTO = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "pactivity_trans_map")
    private List<PActivityTransMapDTO> pActivityTransMapDTO = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wxclick_url")
    private List<WxClickUrlDTO> wxClickUrlDTO = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "was_order_p2")
    private List<WasOrderDTO> orders;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "counterparty_alias")
    private List<CounterPartyAliasDTO> aliasValues;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "withdrawal_review_details")
    private List<WithdrawalReveiwDetailsDTO> withdrawalReviewDetails;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wtransaction_locked_wdrl")
    private List<WtransactionLockedWdrlDTO> wtransactionLockedWdrl;

    public List<PaymentSideDTO> getPaymentSideDTO() {
        return this.paymentSideDTO;
    }

    public void addPaymentSideDTO(final List<PaymentSideDTO> paymentSideDTO) {
        this.paymentSideDTO.addAll(paymentSideDTO);
    }

    public List<MoneyMovementDTO> getMoneyMovementDTO() {
        return this.moneyMovementDTO;
    }

    public void addMoneyMovementDTO(final List<MoneyMovementDTO> moneyMovementDTO) {
        this.moneyMovementDTO.addAll(moneyMovementDTO);
    }

    public List<PActivityTransMapDTO> getpActivityTransMapDTO() {
        return this.pActivityTransMapDTO;
    }

    public void addpActivityTransMapDTO(final List<PActivityTransMapDTO> pActivityTransMapDTO) {
        this.pActivityTransMapDTO.addAll(pActivityTransMapDTO);
    }

    public List<FeeHistoryDTO> getFeeHistory() {
        return this.feeHistory;
    }

    public void setFeeHistory(final List<FeeHistoryDTO> feeHistory) {
        this.feeHistory = feeHistory;
    }

    public List<FXHistoryDTO> getFXHistory() {
        return this.fxHistory;
    }

    public void setFXHistory(final List<FXHistoryDTO> fxHistory) {
        this.fxHistory = fxHistory;
    }

    public List<PActivityTransMapDTO> getPaymentActivityTransactionMap() {
        return this.paymentActivityTransactionMap;
    }

    public void setPaymentActivityTransactionMap(final List<PActivityTransMapDTO> paymentActivityTransactionMap) {
        this.paymentActivityTransactionMap = paymentActivityTransactionMap;
    }

    public List<WAuctionItemDTO> getAuctionItems() {
        return this.auctionItems;
    }

    public void setAuctionItems(final List<WAuctionItemDTO> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public List<WAuctionItemAddlAttrDTO> getAuctionItemAdditionalAttributes() {
        return this.auctionItemAdditionalAttributes;
    }

    public void setAuctionItemAdditionalAttributes(final List<WAuctionItemAddlAttrDTO> auctionItemAdditionalAttributes) {
        this.auctionItemAdditionalAttributes = auctionItemAdditionalAttributes;
    }

    public List<WAuctionItemTransMapDTO> getAuctionItemTransMap() {
        return this.auctionItemTransMap;
    }

    public void setAuctionItemTransMap(final List<WAuctionItemTransMapDTO> auctionItemTransMap) {
        this.auctionItemTransMap = auctionItemTransMap;
    }

    public List<WCollateralLogDTO> getCollateralLogs() {
        return this.collateralLogs;
    }

    public void setCollateralLogs(final List<WCollateralLogDTO> collateralLogs) {
        this.collateralLogs = collateralLogs;
    }

    public List<WEFTransactionDTO> getWefTransactions() {
        return this.wefTransactions;
    }

    public void setWefTransactions(final List<WEFTransactionDTO> wefTransactions) {
        this.wefTransactions = wefTransactions;
    }

    public List<WMerchantPullTransDTO> getMerchantPullTransactions() {
        return this.merchantPullTransactions;
    }

    public void setMerchantPullTransactions(final List<WMerchantPullTransDTO> merchantPullTransactions) {
        this.merchantPullTransactions = merchantPullTransactions;
    }

    public List<WMerchantPullLogDTO> getMerchantPullLogs() {
        return this.merchantPullLogs;
    }

    public void setMerchantPullLogs(final List<WMerchantPullLogDTO> merchantPullLogs) {
        this.merchantPullLogs = merchantPullLogs;
    }

    public List<WPaymentExtensionDataDTO> getPaymentExtensionData() {
        return this.paymentExtensionData;
    }

    public void setPaymentExtensionData(final List<WPaymentExtensionDataDTO> paymentExtensionData) {
        this.paymentExtensionData = paymentExtensionData;
    }

    public List<WRefundNegotiationDTO> getRefundNegotiations() {
        return this.refundNegotiations;
    }

    public void setRefundNegotiations(final List<WRefundNegotiationDTO> refundNegotiations) {
        this.refundNegotiations = refundNegotiations;
    }

    public List<WReturnsFeeDTO> getReturnFees() {
        return this.returnFees;
    }

    public void setReturnFees(final List<WReturnsFeeDTO> returnFees) {
        this.returnFees = returnFees;
    }

    public List<WReturnsReviewDTO> getReturnReviews() {
        return this.returnReviews;
    }

    public void setReturnReviews(final List<WReturnsReviewDTO> returnReviews) {
        this.returnReviews = returnReviews;
    }

    public List<WSubBalanceTransactionDTO> getSubBalanceTransactions() {
        return this.subBalanceTransactions;
    }

    public void setSubBalanceTransactions(final List<WSubBalanceTransactionDTO> subBalanceTransactions) {
        this.subBalanceTransactions = subBalanceTransactions;
    }

    public List<WTransactionDTO> getTransactions() {
        return this.transactions;
    }

    public void addTransaction(final WTransactionDTO transaction) {
        this.transactions.add(transaction);
    }

    public void addTransactions(final List<WTransactionDTO> transactions) {
        this.transactions.addAll(transactions);
    }

    public List<WTransactionAdminDTO> getTransactionAdmin() {
        return this.transactionAdmin;
    }

    public void setTransactionAdmin(final List<WTransactionAdminDTO> transactionAdmin) {
        this.transactionAdmin = transactionAdmin;
    }

    public List<WTransactionAuthDTO> getTransactionAuths() {
        return this.transactionAuths;
    }

    public void setTransactionAuths(final List<WTransactionAuthDTO> transactionAuths) {
        this.transactionAuths = transactionAuths;
    }

    public List<WTransactionBufsDTO> getTransactionsWithBufs() {
        return this.transactionsWithBufs;
    }

    public void setTransactionsWithBufs(final List<WTransactionBufsDTO> transactionsWithBufs) {
        this.transactionsWithBufs = transactionsWithBufs;
    }

    public List<WTransactionDetailsDTO> getTransactionDetails() {
        return this.transactionDetails;
    }

    public void setTransactionDetails(final List<WTransactionDetailsDTO> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public List<WTransactionPendingDTO> getPendingTransactions() {
        return this.pendingTransactions;
    }

    public void setPendingTransactions(final List<WTransactionPendingDTO> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public List<WTransactionMemoDTO> getTransactionMemos() {
        return this.transactionMemos;
    }

    public void setTransactionMemos(final List<WTransactionMemoDTO> transactionMemos) {
        this.transactionMemos = transactionMemos;
    }

    public List<WTransactionUrlDTO> getTransactionUrls() {
        return this.transactionUrls;
    }

    public void setTransactionUrls(final List<WTransactionUrlDTO> transactionUrls) {
        this.transactionUrls = transactionUrls;
    }

    public List<WTransactionWorldDTO> getTransactionsWorld() {
        return this.transactionsWorld;
    }

    public void setTransactionsWorld(final List<WTransactionWorldDTO> transactionsWorld) {
        this.transactionsWorld = transactionsWorld;
    }

    public List<WTransRefundRelationDTO> getTransactionRefundRelations() {
        return this.transactionRefundRelations;
    }

    public void setTransactionRefundRelations(final List<WTransRefundRelationDTO> transactionRefundRelations) {
        this.transactionRefundRelations = transactionRefundRelations;
    }

    public List<WTransReturnDTO> getTransactionReturns() {
        return this.transactionReturns;
    }

    public void setTransactionReturns(final List<WTransReturnDTO> transactionReturns) {
        this.transactionReturns = transactionReturns;
    }

    public List<WUserHoldingDTO> getUserHoldings() {
        return this.userHoldings;
    }

    public void setUserHoldings(final List<WUserHoldingDTO> userHoldings) {
        this.userHoldings = userHoldings;
    }

    public List<WwaxTransactionDTO> getWaxTransactions() {
        return this.waxTransactions;
    }

    public void setWaxTransactions(final List<WwaxTransactionDTO> waxTransactions) {
        this.waxTransactions = waxTransactions;
    }

    public List<WxClickDTO> getWxClicks() {
        return this.wxClicks;
    }

    public void setWxClicks(final List<WxClickDTO> wxClicks) {
        this.wxClicks = wxClicks;
    }

    public List<WxClickUrlDTO> getWxClickUrlDTO() {
        return this.wxClickUrlDTO;
    }

    public void setWxClickUrlDTO(final List<WxClickUrlDTO> wxClickUrlDTO) {
        this.wxClickUrlDTO = wxClickUrlDTO;
    }

    public List<WCartDetailsDTO> getCartDetails() {
        return this.cartDetails;
    }

    public void setCartDetails(final List<WCartDetailsDTO> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public List<WTransDataMapDTO> getTransDataMap() {
        return this.transDataMap;
    }

    public void setTransDataMap(final List<WTransDataMapDTO> wTransDataMap) {
        this.transDataMap = wTransDataMap;
    }

    public List<WasOrderDTO> getOrders() {
        return this.orders;
    }

    public void setOrders(final List<WasOrderDTO> orders) {
        this.orders = orders;
    }

    public List<CounterPartyAliasDTO> getAliasValues() {
        return this.aliasValues;
    }

    public void setAliasValues(final List<CounterPartyAliasDTO> aliasValues) {
        this.aliasValues = aliasValues;
    }

    public List<WithdrawalReveiwDetailsDTO> getWithdrawalReviewDetails() {
        return this.withdrawalReviewDetails;
    }

    public void setWithdrawalReviewDetails(final List<WithdrawalReveiwDetailsDTO> withdrawalReviewDetails) {
        this.withdrawalReviewDetails = withdrawalReviewDetails;
    }

    public List<WtransactionLockedWdrlDTO> getWtransactionLockedWdrl() {
        return this.wtransactionLockedWdrl;
    }

    public void setWtransactionLockedWdrl(final List<WtransactionLockedWdrlDTO> wtransactionLockedWdrl) {
        this.wtransactionLockedWdrl = wtransactionLockedWdrl;
    }

}
