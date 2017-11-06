package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.business.util.XMLElementSorter;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Golden file based validation for publish legacy event request section of activity log
 * "publish_legacy_events_request"
 */
public class LegacyEventsGoldenFileAsserter extends FulfillmentActivityLogBaseAsserter {

    @Inject
    private LegacyEventsXMLVirtualizationTask xmlVirtualizationTask;

    public LegacyEventsGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return "LegacyEvents-validation";
    }

    @Override
    protected String getXPathCriteria() {
        return "//publish_legacy_events_request";
    }

    @Override
    protected XmlVirtualizationTask<Document> getVirtualizationTask() {
        return xmlVirtualizationTask;
    }
    
    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_LEGACY_EVENTS_DBDIFF;
    }

    /**
     * Virtualization logic for legacy events validation
     */
    private static class LegacyEventsXMLVirtualizationTask extends XmlVirtualizationTask<Document> {

        @Inject
        private Configuration configuration;

        @Inject
        private XMLElementSorter xmlSorter;

        @Inject
        private void init() {
            //TODO : separate the virtualization files for SOR and legacy events
            String virtualizeSchemaFileLocation = configuration
                    .getString(BizConfigKeys.ACTIVITY_LOG_LEGACY_EVENTS_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
            try {
                populateVirtualizeSchema(virtualizeSchemaFileLocation);
            } catch (IOException e) {
                throw new TestExecutionException(e);
            }

        }

        @Override
        public Document execute(Document document) {
            document = xmlSorter.sortAlphabetically(document);
            super.transformNodes(document);
            ignoreZeroValueNodes(document);
            ignoreEmptyNodes(document);
            ignoreNodes(document);
            transformNodesUsingXPath(document);
            setValueIfNonEmpty(document);
            ignoreAttributes(document);
            printTransformMapping();
            return document;
        }

    }

}
