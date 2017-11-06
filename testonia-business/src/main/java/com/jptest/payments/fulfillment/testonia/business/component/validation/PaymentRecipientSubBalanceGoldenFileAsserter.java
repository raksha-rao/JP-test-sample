package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.ArrayList;
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
import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.IwTransactionDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WUserHoldingSubBalanceDao;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.money.PaymentRecipientDTO;
import com.jptest.payments.fulfillment.testonia.model.money.RecipientValidationFixtureDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WUserHoldingSubBalanceDTO;


/**
 * Golden file asserter for wuserholding subbalance table. This table gets updated when there is a hold on recipient
 * side.
 */
public class wPaymentRecipientSubBalanceGoldenFileAsserter extends E2EGoldenFileComparisonAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRecipientSubBalanceGoldenFileAsserter.class);

    private static final String OPERATION = "Recipient-Validation";

    @Inject
    private WUserHoldingSubBalanceDao wUserHoldingSubBalanceDao;

    @Inject
    @Named("WTransactionDao")
    private IwTransactionDao wTransactionDao;

    @Inject
    private TransactionHelper transactionHelper;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private PaymentRecipientXmlVirtualizationTask xmlVirtualizationTask;

    public PaymentRecipientSubBalanceGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return OPERATION;
    }
    
    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_PAYMENT_RECIPIENT_SUBBALANCE_DBDIFF;
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        WTransactionVO recipientTransaction = transactionHelper.getRecipientTransaction(wTransactionList);
        return getActualResponse(recipientTransaction);
    }

    /**
     * Populate response object from Database
     *
     * @param senderTransaction
     * @return
     */
    private Document getActualResponse(final WTransactionVO recipientTransaction) {
        RecipientValidationFixtureDTO resultDTO = new RecipientValidationFixtureDTO();
        resultDTO.setRecipientDTO(prepareRecipientDTO(recipientTransaction));

        try {
            Document doc = resultDTO.getDocument();
            doc = xmlVirtualizationTask.execute(doc);
            LOGGER.info("Recipient Actual XML: \n{}", xmlHelper.getPrettyXml(doc));
            return doc;
        } catch (ParserConfigurationException | JAXBException e) {
            throw new TestExecutionException(e);
        }
    }

    /**
     * Prepare recipientDTO's subelements
     *
     * @param originalSenderTransaction
     * @return
     */
    private PaymentRecipientDTO prepareRecipientDTO(WTransactionVO originalRecipientTransaction) {
        PaymentRecipientDTO recipientDTO = new PaymentRecipientDTO();
        List<WTransactionDTO> recipientTransactions = getRecipientWTransactions(originalRecipientTransaction);
        addWUserHoldingSubBalanceElement(recipientDTO, recipientTransactions);
        return recipientDTO;
    }

    /**
     * Get recipient WTransactions
     *
     * @param recipientTransaction
     */
    private List<WTransactionDTO> getRecipientWTransactions(WTransactionVO recipientTransaction) {
        // get sender transaction details from database

        List<WTransactionDTO> wTransactions = new ArrayList<>();
        WTransactionDTO recipientTransactionFromDB = wTransactionDao
                .getTransactionDetails(recipientTransaction.getId());
        Assert.assertNotNull(recipientTransactionFromDB, "Recipient's transaction in DB should be present");
        wTransactions.add(recipientTransactionFromDB);

        // get parent sender transactions
        List<WTransactionDTO> recipientParentTransactionsFromDB = wTransactionDao
                .getParentTransactionsDetails(recipientTransaction.getId());
        Assert.assertNotNull(recipientParentTransactionsFromDB,
                "Recipient's parent transaction(s) in DB should be present");
        wTransactions.addAll(recipientParentTransactionsFromDB);
        return wTransactions;
    }

    /**
     * Add Holding subbalance details to PaymentRecipientDTO
     *
     * @param recipientDTO
     */
    private void addWUserHoldingSubBalanceElement(PaymentRecipientDTO recipientDTO,
            List<WTransactionDTO> recipientTransactions) {
        // get user holding details and add to payment-sender
        RetriableWUserHoldingSubbalanceExistsCheck retriableSubbalanceExistsCheckTask = new RetriableWUserHoldingSubbalanceExistsCheck();
        List<WUserHoldingSubBalanceDTO> userHoldingSubBalances = retriableSubbalanceExistsCheckTask
                .execute(recipientTransactions);
        if (CollectionUtils.isNotEmpty(userHoldingSubBalances)) {
            recipientDTO.setUserHoldingSubBalances(userHoldingSubBalances);
        }
    }

    private class RetriableWUserHoldingSubbalanceExistsCheck
            extends RetriableTask<List<WTransactionDTO>, List<WUserHoldingSubBalanceDTO>> {

        public RetriableWUserHoldingSubbalanceExistsCheck() {
            super(60 * 1000, 10 * 1000);
        }

        @Override
        protected boolean isDesiredOutput(List<WUserHoldingSubBalanceDTO> output) {
            return !output.isEmpty();

        }

        @Override
        protected List<WUserHoldingSubBalanceDTO> retriableExecute(List<WTransactionDTO> recipientTransactions) {
            return wUserHoldingSubBalanceDao
                    .getWUserHoldingSubBalanceDetails(recipientTransactions);
        }

        @Override
        protected List<WUserHoldingSubBalanceDTO> onSuccess(List<WTransactionDTO> input,
                List<WUserHoldingSubBalanceDTO> output) {
            return output;
        }

        @Override
        protected List<WUserHoldingSubBalanceDTO> onFailure(List<WTransactionDTO> input,
                List<WUserHoldingSubBalanceDTO> output) {
            return new ArrayList<>();
        }

    }

}
