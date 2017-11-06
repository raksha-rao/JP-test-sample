package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.money.VirtualizeSchemaDTO;

public abstract class XmlVirtualizationTask<O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlVirtualizationTask.class);

    @Inject
    private FileHelper fileHelper;

    @Inject
    private XMLHelper xmlHelper;

    private VirtualizeSchemaDTO virtualizeSchemaDTO;

    private Map<String, Map<String, String>> transformValueMap = new HashMap<String, Map<String, String>>();;

    public VirtualizeSchemaDTO getVirtualizeSchemaDTO() {
        return virtualizeSchemaDTO;
    }

    // Added for unit testing; will be removed later when we refactor this class
    protected void setVirtualizeSchemaDTO(VirtualizeSchemaDTO virtualizeSchemaDTO) {
        this.virtualizeSchemaDTO = virtualizeSchemaDTO;
    }

    public abstract O
    execute(Document document);

    protected void populateVirtualizeSchema(String virtualizeSchemaFileLocation) throws IOException {
        String jsonContent = fileHelper.readContent(virtualizeSchemaFileLocation);
        ObjectMapper mapper = new ObjectMapper();
        this.virtualizeSchemaDTO = mapper.readValue(jsonContent, new TypeReference<VirtualizeSchemaDTO>() {
        });
    }

    /**
     * TODO:
     *
     * @param unsetFlags
     * @param inputDocument
     */
    protected void unsetFlags(Document inputDocument) {
        if (MapUtils.isEmpty(virtualizeSchemaDTO.getUnsetFlags()))
            return;
        for (Map.Entry<String, String> unsetFlag : virtualizeSchemaDTO.getUnsetFlags().entrySet()) {
            NodeList nodes = inputDocument.getElementsByTagName(unsetFlag.getKey());
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (!scalarNode(node)) {
                    continue;
                }
                Long oldValue = Long.valueOf(node.getTextContent());
                Long newValue = oldValue & ~Long.parseLong(unsetFlag.getValue());
                node.setTextContent(String.valueOf(newValue));
                LOGGER.debug("Founds unsetFlag {}:{} => {}", node.getNodeName(), oldValue, newValue);
            }
        }
    }

    /**
     * Handles scenario where wtransaction.flags has child nodes and wuser_holding.flags has numeric value  
     * @return
     */
    private static boolean scalarNode(Node node) {
        return node.hasChildNodes() && node.getFirstChild().getNodeType() == Node.TEXT_NODE;
    }

    protected void printTransformMapping() {
        if (MapUtils.isNotEmpty(transformValueMap))
            LOGGER.info("The mapping used for this virtualization: {}", this.transformValueMap.toString());
    }

    /**
     * Remove the node if it has 0 value
     *
     * @param ignoreZeroValueNodeList
     * @param inputDocument
     */
    protected void ignoreZeroValueNodes(Document inputDocument) {
        List<String> ignoreZeroValueNodeList = virtualizeSchemaDTO.getIgnoreZeroValueNodeList();
        if (CollectionUtils.isEmpty(ignoreZeroValueNodeList))
            return;
        for (String ignoreZeroValueNode : ignoreZeroValueNodeList) {
            xmlHelper.removeNodeByNameAndValue(inputDocument, ignoreZeroValueNode, "0");
        }
    }

    /**
     * Remove the node if it has no value
     *
     * @param ignoreEmptyNodeList
     * @param inputDocument
     */
    protected void ignoreEmptyNodes(Document inputDocument) {
        List<String> ignoreEmptyNodeList = virtualizeSchemaDTO.getIgnoreEmptyNodeList();
        if (CollectionUtils.isEmpty(ignoreEmptyNodeList))
            return;
        for (String ignoreEmptyNode : ignoreEmptyNodeList) {
            xmlHelper.removeNodeByNameAndValue(inputDocument, ignoreEmptyNode, StringUtils.EMPTY);
        }
    }

    /**
     * @param ignoreNodeList
     * @param inputDocument
     */
    protected void ignoreNodes(Document inputDocument) {
        List<String> ignoreNodeList = virtualizeSchemaDTO.getIgnoreNodeList();
        if (CollectionUtils.isEmpty(ignoreNodeList))
            return;
        for (String ignoreNode : ignoreNodeList) {
            xmlHelper.removeNodeByName(inputDocument, ignoreNode);
        }
    }

    protected void ignoreAttributes(Document inputDocument) {
        List<String> ignoreAttributeList = virtualizeSchemaDTO.getIgnoreAttributeList();
        if (CollectionUtils.isEmpty(ignoreAttributeList))
            return;
        xmlHelper.removeAttributes(ignoreAttributeList, inputDocument);
    }

    /**
     * Ignore nodes based on XPath
     *
     * @param virtualizeSchemaDTO
     * @param inputDocument
     */
    protected void ignoreNodesUsingXPath(Document inputDocument) {
        XPath xp = XPathFactory.newInstance().newXPath();
        for (String ignoreXPath : virtualizeSchemaDTO.getIgnoreXPath()) {
            try {
                NodeList nodes = (NodeList) xp.evaluate(ignoreXPath, inputDocument,
                        XPathConstants.NODESET);
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    node.getParentNode().removeChild(node);
                    LOGGER.debug("Remove node by name {} at path {}", node.getNodeName(), ignoreXPath);
                }
            } catch (XPathExpressionException e) {
                LOGGER.error("Exception occurred getting XPath:{}", ignoreXPath, e);
                throw new TestExecutionException(e);
            }
        }
    }

    /**
     * Transform nodes using element name
     *
     * @param virtualizeSchemaDTO
     * @param inputDocument
     */
    protected void transformNodes(Document inputDocument) {
        for (Map.Entry<String, String> transform : virtualizeSchemaDTO.getTransform().entrySet()) {
            NodeList nodes = inputDocument.getElementsByTagName(transform.getKey());
            String virtualCounterKeyName = transform.getValue();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String oldValue = element.getTextContent();
                    String newValue = getNewValue(virtualCounterKeyName, oldValue);
                    if (StringUtils.isEmpty(newValue)) {
                        LOGGER.debug("Found an empty new value during transformation, skipping the element {}",
                                node.getNodeName());
                        continue;
                    }
                    LOGGER.debug("Found transform {} {} {}", element.getNodeName(), oldValue, newValue);
                    element.setTextContent(newValue);
                }
            }
        }
    }

    /**
     * This method returns the new value for the old value that belongs to the particular 
     * item in the "transform" configuration
     * e.g. the "transform" configuration is
     * "account_number" : "virtual_account_number"
     * and the corresponding  "map" configuration is
     * "virtual_account_number" : 1000 
     * so in that case when we are virtualizing the data,
     * if the first instance of "account_number" element has the actual value of "1234"
     * then that "1234" needs to be transformed to "1000"
     * If the second instance of "account_number" element is also "1234" then that
     * also needs to be transformed to "1000" since they represent the same actual account number 
     * (let's say buyer acct number)
     * Now, if the third instance of "account_number" element has the actual value "5678" 
     * (this one is seller account number as opposed to the buyer account number [1234] ) 
     * In this case this element needs to be transformed to 1001 
     * since in the transformed file 1000 represents the earlier buyer account number 
     * To implement this logic, we are maintaining a 
     * {@link this.transformValueMap} a Map<String, Map<String, String>>
     * This is the example of an entry in this map
     * "virtual_account_number" : ["1234" : "1000", "5678" : "1001"] 
     * so for every transformation,  we lookup the old (actual) value 
     * in this map based on the virtual counter key name ("virtual_account_number") we are trying to use for the transformation. 
     * If the entry is not present, we add the entry so it is present next time we encounter the same
     * actual value for the given element name.
     * 
     * @param oldValue
     * @return
     */
    private String getNewValue(String virtualCounterKeyName,
            String oldValue) {
        if (StringUtils.isEmpty(oldValue))
            return null;
        String newValue;
        // virtualCounterKeyName is "virtual_account_number"
        // oldToNewValueMap is "1234" , "1000"
        Map<String, String> oldToNewValueMap = transformValueMap.get(virtualCounterKeyName);
        if (MapUtils.isNotEmpty(oldToNewValueMap)) {
            // There is at least one oldValue to newValue mapping present for the given element name 
            String assignedValue = oldToNewValueMap.get(oldValue);
            if (StringUtils.isNotEmpty(assignedValue))
                // The mapping is present for the given old value so use it and move on.
                newValue = assignedValue;
            else {
                // The mapping for the given old value is not present 
                // so we just update the map with the new oldValue:newValue mapping
                newValue = incrementAndGetNewValue(virtualCounterKeyName);
                oldToNewValueMap.put(oldValue, newValue);
            }
        } else {
            // No mapping is present for the element name so we create it and update the transformValueMap
            newValue = incrementAndGetNewValue(virtualCounterKeyName);
            updateTransformValueMap(oldValue, newValue, virtualCounterKeyName);
        }
        return newValue;
    }

    private void updateTransformValueMap(String oldValue, String newValue, String virtualCounterKeyName) {
        Map<String, String> oldToNewValueMap = new HashMap<String, String>();
        oldToNewValueMap.put(oldValue, newValue);
        transformValueMap.put(virtualCounterKeyName, oldToNewValueMap);
    }

    private String incrementAndGetNewValue(String virtualCounterKeyName) {
        // virtualCounterKeyValueMap is "virtual_account_number" , "1000"
        Map<String, String> virtualCounterKeyValueMap = virtualizeSchemaDTO.getMap();
        String existingValue = virtualCounterKeyValueMap.get(virtualCounterKeyName);
        if (StringUtils.isEmpty(existingValue))
            throw new TestExecutionException("Virtual_counter_key is invalid, no value found");
        String incrementedValue = String.valueOf((Integer.parseInt(existingValue) + 1));
        virtualCounterKeyValueMap.put(virtualCounterKeyName,
                incrementedValue);
        return incrementedValue;
    }

    /**
     * Transform nodes using XPATH
     *
     * @param virtualizeSchemaDTO
     * @param inputDocument
     */
    protected void transformNodesUsingXPath(Document inputDocument) {
        XPath xp = XPathFactory.newInstance().newXPath();
        for (Map.Entry<String, String> tranformXPath : virtualizeSchemaDTO.getTransformXPath().entrySet()) {
            String virtualCounterKeyName = tranformXPath.getValue();
            try {
                NodeList nodes = (NodeList) xp.evaluate(tranformXPath.getKey(), inputDocument, XPathConstants.NODESET);
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    String oldValue = node.getTextContent();
                    String newValue = getNewValue(virtualCounterKeyName, oldValue);
                    if (StringUtils.isEmpty(newValue)) {
                        LOGGER.debug("Found an empty new value during transformation, skipping the element {}",
                                node.getNodeName());
                        continue;
                    }
                    LOGGER.debug("Found tranform {} {} {}", node.getNodeName(), oldValue, newValue);
                    node.setTextContent(virtualizeSchemaDTO.getMap().get(virtualCounterKeyName));
                }
            } catch (XPathExpressionException e) {
                LOGGER.error("Exception occurred getting XPath:{}", tranformXPath.getKey(), e);
                throw new TestExecutionException(e);
            }
        }
    }

    /**
     * Sets the default value for an element, if element's value is null or empty
     *
     * @param virtualizeSchemaDTO
     * @param inputDocument
     */
    protected void setDefaultValueNodes(Document inputDocument) {
        for (Map.Entry<String, String> defaultValueEntry : virtualizeSchemaDTO.getDefaultValueMap().entrySet()) {
            NodeList nodes = inputDocument.getElementsByTagName(defaultValueEntry.getKey());
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String oldValue = element.getTextContent();
                    if (StringUtils.isEmpty(oldValue)) {
                        String newValue = virtualizeSchemaDTO.getDefaultValues().get(defaultValueEntry.getValue());
                        LOGGER.debug("Found defaultValueEntry {} {} {}", element.getNodeName(), oldValue, newValue);
                        element.setTextContent(newValue);
                    }
                }
            }
        }
    }

    /**
     * Set value if value is not empty
     *
     * @param virtualizeSchemaDTO
     * @param inputDocument
     */
    protected void setValueIfNonEmpty(Document inputDocument) {
        try {
            XPath xp = XPathFactory.newInstance().newXPath();
            for (Map.Entry<String, String> setIfNonEmpty : virtualizeSchemaDTO.getSetIfNonEmpty().entrySet()) {
                NodeList nodes = (NodeList) xp.evaluate(setIfNonEmpty.getKey(), inputDocument, XPathConstants.NODESET);

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    String oldValue = node.getTextContent();
                    String newValue = setIfNonEmpty.getValue();
                    LOGGER.debug("Setting value if non-empty {} {} {}", node.getNodeName(), oldValue, newValue);
                    node.setTextContent(newValue);
                }

            }
        } catch (XPathExpressionException e) {
            LOGGER.error("Exception getting XPATH:", e);
        }
    }
    
    /**
     * Set value if value is not empty
     *
     * @param virtualizeSchemaDTO
     * @param inputDocument
     */
    protected void setValueIfEmpty(Document inputDocument) {
        try {
            XPath xp = XPathFactory.newInstance().newXPath();
            for (Map.Entry<String, String> setIfEmpty : virtualizeSchemaDTO.getSetIfEmpty().entrySet()) {
                NodeList nodes = (NodeList) xp.evaluate(setIfEmpty.getKey(), inputDocument, XPathConstants.NODESET);

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    String oldValue = node.getTextContent();
                    String newValue = setIfEmpty.getValue();
                    if (oldValue == null) {
                        LOGGER.debug("Setting value if empty {} {} {}", node.getNodeName(), oldValue, newValue);
                        node.setTextContent(newValue);
                    }
                }

            }
        }
        catch (XPathExpressionException e) {
            LOGGER.error("Exception getting XPATH:", e);
        }
    }
}
