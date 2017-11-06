/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.business.component.prs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jptest.payments.fulfillment.testonia.business.component.validation.FileRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.GoldenFileComparisonAsserter;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * This task validates the generated response against golden response for PRS.
 *
 * @JP Inc.
 *
 */
public class PRSValidationAsserter extends GoldenFileComparisonAsserter {

    private String actualFileName;

    private Map<String, Object[]> idMap;
    @Inject
    XMLHelper xmlHelper;

    @Inject
    FileHelper fileHelper;

    @Inject
    private FileRetrieverTask fileRetriever;
    private String actualFileDirectory;

    public PRSValidationAsserter(GoldenFileComparisonTaskInput input, final String actualFileName,
            final String actualFileDirectory, Map<String, Object[]> idMap) {
        super(input);
        this.actualFileName = actualFileName;
        this.actualFileDirectory = actualFileDirectory;
        this.idMap = idMap;
    }

    public static class PRSValidationException extends TestExecutionBusinessException {

        private static final long serialVersionUID = 18722853537583711L;

        /**
         * @param message
         */
        private PRSValidationException(final String message, final Throwable e) {
            super(message, e);
        }

        private PRSValidationException(String message) {
            super(message);
        }
    }
    /**
     * 
     * @param document
     */
    private void replaceIDsValue(Document document) {
        NodeList nodeList = document.getElementsByTagName("*");
        Map<String, String> newIdMap = new HashMap<>();
        for (Entry<String, Object[]> entry : idMap.entrySet()) {
            newIdMap.put( String.valueOf(entry.getValue()[0]), entry.getKey());
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (newIdMap.containsKey(element.getTextContent()))
                    element.setTextContent(newIdMap.get(element.getTextContent()));
            }
        }
    }

    @Override
    protected TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_PRS_DBDIFF;
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        Document actualXML;
        actualXML = fileRetriever.execute(actualFileDirectory + actualFileName);
        replaceIDsValue(actualXML);
        return actualXML;
    }

    @Override
    public String getValidationType() {
        return "PRS_VALIDATION : " + actualFileName;
    }

}
