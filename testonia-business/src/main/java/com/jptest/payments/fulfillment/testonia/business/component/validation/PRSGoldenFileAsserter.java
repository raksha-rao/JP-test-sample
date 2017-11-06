package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * 
 * Validates the PRS response with Golden file
 *
 */
public class PRSGoldenFileAsserter extends E2EGoldenFileComparisonAsserter implements TimeoutAwareComponent {

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private PaymentReadServXmlVirtualizationTask xmlVirtualizationTask;

    private static final Logger LOGGER = LoggerFactory.getLogger(PRSGoldenFileAsserter.class);

    private static final String PRS_VALIDATION = "PaymentReadServ-Validation";

    public PRSGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public long getTimeoutInMs() {
        return 100000;
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        GetLegacyEquivalentByPaymentReferenceResponse input = (GetLegacyEquivalentByPaymentReferenceResponse) getDataFromContext(
                context, ContextKeys.PRS_RESPONSE_KEY.getName());
        try {
            String legacyResponseXML = VoHelper.printValueObject(input);
            Document xmlDoc = xmlHelper.convertToXmlDocument(legacyResponseXML);
            xmlDoc = xmlVirtualizationTask.execute(xmlDoc);
            LOGGER.info("Payment read serv Actual XML: \n{}", xmlHelper.getPrettyXml(xmlDoc));
            return xmlDoc;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new TestExecutionException(e);
        }
    }

    @Override
    public String getValidationType() {
        return PRS_VALIDATION;
    }
    
    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_PRS_DBDIFF;
    }

}
