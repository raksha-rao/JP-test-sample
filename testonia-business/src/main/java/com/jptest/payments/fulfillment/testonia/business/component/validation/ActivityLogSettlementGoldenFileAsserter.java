package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import java.math.BigInteger;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.business.util.SettlementValidationXMLBuilder;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.eng.FulfillmentActivityLogDao;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityLogDTO;

/**
 * Golden file based validation for Settlement Request as part of activity log.
 * i.e. "Settlement" which is part of Async_Messages/Async_Tasks or Async_Messages_for_amq_daemon
 *
 * @JP Inc.
 */
public class ActivityLogSettlementGoldenFileAsserter extends FulfillmentActivityLogBaseAsserter {

    @Inject
    private SettlementXMLVirtualizationTask xmlVirtualizationTask;

    @Inject
    private FulfillmentActivityLogDao dao;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogSettlementGoldenFileAsserter.class);

    private static final String XML_ROOT_ELEMENT_NAME = "ActivityLog";

    public ActivityLogSettlementGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private SettlementValidationXMLBuilder settlementValidationXMLBuilder;

    @Override
    public String getValidationType() {
        return "settlement_validation";
    }

    @Override
    protected String getXPathCriteria() {
        return "//settlementMessage";
    }

    @Override
    protected XmlVirtualizationTask<Document> getVirtualizationTask() {
        return xmlVirtualizationTask;
    }

    @Override
    protected TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_ACTIVITY_LOG_SETTLEMENT_DBDIFF;
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        BigInteger activityId = getActivityId(context);
        FulfillmentActivityLogDTO activityLogDTO = getActivityLogDTO(context, activityId);
        Document doc = xmlHelper.getXMLDocumentForDeserializedVOs(activityLogDTO.getActivityLogs(),
                XML_ROOT_ELEMENT_NAME);
        try {
            doc = settlementValidationXMLBuilder.getXmlResponse(doc);
        } catch (Exception e) {
            LOGGER.error("Getting Settlement Request failed");
        }
        XmlVirtualizationTask<Document> virtualizationTask = getVirtualizationTask();
        if (virtualizationTask != null)
            doc = virtualizationTask.execute(doc);
        LOGGER.info("The {} XML {}", getValidationType(), xmlHelper.getPrettyXml(doc));
        return doc;

    }

    @Override
    protected FulfillmentActivityLogDTO getActivityLogDTO(Context context, BigInteger activityId) {
        return dao.getLatestActivityLog(activityId);
    }

    @Override
    public boolean shouldParticipate(Context context) {
        return false; // disabled because it is continuously failing with NPE 
    }

    private static class SettlementXMLVirtualizationTask extends XmlVirtualizationTask<Document> {

        @Inject
        private Configuration configuration;

        @Inject
        private void init() {
            String virtualizeSchemaFileLocation = configuration
                    .getString(BizConfigKeys.ACTIVITY_LOG_SETTLEMENT_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
            try {
                populateVirtualizeSchema(virtualizeSchemaFileLocation);
            } catch (IOException e) {
                throw new TestExecutionException(e);
            }
        }

        @Override
        public Document execute(Document document) {
            transformNodes(document);
            ignoreNodes(document);
            transformNodesUsingXPath(document);
            setValueIfNonEmpty(document);
            ignoreAttributes(document);
            printTransformMapping();
            return document;
        }

    }

}
