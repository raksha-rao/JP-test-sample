package com.jptest.payments.fulfillment.testonia.core.util.xml.diff;

import org.w3c.dom.Document;

/**
 * Wrapper over {@link org.custommonkey.xmlunit.Diff} that holds actual and golden file locations
 * 
 */
public class XmlDiff  {

    private String goldenFileLocation;
    private String actualFileLocation;

    public XmlDiff(Document controlDoc, Document testDoc, String goldenFileLocation, String actualFileLocation) {
       // super(controlDoc, testDoc);
        this.goldenFileLocation = goldenFileLocation;
        this.actualFileLocation = actualFileLocation;
    }

    public String getGoldenFileLocation() {
        return goldenFileLocation;
    }

    public String getActualFileLocation() {
        return actualFileLocation;
    }

    public boolean similar() {
        return false;
    }
}
