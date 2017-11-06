package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.List;

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
import com.jptest.payments.fulfillment.testonia.dao.IwTransactionDao;
import com.jptest.payments.fulfillment.testonia.dao.money.PActivityTransMapDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WRefundNegotiationDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WReturnsFeeDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WReturnsReviewDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WUserHoldingDao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WFeeHistoryP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WFxHistoryP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WSubBalanceTransactionP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransRefundRelationP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionDetailsP2Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionUrlP2Dao;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.money.FXHistoryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.FeeHistoryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.PActivityTransMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.PaymentRecipientDTO;
import com.jptest.payments.fulfillment.testonia.model.money.RecipientValidationFixtureDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WRefundNegotiationDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WReturnsFeeDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WReturnsReviewDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WSubBalanceTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransRefundRelationDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDetailsDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionUrlDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WUserHoldingDTO;

/**
 * Makes VOX call by passing recipient schema and then compares output with golden recipient XML file
 */
public class PaymentRecipientGoldenFileAsserter extends E2EGoldenFileComparisonAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRecipientGoldenFileAsserter.class);

    private static final String OPERATION = "Recipient-Validation";

    @Inject
    private WRefundNegotiationDao wRefundNegotiationDao;

    @Inject
    private WUserHoldingDao wUserHoldingDao;

    @Inject
    private PActivityTransMapDao pActivityTransMapDao;

    @Inject
    private WReturnsFeeDao wReturnsFeeDao;

    @Inject
    private WReturnsReviewDao wReturnsReviewDao;

    @Inject
    private WTransactionDetailsP2Dao wTransactionDetailsDao;

    @Inject
    @Named("WTransactionDao")
    private IwTransactionDao wTransactionDao;

    @Inject
    private WTransactionUrlP2Dao wTransactionUrlP2Dao;

    @Inject
    private WSubBalanceTransactionP2Dao wSubBalanceTransactionP2Dao;

    @Inject
    private WFeeHistoryP2Dao wFeeHistoryP2Dao;

    @Inject
    private WFxHistoryP2Dao wFxHistoryP2Dao;

    @Inject
    private WTransRefundRelationP2Dao wTransRefundRelationP2Dao;

    @Inject
    private TransactionHelper transactionHelper;

    @Inject
    private XMLHelper xmlHelper;
    
    @Inject
    private UnsetIgnorableFlagsHelper unsetIgnorableFlagsHelper;

    @Inject
    private PaymentRecipientXmlVirtualizationTask xmlVirtualizationTask;

    public PaymentRecipientGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return OPERATION;
    }
    
    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_PAYMENT_RECIPIENT_DBDIFF;
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        String ignorableFlagsLocation = (String) getDataFromContext(context, ContextKeys.IGNORABLE_FLAGS_LOCATION.getName());
        WTransactionVO recipientTransaction = transactionHelper.getRecipientTransaction(wTransactionList);
        return getActualResponse(recipientTransaction, ignorableFlagsLocation);
    }

    /**
     * Populate response object from Database
     *
     * @param senderTransaction
     * @return
     */
    private Document getActualResponse(final WTransactionVO recipientTransaction, String ignorableFlagsLocation) {
        RecipientValidationFixtureDTO resultDTO = new RecipientValidationFixtureDTO();
        PaymentRecipientDTO paymentRecipientDTO = prepareRecipientDTO(recipientTransaction);
        resultDTO.setRecipientDTO(paymentRecipientDTO);
        
        try {
            Document doc = resultDTO.getDocument();
            unsetIgnorableFlagsHelper.unsetFlags(ignorableFlagsLocation, doc);
            doc = processActualDocument(doc);
            doc = xmlVirtualizationTask.execute(doc);
            LOGGER.info("Recipient Actual XML: \n{}", xmlHelper.getPrettyXml(doc));
            return doc;
        } catch (ParserConfigurationException | JAXBException e) {
            throw new TestExecutionException(e);
        }
    }
    
    protected Document processActualDocument(Document doc) {
        return doc;
    }

    /**
     * Prepare recipientDTO's subelements
     *
     * @param originalSenderTransaction
     * @return
     */
    private PaymentRecipientDTO prepareRecipientDTO(WTransactionVO originalRecipientTransaction) {
        PaymentRecipientDTO recipientDTO = new PaymentRecipientDTO();
        addWTransactionElement(originalRecipientTransaction, recipientDTO);
        addWUserHoldingElement(recipientDTO);
        addPaymentActivityTransactionMapElement(recipientDTO);
        addWSubBalanceTransactionElement(recipientDTO);
        addFeeHistoryElement(recipientDTO);
        addFXHistoryElement(recipientDTO);
        addWReturnsFeeElement(recipientDTO);
        addWReturnsReviewElement(recipientDTO);
        addWTransactionDetailsElement(recipientDTO);

        addWTransactionUrlElement(recipientDTO);

        addWTransactionRefundRelationElement(recipientDTO);
        addWRefundNegotiationElement(recipientDTO);

        // update WTransaction parent row with actual and percent fee
        updateWTransactionWithFee(recipientDTO);
        return recipientDTO;
    }

    /**
     * Add WTransactions to PaymentRecipientDTO
     *
     * @param senderTransaction
     * @param recipientDTO
     */
    private void addWTransactionElement(WTransactionVO recipientTransaction, PaymentRecipientDTO recipientDTO) {
        // get sender transaction details from database
        WTransactionDTO senderTransactionFromDB = wTransactionDao
                .getTransactionDetails(recipientTransaction.getId());
        Assert.assertNotNull(senderTransactionFromDB, "Recipient's transaction in DB should be present");
        recipientDTO.addTransaction(senderTransactionFromDB);

        // get parent sender transactions
        List<WTransactionDTO> senderParentTransactionsFromDB = wTransactionDao
                .getParentTransactionsDetails(recipientTransaction.getId());
        Assert.assertNotNull(senderParentTransactionsFromDB,
                "Recipient's parent transaction(s) in DB should be present");
        recipientDTO.addTransactions(senderParentTransactionsFromDB);
    }

    /**
     * Add Holding details to PaymentRecipientDTO
     *
     * @param recipientDTO
     */
    private void addWUserHoldingElement(PaymentRecipientDTO recipientDTO) {
        // get user holding details and add to payment-sender
        List<WUserHoldingDTO> userHoldings = wUserHoldingDao.getWUserHoldingDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(userHoldings)) {
            recipientDTO.setUserHoldings(userHoldings);
        }
    }

    /**
     * Add payment activity transaction map details to PaymentRecipientDTO
     *
     * @param recipientDTO
     */
    private void addPaymentActivityTransactionMapElement(PaymentRecipientDTO recipientDTO) {
        // get payment activity transaction map details and add to payment-sender
        List<PActivityTransMapDTO> paymentActivityTransactionMap = pActivityTransMapDao
                .getPActivityTransMapDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(paymentActivityTransactionMap)) {
            recipientDTO.setPaymentActivityTransactionMap(paymentActivityTransactionMap);
        }
    }

    /**
     * Add sub-balance transaction details to PaymentRecipientDTO
     *
     * @param recipientDTO
     */
    private void addWSubBalanceTransactionElement(PaymentRecipientDTO recipientDTO) {
        // get sub-balance transaction details and add to payment-sender
        List<WSubBalanceTransactionDTO> subBalanceTransactions = wSubBalanceTransactionP2Dao
                .getWSubBalanceTransactionDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(subBalanceTransactions)) {
            recipientDTO.setSubBalanceTransactions(subBalanceTransactions);
        }
    }

    /**
     * Add fee history details to PaymentRecipientDTO.
     *
     * @param recipientDTO
     */
    private void addFeeHistoryElement(PaymentRecipientDTO recipientDTO) {
        // get fee history details and add to payment-sender
        List<FeeHistoryDTO> feeHistory = wFeeHistoryP2Dao.getFeeHistoryDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(feeHistory)) {
            recipientDTO.setFeeHistory(feeHistory);
        }
    }

    /**
     * Add FX (foreign exchange) history details to PaymentRecipientDTO - whenever there is cross-currency usecase..
     *
     * @param recipientDTO
     */
    private void addFXHistoryElement(PaymentRecipientDTO recipientDTO) {
        // get foreign exchange history details and add to payment-sender
        List<FXHistoryDTO> fxHistory = wFxHistoryP2Dao.getFXHistoryDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(fxHistory)) {
            recipientDTO.setFXHistory(fxHistory);
        }
    }

    /**
     * Add Return fee details to PaymentRecipientDTO - if applicable.
     *
     * @param recipientDTO
     */
    private void addWReturnsFeeElement(PaymentRecipientDTO recipientDTO) {
        // get return-fee details and add to payment-sender
        List<WReturnsFeeDTO> returnFees = wReturnsFeeDao
                .getWReturnsFeeDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(returnFees)) {
            recipientDTO.setReturnFees(returnFees);
        }
    }

    /**
     * Add Return review details to PaymentRecipientDTO - if applicable.
     *
     * @param recipientDTO
     */
    private void addWReturnsReviewElement(PaymentRecipientDTO recipientDTO) {
        // get return-review details and add to payment-sender
        List<WReturnsReviewDTO> returnReviews = wReturnsReviewDao
                .getWReturnsReviewDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(returnReviews)) {
            recipientDTO.setReturnReviews(returnReviews);
        }
    }

    /**
     * Add transaction details to PaymentRecipientDTO - if applicable.
     *
     * @param recipientDTO
     */
    private void addWTransactionDetailsElement(PaymentRecipientDTO recipientDTO) {
        // get transaction-details and add to payment-sender
        List<WTransactionDetailsDTO> transactionDetails = wTransactionDetailsDao
                .getWTransactionDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionDetails)) {
            recipientDTO.setTransactionDetails(transactionDetails);
        }
    }

    /**
     * Add transaction url details to PaymentRecipientDTO.
     *
     * @param recipientDTO
     */
    private void addWTransactionUrlElement(PaymentRecipientDTO recipientDTO) {
        // get transaction url details and add to payment-sender
        List<WTransactionUrlDTO> transactionUrls = wTransactionUrlP2Dao
                .getWTransactionUrlDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionUrls)) {
            recipientDTO.setTransactionUrls(transactionUrls);
        }

    }

    /**
     * Add transaction refund relation details to PaymentRecipientDTO - if applicable.
     *
     * @param recipientDTO
     */
    private void addWTransactionRefundRelationElement(PaymentRecipientDTO recipientDTO) {
        // get transaction-refund-relation details and add to payment-sender
        List<WTransRefundRelationDTO> transactionRefundRelations = wTransRefundRelationP2Dao
                .getWTransactionRefundRelationDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(transactionRefundRelations)) {
            recipientDTO.setTransactionRefundRelations(transactionRefundRelations);
        }
    }

    /**
     * Add refund negotiation details to PaymentRecipientDTO - if applicable.
     *
     * @param recipientDTO
     */
    private void addWRefundNegotiationElement(PaymentRecipientDTO recipientDTO) {
        // get refund-negotiation details and add to payment-sender
        List<WRefundNegotiationDTO> refundNegotiations = wRefundNegotiationDao
                .getWRefundNegotiationDetails(recipientDTO.getTransactions());
        if (CollectionUtils.isNotEmpty(refundNegotiations)) {
            recipientDTO.setRefundNegotiations(refundNegotiations);
        }
    }

    /**
     * Update WTransaction parent row with fee details
     *
     * @param recipientDTO
     */
    private void updateWTransactionWithFee(PaymentRecipientDTO recipientDTO) {
        if (recipientDTO.getTransactions().size() > 1 && CollectionUtils.isNotEmpty(recipientDTO.getFeeHistory())) {
            recipientDTO.getTransactions().get(1)
                    .setActualFixedFee(recipientDTO.getFeeHistory().get(0).getCurrencyCode()
                            + recipientDTO.getFeeHistory().get(0).getActualFixedFee());
            recipientDTO.getTransactions().get(1)
                    .setActualPercentFee(recipientDTO.getFeeHistory().get(0).getActualPercentFee());
        }
    }

}
