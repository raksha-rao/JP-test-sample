package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import java.math.BigInteger;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.configuration.Configuration;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.jptest.payments.fulfillment.testonia.business.ignorable.flags.UnsetIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.business.util.XMLElementSorter;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.eng.FulfillmentActivityLogDao;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityLogDTO;
import com.jptest.payments.fulfillment.testonia.model.ActivityDetails;


/**
 * Golden file based validation for payment message section of activity log "serialized_payment_message"
 */

/**
 * This class generates and validates the Payment Message which is available in the activity log. This class extends
 * from {@link FulfillmentActivityLogBaseAsserter} and it overrides the impl of getActualResponseXml() since
 * FulfillmentActivityLogBaseAsserter getActualResponseXml() will get only XML based tags and Payment Message will be in
 * JSON format.
 * <li>steps for this logic are:</li>
 * <li>1. Get the activity id (default impl retrieves the FULFILLMENT_ACTIVITY_ID (FFAID) from context using
 * {@link ContextKeys.ENGINE_ACTIVITY_ID_KEY}) but as part of flex post payment migration post payment activity payment
 * message should be validated. Hence getActivityId() should be overriding the Base Class implementation</li>
 * <li>2. Get the {@link FulfillmentActivityLogDTO} based on context and activity id. It is to check if actiivtyID is
 * present in the context and use it. If not, then get it from the DAO based on the activity id.</li>
 * <li>3. Since the activity log is stored as a blob, we get the whole log every time so we need to get the subset of
 * the document for which we are validating.</li>
 * <li>3A. Individual validations need to provide the XPATH that they are interested in. Based on that, we get the
 * subset of the document.</li>
 * <li>4. Convert the JSON which is available in the serialized_payment_message tag from the Activity log to XML
 * <li>5. Virtualization</li>
 * <li>6. After this, the base class {@link GoldenFileComparisonAsserter} kicks in and performs the comparison.</li>
 */

public class ActivityLogPaymentMessageGoldenFileAsserter extends FulfillmentActivityLogBaseAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogPaymentMessageGoldenFileAsserter.class);

    private static final String XML_ROOT_ELEMENT_NAME = "PaymentMessage";

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private UnsetIgnorableFlagsHelper unsetIgnorableFlagsHelper;

    @Inject
    private FulfillmentActivityLogDao dao;

    @Inject
    private PaymentMessageXMLVirtualizationTask xmlVirtualizationTask;

    public ActivityLogPaymentMessageGoldenFileAsserter(final GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return "PaymentMessage-Validation";
    }

    @Override
    protected String getXPathCriteria() {
        return "/PaymentMessage/FulfillmentEngine_FulfillmentActivityLogVO/fulfillment_request/serialized_payment_message";
    }

    @Override
    protected XmlVirtualizationTask<Document> getVirtualizationTask() {
        return this.xmlVirtualizationTask;
    }

    @Override
    protected Document getActualResponseXml(final Context context) {
        final BigInteger activityId = this.getActivityId(context);
        final FulfillmentActivityLogDTO activityLogDTO = this.getActivityLogDTO(context, activityId);
        Document doc = this.xmlHelper.getXMLDocumentForDeserializedVOs(activityLogDTO.getActivityLogs(),
                XML_ROOT_ELEMENT_NAME);

        doc = this.xmlHelper.getSubsetDocument(doc, this.getXPathCriteria());

        final String paymentMessage = doc.getFirstChild().getTextContent();
        final JSONObject paymentMessageAsJson = new JSONObject(paymentMessage);
        final String paymentMessageAsXML = XML.toString(paymentMessageAsJson);
        doc = this.getXMLDocumentFromXMLString(paymentMessageAsXML);
        this.unsetFlags(context, doc);
        doc = this.processActualDocument(doc);
        final XmlVirtualizationTask<Document> virtualizationTask = this.getVirtualizationTask();
        if (virtualizationTask != null) {
            doc = virtualizationTask.execute(doc);
        }
        LOGGER.info("The {} XML {}", this.getValidationType(), this.xmlHelper.getPrettyXml(doc));
        return doc;

    }

    protected Document processActualDocument(final Document doc) {
        return doc;
    }

    @Override
    protected FulfillmentActivityLogDTO getActivityLogDTO(final Context context, final BigInteger activityId) {
        return this.dao.getActivityLog(activityId);
    }

    private void unsetFlags(final Context context, final Document doc) {
        final String ignorableFlagsLocation = (String) this.getDataFromContext(context,
                ContextKeys.IGNORABLE_FLAGS_LOCATION.getName());
        this.unsetIgnorableFlagsHelper.unsetFlags(ignorableFlagsLocation, doc);
    }

    @Override
    protected BigInteger getActivityId(final Context context) {
        final ActivityDetails activityDetails = (ActivityDetails) this.getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_ACTIVITY_DETAILS_KEY.getName());
        return activityDetails.getActivityId();
    }

    protected Document getXMLDocumentFromXMLString(final String xmlString) {
        Document document = null;
        try {
            document = this.xmlHelper.convertToXmlDocument(xmlString);
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            throw new TestExecutionException("Error Converting  Payment Message String to XML Document", e);
        }
        return document;
    }

    /**
     * Virtualization logic for legacy events validation
     */
    private static class PaymentMessageXMLVirtualizationTask extends XmlVirtualizationTask<Document> {

        @Inject
        private Configuration configuration;

        @Inject
        private XMLElementSorter xmlSorter;

        @Inject
        private void init() {
            final String virtualizeSchemaFileLocation = this.configuration
                    .getString(BizConfigKeys.PAYMENT_MESSAGE_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
            try {
                this.populateVirtualizeSchema(virtualizeSchemaFileLocation);
            }
            catch (final IOException e) {
                throw new TestExecutionException(e);
            }

        }

        @Override
        public Document execute(Document document) {
            document = this.xmlSorter.sortAlphabetically(document);
            super.transformNodes(document);
            this.ignoreNodesUsingXPath(document);
            this.transformNodesUsingXPath(document);
            this.setValueIfNonEmpty(document);
            this.ignoreAttributes(document);
            this.printTransformMapping();
            return document;
        }

    }

    @Override
    protected TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_ACTIVITY_LOG_PAYMENT_MESSAGE_DBDIFF;
    }

}
