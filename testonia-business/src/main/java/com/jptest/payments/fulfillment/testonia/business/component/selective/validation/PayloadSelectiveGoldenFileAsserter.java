package com.jptest.payments.fulfillment.testonia.business.component.selective.validation;

import javax.inject.Inject;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PayloadGoldenFileAsserter;
import com.jptest.payments.fulfillment.testonia.core.util.xml.filter.XMLFilterHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Filters Actual Payload XML on the basis of Golden File.
 * 
 * @JP Inc.
 *
 */
public class PayloadSelectiveGoldenFileAsserter extends PayloadGoldenFileAsserter {

    public PayloadSelectiveGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Inject
    private XMLFilterHelper xmlFilterHelper;
    
    protected Document processActualDocument(Document doc) {
        return xmlFilterHelper.getFilteredXML(doc, goldenFileXMLContent);
    }
}
