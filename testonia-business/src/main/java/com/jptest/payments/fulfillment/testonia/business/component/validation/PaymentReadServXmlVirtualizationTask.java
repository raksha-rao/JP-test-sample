package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import javax.inject.Inject;
import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;


/**
 * Performs virtualization on PRS response validation XML
 */
public class PaymentReadServXmlVirtualizationTask extends XmlVirtualizationTask<Document> {

    @Inject
    private Configuration config;

    @Inject
    public void init() {
        String virtualizeSchemaFileLocation = config
                .getString(BizConfigKeys.PAYMENT_READSERV_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
        try {
            populateVirtualizeSchema(virtualizeSchemaFileLocation);
        } catch (IOException e) {
            throw new TestExecutionException("Error reading PRS virtualize schema file", e);
        }
    }

    @Override
    public Document execute(Document document) {
        ignoreAttributes(document);
        ignoreNodes(document);
        transformNodes(document);
        transformNodesUsingXPath(document);
        ignoreNodesUsingXPath(document);
        printTransformMapping();
        return document;
    }

}
