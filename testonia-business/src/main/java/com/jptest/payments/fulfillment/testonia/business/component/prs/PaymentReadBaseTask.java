/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.business.component.prs;

import java.io.IOException;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.inject.Singleton;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.business.component.validation.XmlVirtualizationTask;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.vo.ValueObject;

/**
 * This class executes PRS API for collections of PaymentReferenceType and reference value.
 * 
 * @JP Inc.
 *
 */
public abstract class PaymentReadBaseTask<T, S> extends BaseTask<S> implements PaymentReadOperationsTask<T, S> {

    // file name to persist the response
    protected String fileName;

    // Output directory to persist response
    protected String writeDir;

    // PRS reference type
    protected PaymentReferenceTypeCode refCode;

    // reference value
    protected String referenceValue;

    // flag to enable/disable persistence of response
    protected boolean persistOutPut;

    // API_KEY
    protected PRSApiTypeCode apiKey;

    @Inject
    protected PaymentReadServBridge bridge;

    @Inject
    private FileHelper fileHelper;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private PRSResponseVirtualizationTask prsVirtualizationTask;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentReadBaseTask.class);

    private static final String DELIMITER = "_";

    protected static enum PRSOperations {
        CONSTRUCT_REQUEST("CONSTRUCT_PRS_REQUEST"),
        CALL_PRS("CALL_PRS"),
        PERSIST_RESPONSE("PERSIST_RESPONSE"),
        VALIDATE_RESPONSE("VALIDATE_RESPONSE");
        private PRSOperations(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Enums for PRS APIs.
     * 
     * @JP Inc.
     *
     */
    public static enum PRSApiTypeCode {
        LEBPR,
        CTBAI,
        CTLEBAI,
        FTWPAI,
        IVOBPR,
        ALL;
    }

    @Override
    public S process(Context context) {
        S response = null;
        long startTime = 0;
        long timeTaken = 0;
        final String timeTakenPhrase = "Time taken for {} {}";
        String value = referenceValue;
        try {
            startTime = System.currentTimeMillis();
            final T request = this.constructPRSRequest(refCode, value);
            timeTaken = (System.currentTimeMillis() - startTime);

            LOGGER.info(new StringBuilder(timeTakenPhrase).toString(),
                    new StringBuilder(PRSOperations.CONSTRUCT_REQUEST.name).append(DELIMITER).append(fileName)
                            .toString(),
                    timeTaken);
            startTime = System.currentTimeMillis();
            LOGGER.info(new StringBuilder("Executing ").append(apiKey.toString()).append(" with reference type code ")
                    .append(refCode.getValue()).append(" and reference value ").append(value).toString());
            response = this.executePrs(request);
            timeTaken = (System.currentTimeMillis() - startTime);

            LOGGER.info(new StringBuilder(timeTakenPhrase).toString(),
                    new StringBuilder(PRSOperations.CALL_PRS.name).append(DELIMITER).append(fileName).toString(),
                    timeTaken);

            startTime = System.currentTimeMillis();
            if (persistOutPut) {
                persistOutPut(response);
                timeTaken = (System.currentTimeMillis() - startTime);
                LOGGER.info(new StringBuilder(timeTakenPhrase).toString(),
                        new StringBuilder(PRSOperations.PERSIST_RESPONSE.name).append(DELIMITER).append(fileName)
                                .toString(),
                        timeTaken);
            }
        } catch (TestExecutionException e) {
            StringBuilder sb = new StringBuilder("Error in processing PRS task for ").append(fileName)
                    .append(" due to ").append(e.getMessage());
            LOGGER.error(sb.toString(), e);
            throw new PRSOperationException(sb.toString(), e);
        }

        return response;
    }

    public static class PRSOperationException extends TestExecutionBusinessException {

        private static final long serialVersionUID = 3653938754890622078L;

        /**
         * @param message
         */
        private PRSOperationException(final String message, final Throwable e) {
            super(message, e);
        }
    }

    /**
     * 
     * @param response
     */
    private void persistOutPut(S response) {
        try {
            Document doc = xmlHelper.convertVoToXmlDocument((ValueObject) response);
            prsVirtualizationTask.execute(doc);
            fileHelper.writeToFile(FilenameUtils.concat(writeDir, fileName), xmlHelper.getPrettyXml(doc));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            StringBuilder sb = new StringBuilder("Error in processing PRS task for ").append(fileName)
                    .append(" due to ").append(e.getMessage());
            LOGGER.error(sb.toString(), e);
            throw new PRSOperationException(sb.toString(), e);
        }
    }

    @Singleton
    private static class PRSResponseVirtualizationTask extends XmlVirtualizationTask<Document> {

        @Inject
        private Configuration configuration;

        @Inject
        private void init() {
            String virtualizeSchemaFileLocation = configuration
                    .getString(BizConfigKeys.PRS_RESPONSE_VIRTUALIZE_SCHEMA_FILE_LOCATION.getName());
            try {
                populateVirtualizeSchema(virtualizeSchemaFileLocation);
            } catch (IOException e) {
                throw new TestExecutionException(e);
            }
        }

        @Override
        public Document execute(Document document) {
            ignoreNodesUsingXPath(document);
            ignoreAttributes(document);
            return document;
        }
    }
}
