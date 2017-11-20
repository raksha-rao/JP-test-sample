package com.jptest.sampletest;

import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.diff.Diff;
import org.w3c.dom.Document;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SampleDiffHelper {

    @Inject
    private FileHelper fileHelper;

    @Inject
    private XMLHelper xmlHelper;

    public String getDiffs(Diff myDiff) {
        StringBuilder diffMessages = new StringBuilder();
        return diffMessages.toString();
    }

    public void storeXmlOnFileSystem(Document xmlDoc, String fileLocation) {
        String prettyXml = xmlHelper.getPrettyXml(xmlDoc);
        fileHelper.writeToFile(fileLocation, prettyXml);
    }
}
