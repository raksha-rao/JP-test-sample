package com.jptest.payments.fulfillment.testonia.business.ignorable.flags;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.configuration.Configuration;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.IgnorableFlags;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WTransactionIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.model.ignorable.flags.WUserHoldingIgnorableFlagsHelper;


public class UnsetIgnorableFlagsHelperTest {

    @Mock
    Configuration configuration;

    private UnsetIgnorableFlagsHelper unsetIgnorableFlagsHelper;

    @Test
    public void testGetIgnorableFlags() {
        configuration = Mockito.mock(Configuration.class);
        setup();
        Mockito.when(configuration.getString(BizConfigKeys.DEFAULT_IGNORABLE_FLAGS_LOCATION.getName()))
                .thenReturn("schema/DefaultIgnorableFlags.json");
        IgnorableFlags ignorableFlags = unsetIgnorableFlagsHelper.getIgnorableFlags(null);
        Assert.assertNotNull(ignorableFlags);
    }

    @Test
    public void testIgnorableFlagsValue() throws ParserConfigurationException, SAXException, IOException {
        configuration = Mockito.mock(Configuration.class);
        setup();
        Mockito.when(configuration.getString(BizConfigKeys.DEFAULT_IGNORABLE_FLAGS_LOCATION.getName()))
                .thenReturn("schema/DefaultIgnorableFlags.json");
        String inputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><wtransaction><item><flags><flags4_SEND_MONEY>1</flags4_SEND_MONEY><flags6_IS_MILLENNIUM_SETTLEMENT_PROCESSED>1</flags6_IS_MILLENNIUM_SETTLEMENT_PROCESSED></flags><flags1>0</flags1><flags2>0</flags2><flags3>0</flags3><flags4>536870912</flags4><flags5>0</flags5><flags6>2048</flags6><flags7>0</flags7></item></wtransaction>";
        String outputString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><wtransaction><item><flags><flags4_SEND_MONEY>1</flags4_SEND_MONEY></flags><flags1>0</flags1><flags2>0</flags2><flags3>0</flags3><flags4>536870912</flags4><flags5>0</flags5><flags6>0</flags6><flags7>0</flags7></item></wtransaction>";
        XMLHelper xmlHelper = new XMLHelper();
        Document inputXML = xmlHelper.convertToXmlDocument(inputString);
        unsetIgnorableFlagsHelper.unsetFlags(null, inputXML);
        
        Document outputXML = xmlHelper.convertToXmlDocument(outputString);
        
        Diff diff = new Diff(inputXML, outputXML);
        diff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());
        Assert.assertEquals(diff.similar(), true);
    }

    private void setup() {
        FileHelper fileHelper = new FileHelper();
        WTransactionIgnorableFlagsHelper wTransactionIgnorableFlagsHelper = new WTransactionIgnorableFlagsHelper();
        WUserHoldingIgnorableFlagsHelper wUserHoldingIgnorableFlagsHelper = new WUserHoldingIgnorableFlagsHelper();
        unsetIgnorableFlagsHelper = new UnsetIgnorableFlagsHelper(configuration, fileHelper,
                wTransactionIgnorableFlagsHelper,
                wUserHoldingIgnorableFlagsHelper);
    }

}
