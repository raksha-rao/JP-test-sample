package com.jptest.payments.fulfillment.testonia.core.util.xml.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WTransactionIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WUserHoldingIgnorableFlagsHelper;


public class ExpandedFlagsHelperTest {

    private ExpandedFlagsHelper expandedFlagsHelper = new ExpandedFlagsHelper();

    @Test
    public void testWtransactionFlags() throws ParserConfigurationException, SAXException, IOException {
        setup();
        WTransactionIgnorableFlagsHelper wTransactionIgnorableFlagsHelper = new WTransactionIgnorableFlagsHelper();
        XMLHelper xmlHelper = new XMLHelper();

        String goldenFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><wtransaction><item><flags4>123</flags4></item></wtransaction>";
        Document goldenFileXML = xmlHelper.convertToXmlDocument(goldenFile);

        Map<String, List<String>> flagsInfo = new HashMap<String, List<String>>();
        flagsInfo.put("flags4", new ArrayList<>(Arrays.asList("2147483647")));

        Set<String> expectedOutput = wTransactionIgnorableFlagsHelper.getAllExpandedFlags(flagsInfo);

        Set<String> actualOutput = expandedFlagsHelper.getExpandedFlags(goldenFileXML);
        System.out.println(expectedOutput);

        Assert.assertTrue(expectedOutput.containsAll(actualOutput) && actualOutput.containsAll(expectedOutput));
    }

    @Test
    public void testWUserFlags() throws ParserConfigurationException, SAXException, IOException {
        setup();
        WUserHoldingIgnorableFlagsHelper wUserHoldingIgnorableFlagsHelper = new WUserHoldingIgnorableFlagsHelper();
        XMLHelper xmlHelper = new XMLHelper();

        String goldenFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><wuser_holding><item><flags>123</flags></item></wuser_holding>";
        Document goldenFileXML = xmlHelper.convertToXmlDocument(goldenFile);

        Map<String, List<String>> flagsInfo = new HashMap<String, List<String>>();
        flagsInfo.put("flags", new ArrayList<>(Arrays.asList("2147483647")));

        Set<String> expectedOutput = wUserHoldingIgnorableFlagsHelper.getAllExpandedFlags(flagsInfo);

        Set<String> actualOutput = expandedFlagsHelper.getExpandedFlags(goldenFileXML);
        System.out.println(expectedOutput);

        Assert.assertTrue(expectedOutput.containsAll(actualOutput) && actualOutput.containsAll(expectedOutput));
    }

    private void setup() {
        WTransactionIgnorableFlagsHelper wTransactionIgnorableFlagsHelper = new WTransactionIgnorableFlagsHelper();
        WUserHoldingIgnorableFlagsHelper wUserHoldingIgnorableFlagsHelper = new WUserHoldingIgnorableFlagsHelper();
        XMLHelper xmlHelper = new XMLHelper();
        expandedFlagsHelper = new ExpandedFlagsHelper(wTransactionIgnorableFlagsHelper,
                wUserHoldingIgnorableFlagsHelper, xmlHelper);
    }

}
