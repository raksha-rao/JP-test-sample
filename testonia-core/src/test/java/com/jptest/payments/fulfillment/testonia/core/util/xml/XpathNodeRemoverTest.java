package com.jptest.payments.fulfillment.testonia.core.util.xml;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import javax.xml.parsers.ParserConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;


/**
 * Unit test for {@link XpathNodeRemover}
 */
public class XpathNodeRemoverTest {

    private FileHelper fileHelper = new FileHelper();
    private XMLHelper xmlHelper = new XMLHelper();

    @Test
    public void testRemove() throws IOException, SAXException, ParserConfigurationException {
        XpathNodeRemover remover = new XpathNodeRemover(xmlHelper);
        // prepare test
        String inputXmlString = fileHelper.readContentFromTestResource("input-PaymentValidation-recipient-golden.xml");
        String expectedXmlString = fileHelper
                .readContentFromTestResource("expected-PaymentValidation-recipient-golden.xml");
        Document inputDoc = xmlHelper.convertToXmlDocument(inputXmlString);

        // perform test
        Document actualDoc = remover.remove(inputDoc, new HashSet<>(
                Arrays.asList("//validation_fixture/recipient/fee_history/item[transaction_type='X']/data",
                        "//validation_fixture/sender/fee_history/item[transaction_type='X']/data")));
        // verify
        Assert.assertEquals(xmlHelper.getPrettyXml(actualDoc), expectedXmlString);
    }

}
