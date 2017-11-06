package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import javax.inject.Inject;
import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;


/**
 * Performs virtualization on payment-side validation response XML
 */
public class PaymentSideXmlVirtualizationTask extends XmlVirtualizationTask<Document> {

    @Inject
    private Configuration configuration;

    @Inject
    public void init() {
        String virtualizeSchemaFileLocation = configuration
                .getString(BizConfigKeys.PAYMENT_SIDE_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
        try {
            populateVirtualizeSchema(virtualizeSchemaFileLocation);
        }
        catch (IOException e) {
            throw new TestExecutionException("Error reading payment-side virtualize schema file", e);
        }

    }

    @Override
    public Document execute(Document document) {
        setValueIfEmpty(document);
        transformNodes(document);
        transformNodesUsingXPath(document);
        ignoreNodesUsingXPath(document);
        setValueIfNonEmpty(document);
        printTransformMapping();
        return document;
    }

}
