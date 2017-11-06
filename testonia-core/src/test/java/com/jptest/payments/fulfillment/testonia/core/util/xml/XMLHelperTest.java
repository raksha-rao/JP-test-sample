package com.jptest.payments.fulfillment.testonia.core.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;


/**
 * Unit test for {@link XMLHelper}
 */
public class XMLHelperTest {

    @Test(dataProvider = "convertToXmlDocumentDP")
    public void convertToXmlDocument(String xmlString) throws ParserConfigurationException, SAXException, IOException {
        Document document = new XMLHelper().convertToXmlDocument(xmlString);
        Assert.assertNotNull(document, "Document should not be null");
    }

    @DataProvider
    private static Object[][] convertToXmlDocumentDP() {
        return new Object[][] {
                { "<a><b>test</b></a>" },
                { "<a>  <b>test</b>  </a>" }
        };
    }

    @Test(expectedExceptions = SAXParseException.class, dataProvider = "convertToXmlDocumentForInvalidXmlDP")
    public void convertToXmlDocumentForInvalidXml(String invalidXmlString)
            throws ParserConfigurationException, SAXException, IOException {
        new XMLHelper().convertToXmlDocument(invalidXmlString);
    }

    @DataProvider
    private static Object[][] convertToXmlDocumentForInvalidXmlDP() {
        return new Object[][] {
                { "<a><b>test</b>" },
                { "" },
                { "  " }
        };
    }

    @Test
    public void getPrettyXml() throws ParserConfigurationException, SAXException, IOException {
        XMLHelper helper = new XMLHelper();
        Document document = helper.convertToXmlDocument("<a><b>test</b></a>");
        String output = helper.getPrettyXml(document);
        Assert.assertNotNull(output, "pretty print output should not be null");
    }

    @Test(dataProvider = "removeAttributesDP")
    public void removeAttributes(String inputXml, List<String> attributesToBeRemoved)
            throws ParserConfigurationException, SAXException, IOException {
        XMLHelper helper = new XMLHelper();
        Document document = helper
                .convertToXmlDocument(inputXml);
        helper.removeAttributes(attributesToBeRemoved, document);
        String outputAfterRemovingAttributes = helper.getPrettyXml(document);
        for (String attributeToBeRemoved : attributesToBeRemoved) {
            Assert.assertFalse(outputAfterRemovingAttributes.contains(" " + attributeToBeRemoved + "="),
                    "document should not contain " + attributeToBeRemoved);
        }
    }

    @DataProvider
    private static Object[][] removeAttributesDP() {
        return new Object[][] {
                { "<a><b type=\"test\"></b></a>", Arrays.asList("type") }
        };
    }

    @Test(dataProvider = "removeAttributesDP2")
    public void removeAttributes(String inputXml, String elementName, List<String> attributesToBeRemoved)
            throws ParserConfigurationException, SAXException, IOException {
        XMLHelper helper = new XMLHelper();
        Document document = helper
                .convertToXmlDocument(inputXml);
        helper.removeAttributes(elementName, attributesToBeRemoved, document);
        String outputAfterRemovingAttributes = helper.getPrettyXml(document);
        for (String attributeToBeRemoved : attributesToBeRemoved) {
            Assert.assertFalse(outputAfterRemovingAttributes.contains(" " + attributeToBeRemoved + "="),
                    "document should not contain " + attributeToBeRemoved);
        }
    }

    @DataProvider
    private static Object[][] removeAttributesDP2() {
        return new Object[][] {
                { "<a><b type=\"test\"></b></a>", "b", Arrays.asList("type") }
        };
    }

    @Test(dataProvider = "removeNodeByNameDP")
    public void removeNodeByName(String inputXml, String elementName)
            throws ParserConfigurationException, SAXException, IOException {
        XMLHelper helper = new XMLHelper();
        Document document = helper.convertToXmlDocument(inputXml);
        helper.removeNodeByName(document, elementName);
        String outputAfterRemovingElements = helper.getPrettyXml(document);
        Assert.assertFalse(outputAfterRemovingElements.contains("<" + elementName + ">"),
                "document should not contain " + outputAfterRemovingElements);
    }

    @DataProvider
    private static Object[][] removeNodeByNameDP() {
        return new Object[][] {
                { "<root><a><b></b></a><c><b></b><b></b></c></root>", "b" }
        };
    }

    @Test(dataProvider = "removeNodeByNameAndValueDP")
    public void removeNodeByNameAndValue(String inputXml, String elementName, String elementValue)
            throws ParserConfigurationException, SAXException, IOException {
        XMLHelper helper = new XMLHelper();
        Document document = helper.convertToXmlDocument(inputXml);
        helper.removeNodeByNameAndValue(document, elementName, elementValue);
        String outputAfterRemovingElements = helper.getPrettyXml(document);
        Assert.assertFalse(outputAfterRemovingElements.contains(">" + elementValue + "<"));
    }

    @DataProvider
    private static Object[][] removeNodeByNameAndValueDP() {
        return new Object[][] {
                { "<root><a><b>test</b></a><c><b></b><b>test</b><b>test</b></c></root>", "b", "test" }
        };
    }

    @Test
    public void testRemoveNodeByXPath()
            throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        XMLHelper xmlHelper = new XMLHelper();
        String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><wtransaction><item><flags><flags4_SEND_MONEY>1</flags4_SEND_MONEY></flags></item></wtransaction>";
        Document inputXML = xmlHelper.convertToXmlDocument(inputString);
        xmlHelper.removeNodesByXPath(inputXML, "//wtransaction//flags4_SEND_MONEY");

        String outputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><wtransaction><item><flags></flags></item></wtransaction>";
        Document outputXML = xmlHelper.convertToXmlDocument(outputString);

        Diff diff = new Diff(inputXML, outputXML);
        diff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());
        Assert.assertEquals(diff.similar(), true);
    }

    @Test
    public void testIsXPathPresent()
            throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        XMLHelper xmlHelper = new XMLHelper();
        String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><wtransaction><item><flags><flags4_SEND_MONEY>1</flags4_SEND_MONEY></flags></item></wtransaction>";
        Document inputXML = xmlHelper.convertToXmlDocument(inputString);
        boolean result = xmlHelper.isXPathPresent(inputXML, "//wtransaction//flags4_SEND_MONEY");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsXPathPresentNegative()
            throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        XMLHelper xmlHelper = new XMLHelper();
        String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><wtransaction><item><flags><flags4_SEND_MONEY>1</flags4_SEND_MONEY></flags></item></wtransaction>";
        Document inputXML = xmlHelper.convertToXmlDocument(inputString);
        boolean result = xmlHelper.isXPathPresent(inputXML, "//wtransaction//flags5_XYZ");
        Assert.assertFalse(result);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetSubsetDocumentwithNull() {
        XMLHelper xmlHelper = new XMLHelper();
        xmlHelper.getSubsetDocument(null, null);
    }

    @Test
    public void testGetSubsetDocument()
            throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        XMLHelper xmlHelper = new XMLHelper();
        String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><participant_data_extensions repeating=\"true\" type=\"default-vo-class\"><item typesig=\"0\" type=\"Money::IPNPayDataVO\" id=\"483\"><credit_fee_amount type=\"Currency\">USD71</credit_fee_amount></item></participant_data_extensions>";
        Document inputXML = xmlHelper.convertToXmlDocument(inputString);
        Document doc = xmlHelper.getSubsetDocument(inputXML,
                "//participant_data_extensions/item[@type='Money::IPNPayDataVO']");
        Assert.assertNotNull(doc);

        inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><participant_data_extensions repeating=\"true\" type=\"default-vo-class\"></participant_data_extensions>";
        inputXML = xmlHelper.convertToXmlDocument(inputString);
        doc = xmlHelper.getSubsetDocument(inputXML, "//participant_data_extensions/item[@type='Money::IPNPayDataVO']");

        inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><item><participant_data_extensions repeating=\"true\" type=\"default-vo-class\"></participant_data_extensions><participant_data_extensions repeating=\"true\" type=\"default-vo-class\"></participant_data_extensions></item>";
        inputXML = xmlHelper.convertToXmlDocument(inputString);
        doc = xmlHelper.getSubsetDocument(inputXML, "//participant_data_extensions/item[@type='Money::IPNPayDataVO']");

        inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><participant_data_extensions repeating=\"true\" type=\"default-vo-class\"><item><credit_fee_amount type=\"Currency\">USD71</credit_fee_amount></item></participant_data_extensions>";
        inputXML = xmlHelper.convertToXmlDocument(inputString);
        doc = xmlHelper.getSubsetDocument(inputXML, "//participant_data_extensions/item[@type='Money::IPNPayDataVO']");

        inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><participant_data_extensions repeating=\"true\" type=\"default-vo-class\"><item typesig=\"0\" type=\"Money::IPNPayData\" id=\"483\"><credit_fee_amount type=\"Currency\">USD71</credit_fee_amount></item><item typesig=\"0\" type=\"Money::IPNPayDataVO\" id=\"483\"><credit_fee_amount type=\"Currency\">USD71</credit_fee_amount></item></participant_data_extensions>";
        inputXML = xmlHelper.convertToXmlDocument(inputString);
        doc = xmlHelper.getSubsetDocument(inputXML, "//participant_data_extensions/item[@type='Money::IPNPayDataVO']");

        inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><item></item>";
        inputXML = xmlHelper.convertToXmlDocument(inputString);
        doc = xmlHelper.getSubsetDocument(inputXML, "//participant_data_extensions/item[@type='Money::IPNPayDataVO']");
    }

    @Test
    public void testGetSubsetDocumentWithChildNode() throws IOException, SAXException, ParserConfigurationException,
            TransformerException, XPathExpressionException {
        XMLHelper xmlHelper = new XMLHelper();
        String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><async_tasks repeating=\"true\" type=\"default-vo-class\"><item typesig=\"0\" type=\"Money::IPNPayDataVO\" id=\"483\"><credit_fee_amount type=\"Currency\">USD71</credit_fee_amount></item><item><type>SETTLEMENT</type><json_request>\"sample\":{}</json_request></item></async_tasks>";
        Document inputXML = xmlHelper.convertToXmlDocument(inputString);
        Document doc = xmlHelper.getSubsetDocument(inputXML, "//async_tasks/item[type='SETTLEMENT']");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testGetSubsetDocumentWithAttribute() throws IOException, SAXException, ParserConfigurationException,
            TransformerException, XPathExpressionException {
        XMLHelper xmlHelper = new XMLHelper();
        String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><async_tasks repeating=\"true\" type=\"default-vo-class\"><item typesig=\"0\" type=\"Money::IPNPayDataVO\" id=\"483\"><credit_fee_amount type=\"Currency\">USD71</credit_fee_amount></item><item><type>SETTLEMENT</type><json_request>\"sample\":{}</json_request></item></async_tasks>";
        Document inputXML = xmlHelper.convertToXmlDocument(inputString);
        Document doc = xmlHelper.getSubsetDocument(inputXML, "//async_tasks/item[@type='Money::IPNPayDataVO']");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testReplaceAttributeValue() throws IOException, SAXException, ParserConfigurationException {
        XMLHelper xmlHelper = new XMLHelper();
        String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><async_tasks repeating=\"true\" type=\"default-vo-class\"><item typesig=\"0\" type=\"Money::IPNPayDataVO\" id=\"483\"><credit_fee_amount type=\"Currency\">USD71</credit_fee_amount></item><item><type>SETTLEMENT</type><json_request>\"sample\":{}</json_request></item></async_tasks>";
        Document inputXML = xmlHelper.convertToXmlDocument(inputString);
        Map<String, Map<String, String>> attributeValuesToBeReplaced = new HashMap<>();
        xmlHelper.replaceAttributeValue(attributeValuesToBeReplaced, inputXML);
    }

    @Test(enabled = false)
    public void prettyPrintVO() throws IOException, ParserConfigurationException, SAXException {
        String file = "<VO-FILE-PATH>";
        String fileContent = null;
        try (FileInputStream fis = new FileInputStream(new File(file))) {
            fileContent = new FileHelper().readContent(fis);
        }

        XMLHelper xmlHelper = new XMLHelper();
        Document inputXML = xmlHelper.convertToXmlDocument(fileContent);
        new FileHelper().writeToFile(file, xmlHelper.getPrettyXml(inputXML));
    }
}
