package com.jptest.payments.fulfillment.testonia.business.component.selective.validation;

import javax.inject.Inject;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.component.validation.ActivityLogPaymentMessageGoldenFileAsserter;
import com.jptest.payments.fulfillment.testonia.core.util.xml.filter.XMLFilterHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;


/**
 * Filters the Actual Document on the basis of golden file.
 *
 * @JP Inc.
 */

public class ActivityLogPaymentMessageSelectiveGoldenFileAsserter extends ActivityLogPaymentMessageGoldenFileAsserter {

    @Inject
    private XMLFilterHelper xmlFilterHelper;

    public ActivityLogPaymentMessageSelectiveGoldenFileAsserter(final GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    protected Document processActualDocument(final Document doc) {
        return this.xmlFilterHelper.getFilteredXML(doc, this.goldenFileXMLContent);
    }
}
