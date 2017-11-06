package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Golden file based validation for IPN data as part of activity log.
 * i.e. "IPNPAYDATAVO" which is part of fulfillment_request
 * @JP Inc.
 */
public class ActivityLogIPNGoldenFileAsserter extends FulfillmentActivityLogBaseAsserter{

    @Inject
    private IPNXMLVirtualizationTask xmlVirtualizationTask;

    public ActivityLogIPNGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return "ipn_validation";
    }

    @Override
    protected String getXPathCriteria() {
        return "//participant_data_extensions/item[@type='Money::IPNPayDataVO']";
    }

    @Override
    protected XmlVirtualizationTask<Document> getVirtualizationTask() {
        return xmlVirtualizationTask;
    }
    
    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_ACTIVITY_LOG_IPN_DBDIFF;
    }

    /**
     * Virtualization logic for IPN data as part of activity log
     */
    private static class IPNXMLVirtualizationTask extends XmlVirtualizationTask<Document> {

        @Inject
        private Configuration configuration;

        @Inject
        private void init() {
            String virtualizeSchemaFileLocation = configuration
                    .getString(BizConfigKeys.ACTIVITY_LOG_IPN_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
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
