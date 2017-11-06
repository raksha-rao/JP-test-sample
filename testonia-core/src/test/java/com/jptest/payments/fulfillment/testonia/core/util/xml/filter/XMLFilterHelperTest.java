package com.jptest.payments.fulfillment.testonia.core.util.xml.filter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.configuration.Configuration;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.testng.annotations.Test;
import org.mockito.Mockito;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WTransactionIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WUserHoldingIgnorableFlagsHelper;


public class XMLFilterHelperTest {

    private XMLFilterHelper xmlFilterHelper;

    private Configuration configuration;

    @Test
    public void testGetAllXPathPatterns() {
        setup();
        String xpath = "/root/x/y";
        Set<String> expectedOutput = new HashSet<>(Arrays.asList("/root/x/y", "/root/x", "/root"));
        Set<String> actualOutput = xmlFilterHelper.getAllXPathPatterns(xpath);
        Assert.assertTrue(actualOutput.containsAll(expectedOutput) && expectedOutput.containsAll(actualOutput));
    }

    @Test
    public void testGetXPath() throws ParserConfigurationException, SAXException, IOException {
        setup();
        Mockito.when(configuration.getString(CoreConfigKeys.XML_XPATH_EXTRACTOR_XSLT_LOCATION.getName()))
                .thenReturn("XMLXPathExtractor.xsl");
        XMLHelper xmlHelper = new XMLHelper();

        String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><x><y>Hello</y></x><y><z>World</z></y></root>";
        Document inputXML = xmlHelper.convertToXmlDocument(inputString);
        Set<String> actualOutput = xmlFilterHelper.getXPath(inputXML);
        Set<String> expectedOutput = new HashSet<String>(
                Arrays.asList("/root/x/y", "/root/y/z", "/root/x", "/root", "/root/y", "/root"));

        Assert.assertTrue(actualOutput.containsAll(expectedOutput) && expectedOutput.containsAll(actualOutput));
    }

    @Test
    public void testGetFilteredNodes() throws ParserConfigurationException, SAXException, IOException {
        setup();
        Mockito.when(configuration.getString(CoreConfigKeys.XML_XPATH_EXTRACTOR_XSLT_LOCATION.getName()))
                .thenReturn("XMLXPathExtractor.xsl");
        String inputString1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><x><y>Hello</y></x><wtransaction><flags4>123</flags4></wtransaction></root>";
        String inputString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><x><y>Hello</y></x><y><z>World</z></y><wtransaction><flags4>123</flags4><flags5>123</flags5></wtransaction><flags4_SEND_MONEY>1</flags4_SEND_MONEY><flags5_MASSPAY_2_0>1</flags5_MASSPAY_2_0></root>";
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><x><y>Hello</y></x><wtransaction><flags4>123</flags4></wtransaction><flags4_SEND_MONEY>1</flags4_SEND_MONEY></root>";

        XMLHelper xmlHelper = new XMLHelper();
        Document expectedOutputXML = xmlHelper.convertToXmlDocument(expectedOutput);
        Document inputXML1 = xmlHelper.convertToXmlDocument(inputString1);
        Document inputXML2 = xmlHelper.convertToXmlDocument(inputString2);

        xmlFilterHelper.getFilteredXML(inputXML2, inputXML1);
        Diff diff = new Diff(inputXML2, expectedOutputXML);
        diff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());
        Assert.assertEquals(diff.similar(), true);
    }

    @Test
    public void testRemovalOfExpandedFlagsSection() throws ParserConfigurationException, SAXException, IOException {
        setup();
        Mockito.when(configuration.getString(CoreConfigKeys.XML_XPATH_EXTRACTOR_XSLT_LOCATION.getName()))
                .thenReturn("XMLXPathExtractor.xsl");
        String inputString1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><x><y>Hello</y></x></root>";
        String inputString2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><x><y>Hello</y></x><y><z>World</z></y><flags><flags4_SEND_MONEY>1</flags4_SEND_MONEY></flags></root>";
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><x><y>Hello</y></x></root>";

        XMLHelper xmlHelper = new XMLHelper();
        Document expectedOutputXML = xmlHelper.convertToXmlDocument(expectedOutput);
        Document inputXML1 = xmlHelper.convertToXmlDocument(inputString1);
        Document inputXML2 = xmlHelper.convertToXmlDocument(inputString2);

        xmlFilterHelper.getFilteredXML(inputXML2, inputXML1);

        Diff diff = new Diff(inputXML2, expectedOutputXML);
        diff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());
        Assert.assertEquals(diff.similar(), true);
    }

    private void setup() {
        FileHelper fileHelper = new FileHelper();
        XSLTHelper xsltHelper = new XSLTHelper(fileHelper);
        configuration = Mockito.mock(Configuration.class);
        XMLHelper xmlHelper = new XMLHelper();
        WTransactionIgnorableFlagsHelper wTransactionIgnorableFlagsHelper = new WTransactionIgnorableFlagsHelper();
        WUserHoldingIgnorableFlagsHelper wUserHoldingIgnorableFlagsHelper = new WUserHoldingIgnorableFlagsHelper();
        ExpandedFlagsHelper expandedFlagsHelper = new ExpandedFlagsHelper(wTransactionIgnorableFlagsHelper,
                wUserHoldingIgnorableFlagsHelper, xmlHelper);
        xmlFilterHelper = new XMLFilterHelper(xsltHelper, xmlHelper, configuration, expandedFlagsHelper);
    }

    public String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        }
        catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
