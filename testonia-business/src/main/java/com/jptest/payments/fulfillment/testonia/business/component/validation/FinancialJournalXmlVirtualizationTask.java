package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;

/**
 * Performs virtualization on payment-sender validation response XML
 */
public class FinancialJournalXmlVirtualizationTask extends XmlVirtualizationTask<Document> {

    @Inject
    private Configuration configuration;

    @Inject
    private void init() {
        String virtualizeSchemaFileLocation = configuration
                .getString(BizConfigKeys.FINANCIAL_JOURNAL_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
        try {
            populateVirtualizeSchema(virtualizeSchemaFileLocation);
        } catch (IOException e) {
            throw new TestExecutionException("Error reading FJ virtualize schema file", e);
        }
    }

    @Override
    public Document execute(Document document) {
        transformNodes(document);
        transformNodesUsingXPath(document);
        setValueIfNonEmpty(document);
        ignoreNodesUsingXPath(document);
        printTransformMapping();
        return document;
    }

}
