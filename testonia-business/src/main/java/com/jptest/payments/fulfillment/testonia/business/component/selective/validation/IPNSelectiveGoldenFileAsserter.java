package com.jptest.payments.fulfillment.testonia.business.component.selective.validation;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.component.validation.IPNGoldenFileAsserter;
import com.jptest.payments.fulfillment.testonia.core.util.xml.filter.XMLFilterHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Filters the Actual Document on the basis of Golden File.
 * 
 * @JP Inc.
 *
 */
@Singleton
public class IPNSelectiveGoldenFileAsserter extends IPNGoldenFileAsserter {

    @Inject
    private XMLFilterHelper xmlFilterHelper;
    
    public IPNSelectiveGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }
    
    protected Document processActualDocument(Document doc) {
        return xmlFilterHelper.getFilteredXML(doc, goldenFileXMLContent);
    }
}
