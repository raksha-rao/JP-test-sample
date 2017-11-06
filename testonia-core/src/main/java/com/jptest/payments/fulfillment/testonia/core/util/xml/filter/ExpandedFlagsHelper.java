package com.jptest.payments.fulfillment.testonia.core.util.xml.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WTransactionIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WUserHoldingIgnorableFlagsHelper;

/**
 * This class helps to get all the expanded flags
 * 
 * @see ExpandedFlagsHelperTest
 * 
 * @JP Inc.
 *
 */
@Singleton
public class ExpandedFlagsHelper {

    @Inject
    private WTransactionIgnorableFlagsHelper wTransactionIgnorableFlagsHelper;

    @Inject
    private WUserHoldingIgnorableFlagsHelper wUserHoldingIgnorableFlagsHelper;
     
    @Inject
    private XMLHelper xmlHelper;

    public ExpandedFlagsHelper() {
        super();
    }

    /*
     * For testing purpose
     */
    protected ExpandedFlagsHelper(WTransactionIgnorableFlagsHelper wTransactionIgnorableFlagsHelper,
            WUserHoldingIgnorableFlagsHelper wUserHoldingIgnorableFlagsHelper, XMLHelper xmlHelper) {
        this.wTransactionIgnorableFlagsHelper = wTransactionIgnorableFlagsHelper;
        this.wUserHoldingIgnorableFlagsHelper = wUserHoldingIgnorableFlagsHelper;
        this.xmlHelper = xmlHelper;
    }

    public Set<String> getExpandedFlags(Document goldenFileXML) {
        Set<String> flagsXPath = new HashSet<String>();
        flagsXPath.addAll(getWTransactionFlags(goldenFileXML));
        flagsXPath.addAll(getWUserHoldingFlags(goldenFileXML));
        return flagsXPath;
    }

    private Set<String> getWTransactionFlags(Document goldenFileXML) {
        Map<String, List<String>> flagsInfo;
        try {
            flagsInfo = getWTransactionFlagsInfo(goldenFileXML);
        }
        catch (XPathExpressionException e) {
            throw new TestExecutionException("Failed to retrieve WTransaction Expanded Flags");
        }
        return wTransactionIgnorableFlagsHelper.getAllExpandedFlags(flagsInfo);
    }

    private Set<String> getWUserHoldingFlags(Document goldenFileXML) {
        Map<String, List<String>> flagsInfo;
        try {
            flagsInfo = getWUserHoldingFlagsInfo(goldenFileXML);
        }
        catch (XPathExpressionException e) {
           throw new TestExecutionException("Failed to retrieve WuserHolding Expanded Flags", e);
        }
        return wUserHoldingIgnorableFlagsHelper.getAllExpandedFlags(flagsInfo);
    }

    private Map<String, List<String>> getWTransactionFlagsInfo(Document goldenFileXML) throws XPathExpressionException {
        Map<String, List<String>> flagsInfo = new HashMap<String, List<String>>();
        if(xmlHelper.isXPathPresent(goldenFileXML, FlagXPath.WTRANSACTION_FLAGS1.getXPath())) {
            flagsInfo.put("flags1", new ArrayList<>(Arrays.asList(FlagMaxValue.MAX_32_BIT.getMaxValue())));
        }
        
        if(xmlHelper.isXPathPresent(goldenFileXML, FlagXPath.WTRANSACTION_FLAGS2.getXPath())) {
            flagsInfo.put("flags2", new ArrayList<>(Arrays.asList(FlagMaxValue.MAX_32_BIT.getMaxValue())));
        }
        
        if(xmlHelper.isXPathPresent(goldenFileXML, FlagXPath.WTRANSACTION_FLAGS3.getXPath())) {
            flagsInfo.put("flags3", new ArrayList<>(Arrays.asList(FlagMaxValue.MAX_32_BIT.getMaxValue())));
        }
        
        if(xmlHelper.isXPathPresent(goldenFileXML, FlagXPath.WTRANSACTION_FLAGS4.getXPath())) {
            flagsInfo.put("flags4", new ArrayList<>(Arrays.asList(FlagMaxValue.MAX_32_BIT.getMaxValue())));
        }
        
        if(xmlHelper.isXPathPresent(goldenFileXML, FlagXPath.WTRANSACTION_FLAGS5.getXPath())) {
            flagsInfo.put("flags5", new ArrayList<>(Arrays.asList(FlagMaxValue.MAX_64_BIT.getMaxValue())));
        }
        
        if(xmlHelper.isXPathPresent(goldenFileXML, FlagXPath.WTRANSACTION_FLAGS6.getXPath())) {
            flagsInfo.put("flags6", new ArrayList<>(Arrays.asList(FlagMaxValue.MAX_64_BIT.getMaxValue())));
        }
        
        return flagsInfo;
    }

    private Map<String, List<String>> getWUserHoldingFlagsInfo(Document goldenFileXML) throws XPathExpressionException {
        Map<String, List<String>> flagsInfo = new HashMap<String, List<String>>();
        if(xmlHelper.isXPathPresent(goldenFileXML, FlagXPath.WUSER_HOLDING_FLAGS.getXPath())) {
            flagsInfo.put("flags", new ArrayList<>(Arrays.asList(FlagMaxValue.MAX_32_BIT.getMaxValue())));
        }

        return flagsInfo;
    }
    
    private enum FlagXPath
    {
        WTRANSACTION_FLAGS1("//wtransaction//flags1"),
        WTRANSACTION_FLAGS2("//wtransaction//flags2"),
        WTRANSACTION_FLAGS3("//wtransaction//flags3"),
        WTRANSACTION_FLAGS4("//wtransaction//flags4"),
        WTRANSACTION_FLAGS5("//wtransaction//flags5"),
        WTRANSACTION_FLAGS6("//wtransaction//flags6"),
        WUSER_HOLDING_FLAGS("//wuser_holding//flags");
        
        private String xpath;
        
        private FlagXPath(String xpath) {
            this.xpath = xpath;
        }
        
        public String getXPath() {
            return xpath;
        }
    }
    
    private enum FlagMaxValue {
        
        MAX_32_BIT("2147483647"),
        MAX_64_BIT("9223372036854775807");
        
        private String maxValue;
        
        private FlagMaxValue(String maxValue) {
            this.maxValue = maxValue;
        }
        
        public String getMaxValue() {
            return maxValue;
        }
    }
}
