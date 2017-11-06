package com.jptest.payments.fulfillment.testonia.business.component.selective.validation;

import javax.inject.Inject;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentRecipientGoldenFileAsserter;
import com.jptest.payments.fulfillment.testonia.core.util.xml.filter.XMLFilterHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Filters Actual Document on the basis of Golden File.
 * @JP Inc.
 *
 */
public class PaymentRecipientSelectiveGoldenFileAsserter extends PaymentRecipientGoldenFileAsserter {

    @Inject
    private XMLFilterHelper xmlFilterHelper;
    
    public PaymentRecipientSelectiveGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }
    
    protected Document processActualDocument(Document doc) {
        return xmlFilterHelper.getFilteredXML(doc, goldenFileXMLContent);
    }
}
