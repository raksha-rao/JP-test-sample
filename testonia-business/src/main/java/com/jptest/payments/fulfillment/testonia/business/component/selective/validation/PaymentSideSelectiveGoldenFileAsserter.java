package com.jptest.payments.fulfillment.testonia.business.component.selective.validation;

import javax.inject.Inject;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentSideGoldenFileAsserter;
import com.jptest.payments.fulfillment.testonia.core.util.xml.filter.XMLFilterHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Filters Actual Document on the basis of Golden File.
 * @JP Inc.
 *
 */
public class PaymentSideSelectiveGoldenFileAsserter extends PaymentSideGoldenFileAsserter {

    @Inject
    private XMLFilterHelper xmlFilterHelper;
    
    public PaymentSideSelectiveGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }
    
    protected Document processActualDocument(Document doc) {
        return xmlFilterHelper.getFilteredXML(doc, goldenFileXMLContent);
    }
}
