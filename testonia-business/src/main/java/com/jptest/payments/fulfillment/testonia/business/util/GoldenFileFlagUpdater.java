package com.jptest.payments.fulfillment.testonia.business.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;

/**
 * Utility to update all golden files within input directory, if the golden file contains input expanded-flag.
 * 
 * Performs below 3 operations:
 * <li>Remove expanded flag from golden file(e.g. flags3_EXEMPT_NON_CC_PORTION_FROM_LIMIT from PaymentValidation-sender.xml)</li>
 * <li>Unset the flag bit value in the same file (e.g. subtract 131072 from corresponding "flags3" element in PaymentValidation-sender.xml)</li>
 * <li>Unset the flag bit value in corresponding payload file (e.g. subtract 131072 from corresponding "transaction_flags3" element 
 * in PayloadValidation.xml file within same directory)</li>
 * 
 * <p>
 * It should be executed as a stand alone application. Pass 3 parameters as program argument e.g. 
 * /Users/saurijoshi/Documents/gitrepo/fulfillment-stf/src/test/resources/golden flags3_EXEMPT_NON_CC_PORTION_FROM_LIMIT 131072
 * 
 * @see GoldenFileNodeDeleter
 */
public class GoldenFileFlagUpdater {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoldenFileFlagUpdater.class);

    private static final String PAYLOAD_VALIDATION_FILE = "PayloadValidation.xml";

    public static void main(String[] args) throws IOException, InterruptedException {
        updateHardCodedFlags();
        //updateFlagsFromInput(args);
    }

    public static void updateFlagsFromInput(String[] args) throws IOException, InterruptedException {
        if (args.length < 3) {
            LOGGER.error("Please provide 3 input parameters. <INPUT_PATH> <EXPANDED_FLAG_NAME> <FLAG_VALUE>");
            LOGGER.error("Sample usage: . flags3_EXEMPT_NON_CC_PORTION_FROM_LIMIT 131072");
            return;
        }

        updateGoldenFiles(args[0], args[1], args[2]);
    }

    public static void updateHardCodedFlags() throws IOException, InterruptedException {
        String inputPath = "C:/vmulay-txn/stfmigration/tefulfillment/src/test/resources/golden";
        //        List<List<String>> inputFlagList = Arrays.asList(
        //                Arrays.asList(
        //                        "flags3_EXEMPT_NON_CC_PORTION_FROM_LIMIT", "131072"),
        //                Arrays.asList(
        //                        "flags3_jptest_BUYER_PROTECTION", "32768"),
        //                Arrays.asList(
        //                        "flags5_ITEM_NOT_RECEIVED_SELLER_PROTECTION_ELIGIBLE", "16"),
        //                Arrays.asList(
        //                        "flags5_CHEETAH_PROCESSED", "18014398509481984"),
        //                Arrays.asList("flags1_PAYMENTSERV_PROCESSED", "4"),
        //                Arrays.asList("flags6_IS_MILLENNIUM_SETTLEMENT_PROCESSED", "2048"),
        //                Arrays.asList("flags6_IS_FLEX_PROCESSED", "65536"));
        List<List<String>> inputFlagList = Arrays.asList(
                Arrays.asList(
                        "flags5_UNAUTH_SELLER_PROTECTION_ELIGIBLE", "32"),
                Arrays.asList(
                        "flags1_BUYER_GUARANTEE", "64"));

        for (List<String> inputFlag : inputFlagList) {
            updateGoldenFiles(inputPath, inputFlag.get(0), inputFlag.get(1));
        }
    }

    /**
     * Iterate over all XML files within directory specified by inputPath
     *  
     * @param inputPath
     * @param inputExpandedFlagName
     * @param inputFlagValue
     * @throws IOException
     */
    private static void updateGoldenFiles(String inputPath, String inputExpandedFlagName, String inputFlagValue)
            throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(inputPath))) {
            paths
                    .filter(filePath -> filePath.toFile().getAbsolutePath().endsWith(".xml"))
                    .forEach(filePath -> {
                        if (Files.isRegularFile(filePath)) {
                            try {
                                updateIfFileContainsExpandedFlag(filePath, inputExpandedFlagName, inputFlagValue);
                            } catch (ParserConfigurationException | SAXException | IOException e) {
                                LOGGER.error("Error occurred updating file {}. Ignoring file => ", filePath, e);
                            }
                        }
                    });
        }
    }

    /**
     * Read file specified by filePath, convert it to XML Document
     * 
     * @param filePath
     * @param inputExpandedFlagName
     * @param inputFlagValue
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private static void updateIfFileContainsExpandedFlag(Path filePath, String inputExpandedFlagName,
            String inputFlagValue)
                    throws IOException, ParserConfigurationException, SAXException {
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        Document document = new XMLHelper().convertToXmlDocument(content);
        removeAndUnsetFlagValueIfPresent(filePath, document, inputExpandedFlagName, inputFlagValue);
    }

    /**
     * If document contains inputExpandedFlagName element, then remove the element 
     * and rewrite the file with updated document
     * 
     * @param filePath
     * @param document
     * @param inputExpandedFlagName
     * @param inputFlagValue
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static void removeAndUnsetFlagValueIfPresent(Path filePath, Document document, String inputExpandedFlagName,
            String inputFlagValue) throws ParserConfigurationException, SAXException, IOException {
        boolean isFileUpdated = false;
        String flagGroup = getFlagGroup(inputExpandedFlagName);
        NodeList nodes = document.getElementsByTagName(inputExpandedFlagName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Node expandedFlagNode = element.getParentNode();
                updateFlagGroupValueInAllRelevantFiles(filePath, expandedFlagNode, flagGroup, inputFlagValue);
                LOGGER.debug("[{}] Deleting expanded flag {}.", filePath, element.getNodeName());
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

    /**
     * Unset flag bit value in updated file
     * 
     * @param filePath
     * @param expandedFlagNode
     * @param flagGroup
     * @param inputFlagValue
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static void updateFlagGroupValueInAllRelevantFiles(Path filePath, Node expandedFlagNode, String flagGroup,
            String inputFlagValue) throws ParserConfigurationException, SAXException, IOException {
        NodeList elementList = expandedFlagNode.getParentNode().getChildNodes();
        for (int j = 0; j < elementList.getLength(); j++) {
            Node elementNode = elementList.item(j);
            if (flagGroup.equals(elementNode.getNodeName())) {
                Long originalValue = Long.valueOf(elementNode.getTextContent());
                Long newValue = originalValue & ~Long.parseLong(inputFlagValue);
                // first update payload file, if it fails then drop subsequent changes
                updateFlagInPayloadGoldenFile(filePath, flagGroup, originalValue, newValue);
                // now update current file
                elementNode.setTextContent(String.valueOf(newValue));
                LOGGER.debug("[{}] Updated flag value {} with value {} for {} .", filePath, originalValue,
                        newValue, elementNode.getNodeName());

            }
        }
    }

    /**
     * Unset flag bit value in corresponding payload file
     *  
     * @param filePath
     * @param flagGroup
     * @param originalValue
     * @param newValue
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static void updateFlagInPayloadGoldenFile(Path filePath, String flagGroup, Long originalValue,
            Long newValue)
                    throws ParserConfigurationException, SAXException, IOException {
        boolean isFileUpdated = false;
        String payloadFlag = getPayloadFlag(flagGroup);
        Path payloadValidationFilePath = null;
        try {
            payloadValidationFilePath = filePath.resolveSibling(PAYLOAD_VALIDATION_FILE);
        } catch (InvalidPathException e) {
            LOGGER.warn("No {} found for path {}. Ignoring.", PAYLOAD_VALIDATION_FILE, filePath);
            return;
        }
        if (!Files.exists(payloadValidationFilePath)) {
            LOGGER.warn("No {} found for path {}. Ignoring.", PAYLOAD_VALIDATION_FILE, filePath);
            return;
        }
        String content = new String(Files.readAllBytes(payloadValidationFilePath), StandardCharsets.UTF_8);
        XMLHelper xmlHelper = new XMLHelper();
        Document document = xmlHelper.convertToXmlDocument(content);
        NodeList nodes = document.getElementsByTagName(payloadFlag);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String originalValueStr = String.valueOf(originalValue);
            if (originalValueStr.equals(node.getTextContent())) {
                node.setTextContent(String.valueOf(newValue));
                isFileUpdated = true;
                LOGGER.debug("[{}] Updated flag value {} with value {} for {}.", payloadValidationFilePath,
                        originalValue, newValue, node.getNodeName());
            }
        }
        if (isFileUpdated) {
            Files.write(payloadValidationFilePath,
                    new XMLHelper().getPrettyXml(document).getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * For input flags3_EXEMPT_NON_CC_PORTION_FROM_LIMIT, the method returns flag3.
     * 
     * @param inputExpandedFlagName
     * @return
     */
    private static String getFlagGroup(String inputExpandedFlagName) {
        return inputExpandedFlagName.split("_")[0];
    }

    /**
     * Converts flag to corresponding payload flag
     * e.g.
     * 
     * flags1 -> transaction_flags
     * flags2 -> transaction_flags2
     * flags3 -> transaction_flags3
     * flags4 -> transaction_flags4
     * flags5 -> transaction_flags5
     * flags6 -> transaction_flags6
     * 
     * @param flagGroup
     * @return
     */
    private static String getPayloadFlag(String flagGroup) {
        if (flagGroup.equals("flags1")) {
            flagGroup = "flags";
        }
        return "transaction_" + flagGroup;
    }
}
