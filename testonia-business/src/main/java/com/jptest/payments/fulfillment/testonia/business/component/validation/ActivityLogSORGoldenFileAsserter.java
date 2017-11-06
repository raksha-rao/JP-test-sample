package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.springframework.util.Assert;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.business.util.XMLElementSorter;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Golden file based validation for SOR data as part of activity log.
 * i.e. "payment_data" section
 */
public class ActivityLogSORGoldenFileAsserter extends FulfillmentActivityLogBaseAsserter {

    @Inject
    private SORXMLVirtualizationTask xmlVirtualizationTask;

    public ActivityLogSORGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return "payments_sor_validation";
    }

    @Override
    protected String getXPathCriteria() {
        return "//payment_data";
    }

    @Override
    protected XmlVirtualizationTask<Document> getVirtualizationTask() {
        return xmlVirtualizationTask;
    }
    
    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_ACTIVITY_LOG_SOR_DBDIFF;
    }

    /**
     * Virtualization logic for SOR data as part of activity log
     */
    private static class SORXMLVirtualizationTask extends XmlVirtualizationTask<Document> {

        @Inject
        private Configuration configuration;

        @Inject
        private XMLElementSorter xmlSorter;

        @Inject
        private FileHelper fileHelper;

        private String xslSortTemplate;

        @Inject
        private void init() {
            String virtualizeSchemaFileLocation = configuration
                    .getString(BizConfigKeys.ACTIVITY_LOG_SOR_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
            try {
                populateVirtualizeSchema(virtualizeSchemaFileLocation);
            } catch (IOException e) {
                throw new TestExecutionException(e);
            }

            String xslSortTemplateFileLocation = configuration
                    .getString(BizConfigKeys.ACTIVITY_LOG_XSL_SOR_TEMPLATE_FILE_LOCATION.getName());
            try {
                this.xslSortTemplate = fileHelper.readContent(xslSortTemplateFileLocation);
            } catch (IOException e) {
                throw new TestExecutionException("Exception reading XSLT Template for sorting", e);
            }
            Assert.notNull("xslSortTemplate can not be null", xslSortTemplate);
        }

        @Override
        public Document execute(Document document) {
            document = xmlSorter.sortAlphabetically(document, xslSortTemplate);
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
