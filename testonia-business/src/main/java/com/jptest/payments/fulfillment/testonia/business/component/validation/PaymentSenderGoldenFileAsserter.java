package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.w3c.dom.Document;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.ignorable.flags.UnsetIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.FeeHistoryDao;
import com.jptest.payments.fulfillment.testonia.dao.FxHistoryDao;
import com.jptest.payments.fulfillment.testonia.dao.IwTransactionDao;
import com.jptest.payments.fulfillment.testonia.dao.WAuctionItemDao;
import com.jptest.payments.fulfillment.testonia.dao.WAuctionItemTransMapDao;
import com.jptest.payments.fulfillment.testonia.dao.WCartDetailsDao;
import com.jptest.payments.fulfillment.testonia.dao.WPaymentExtensionDataDao;
import com.jptest.payments.fulfillment.testonia.dao.WTransDataMapDao;
import com.jptest.payments.fulfillment.testonia.dao.WTransactionDetailsDao;
import com.jptest.payments.fulfillment.testonia.dao.WTransactionPendingDao;
import com.jptest.payments.fulfillment.testonia.dao.WTransactionUrlDao;
import com.jptest.payments.fulfillment.testonia.dao.WXClickDao;
import com.jptest.payments.fulfillment.testonia.dao.WxClickUrlDao;
import com.jptest.payments.fulfillment.testonia.dao.money.PActivityTransMapDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WCollateralLogDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WEftExternalRecordDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WMerchantPullLogDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WMerchantPullTransDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WRefundNegotiationDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WReturnsFeeDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WReturnsReviewDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransReturnDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionBufsDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionMemoDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionWorldDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WUserHoldingDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WithdrawalReviewDetailsDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WtransactionLockedWdrlDao;
import com.jptest.payments.fulfillment.testonia.dao.pymt.CounterPartyAliasDao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WAuctionItemAddlAttrP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WSubBalanceTransactionP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransRefundRelationP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionAdminP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionAuthP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WWaxTransactionP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WasOrderP2Dao;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.money.FXHistoryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.FeeHistoryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.PActivityTransMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.PaymentSenderDTO;
import com.jptest.payments.fulfillment.testonia.model.money.SenderValidationFixtureDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WAuctionItemAddlAttrDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WAuctionItemDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WAuctionItemTransMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WCartDetailsDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WCollateralLogDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WEFTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WMerchantPullLogDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WMerchantPullTransDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WPaymentExtensionDataDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WRefundNegotiationDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WReturnsFeeDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WReturnsReviewDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WSubBalanceTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransDataMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransRefundRelationDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransReturnDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionAdminDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionAuthDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionBufsDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDetailsDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionMemoDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionPendingDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionUrlDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionWorldDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WUserHoldingDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WasOrderDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WithdrawalReveiwDetailsDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WtransactionLockedWdrlDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WwaxTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WxClickDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WxClickUrlDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.CounterPartyAliasDTO;

/**
 * Makes VOX call by passing sender schema and then compares output with golden sender XML file
 */
public class PaymentSenderGoldenFileAsserter extends E2EGoldenFileComparisonAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSenderGoldenFileAsserter.class);

    private static final String OPERATION = "Sender-Validation";

    @Inject
    private WTransactionPendingDao wTransactionPendingDao;

    @Inject
    private WTransactionBufsDao wTransactionBufsDao;

    @Inject
    private WEftExternalRecordDao wEftExternalRecordDao;

    @Inject
    private WTransactionWorldDao wTransactionWorldDao;

    @Inject
    private WRefundNegotiationDao wRefundNegotiationDao;

    @Inject
    private WUserHoldingDao wUserHoldingDao;

    @Inject
    private WCollateralLogDao wCollateralLogDao;

    @Inject
    private PActivityTransMapDao pActivityTransMapDao;

    @Inject
    private WReturnsFeeDao wReturnsFeeDao;

    @Inject
    private WReturnsReviewDao wReturnsReviewDao;

    @Inject
    private WTransactionDetailsDao wTransactionDetailsDao;

    @Inject
    private WMerchantPullLogDao wMerchantPullLogDao;

    @Inject
    private WMerchantPullTransDao wMerchantPullTransDao;

    @Inject
    private WTransReturnDao wTransReturnDao;

    @Inject
    private WTransactionMemoDao wTransactionMemoDao;

    @Inject
    @Named("WTransactionDao")
    private IwTransactionDao wTransactionDao;

    @Inject
    private WTransactionUrlDao wTransactionUrlDao;

    @Inject
    private WTransactionAuthP2Dao wTransactionAuthP2Dao;

    @Inject
    private WSubBalanceTransactionP2Dao wSubBalanceTransactionP2Dao;

    @Inject
    private FeeHistoryDao feeHistoryDao;

    @Inject
    private FxHistoryDao fxHistoryDao;

    @Inject
    private WTransRefundRelationP2Dao wTransRefundRelationP2Dao;

    @Inject
    private WAuctionItemDao wAuctionItemDao;

    @Inject
    private WXClickDao wXClickDao;

    @Inject
    private WWaxTransactionP2Dao wWaxTransactionP2Dao;

    @Inject
    private WAuctionItemTransMapDao wAuctionItemTransMapDao;

    @Inject
    private WAuctionItemAddlAttrP2Dao wAuctionItemAddlAttrP2Dao;

    @Inject
    private WPaymentExtensionDataDao wPaymentExtensionDataDao;

    @Inject
    private WTransactionAdminP2Dao wTransactionAdminP2Dao;

    @Inject
    private WxClickUrlDao wxClickUrlDao;

    @Inject
    private WCartDetailsDao wCartDetailsDao;

    @Inject
    private WTransDataMapDao wTransDataMapDao;

    @Inject
    private WasOrderP2Dao wasOrderP2Dao;

    @Inject
    private CounterPartyAliasDao counterPartyAliasDao;

    @Inject
    private WithdrawalReviewDetailsDao withdrawalReviewDetailsDao;

    @Inject
    private WtransactionLockedWdrlDao wtransactionLockedWdrlDao;

    @Inject
    private TransactionHelper transactionHelper;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private PaymentSenderXmlVirtualizationTask xmlVirtualizationTask;

    @Inject
    private UnsetIgnorableFlagsHelper unsetIgnorableFlagsHelper;

    public PaymentSenderGoldenFileAsserter(final GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return OPERATION;
    }

    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_PAYMENT_SENDER_DBDIFF;
    }

    @Override
    protected Document getActualResponseXml(final Context context) {
        final List<WTransactionVO> wTransactionList = (List<WTransactionVO>) this.getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        final String ignorableFlagsLocation = (String) this.getDataFromContext(context, ContextKeys.IGNORABLE_FLAGS_LOCATION.getName());
        final WTransactionVO senderTransaction = this.transactionHelper.getSenderTransaction(wTransactionList);
        return this.getActualResponse(senderTransaction, ignorableFlagsLocation, context);
    }

    /**
     * Populate response object from Database
     *
     * @param senderTransaction
     * @return
     */
    private Document getActualResponse(final WTransactionVO senderTransaction, final String ignorableFlagsLocation,
            final Context context) {
        final SenderValidationFixtureDTO resultDTO = new SenderValidationFixtureDTO();
        resultDTO.setSenderDTO(this.prepareSenderDTO(senderTransaction, context));

        try {
            Document doc = resultDTO.getDocument();
            this.unsetIgnorableFlagsHelper.unsetFlags(ignorableFlagsLocation, doc);
            doc = this.processActualDocument(doc);
            doc = this.xmlVirtualizationTask.execute(doc);
            LOGGER.info("Sender Actual XML: \n{}", this.xmlHelper.getPrettyXml(doc));
            return doc;
        } catch (ParserConfigurationException | JAXBException e) {
            throw new TestExecutionException(e);
        }
    }

    protected Document processActualDocument(final Document doc) {
        return doc;
    }

    /**
     * Prepare senderDTO's subelements
     *
     * @param originalSenderTransaction
     * @return
     */
    private PaymentSenderDTO prepareSenderDTO(final WTransactionVO originalSenderTransaction, final Context context) {
        final PaymentSenderDTO senderDTO = new PaymentSenderDTO();
        this.addWTransactionElement(originalSenderTransaction, senderDTO, context);
        this.addWTransactionPendingElement(senderDTO);
        this.addWTransactionBufsElement(senderDTO);
        this.addWTransactionWorldElement(senderDTO);
        this.addWEFTransactionElement(senderDTO);
        this.addWUserHoldingElement(senderDTO);
        this.addWTransactionAuthElement(senderDTO);
        this.addWCollateralLogElement(senderDTO);
        this.addPaymentActivityTransactionMapElement(senderDTO);
        this.addWSubBalanceTransactionElement(senderDTO);
        this.addFeeHistoryElement(senderDTO);
        this.addFXHistoryElement(senderDTO);
        this.addWxClickElement(senderDTO);
        this.addWwaxTransactionElement(senderDTO);
        this.addWAuctionItemTransMapElement(senderDTO);
        this.addWAuctionItemElement(senderDTO);
        this.addWAuctionItemAdditionalAttributesElement(senderDTO);
        this.addWMerchantPullTransactionElement(senderDTO);
        this.addWMerchantPullLogElement(senderDTO);
        this.addWReturnsFeeElement(senderDTO);
        this.addWReturnsReviewElement(senderDTO);
        this.addWTransactionReturnElement(senderDTO);
        this.addWTransactionMemoElement(senderDTO);
        this.addWTransactionRefundRelationElement(senderDTO);
        this.addWRefundNegotiationElement(senderDTO);
        this.addWTransactionDetailsElement(senderDTO);
        this.addWPaymentExtensionDataElement(senderDTO);
        this.addWTransactionUrlElement(senderDTO);
        this.addWTransactionAdminElement(senderDTO);
        this.addWxClickUrlElement(senderDTO);
        this.addWCartDetailsElement(senderDTO);
        this.addTransDataMapElement(senderDTO);
        this.addWasOrderP2Element(senderDTO);
        this.addCounterPartyAliasElement(senderDTO);
        this.addWithdrawalReviewDetailsElement(senderDTO);
        this.addWtransactionLockedWdrlElement(senderDTO);
        return senderDTO;
    }

    /**
     * Add WTransactions to PaymentSenderDTO
     *
     * @param senderTransaction
     * @param senderDTO
     */
    private void addWTransactionElement(final WTransactionVO senderTransaction, final PaymentSenderDTO senderDTO,
            final Context context) {
        // get sender transaction details from database
        final WTransactionDTO senderTransactionFromDB = this.wTransactionDao.getTransactionDetails(senderTransaction.getId());
        Assert.assertNotNull(senderTransactionFromDB, "Sender's transaction in DB should be present");
        senderDTO.addTransaction(senderTransactionFromDB);

        // get parent sender transactions
        final List<WTransactionDTO> senderParentTransactionsFromDB = this.wTransactionDao
                .getParentTransactionsDetails(senderTransaction.getId());
        Assert.assertNotNull(senderParentTransactionsFromDB, "Sender's parent transaction(s) in DB should be present");
        senderDTO.addTransactions(senderParentTransactionsFromDB);

        // Get funder account transactions if present.
        // For example, use cases like incentives will have funder transactions
        final Set<String> funders = (Set<String>) this.getDataFromContext(context, ContextKeys.FUNDER_ACCOUNT_LIST.getName());
        if (funders != null && !funders.isEmpty()) {
            final List<WTransactionDTO> funderTransactionsFromDB = new ArrayList<>();
            for (final String funder : funders) {
                final List<WTransactionDTO> funderTxnsFromDB = this.wTransactionDao.getTransactionsByAccountNumber(funder);
                Assert.assertNotNull(funderTxnsFromDB, "Unable to find funder side txns for funder: " + funder);
                funderTransactionsFromDB.addAll(funderTxnsFromDB);
            }
            senderDTO.addTransactions(funderTransactionsFromDB);
        }
    }

    /**
     * Add Pending WTransactions to PaymentSenderDTO
     *
     * @param senderDTO
     */
    private void addWTransactionPendingElement(final PaymentSenderDTO senderDTO) {
        // get pending transactions, if any - and add to payment-sender
        final List<WTransactionPendingDTO> pendingTransactions = this.wTransactionPendingDao
                .getPendingTransactionDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(pendingTransactions)) {
            senderDTO.setPendingTransactions(pendingTransactions);
        }
    }

    /**
     * Add Transactions with Bufs to PaymentSenderDTO
     *
     * @param senderDTO
     */
    private void addWTransactionBufsElement(final PaymentSenderDTO senderDTO) {
        // get transaction bufs details, if any - and add to payment-sender
        final List<WTransactionBufsDTO> transactionsWithBufs = this.wTransactionBufsDao
                .getTransactionBufsDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionsWithBufs)) {
            senderDTO.setTransactionsWithBufs(transactionsWithBufs);
        }
    }

    /**
     * Add details for cross country transaction to PaymentSenderDTO
     *
     * @param senderDTO
     */
    private void addWTransactionWorldElement(final PaymentSenderDTO senderDTO) {
        // get transaction world details, if any - and add to payment-sender.
        final List<WTransactionWorldDTO> transactionsWorld = this.wTransactionWorldDao
                .getTransactionWorldDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionsWorld)) {
            senderDTO.setTransactionsWorld(transactionsWorld);
        }
    }

    /**
     * Add WEF (German) Transaction details to PaymentSenderDTO
     *
     * @param senderDTO
     */
    private void addWEFTransactionElement(final PaymentSenderDTO senderDTO) {
        // check if eligible for WEF
        if (!senderDTO.getTransactions().stream().anyMatch(transaction -> transaction.isEFT())) {
            LOGGER.debug("Not EFT - ignoring WEF-transaction check.");
            return;
        }
        // get WEF transaction details and add to payment-sender
        final List<WEFTransactionDTO> wefTransactions = this.wEftExternalRecordDao
                .getWEFTransactionDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(wefTransactions)) {
            senderDTO.setWefTransactions(wefTransactions);
        }
    }

    /**
     * Add Holding details to PaymentSenderDTO
     *
     * @param senderDTO
     */
    private void addWUserHoldingElement(final PaymentSenderDTO senderDTO) {
        // get user holding details and add to payment-sender
        final List<WUserHoldingDTO> userHoldings = this.wUserHoldingDao.getWUserHoldingDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(userHoldings)) {
            senderDTO.setUserHoldings(userHoldings);
        }
    }

    /**
     * Add Holding details to PaymentSenderDTO for AS1, AS2 transactions
     *
     * @param senderDTO
     */
    private void addWTransactionAuthElement(final PaymentSenderDTO senderDTO) {
        // get transaction-auth details and add to payment-sender
        final List<WTransactionAuthDTO> transactionAuths = this.wTransactionAuthP2Dao
                .getWTransactionAuthDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionAuths)) {
            senderDTO.setTransactionAuths(transactionAuths);
        }
    }

    /**
     * Add Collateral log to PaymentSenderDTO when transaction is placed on hold
     *
     * @param senderDTO
     */
    private void addWCollateralLogElement(final PaymentSenderDTO senderDTO) {
        // get collateral log details and add to payment-sender
        final List<WCollateralLogDTO> collateralLogs = this.wCollateralLogDao.getCollateralLogDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(collateralLogs)) {
            senderDTO.setCollateralLogs(collateralLogs);
        }
    }

    /**
     * Add payment activity transaction map details to PaymentSenderDTO
     *
     * @param senderDTO
     */
    private void addPaymentActivityTransactionMapElement(final PaymentSenderDTO senderDTO) {
        // get payment activity transaction map details and add to payment-sender
        final List<PActivityTransMapDTO> paymentActivityTransactionMap = this.pActivityTransMapDao
                .getPActivityTransMapDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(paymentActivityTransactionMap)) {
            senderDTO.setPaymentActivityTransactionMap(paymentActivityTransactionMap);
        }
    }

    /**
     * Add sub-balance transaction details to PaymentSenderDTO
     *
     * @param senderDTO
     */
    private void addWSubBalanceTransactionElement(final PaymentSenderDTO senderDTO) {
        // get sub-balance transaction details and add to payment-sender
        final List<WSubBalanceTransactionDTO> subBalanceTransactions = this.wSubBalanceTransactionP2Dao
                .getWSubBalanceTransactionDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(subBalanceTransactions)) {
            senderDTO.setSubBalanceTransactions(subBalanceTransactions);
        }
    }

    /**
     * Add fee history details to PaymentSenderDTO. Sender is charged fee mainly in case of P2P transaction.
     *
     * @param senderDTO
     */
    private void addFeeHistoryElement(final PaymentSenderDTO senderDTO) {
        // get fee history details and add to payment-sender
        final List<FeeHistoryDTO> feeHistory = this.feeHistoryDao.getFeeHistoryDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(feeHistory)) {
            senderDTO.setFeeHistory(feeHistory);
        }
    }

    /**
     * Add FX (foreign exchange) history details to PaymentSenderDTO - whenever there is cross-currency usecase..
     *
     * @param senderDTO
     */
    private void addFXHistoryElement(final PaymentSenderDTO senderDTO) {
        // get foreign exchange history details and add to payment-sender
        final List<FXHistoryDTO> fxHistory = this.fxHistoryDao.getFXHistoryDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(fxHistory)) {
            senderDTO.setFXHistory(fxHistory);
        }
    }

    /**
     * Add WxClick details to PaymentSenderDTO
     *
     * @param senderDTO
     */
    private void addWxClickElement(final PaymentSenderDTO senderDTO) {
        // get Xclick details and add to payment-sender
        final List<WxClickDTO> xClicks = this.wXClickDao.getWxClickDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(xClicks)) {
            senderDTO.setWxClicks(xClicks);
        }
    }

    /**
     * Add Wwax transaction details to PaymentSenderDTO - in case of guest checkout.
     *
     * @param senderDTO
     */
    private void addWwaxTransactionElement(final PaymentSenderDTO senderDTO) {
        // get WAX details and add to payment-sender
        final List<WwaxTransactionDTO> waxTransactions = this.wWaxTransactionP2Dao
                .getWwaxTransactionDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(waxTransactions)) {
            senderDTO.setWaxTransactions(waxTransactions);
        }
    }

    /**
     * Add Auction Item Transaction Map details to PaymentSenderDTO - in case of auction.
     *
     * @param senderDTO
     */
    private void addWAuctionItemTransMapElement(final PaymentSenderDTO senderDTO) {
        // get auction item transaction map details and add to payment-sender
        final List<WAuctionItemTransMapDTO> auctionItemTransMap = this.wAuctionItemTransMapDao
                .getWAuctionItemTransMapDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(auctionItemTransMap)) {
            senderDTO.setAuctionItemTransMap(auctionItemTransMap);
        }
    }

    /**
     * Add Auction Item details to PaymentSenderDTO - in case of auction.
     *
     * @param senderDTO
     */
    private void addWAuctionItemElement(final PaymentSenderDTO senderDTO) {
        // check if applicable
        if (CollectionUtils.isEmpty(senderDTO.getAuctionItemTransMap())) {
            LOGGER.debug("Not an auction-item - ignoring auction-item check.");
            return;
        }
        // get auction item details and add to payment-sender
        final List<WAuctionItemDTO> auctionItems = this.wAuctionItemDao
                .getWAuctionItemDetails(this.mapIdToString(senderDTO.getAuctionItemTransMap()));
        if (CollectionUtils.isNotEmpty(auctionItems)) {
            senderDTO.setAuctionItems(auctionItems);
        }
    }

    /**
     * Add Auction Item Additional Attribute details to PaymentSenderDTO - in case of auction.
     *
     * @param senderDTO
     */
    private void addWAuctionItemAdditionalAttributesElement(final PaymentSenderDTO senderDTO) {
        // check if applicable
        if (CollectionUtils.isEmpty(senderDTO.getAuctionItemTransMap())) {
            LOGGER.debug("Not an auction-item - ignoring auction-item-additional-attributes check.");
            return;
        }
        // get auction item additional attributes details and add to payment-sender
        final List<WAuctionItemAddlAttrDTO> auctionItemAdditionalAttributes = this.wAuctionItemAddlAttrP2Dao
                .getWAuctionItemAdditionalAttributesDetails(this.mapIdToString(senderDTO.getAuctionItemTransMap()));
        if (CollectionUtils.isNotEmpty(auctionItemAdditionalAttributes)) {
            senderDTO.setAuctionItemAdditionalAttributes(auctionItemAdditionalAttributes);
        }

    }

    private String mapIdToString(final List<WAuctionItemTransMapDTO> list) {
        return list
                .stream()
                .map(transaction -> transaction.getAuctionItemId().toString())
                .distinct()
                .collect(Collectors.joining(","));
    }

    /**
     * Add Merchant Pull Transaction details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWMerchantPullTransactionElement(final PaymentSenderDTO senderDTO) {
        // check if applicable
        if (!senderDTO.getTransactions().stream().anyMatch(transaction -> transaction.isMerchantPull())) {
            LOGGER.debug("Not a merchant-pull transaction - ignoring merchant-pull transaction check.");
            return;
        }
        // get merchant pull transaction details and add to payment-sender
        final List<WMerchantPullTransDTO> merchantPullTransactions = this.wMerchantPullTransDao
                .getWMerchantPullTransactionsDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(merchantPullTransactions)) {
            senderDTO.setMerchantPullTransactions(merchantPullTransactions);
        }
    }

    /**
     * Add Merchant Pull Log details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWMerchantPullLogElement(final PaymentSenderDTO senderDTO) {
        // check if applicable
        if (!senderDTO.getTransactions().stream().anyMatch(transaction -> transaction.isMerchantPull())) {
            LOGGER.debug("Not a merchant-pull transaction - ignoring merchant-pull log check.");
            return;
        }
        // get merchant pull log details and add to payment-sender
        final List<WMerchantPullLogDTO> merchantPullLogs = this.wMerchantPullLogDao
                .getWMerchantPullLogDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(merchantPullLogs)) {
            senderDTO.setMerchantPullLogs(merchantPullLogs);
        }
    }

    /**
     * Add Return fee details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWReturnsFeeElement(final PaymentSenderDTO senderDTO) {
        // get return-fee details and add to payment-sender
        final List<WReturnsFeeDTO> returnFees = this.wReturnsFeeDao
                .getWReturnsFeeDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(returnFees)) {
            senderDTO.setReturnFees(returnFees);
        }
    }

    /**
     * Add Return review details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWReturnsReviewElement(final PaymentSenderDTO senderDTO) {
        // get return-review details and add to payment-sender
        final List<WReturnsReviewDTO> returnReviews = this.wReturnsReviewDao
                .getWReturnsReviewDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(returnReviews)) {
            senderDTO.setReturnReviews(returnReviews);
        }
    }

    /**
     * Add transaction return details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWTransactionReturnElement(final PaymentSenderDTO senderDTO) {
        // get transaction-return details and add to payment-sender
        final List<WTransReturnDTO> transactionReturns = this.wTransReturnDao
                .getWTransactionReturnDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionReturns)) {
            senderDTO.setTransactionReturns(transactionReturns);
        }
    }

    /**
     * Add transaction memo details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWTransactionMemoElement(final PaymentSenderDTO senderDTO) {
        // get memo details and add to payment-sender
        final List<WTransactionMemoDTO> transactionMemos = this.wTransactionMemoDao
                .getWTransactionMemoDetails(senderDTO.getTransactions().get(0).getSharedId().toString());
        if (CollectionUtils.isNotEmpty(transactionMemos)) {
            senderDTO.setTransactionMemos(transactionMemos);
        }
    }

    /**
     * Add transaction refund relation details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWTransactionRefundRelationElement(final PaymentSenderDTO senderDTO) {
        // get transaction-refund-relation details and add to payment-sender
        final List<WTransRefundRelationDTO> transactionRefundRelations = this.wTransRefundRelationP2Dao
                .getWTransactionRefundRelationDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionRefundRelations)) {
            senderDTO.setTransactionRefundRelations(transactionRefundRelations);
        }
    }

    /**
     * Add refund negotiation details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWRefundNegotiationElement(final PaymentSenderDTO senderDTO) {
        // get refund-negotiation details and add to payment-sender
        final List<WRefundNegotiationDTO> refundNegotiations = this.wRefundNegotiationDao
                .getWRefundNegotiationDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(refundNegotiations)) {
            senderDTO.setRefundNegotiations(refundNegotiations);
        }
    }

    /**
     * Add transaction details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWTransactionDetailsElement(final PaymentSenderDTO senderDTO) {
        // get transaction-details and add to payment-sender
        final List<WTransactionDetailsDTO> transactionDetails = this.wTransactionDetailsDao
                .getWTransactionDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionDetails)) {
            senderDTO.setTransactionDetails(transactionDetails);
        }
    }

    /**
     * Add payment extension data to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWPaymentExtensionDataElement(final PaymentSenderDTO senderDTO) {
        // check if applicable
        if (CollectionUtils.isEmpty(senderDTO.getPaymentActivityTransactionMap())) {
            LOGGER.debug(
                    "No activity Id found in payment-activity-transaction-map - ignoring payment-extension-data check.");
            return;
        }
        // get payment extension data and add to payment-sender
        final List<WPaymentExtensionDataDTO> paymentExtensionData = this.wPaymentExtensionDataDao
                .getWPaymentExtensionDataDetails(senderDTO.getPaymentActivityTransactionMap());
        if (CollectionUtils.isNotEmpty(paymentExtensionData)) {
            senderDTO.setPaymentExtensionData(paymentExtensionData);
        }
    }

    /**
     * Add transaction url details to PaymentSenderDTO.
     *
     * @param senderDTO
     */
    private void addWTransactionUrlElement(final PaymentSenderDTO senderDTO) {
        // get transaction url details and add to payment-sender
        final List<WTransactionUrlDTO> transactionUrls = this.wTransactionUrlDao
                .getWTransactionUrlDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionUrls)) {
            senderDTO.setTransactionUrls(transactionUrls);
        }

    }

    /**
     * Add transaction admin details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWTransactionAdminElement(final PaymentSenderDTO senderDTO) {
        // get transaction-admin details and add to payment-sender
        final List<WTransactionAdminDTO> transactionAdmin = this.wTransactionAdminP2Dao
                .getWTransactionAdminDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionAdmin)) {
            senderDTO.setTransactionAdmin(transactionAdmin);
        }

    }

    /**
     * Add wxclick url to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWxClickUrlElement(final PaymentSenderDTO senderDTO) {
        // get wxclick-url details and add to payment-sender
        final List<WxClickUrlDTO> wxClickUrls = this.wxClickUrlDao
                .getWxClickUrlDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(wxClickUrls)) {
            senderDTO.setWxClickUrlDTO(wxClickUrls);
        }

    }

    /**
     * Add wcart details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addWCartDetailsElement(final PaymentSenderDTO senderDTO) {
        // get wxclick-url details and add to payment-sender
        final List<WCartDetailsDTO> wCartDetails = this.wCartDetailsDao
                .getCartDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(wCartDetails)) {
            senderDTO.setCartDetails(wCartDetails);
        }

    }

    /**
     * Add wcart details to PaymentSenderDTO - if applicable.
     *
     * @param senderDTO
     */
    private void addTransDataMapElement(final PaymentSenderDTO senderDTO) {
        // get wtrans_data_map details and add to payment-sender
        final List<WTransDataMapDTO> transDataMapDetails = this.wTransDataMapDao
                .getTransDataMapDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transDataMapDetails)) {
            senderDTO.setTransDataMap(transDataMapDetails);
        }

    }

    /**
     * Add order details to PaymentSenderDTO
     * @param senderDTO
     */
    private void addWasOrderP2Element(final PaymentSenderDTO senderDTO) {
        // get order details and add to payment-sender
        final List<WasOrderDTO> orders = this.wasOrderP2Dao.getWasOrderP2Details(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(orders)) {
            senderDTO.setOrders(orders);
        }
    }

    /**
     * Add counterparty details to PaymentSenderDTO
     * @param senderDTO
     */
    private void addCounterPartyAliasElement(final PaymentSenderDTO senderDTO) {
        // get order details and add to payment-sender
        final List<CounterPartyAliasDTO> aliasValues = this.counterPartyAliasDao.getCounterPartyAliasDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(aliasValues)) {
            senderDTO.setAliasValues(aliasValues);
        }
    }

    /**
     * Add withdrawal_review_details to PaymentSenderDTO
     *
     * @param senderDTO
     */

    private void addWithdrawalReviewDetailsElement(final PaymentSenderDTO senderDTO) {
        // get withdrawal_review_details and add to payment-sender
        final List<WithdrawalReveiwDetailsDTO> withdrawalReviewDetailsValues = this.withdrawalReviewDetailsDao
                .getWithdrawalReveiwDetails(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(withdrawalReviewDetailsValues)) {
            senderDTO.setWithdrawalReviewDetails(withdrawalReviewDetailsValues);
        }
    }

    /**
     * Add wtransaction_locked_wdrl details to PaymentSenderDTO
     *
     * @param senderDTO
     */

    private void addWtransactionLockedWdrlElement(final PaymentSenderDTO senderDTO) {
        // get wtransaction_locked_wdrl and add to payment-sender
        final List<WtransactionLockedWdrlDTO> wtransactionLockedWdrlValues = this.wtransactionLockedWdrlDao
                .getWtransactionLockedWdrl(senderDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(wtransactionLockedWdrlValues)) {
            senderDTO.setWtransactionLockedWdrl(wtransactionLockedWdrlValues);
        }
    }
}
