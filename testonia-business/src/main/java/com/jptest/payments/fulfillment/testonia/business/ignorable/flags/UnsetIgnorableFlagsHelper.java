package com.jptest.payments.fulfillment.testonia.business.ignorable.flags;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.FlagsInfo;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.IgnorableFlags;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WTransactionIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WUserHoldingIgnorableFlagsHelper;


/**
 * This Class unsets the ignorable flags from flag values and removes the ignorable
 * expanded flags from Actual Document
 * 
 * @see UnsetIgnorableFlagsHelperTest
 * 
 * @JP Inc.
 */
public class UnsetIgnorableFlagsHelper {

    @Inject
    private Configuration configuration;

    @Inject
    private FileHelper fileHelper;

    @Inject
    private WTransactionIgnorableFlagsHelper wTransactionIgnorableFlagsHelper;
    private final String WTRANSACTION_FLAG_TYPE = "wtransaction";

    @Inject
    private WUserHoldingIgnorableFlagsHelper wUserHoldingIgnorableFlagsHelper;
    private final String WUSER_HOLDING_FLAG_TYPE = "wuser_holding";

    public UnsetIgnorableFlagsHelper() {
        super();
    }

    /*
     * For testing purpose
     */
    protected UnsetIgnorableFlagsHelper(Configuration configuration, FileHelper fileHelper,
            WTransactionIgnorableFlagsHelper wTransactionIgnorableFlagsHelper,
            WUserHoldingIgnorableFlagsHelper wUserHoldingIgnorableFlagsHelper) {
        this.configuration = configuration;
        this.fileHelper = fileHelper;
        this.wTransactionIgnorableFlagsHelper = wTransactionIgnorableFlagsHelper;
        this.wUserHoldingIgnorableFlagsHelper = wUserHoldingIgnorableFlagsHelper;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UnsetIgnorableFlagsHelper.class);

    public void unsetFlags(String ignorableFlagsLocation, Document document) {

        IgnorableFlags ignorableFlags = getIgnorableFlags(ignorableFlagsLocation);

        if (document == null) {
            LOGGER.warn("XML is null");
            return;
        }

        if (ignorableFlags.getFlagsInfo() == null) {
            LOGGER.warn("No Flags to unset");
            return;
        }

        unsetIgnorableFlagsValue(ignorableFlags, document);
        removeExpandedFlags(ignorableFlags, document);
    }

    protected IgnorableFlags getIgnorableFlags(String ignorableFlagsLocation) {
        /*
         * Falling back to default if ignorable flags location passed is empty or null
         */
        if (StringUtils.isEmpty(ignorableFlagsLocation)) {
            LOGGER.info("Override Ignorable Flags Location is empty. Proceeding with default ignorable flags");
            ignorableFlagsLocation = configuration.getString(BizConfigKeys.DEFAULT_IGNORABLE_FLAGS_LOCATION.getName());
        }

        return populateIgnorableFlags(ignorableFlagsLocation);
    }

    protected IgnorableFlags populateIgnorableFlags(String ignorableFlagsLocation) {
        String jsonContent;
        try {
            jsonContent = fileHelper.readContent(ignorableFlagsLocation);
            ObjectMapper mapper = new ObjectMapper();
            IgnorableFlags ignorableFlags = mapper.readValue(jsonContent, new TypeReference<IgnorableFlags>() {
            });
            LOGGER.info("Ignorable Flags : {}", ignorableFlags);
            return ignorableFlags;
        }
        catch (IOException e) {
            throw new TestExecutionException("Unable to Populate Ignorable Flags", e);
        }
    }

    protected void unsetIgnorableFlagsValue(IgnorableFlags ignorableFlags, Document document) {
        for (FlagsInfo flagsInfo : ignorableFlags.getFlagsInfo()) {

            String ancestorTag = flagsInfo.getAncestorTag();
            for (Entry<String, List<String>> flagInfoEntry : flagsInfo.getFlagsToIgnore().entrySet()) {
                String flagTag = flagInfoEntry.getKey();
                for (String flagValue : flagInfoEntry.getValue()) {
                    unsetFlagValue(document, ancestorTag, flagTag, flagValue);
                }
            }
        }
    }

    protected void removeExpandedFlags(IgnorableFlags ignorableFlags, Document document) {
        removeWtransactionExpandedFlags(ignorableFlags, document);
        removeWuserHoldingExpandedFlags(ignorableFlags, document);
    }

    protected void removeWtransactionExpandedFlags(IgnorableFlags ignorableFlags, Document document) {
        Map<String, List<String>> allFlags = getAllFlags(ignorableFlags, WTRANSACTION_FLAG_TYPE);
        Set<String> expandedIgnorableFlags = wTransactionIgnorableFlagsHelper.getAllExpandedFlags(allFlags);
        for (String expandedFlag : expandedIgnorableFlags) {
            removeTextFlag(document, WTRANSACTION_FLAG_TYPE, expandedFlag);
        }
    }

    protected void removeWuserHoldingExpandedFlags(IgnorableFlags ignorableFlags, Document document) {
        Map<String, List<String>> allFlags = getAllFlags(ignorableFlags, WUSER_HOLDING_FLAG_TYPE);
        Set<String> expandedIgnorableFlags = wUserHoldingIgnorableFlagsHelper.getAllExpandedFlags(allFlags);
        for (String expandedFlag : expandedIgnorableFlags) {
            removeTextFlag(document, WUSER_HOLDING_FLAG_TYPE, expandedFlag);
        }
    }

    private Map<String, List<String>> getAllFlags(IgnorableFlags ignorableFlags, String flagType) {
        if (ignorableFlags.getFlagsInfo() == null) {
            LOGGER.warn("Null Flag");
            /*
             * Returning empty map to avoid null pointer exceptions
             */
            return new HashMap<String, List<String>>();
        }

        for (FlagsInfo flagInfo : ignorableFlags.getFlagsInfo()) {
            if (flagInfo.getAncestorTag().equals(flagType)) {
                return flagInfo.getFlagsToIgnore();
            }
        }
        throw new TestExecutionException("Flag Type Not found : " + flagType);
    }

    protected void removeTextFlag(Document document, String ancestorTag, String textFlag) {
        XPathExpression xPathExpression;
        NodeList nodes;
        try {
            xPathExpression = getXPathExpression(ancestorTag, textFlag);
            nodes = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e) {
            throw new TestExecutionException("Failed to Evaluate XPathExpression", e);
        }

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            LOGGER.debug("Removing Expanded Flag {}", node.getNodeName());
            node.getParentNode().removeChild(node);
        }
    }

    protected void unsetFlagValue(Document document, String ancestorTag, String flagTag, String flagValue) {

        XPathExpression xPathExpression;
        NodeList nodes;
        try {
            xPathExpression = getXPathExpression(ancestorTag, flagTag);
            nodes = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e) {
            throw new TestExecutionException("Failed to Evaluate XPathExpression", e);
        }

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            Long oldValue = Long.valueOf(node.getTextContent());
            Long newValue = oldValue & ~Long.parseLong(flagValue);
            node.setTextContent(String.valueOf(newValue));
            LOGGER.debug("Founds unsetFlag {}:{} => {}", node.getNodeName(), oldValue, newValue);
        }

    }

    /**
     * A particular flag can be identified by it's tag name and it's ancestor. 
     * Ancestor provides a sort of filtering
     * <a><tag1 /></a><b><tag1 /></b> : a/tag1 can be identified by ancestor tag = a & tag = tag1 
     * Incase tag1 are required to be changed without any filtering give ancestor tag = *
     * 
     * @param ancestorTag
     * @param flagTag
     * @return
     * @throws XPathExpressionException
     */
    protected XPathExpression getXPathExpression(String ancestorTag, String flagTag)
            throws XPathExpressionException {
        final String XPATH_EXRESSION_PATTERN = "//{0}//{1}";
        String expression = MessageFormat.format(XPATH_EXRESSION_PATTERN, ancestorTag, flagTag);
        LOGGER.debug("Xpath Expression : {}\n", expression);

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        return xpath.compile(expression);
    }

}
