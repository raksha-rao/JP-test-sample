package com.jptest.payments.fulfillment.testonia.business.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;

/**
 * Utility to delete input nodes from all xml files of input directory
 * 
 * @see GoldenFileFlagUpdater
 */
public class GoldenFileNodeDeleter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoldenFileNodeDeleter.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        deleteNodeFromXml();
    }

    public static void deleteNodeFromXml() throws IOException {
        String inputPath = "C:/vmulay-txn/txnfulfillmentserv/functional-tests/src/test/resources/golden";
        //        List<String> nodesToBeRemoved = Arrays.asList("balance_at_time_created");
        //        List<String> nodesToBeRemoved = Arrays.asList("payer_status");
        List<String> nodesToBeRemoved = Arrays.asList("payment_extensions");
        for (String nodeToBeRemoved : nodesToBeRemoved) {
            updateGoldenFilesToDeleteNode(inputPath, nodeToBeRemoved);
        }

    }

    public static void deleteChildrenAndUpdateValue() throws IOException {
        String inputPath = "C:/vmulay-txn/stfmigration/tefulfillment/src/test/resources/golden";
        //        List<String> nodesToBeRemoved = Arrays.asList("balance_at_time_created");
        List<String> nodesToBeRemoved = Arrays.asList("jptest_account_info");

        for (String nodeToBeRemoved : nodesToBeRemoved) {
            updateGoldenFilesToReplaceValue(inputPath, nodeToBeRemoved);
        }

    }

    private static void updateGoldenFilesToReplaceValue(String inputPath, String inputNode)
            throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(inputPath))) {
            paths
                    .filter(filePath -> filePath.toFile().getAbsolutePath().endsWith(".xml"))
                    .forEach(filePath -> {
                        if (Files.isRegularFile(filePath)) {
                            try {
                                updateIfFileContainsNodeToBeReplaced(filePath, inputNode);
                            } catch (ParserConfigurationException | SAXException | IOException e) {
                                LOGGER.error("Error occurred updating file {}. Ignoring file => ", filePath, e);
                            }
                        }
                    });
        }
    }

    private static void updateIfFileContainsNodeToBeReplaced(Path filePath, String inputExpandedFlagName)
            throws IOException, ParserConfigurationException, SAXException {
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        Document document = new XMLHelper().convertToXmlDocument(content);
        replaceNodeIfPresent(filePath, document, inputExpandedFlagName);
    }

    private static void replaceNodeIfPresent(Path filePath, Document document, String inputNode)
            throws ParserConfigurationException, SAXException, IOException {
        boolean isFileUpdated = false;
        NodeList nodes = document.getElementsByTagName(inputNode);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String newValue = "present";
            node.setTextContent(newValue);
            isFileUpdated = true;
        }

        if (isFileUpdated) {
            Files.write(filePath, new XMLHelper().getPrettyXml(document).getBytes(StandardCharsets.UTF_8));
            LOGGER.info("*************************************************");
        }
    }

    /**
     * Set value if value is not empty
     *
     * @param virtualizeSchemaDTO
     * @param inputDocument
     */
    protected void setValueIfNonEmpty(Document inputDocument, String nodeName, String newVal) {
        try {
            XPath xp = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xp.evaluate(nodeName, inputDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String newValue = newVal;
                node.setTextContent(newValue);
            }

        } catch (XPathExpressionException e) {
            LOGGER.error("Exception getting XPATH:", e);
        }
    }

    private static void updateGoldenFilesToDeleteNode(String inputPath, String inputNode)
            throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(inputPath))) {
            paths
                    .filter(filePath -> filePath.toFile().getAbsolutePath().endsWith(".xml"))
                    .forEach(filePath -> {
                        if (Files.isRegularFile(filePath)) {
                            try {
                                updateIfFileContainsNode(filePath, inputNode);
                            } catch (ParserConfigurationException | SAXException | IOException e) {
                                LOGGER.error("Error occurred updating file {}. Ignoring file => ", filePath, e);
                            }
                        }
                    });
        }
    }

    private static void updateIfFileContainsNode(Path filePath, String inputExpandedFlagName)
            throws IOException, ParserConfigurationException, SAXException {
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        Document document = new XMLHelper().convertToXmlDocument(content);
        removeAndUnsetNodeIfPresent(filePath, document, inputExpandedFlagName);
    }

    private static void removeAndUnsetNodeIfPresent(Path filePath, Document document, String inputNode)
            throws ParserConfigurationException, SAXException, IOException {
        boolean isFileUpdated = false;
        NodeList nodes = document.getElementsByTagName(inputNode);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Node expandedFlagNode = element.getParentNode();
                LOGGER.debug("[{}] Deleting node {}.", filePath, element.getNodeName());
                expandedFlagNode.removeChild(element);
                i--;
                isFileUpdated = true;
            }
        }

        if (isFileUpdated) {
            Files.write(filePath, new XMLHelper().getPrettyXml(document).getBytes(StandardCharsets.UTF_8));
            LOGGER.info("*************************************************");
        }
    }
}
