package com.jptest.payments.fulfillment.testonia.business.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;

/**
 * Sorts the w3c document elements by element name
 */
@Singleton
public class XMLElementSorter {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLElementSorter.class);

    @Inject
    private Configuration configuration;

    @Inject
    private FileHelper fileHelper;

    @Inject
    private XMLHelper xmlHelper;

    private String xslSortTemplate;

    @Inject
    public void init() {
        String xslSortTemplateFileLocation = configuration
                .getString(BizConfigKeys.XSL_SORT_TEMPLATE_FILE_LOCATION.getName());
        try {
            this.xslSortTemplate = fileHelper.readContent(xslSortTemplateFileLocation);
        } catch (IOException e) {
            throw new TestExecutionException("Exception reading XSLT Template for sorting", e);
        }
        Assert.notNull("xslSortTemplate can not be null", xslSortTemplate);
    }

    public Document sortAlphabetically(Document document) {
        String template = xslSortTemplate;
        return sortAlphabetically(document, template);
    }

    /**
     * Sorts the doc based on the given template which is a string representation of
     * XSLT template
     * @param document
     * @param template
     * @return
     */
    public Document sortAlphabetically(Document document, String template) {
        try {
            StreamSource stylesource = new StreamSource(new StringReader(template));
            Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesource);

            DOMSource source = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            return xmlHelper.convertToXmlDocument(writer.toString());
        } catch (TransformerFactoryConfigurationError | TransformerException | ParserConfigurationException
                | SAXException | IOException e) {
            LOGGER.error("Exception sorting XML", e);
        }
        return null;
    }

}
