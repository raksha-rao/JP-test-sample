package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.ignorable.flags.UnsetIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Gets AMQ messageIds for Credit and Debit payloads from activity-logs and makes VOX call to get Payload for these Ids.
 * <p>
 * It then compares the Credit and Debit payloads with golden file and displays difference, if any.
 */
public abstract class PayloadValidationBaseAsserter extends E2EGoldenFileComparisonAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadValidationBaseAsserter.class);

    private static final String OPERATION = "Payload-Validation";

    private static final String XML_ROOT_ELEMENT_NAME = "DequeueFixture_ResultVO";

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private PayloadXmlVirtualizationTask xmlVirtualizationTask;
    
    @Inject
    private UnsetIgnorableFlagsHelper unsetIgnorableFlagsHelper;

    public PayloadValidationBaseAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return OPERATION;
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        List<String> messageIds = getMessageIds(context);
        String ignorableFlagsLocation = (String) getDataFromContext(context, ContextKeys.IGNORABLE_FLAGS_LOCATION.getName());

        try {
            return getActualResponse(messageIds, ignorableFlagsLocation);
        } catch (Exception e) {
            LOGGER.error("Exception occurred in getting payload:", e);
        }

        return null;
    }

    protected abstract List<String> getMessageIds(Context context);

    /**
     * Populate response object from Database
     *  
     * @param senderTransaction
     * @return
     */
    private Document getActualResponse(final List<String> messageIds, String ignorableFlagsLocation) {
        Document doc = createActualResponseXml(messageIds);
        unsetIgnorableFlagsHelper.unsetFlags(ignorableFlagsLocation, doc);
        doc = processActualDocument(doc);
        doc = xmlVirtualizationTask.execute(doc);
        LOGGER.info("Payload Actual XML: \n{}", xmlHelper.getPrettyXml(doc));
        return doc;
    }
    
    protected Document processActualDocument(Document doc) {
        return doc;
    }

    /**
     * Makes AMQ DB call and prepares XML document 
     * 
     * @param messageIds
     * @return
     */
    private Document createActualResponseXml(final List<String> messageIds) {
        List<String> deserializedVOs = new ArrayList<String>();
        for (String messageId : messageIds) {
            deserializedVOs.add(getActualResponseXmlString(messageId));
        }
        return xmlHelper.getXMLDocumentForDeserializedVOs(deserializedVOs, XML_ROOT_ELEMENT_NAME);
    }
    
    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_PAYLOAD_DBDIFF;
    }

    protected abstract String getActualResponseXmlString(String messageId);
}
