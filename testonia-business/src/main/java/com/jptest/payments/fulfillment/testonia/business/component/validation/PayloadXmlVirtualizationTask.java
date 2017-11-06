package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.business.util.XMLElementSorter;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;

/**
 * Performs virtualization on payload validation response XML
 */
public class PayloadXmlVirtualizationTask extends XmlVirtualizationTask<Document> {

    @Inject
    private Configuration configuration;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private XMLElementSorter xmlElementSorter;

    @Inject
    private void init() {

        String virtualizeSchemaFileLocation = configuration
                .getString(BizConfigKeys.PAYLOAD_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
        try {
            populateVirtualizeSchema(virtualizeSchemaFileLocation);
        } catch (IOException e) {
            throw new TestExecutionException("Error reading Payload virtualize schema file", e);
        }
    }

    @Override
    public Document execute(Document document) {
        // sort document elements alphabetically
        document = xmlElementSorter.sortAlphabetically(document);
        ignoreZeroValueNodes(document);
        ignoreEmptyNodes(document);
        ignoreNodes(document);
        ignoreAttributes(document);
        transformNodes(document);
        setDefaultValueNodes(document);
        setValueIfNonEmpty(document);
        ignoreNodesUsingXPath(document);
        printTransformMapping();
        return document;
    }

}
