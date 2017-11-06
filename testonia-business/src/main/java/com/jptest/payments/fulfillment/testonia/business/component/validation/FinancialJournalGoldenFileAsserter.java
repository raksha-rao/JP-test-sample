package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.eng.BonusGrantEntryDao;
import com.jptest.payments.fulfillment.testonia.dao.eng.ClearingJournalEntryDao;
import com.jptest.payments.fulfillment.testonia.dao.eng.FeesChargedJournalEntryDao;
import com.jptest.payments.fulfillment.testonia.dao.eng.FinancialJournal2Dao;
import com.jptest.payments.fulfillment.testonia.dao.eng.PayableJournalEntryDao;
import com.jptest.payments.fulfillment.testonia.dao.eng.ReceivableJournalEntryDao;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.money.BonusGrantEntryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.ClearingJournalEntryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.FeesChargedJournalEntryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.FinancialJournal2DTO;
import com.jptest.payments.fulfillment.testonia.model.money.FinancialJournalDTO;
import com.jptest.payments.fulfillment.testonia.model.money.FinancialJournalValidationFixtureDTO;
import com.jptest.payments.fulfillment.testonia.model.money.PayableJournalEntryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.ReceivableJournalEntryDTO;
import com.jptest.payments.fulfillment.testonia.model.ActivityDetails;

/**
 * Makes VOX call and gets financial journal data and compares with golden file
 * 
 */
public class FinancialJournalGoldenFileAsserter extends E2EGoldenFileComparisonAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialJournalGoldenFileAsserter.class);

    private static final String OPERATION = "FinancialJournal-Validation";

    @Inject
    private FinancialJournal2Dao financialJournal2Dao;

    @Inject
    private ClearingJournalEntryDao clearingJournalEntryDao;

    @Inject
    private FeesChargedJournalEntryDao feesChargedJournalEntryDao;

    @Inject
    private BonusGrantEntryDao bonusGrantEntryDao;

    @Inject
    private PayableJournalEntryDao payableJournalEntryDao;

    @Inject
    private ReceivableJournalEntryDao receivableJournalEntryDao;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private FinancialJournalXmlVirtualizationTask xmlVirtualizationTask;

    @Override
    public String getValidationType() {
        return OPERATION;
    }
    
    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_FINANCIAL_JOURNAL_DBDIFF;
    }

    public FinancialJournalGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        ActivityDetails activityDetails = (ActivityDetails) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_ACTIVITY_DETAILS_KEY.getName());
        FinancialJournalValidationFixtureDTO resultDTO = new FinancialJournalValidationFixtureDTO();
        resultDTO.setFinancialJournalDTO(prepareFinancialJournalDTO(activityDetails));

        try {
            Document doc = resultDTO.getDocument();
            doc = processActualDocument(doc);
            doc = xmlVirtualizationTask.execute(doc);
            LOGGER.info("Financial Journal Actual XML: \n{}", xmlHelper.getPrettyXml(doc));
            return doc;
        } catch (ParserConfigurationException | JAXBException e) {
            throw new TestExecutionException(e);
        }
    }
    
    protected Document processActualDocument(Document doc) {
        return doc;
    }

    /**
     * Prepare financialJournalDTO subelements
     *  
     * @param activityDetails
     * @return
     */
    private FinancialJournalDTO prepareFinancialJournalDTO(final ActivityDetails activityDetails) {
        FinancialJournalDTO financialJournalDTO = new FinancialJournalDTO();
        addFinancialJournal2Element(activityDetails, financialJournalDTO);
        addClearingJournalEntryElement(activityDetails, financialJournalDTO);
        addFeesChargedJournalEntryElement(activityDetails, financialJournalDTO);
        addBonusGrantEntryElement(activityDetails, financialJournalDTO);
        addPayableJournalEntryElement(activityDetails, financialJournalDTO);
        addReceivableJournalEntryElement(activityDetails, financialJournalDTO);
        return financialJournalDTO;
    }

    /**
     * Add FinancialJournal2 to FinancialJournalDTO
     * @param activityDetails
     * @param financialJournalDTO
     */
    private void addFinancialJournal2Element(ActivityDetails activityDetails, FinancialJournalDTO financialJournalDTO) {
        // get FinancialJournal2 entries, if any - and add to FinancialJournalDTO
        List<FinancialJournal2DTO> financialJournal2Entries = financialJournal2Dao
                .getFinancialJournal2Details(activityDetails.getActivityId());
        if (CollectionUtils.isNotEmpty(financialJournal2Entries)) {
            financialJournalDTO.setFinancialJournal2Entries(financialJournal2Entries);
        }
    }

    /**
     * Add ClearingJournalEntry to FinancialJournalDTO
     * @param activityDetails
     * @param financialJournalDTO
     */
    private void addClearingJournalEntryElement(ActivityDetails activityDetails,
            FinancialJournalDTO financialJournalDTO) {
        // get ClearingJournal entries, if any - and add to FinancialJournalDTO
        List<ClearingJournalEntryDTO> clearingJournalEntries = clearingJournalEntryDao
                .getClearingJournalEntryDetails(activityDetails.getActivityId());
        if (CollectionUtils.isNotEmpty(clearingJournalEntries)) {
            financialJournalDTO.setClearingJournalEntries(clearingJournalEntries);
        }
    }

    /**
     * Add FeesChargedJournalEntry to FinancialJournalDTO
     * @param activityDetails
     * @param financialJournalDTO
     */
    private void addFeesChargedJournalEntryElement(ActivityDetails activityDetails,
            FinancialJournalDTO financialJournalDTO) {
        // get FeesChargedJournal entries, if any - and add to FinancialJournalDTO
        List<FeesChargedJournalEntryDTO> feesChargedJournalEntries = feesChargedJournalEntryDao
                .getFeesChargedJournalEntryDetails(activityDetails.getActivityId());
        if (CollectionUtils.isNotEmpty(feesChargedJournalEntries)) {
            financialJournalDTO.setFeesChargedJournalEntries(feesChargedJournalEntries);
        }
    }

    /**
     * Add BonusGrantEntry to FinancialJournalDTO
     * @param activityDetails
     * @param financialJournalDTO
     */
    private void addBonusGrantEntryElement(ActivityDetails activityDetails, FinancialJournalDTO financialJournalDTO) {
        // get BonusGrant entries, if any - and add to FinancialJournalDTO
        List<BonusGrantEntryDTO> bonusGrantEntries = bonusGrantEntryDao
                .getBonusGrantEntryDetails(activityDetails.getActivityId());
        if (CollectionUtils.isNotEmpty(bonusGrantEntries)) {
            financialJournalDTO.setBonusGrantEntries(bonusGrantEntries);
        }
    }

    /**
     * Add PayableJournalEntry to FinancialJournalDTO
     * @param activityDetails
     * @param financialJournalDTO
     */
    private void addPayableJournalEntryElement(ActivityDetails activityDetails,
            FinancialJournalDTO financialJournalDTO) {
        // get PayableJournal entries, if any - and add to FinancialJournalDTO
        List<PayableJournalEntryDTO> payableJournalEntries = payableJournalEntryDao
                .getPayableJournalEntryDetails(activityDetails.getActivityId());
        if (CollectionUtils.isNotEmpty(payableJournalEntries)) {
            financialJournalDTO.setPayableJournalEntries(payableJournalEntries);
        }
    }

    /**
     * Add ReceivableJournalEntry to FinancialJournalDTO
     * @param activityDetails
     * @param financialJournalDTO
     */
    private void addReceivableJournalEntryElement(ActivityDetails activityDetails,
            FinancialJournalDTO financialJournalDTO) {
        // get ReceivableJournal entries, if any - and add to FinancialJournalDTO
        List<ReceivableJournalEntryDTO> receivableJournalEntries = receivableJournalEntryDao
                .getReceivableJournalEntryDetails(activityDetails.getActivityId());
        if (CollectionUtils.isNotEmpty(receivableJournalEntries)) {
            financialJournalDTO.setReceivableJournalEntries(receivableJournalEntries);
        }
    }

}
