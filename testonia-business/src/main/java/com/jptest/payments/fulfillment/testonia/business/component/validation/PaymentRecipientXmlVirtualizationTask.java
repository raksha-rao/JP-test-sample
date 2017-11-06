package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;

/**
 * Performs virtualization on payment-recipient validation response XML
 */
public class PaymentRecipientXmlVirtualizationTask extends XmlVirtualizationTask<Document> {

    @Inject
    private Configuration configuration;

    @Inject
    private void init() {
        String virtualizeSchemaFileLocation = configuration
                .getString(BizConfigKeys.PAYMENT_RECIPIENT_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
        try {
            populateVirtualizeSchema(virtualizeSchemaFileLocation);
        } catch (IOException e) {
            throw new TestExecutionException("Error reading payment-recipient virtualize schema file", e);
        }
    }

    @Override
    public Document execute(Document document) {
        transformNodes(document);
        transformNodesUsingXPath(document);
        ignoreNodesUsingXPath(document);
        setValueIfNonEmpty(document);
        printTransformMapping();
        return document;
    }

}
