package com.jptest.payments.fulfillment.testonia.business.component.selective.validation;

import javax.inject.Inject;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PaymentSenderGoldenFileAsserter;
import com.jptest.payments.fulfillment.testonia.core.util.xml.filter.XMLFilterHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Filters the Actual Document on the basis of golden file.
 * @JP Inc.
 *
 */
public class PaymentSenderSelectiveGoldenFileAsserter extends PaymentSenderGoldenFileAsserter {

    @Inject
    private XMLFilterHelper xmlFilterHelper;

    public PaymentSenderSelectiveGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }
    
    protected Document processActualDocument(Document doc) {
        return xmlFilterHelper.getFilteredXML(doc, goldenFileXMLContent);
    }
}
