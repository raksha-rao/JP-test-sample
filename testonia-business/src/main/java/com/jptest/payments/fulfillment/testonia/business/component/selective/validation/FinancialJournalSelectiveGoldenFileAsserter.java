package com.jptest.payments.fulfillment.testonia.business.component.selective.validation;

import javax.inject.Inject;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.component.validation.FinancialJournalGoldenFileAsserter;
import com.jptest.payments.fulfillment.testonia.core.util.xml.filter.XMLFilterHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;


/**
 * Filters the actual document on the basis of golden file
 * 
 * @JP Inc.
 */
public class FinancialJournalSelectiveGoldenFileAsserter extends FinancialJournalGoldenFileAsserter {

    public FinancialJournalSelectiveGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Inject
    private XMLFilterHelper xmlFilterHelper;

    protected Document processActualDocument(Document doc) {
        return xmlFilterHelper.getFilteredXML(doc, goldenFileXMLContent);
    }
}
