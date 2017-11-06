package com.jptest.payments.fulfillment.testonia.business.component.selective.validation;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.component.validation.PostPayResponseGoldenFileAsserter;
import com.jptest.payments.fulfillment.testonia.core.util.xml.filter.XMLFilterHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;


/**
 * Selective validation on the Post Payment Response
 * <li>Filters the Actual Document on the basis of Golden File.</li>
 *
 * @JP Inc.
 */

@Singleton
public class PostPaymentResponseSelectiveGoldenFileAsserter extends PostPayResponseGoldenFileAsserter {

    @Inject
    private XMLFilterHelper xmlFilterHelper;

    public PostPaymentResponseSelectiveGoldenFileAsserter(final GoldenFileComparisonTaskInput input) {
        super(input);
    }

    protected Document processActualDocument(final Document doc) {
        return this.xmlFilterHelper.getFilteredXML(doc, this.goldenFileXMLContent);
    }
}
