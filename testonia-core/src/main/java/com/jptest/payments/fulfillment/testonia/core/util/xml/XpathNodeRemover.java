package com.jptest.payments.fulfillment.testonia.core.util.xml;

import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;


/**
 * @see XpathNodeRemoverTest
 */
@Singleton
public class XpathNodeRemover {

    private XMLHelper xmlHelper;

    @Inject
    public XpathNodeRemover(XMLHelper xmlHelper) {
        this.xmlHelper = xmlHelper;
    }

    /**
     * Removes the elements mentioned specified in the excludeNode set using XPATH.
     *
     * @param doc
     *            org.w3c.dom.Document
     * @return org.w3c.dom.Document
     */
    public Document remove(Document inputDocument, Set<String> excludedXPaths) {
        for (String excludedXPath : excludedXPaths) {
            try {
                xmlHelper.removeNodesByXPath(inputDocument, excludedXPath);
            }
            catch (XPathExpressionException e) {
                throw new TestExecutionException("Exception occurred removing attribute", e);
            }
        }
        return inputDocument;
    }
}
