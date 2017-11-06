package com.jptest.payments.fulfillment.testonia.model.util.jaxb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.jptest.vo.ValueObject;
import com.jptest.vo.serialization.Formats;
import com.jptest.vo.serialization.UniversalSerializer;

/**
 * JAXB annotation to convert ValueObject to XML
 */
public class JAXBVOToXMLAdapter extends XmlAdapter<Object, ValueObject> {

    @Override
    public ValueObject unmarshal(Object element) throws Exception {
        throw new UnsupportedOperationException("Unmarshalling to VO is not supported");
    }

    @Override
    public Object marshal(ValueObject valueObject) throws Exception {
        return convertToXmlDocument(
                new String(serializeValueObject(valueObject, Formats.XML, false), StandardCharsets.UTF_8))
                        .getDocumentElement();
    }

    /**
     * @see XMLHelper.convertToXmlDocument()
     */
    private static Document convertToXmlDocument(String xmlContent)
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
     * @see XMLHelper.removeWhitespaceOnlyTextNode()
     */
    private static void removeWhitespaceOnlyTextNode(Document doc) throws XPathExpressionException {
        XPath xp = XPathFactory.newInstance().newXPath();
        NodeList nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", doc, XPathConstants.NODESET);

        for (int i = 0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            node.getParentNode().removeChild(node);
        }
    }

    /**
     * @see VoHelper.serializeValueObject()
     */
    private static byte[] serializeValueObject(ValueObject vo, Formats format, boolean compress) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new UniversalSerializer(format, false, compress, false).serialize(vo, os);
        return os.toByteArray();
    }

}
