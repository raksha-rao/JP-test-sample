package com.jptest.payments.fulfillment.testonia.core.util.xml.diff;

import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
//import com.jptest.payments.fulfillment.testonia.core.util.xml.XpathNodeRemover;
import org.apache.commons.io.FilenameUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

@Singleton
public class XMLDiffHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLDiffHelper.class);

    private static final String IGNORABLE_DIFFERENCE = "Expected sequence of child nodes";

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private FileHelper fileHelper;

  /*  @Inject
    private XpathNodeRemover nodeRemover;*/

    public XmlDiff performDiff(Document actualXML, Document goldenXML, Set<String> xpaths, String outputLocation) {
      /*  if (!xpaths.isEmpty()) {
            actualXML = nodeRemover.remove(actualXML, xpaths);
            goldenXML = nodeRemover.remove(goldenXML, xpaths);
        }
*/
        concatTextNodes(actualXML.getChildNodes());
        concatTextNodes(goldenXML.getChildNodes());

        // This is fail safe, so if the outputlocation doesn't have a filename with extension, this doesn't fail
        outputLocation = FilenameUtils.removeExtension(outputLocation);

        String actualFileLocation = outputLocation + "-actual.xml";
        String goldenFileLocation = outputLocation + "-golden.xml";

        storeXmlOnFileSystem(actualXML, actualFileLocation);
        storeXmlOnFileSystem(goldenXML, goldenFileLocation);
        return getDiff(goldenXML, actualXML, goldenFileLocation, actualFileLocation);
    }

    /**
     * Performs concatenation of content TextNode, which is spread across multiple lines.
     *
     * @param list NodeList
     */
    private void concatTextNodes(NodeList list) {
        if (list == null) {
            return;
        }
        StringBuilder textValue = new StringBuilder();
        int textNodeIdx = 0;
        for (int i = 0; i < list.getLength(); ++i) {
            org.w3c.dom.Node n = list.item(i);
            if (n.getNodeType() == Node.TEXT_NODE) {
                if (textValue.length() == 0) {
                    textNodeIdx = i;
                    textValue.append(n.getNodeValue());
                } else {
                    if (n.getNodeValue() != null) {
                        textValue.append(n.getNodeValue());
                        n.setNodeValue("");
                    }
                }
            }
            concatTextNodes(n.getChildNodes());
        }
        if (textValue.length() > 0) {
            list.item(textNodeIdx).setNodeValue(textValue.toString());
        }
    }

    public void storeXmlOnFileSystem(Document xmlDoc, String fileLocation) {
        String prettyXml = xmlHelper.getPrettyXml(xmlDoc);
        fileHelper.writeToFile(fileLocation, prettyXml);
    }

    protected XmlDiff getDiff(Document goldenXML, Document actualXML, String goldenFileLocation,
            String actualFileLocation) {
        XmlDiff diff = new XmlDiff(goldenXML, actualXML, goldenFileLocation, actualFileLocation);
        diff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());
        return diff;
    }

    /**
     * Loop through all the differences and prints it to console.
     *
     * @param myDiff
     *            //{@link org.custommonkey.xmlunit.Diff} type object.
     */
    public void displayDiffs(Diff myDiff) {
        DetailedDiff detDiff = new DetailedDiff(myDiff);
        Difference difference = null;
        @SuppressWarnings("rawtypes")
        List differences = detDiff.getAllDifferences();
        for (Object object : differences) {
            difference = (Difference) object;
            if (!difference.toString().contains(IGNORABLE_DIFFERENCE)) {
                LOGGER.warn("Difference => {}", difference.toString());
            }
        }
    }

    /**
     * Loop through all the differences and returns them as a string
     *
     * @param myDiff
     *            {@link org.custommonkey.xmlunit.Diff} type object.
     */
    public String getDiffs(Diff myDiff) {
        StringBuilder diffMessages = new StringBuilder();
        DetailedDiff detDiff = new DetailedDiff(myDiff);
        Difference difference = null;
        @SuppressWarnings("rawtypes")
        List differences = detDiff.getAllDifferences();
        for (Object object : differences) {
            difference = (Difference) object;
            if (!difference.toString().contains(IGNORABLE_DIFFERENCE)) {
                diffMessages.append(difference.toString()).append("\n");
            }
        }
        return diffMessages.toString();
    }

}
