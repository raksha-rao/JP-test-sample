package com.jptest.payments.fulfillment.testonia.core.util.xml;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
//import com.jptest.vo.ValueObject;
//import com.jptest.vo.serialization.Formats;
//import com.jptest.vo.serialization.UniversalSerializer;

/**
 * 
 * @see //XMLHelperTest
 */
@Singleton
public class XMLHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLHelper.class);

    private static final String INDENT_YES = "yes";
    private static final String INDENT_AMOUNT_PROPERTY_KEY = "{http://xml.apache.org/xslt}indent-amount";
    private static final String INDENT_AMOUNT_PROPERTY_VALUE = "2";

    public Document convertToXmlDocument(String xmlContent)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

        try {
            removeWhitespaceOnlyTextNode(doc);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    /**
     * Since XML spec allows whitespace only nodes, it causes 2 issues when we read golden file into Document
     * <li>when we pretty print XML it prints empty lines.</li>
     * <li>XML diff tries to compare null node with empty-text node which results shows Diff</li>
     * <p>
     * To fix this, while generating Document, I am removing whitespace only nodes.
     *
     * @param doc
     * @throws XPathExpressionException
     */
    private void removeWhitespaceOnlyTextNode(Document doc) throws XPathExpressionException {
        NodeList nl = getNodesByXpath(doc, "//text()[normalize-space(.)='']");
        for (int i = 0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            node.getParentNode().removeChild(node);
        }
    }

    public String getPrettyXml(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, INDENT_YES);
            transformer.setOutputProperty(INDENT_AMOUNT_PROPERTY_KEY, INDENT_AMOUNT_PROPERTY_VALUE);
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            return writer.toString();
        } catch (TransformerFactoryConfigurationError | TransformerException e) {
            LOGGER.error("Exception pretty-printing XML", e);
        }
        return null;
    }

    /**
     * Converts the XML deserialized VO String into XML document.
     * 
     * @param valueObjects
     * @param xmlRoot
     * @return
     */
    public Document getXMLDocumentForDeserializedVOs(List<String> valueObjects, String xmlRoot) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = doc.createElement(xmlRoot);
            doc.appendChild(rootElement);

            for (String voString : valueObjects) {
                try {
                    Element childElement = convertToXmlDocument(voString).getDocumentElement();
                    rootElement.appendChild(doc.importNode(childElement, true));
                } catch (SAXException | IOException e) {
                    LOGGER.error("Exception occurred converting  VO to XML document", e);
                    throw new TestExecutionException("Exception occurred while converting to XML : " + e.getMessage());
                }
            }
            return doc;
        } catch (ParserConfigurationException e) {
            throw new TestExecutionException("Exception occurred during XML document construction", e);
        }
    }

    public void removeAttributes(List<String> attributesToBeRemoved, Document document) {
        NodeList nodeList = document.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NamedNodeMap attributes = element.getAttributes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    String attributeName = attributes.item(j).getNodeName();
                    if (attributesToBeRemoved.contains(attributeName)) {
                        ((Element) node).removeAttribute(attributeName);
                        j--;
                    }
                }
            }
        }
    }

    public void removeAttributes(String elementName, List<String> attributesToBeRemoved, Document document) {
        NodeList nodeList = document.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (elementName.equals(element.getNodeName())) {
                    NamedNodeMap attributes = element.getAttributes();
                    for (int j = 0; j < attributes.getLength(); j++) {
                        String attributeName = attributes.item(j).getNodeName();
                        if (attributesToBeRemoved.contains(attributeName)) {
                            ((Element) node).removeAttribute(attributeName);
                            j--;
                        }
                    }
                }
            }
        }
    }

    public void replaceAttributeValue(Map<String, Map<String, String>> attributeValuesToBeReplaced, Document document) {
        NodeList nodeList = document.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NamedNodeMap attributes = element.getAttributes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    String attributeName = attributes.item(j).getNodeName();
                    String attributeValue = attributes.item(j).getNodeValue();
                    if (attributeValuesToBeReplaced.containsKey(attributeName)
                            && attributeValuesToBeReplaced.get(attributeName).containsKey(attributeValue)) {
                        attributes.item(j)
                                .setNodeValue(attributeValuesToBeReplaced.get(attributeName).get(attributeValue));
                    }
                }
            }
        }
    }

    /**
     * This method will get content of an element from xml string giving tag name and element name and validate the value
     * sample node 
     * <post_ofac_decision id="20" type="Money::ComplianceTxnPostProcessDecisionVO">
     *     <txn_sanctions_decision type="String">IGNORED</txn_sanctions_decision>
     * </post_ofac_decision>
     * @param xmlString
     * @param tagName
     * @param eleName
     * @param //value
     * @return
     * @throws Exception
     */
    public String getResultByTagElementName(String xmlString, String tagName, String eleName) throws Exception {
        String result = null;
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        String utfStr = new String(xmlString.toString().getBytes(), StandardCharsets.UTF_8);

        // convert xml string into byte array
        ByteArrayInputStream stream = new ByteArrayInputStream(utfStr.getBytes());
        Document doc = db.parse(stream);

        NodeList nodes = doc.getElementsByTagName(tagName);

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);

            NodeList name = element.getElementsByTagName(eleName);
            Element line = (Element) name.item(0);
            result = getCharacterDataFromElement(line);

            LOGGER.info("actual value : {}", result);
        }

        return result;
    }

    /**
     * This method get the content of element from xml string giving tag name and validate the value
     * sample node 
     * <post_auth_risk_decline type="sint32">0</post_auth_risk_decline>
     * 
     * @param xmlString
     * @param tagName
     * @param //value
     * @return
     * @throws Exception
     */
    public String getResultByTagName(String xmlString, String tagName) throws Exception {
        String result = null;
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        // convert xml string into byte array
        ByteArrayInputStream stream = new ByteArrayInputStream(xmlString.getBytes());
        Document doc = db.parse(stream);

        NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            Element element = (Element) nodes.item(0);
            LOGGER.info("element node name = {}, txt content = {}", element.getNodeName(), element.getTextContent());
            result = element.getTextContent();
        }
        return result;
    }

    private String getCharacterDataFromElement(Element slement) {
        Node child = slement.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return child.getNodeValue();
    }

    public void removeNodeByName(Document document, String elementName) {
        NodeList nodes = document.getElementsByTagName(elementName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                LOGGER.debug("Deleting node {} {}", element.getNodeName(), element.getTextContent());
                element.getParentNode().removeChild(element);
                i--;
            }
        }
    }

    public void removeNodeByNameAndValue(Document document, String elementName, String elementValue) {
        NodeList nodes = document.getElementsByTagName(elementName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String oldValue = element.getTextContent();
                if (elementValue.equals(oldValue)) {
                    LOGGER.debug("Deleting node {} with value {}", element.getNodeName(), element.getTextContent());
                    element.getParentNode().removeChild(element);
                    i--;
                }
            }
        }
    }

    public void removeNodesByXPath(Document document, String xPath) throws XPathExpressionException {
        NodeList nodeList = getNodesByXpath(document, xPath);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            node.getParentNode().removeChild(node);
        }
    }

    public boolean isXPathPresent(Document document, String xPath) throws XPathExpressionException {
        NodeList nodeList = getNodesByXpath(document, xPath);
        return !(nodeList == null || nodeList.getLength() == 0);
    }

    /**
     * Gets the subset of the document by returning the first instance of the node that 
     * satisfies the xpathCriteria as a document.
     * @param source
     * @param xpathCriteria
     * @return
     */
    public Document getSubsetDocument(Document source, String xpathCriteria) {
        try {
            NodeList nodes = getNodesByXpath(source, xpathCriteria);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document newDocument = builder.newDocument();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Node importNode = newDocument.importNode(node, true);
                    newDocument.appendChild(importNode);
                    break;
                }
            }
            return newDocument;
        } catch (XPathExpressionException | ParserConfigurationException e) {
            LOGGER.warn("Couldn't get a subset of the document for {}", xpathCriteria);
            return null;
        }
    }

    public NodeList getNodesByXpath(Document source, String xpathCriteria) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile(xpathCriteria);
        NodeList nodes = (NodeList) expr.evaluate(source, XPathConstants.NODESET);
        return nodes;
    }

    public byte[] convertXMLToByteArr(Document doc) {
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        byte[] result;

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            // for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            StreamResult toByteAr = new StreamResult(resultStream);

            // write data
            transformer.transform(source, toByteAr);

        } catch (TransformerException e) {
            LOGGER.warn("Could not convert given xml to byte array due to " + e.getMessage(), e);
            return null;
        }
        result = resultStream.toByteArray();
        return result;
    }

    public void writeXmlToFile(String filePath, Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            // for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            // write to console or file
            // StreamResult console = new StreamResult(System.out);

            File file = new File(filePath);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                // if parent directory does not exist create it
                if (!file.getParentFile().exists()) {
                    boolean dirCreated = file.getParentFile().mkdirs();
                    if (!dirCreated) {
                        LOGGER.warn("Unable to create new directory");
                    }
                }
                boolean fileCreated = file.createNewFile();
                if (!fileCreated) {
                    LOGGER.warn("Unable to create new file");
                }
            }
            StreamResult toFile = new StreamResult(file);

            // write data
            // transformer.transform(source, console);
            transformer.transform(source, toFile);

        } catch (TransformerException | IOException e) {
            LOGGER.warn("Could not write given xml to file due to " + e.getMessage(), e);
        }
    }

   /* public Document convertVoToXmlDocument(final ValueObject vo)
            throws IOException, ParserConfigurationException, SAXException {
        if (vo == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        UniversalSerializer serializer = new UniversalSerializer(Formats.XML, false, false, false);
        serializer.serialize(vo, os);
        return convertToXmlDocument(new String(os.toByteArray(), "UTF-8"));
    }*/
}
