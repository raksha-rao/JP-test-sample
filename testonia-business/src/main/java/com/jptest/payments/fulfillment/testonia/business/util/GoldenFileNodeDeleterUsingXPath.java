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
public class GoldenFileNodeDeleterUsingXPath {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoldenFileNodeDeleterUsingXPath.class);

    public static void main(String[] args) throws IOException, InterruptedException {
    	deleteNodeFromXmlUsingXPath();
    }

    public static void deleteNodeFromXmlUsingXPath() throws IOException {
        String inputPath = "/Users/saurijoshi/Documents/gitrepo/txnfulfillmentserv/functional-tests/src/test/resources/golden";
		
        List<String> nodesToBeRemoved = Arrays.asList(
                "//validation_fixture/engine/clearing_journal_entry/item[*]/trans_amount",
                "//validation_fixture/engine/fees_charged_journal_entry/item[*]/data",
                "//validation_fixture/engine/fees_charged_journal_entry/item[*]/fee_amount",
                "//validation_fixture/engine/fees_charged_journal_entry/item[*]/fee_amount_usd",
                "//validation_fixture/engine/financial_journal_2/item[*]/amount",
                "//DequeueFixture_ResultVO/Money_RefundPostProcessHandlerRequest/refund_post_process_message/holds_message/amount",
                "//DequeueFixture_ResultVO/Money_PaymentCreditPostprocessRequest/payment_credit_postprocess_message/amount_credited_to_recipient_balance",
                "//DequeueFixture_ResultVO/Money_PaymentCreditPostprocessRequest/payment_credit_postprocess_message/funding_info/credit_fee_amount_usd",
                "//DequeueFixture_ResultVO/Money_PaymentCreditPostprocessRequest/payment_credit_postprocess_message/funding_info/credit_fee_amount",
                "//DequeueFixture_ResultVO/Money_PaymentDebitPostprocessRequest/payment_debit_postprocess_message/funding_info/credit_fee_amount",
                "//DequeueFixture_ResultVO/Money_PaymentDebitPostprocessRequest/payment_debit_postprocess_message/funding_info/credit_fee_amount_usd",
                "//DequeueFixture_ResultVO/Money_AchDepositCompletedPostprocessRequest/ach_deposit_completed_postprocess/sender_transaction/amount_usd",
                "//DequeueFixture_ResultVO/Money_BankCompletionPostProcessHandlerRequest/bank_completion_post_process_message/transaction_amount_in_usd",
        		
        		"//IPNFixture_ResultVO/ipn_values/mc_fee",
        		"//IPNFixture_ResultVO/ipn_values/payment_fee"
        		
        		);
        for (String nodeToBeRemoved : nodesToBeRemoved) {
            updateGoldenFilesToDeleteNodeUsingXPath(inputPath, nodeToBeRemoved);
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

    private static void updateGoldenFilesToDeleteNodeUsingXPath(String inputPath, String inputNode)
            throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(inputPath))) {
            paths
                    .filter(filePath -> filePath.toFile().getAbsolutePath().endsWith(".xml"))
                    .forEach(filePath -> {
                        if (Files.isRegularFile(filePath)) {
                            try {
                                updateIfFileContainsXPathNode(filePath, inputNode);
                            } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
                                LOGGER.error("Error occurred updating file {}. Ignoring file => ", filePath, e);
                            }
                        }
                    });
        }
    }

    private static void updateIfFileContainsXPathNode(Path filePath, String inputExpandedFlagName)
            throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        Document document = new XMLHelper().convertToXmlDocument(content);
        removeAndUnsetXPathNodeIfPresent(filePath, document, inputExpandedFlagName);
    }

    private static void removeAndUnsetXPathNodeIfPresent(Path filePath, Document document, String inputNode)
            throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        boolean isFileUpdated = false;
        XPath xp = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xp.evaluate(inputNode, document, XPathConstants.NODESET);
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Node parentNode = element.getParentNode();
                LOGGER.debug("[{}] Deleting node {} of expression {}.", filePath, element.getNodeName(), inputNode);
                parentNode.removeChild(element);
                isFileUpdated = true;
            }
        }

        if (isFileUpdated) {
            Files.write(filePath, new XMLHelper().getPrettyXml(document).getBytes(StandardCharsets.UTF_8));
            LOGGER.info("*************************************************");
        }
    }
}
