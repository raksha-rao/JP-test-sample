package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.google.inject.Inject;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.vo.ValueObject;


/**
 * Validates the Post Payment Response with Golden file
 * <li>Comparing Actual Post Payment Response with Golden Post Payment Response files</li>
 *
 * @JP Inc.
 */

public class PostPayResponseGoldenFileAsserter extends GoldenFileComparisonAsserter {

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private PostPaymentResponseXmlVirtualizationTask postPaymentResponseXmlVirtualizationTask;

    private static final Logger LOGGER = LoggerFactory.getLogger(PostPayResponseGoldenFileAsserter.class);

    private static final String POST_PAY_RESPONSE_VALIDATION = "PostPaymentResponse-Validation";

    public PostPayResponseGoldenFileAsserter(final GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    protected Document getActualResponseXml(final Context context) {
        final ValueObject postPayResponse = (ValueObject) this.getDataFromContext(context,
                ContextKeys.POST_PAYMENT_RESPONSE_KEY.getName());
        Assert.assertNotNull(postPayResponse);
        try {
            final String legacyResponseXML = VoHelper.printValueObject(postPayResponse);
            Document xmlDoc = this.xmlHelper.convertToXmlDocument(legacyResponseXML);
            xmlDoc = this.postPaymentResponseXmlVirtualizationTask.execute(xmlDoc);
            LOGGER.info("Post Payment Response Actual XML: \n{}", this.xmlHelper.getPrettyXml(xmlDoc));
            return xmlDoc;
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            throw new TestExecutionException(e);
        }
    }

    @Override
    public String getValidationType() {
        return POST_PAY_RESPONSE_VALIDATION;
    }

    @Override
    protected TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_POST_PAYMENT_RESPONSE_DBDIFF;
    }

}
