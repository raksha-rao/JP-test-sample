package com.jptest.payments.fulfillment.testonia.core.util.xml.diff;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.custommonkey.xmlunit.Diff;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLDiffHelperTest {
    private final String XML_STRING1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><items><item>a</item><item>b</item></items>";
    private final String XML_STRING2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><items><item>b</item><item>a</item></items>";

    private Document getXMLDocumentFromString(String xmlString)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlString));
        return dBuilder.parse(is);
    }

    @Test
    public void testOrderingOfNodes() throws ParserConfigurationException, SAXException, IOException {
        Document xmlDocument1 = getXMLDocumentFromString(XML_STRING1);
        Document xmlDocument2 = getXMLDocumentFromString(XML_STRING2);

        XMLDiffHelper xmlDiffHelper = new XMLDiffHelper();
        Diff diff = xmlDiffHelper.getDiff(xmlDocument1, xmlDocument2, null, null);
        Assert.assertEquals(diff.similar(), true);
    }

    @Test
    public void testGetDiffs() throws Exception {
        Document xmlDocument1 = getXMLDocumentFromString(XML_STRING1);
        Document xmlDocument2 = getXMLDocumentFromString(XML_STRING2);
        Diff diff = new Diff(xmlDocument1, xmlDocument2);
        XMLDiffHelper xmlDiffHelper = new XMLDiffHelper();
        String diffMessages = xmlDiffHelper.getDiffs(diff);
        xmlDiffHelper.displayDiffs(diff);
        Assert.assertNotNull(diffMessages);
    }
}
