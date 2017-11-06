package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.business.ignorable.flags.UnsetIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.StringHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.qnotif.WHttpMsgQueueDAO;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.money.IPNFixtureResultVO;
import com.jptest.payments.fulfillment.testonia.model.money.NotificationDTO;

/**
 * Compares IPN sent to receiver with IPN from goldenFile using XML-Diff.
 */
@Singleton
public class IPNGoldenFileAsserter extends E2EGoldenFileComparisonAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPNGoldenFileAsserter.class);

    private static final String ERROR_MESSAGE_CONTENT = "IPN not generated under /tmp/ipn_listener_logs for transaction {transactionId}";
    private static final String TRANSACTION_ID_REPLACEMENT_TOKEN = "{transactionId}";
    private static final String OPERATION = "IPNValidation";

    @Inject
    private IPNXmlVirtualizationTask xmlVirtualizationTask;

    @Inject
    private StringHelper stringHelper;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private WHttpMsgQueueDAO wHttpMsgQueueDAO;

    @Inject
    private UnsetIgnorableFlagsHelper unsetIgnorableFlagsHelper;

    public IPNGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return OPERATION;
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        String ignorableFlagsLocation = (String) getDataFromContext(context,
                ContextKeys.IGNORABLE_FLAGS_LOCATION.getName());

        // 1. get receiver's transaction id
        String encryptedId = (String) getDataFromContext(context, ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        Assert.assertNotNull(encryptedId, this.getClass().getSimpleName() +
                ".getActualResponseXml() - IPN Encrypted Id should NOT be NULL");

        LOGGER.info("IPNGoldenFileAsserter - encryptedId: {}", encryptedId);

        // 2. Reset the key
        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());

        // 3. using data from step 1, get notification data
        RetriableNotificationByStatusCheckTask retriableNotificationExistsCheckTask =
                new RetriableNotificationByStatusCheckTask(wHttpMsgQueueDAO);

        List<NotificationDTO> notificationResults = retriableNotificationExistsCheckTask.execute(encryptedId);
        LOGGER.info("IPN notification count: {}", notificationResults.size());

        context.addReportingAttribute(ReportingAttributes.IPN_NOTIFICATION_COUNT,
                String.valueOf(notificationResults.size()));

        if (notificationResults.isEmpty()) {
            LOGGER.warn("No IPN Details found for paymentEncryptedId: {}", encryptedId);
            return null;
        }

        // 3. convert notification result to XML
        Document actualXML = convertNotificationResponseToXML(encryptedId, notificationResults);

        // 4. Unset Ignorable flags
        unsetIgnorableFlagsHelper.unsetFlags(ignorableFlagsLocation, actualXML);

        // 5. Process Actual Document
        actualXML = processActualDocument(actualXML);

        // 6. virtualize XML
        xmlVirtualizationTask.execute(actualXML);

        LOGGER.info("IPN Actual XML: \n{}", xmlHelper.getPrettyXml(actualXML));

        return actualXML;
    }

    protected Document processActualDocument(Document doc) {
        return doc;
    }

    /**
     * Converts notificationResults to IPNFixtureResultVO which then returns Document using JAXB
     *
     * @param encryptedId
     * @param notificationResults
     * @return
     */
    private Document convertNotificationResponseToXML(String encryptedId,
            List<NotificationDTO> notificationResults) {
        IPNFixtureResultVO resultVO = new IPNFixtureResultVO();
        if (notificationResults.isEmpty()) {
            resultVO.setHttpResponse(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
            resultVO.setErrorMessage(ERROR_MESSAGE_CONTENT
                    .replace(TRANSACTION_ID_REPLACEMENT_TOKEN, encryptedId));
        } else {
            resultVO.setHttpResponse(String.valueOf(HttpStatus.SC_OK));
            resultVO.setIpnValues(stringHelper.convertStringToMap(notificationResults.get(0).getContent()));
        }

        try {
            return resultVO.getDocument();
        } catch (ParserConfigurationException | JAXBException e) {
            throw new TestExecutionException(e);
        }
    }

    @Override
    protected boolean isBestEffort() {
        return true;
    }

    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_IPN_DBDIFF;
    }

    private static class RetriableNotificationByStatusCheckTask
            extends RetriableTask<String, List<NotificationDTO>> {

        private WHttpMsgQueueDAO wHttpMsgQueueDAO;

        public RetriableNotificationByStatusCheckTask(WHttpMsgQueueDAO wHttpMsgQueueDAO) {
            super(60 * 1000, 10 * 1000);
            this.wHttpMsgQueueDAO = wHttpMsgQueueDAO;
        }

        @Override
        protected boolean isDesiredOutput(List<NotificationDTO> output) {
            return !output.isEmpty();
        }

        @Override
        protected List<NotificationDTO> retriableExecute(String encryptedId) {
            return wHttpMsgQueueDAO.getNotificationContentUsingEncryptedId(encryptedId);
        }

        @Override
        protected List<NotificationDTO> onSuccess(String encryptedId, List<NotificationDTO> output) {
            return output;
        }

        @Override
        protected List<NotificationDTO> onFailure(String encryptedId, List<NotificationDTO> output) {
            return new ArrayList<>();
        }
    }
}
