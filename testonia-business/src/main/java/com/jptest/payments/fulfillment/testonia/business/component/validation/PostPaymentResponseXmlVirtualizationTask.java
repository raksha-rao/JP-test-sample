package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import javax.inject.Inject;
import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;


/**
 * Performs virtualization on Post Payment response validation XML
 * <li>Virtualize the Post Payment response</li>
 *
 * @JP Inc.
 */

public class PostPaymentResponseXmlVirtualizationTask extends XmlVirtualizationTask<Document> {

    @Inject
    private Configuration config;

    @Inject
    public void init() {
        final String virtualizeSchemaFileLocation = this.config
                .getString(BizConfigKeys.POST_PAYMENT_RESPONSE_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
        try {
            this.populateVirtualizeSchema(virtualizeSchemaFileLocation);
        }
        catch (final IOException e) {
            throw new TestExecutionException("Error reading Post Payment virtualize schema file", e);
        }
    }

    @Override
    public Document execute(final Document document) {
        this.ignoreAttributes(document);
        this.transformNodes(document);
        this.transformNodesUsingXPath(document);
        this.ignoreNodesUsingXPath(document);
        this.printTransformMapping();
        return document;
    }

}
