package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;

/**
 * Performs virtualization on IPN response XML
 */
public class IPNXmlVirtualizationTask extends XmlVirtualizationTask<Void> {

    @Inject
    private Configuration configuration;

    @Inject
    private void init() {
        String virtualizeSchemaFileLocation = configuration
                .getString(BizConfigKeys.IPN_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
        try {
            populateVirtualizeSchema(virtualizeSchemaFileLocation);
        } catch (IOException e) {
            throw new TestExecutionException("Error reading IPN virtualize schema file", e);
        }

    }

    @Override
    public Void execute(Document inputDocument) {
        ignoreZeroValueNodes(inputDocument);
        ignoreEmptyNodes(inputDocument);
        ignoreNodes(inputDocument);
        transformNodes(inputDocument);
        setDefaultValueNodes(inputDocument);
        ignoreNodesUsingXPath(inputDocument);
        printTransformMapping();
        return null;
    }
}
