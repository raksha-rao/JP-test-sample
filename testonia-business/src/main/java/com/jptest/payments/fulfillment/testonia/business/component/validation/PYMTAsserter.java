package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.service.TransactionHelper;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * Performs PYMT validation by calling PYMT ruby script
 */
public class PYMTAsserter extends BaseAsserter
        implements TimeoutAwareComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(PYMTAsserter.class);

    @Inject
    private Configuration configuration;

    @Inject
    private TransactionHelper transactionHelper;

    @Override
    public void validate(Context context) {
        List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        execute(wTransactionList);
    }

    @Override
    public boolean shouldParticipate(Context context) {
        return configuration.getBoolean(BizConfigKeys.PYMTVALIDATION_ENABLE.getName(), true);
    }

    private void execute(List<WTransactionVO> wTransactionList) {
        // 1. get sender's transaction id from the list
        WTransactionVO senderTransaction = transactionHelper.getSenderTransaction(wTransactionList);

        LOGGER.debug("Performing PYMT validations for {}", senderTransaction.getId());
        String pymtValidationURL = configuration.getString(BizConfigKeys.PYMT_VALIDATION_TOOL_URL.getName())
                .replace("{dummyTransId}", senderTransaction.getId().toString());

        LOGGER.info("PYMT Validation URL:{}", pymtValidationURL);
        try {
            ObjectNode responseNode = new ObjectMapper().readValue(new URL(pymtValidationURL), ObjectNode.class);
            if (responseNode == null || responseNode.size() == 0) {
                throw new PYMTAssertionException(pymtValidationURL, "Empty PYMT response");
            }

            JsonNode statusNode = responseNode.get("status");
            if (statusNode == null || statusNode.asText() == null
                    || !statusNode.asText().toLowerCase().equals("valid")) {
                LOGGER.error("PYMT Validation response does not have valid status: {}", responseNode);
                throw new PYMTAssertionException(pymtValidationURL, "[Invalid status] " + responseNode.toString());
            }

            JsonNode validationDetailsNode = responseNode.get("validations");
            if (validationDetailsNode == null || !validationDetailsNode.isObject()
                    || ((ObjectNode) validationDetailsNode).size() == 0) {
                LOGGER.error("PYMT Validation response does not have <validations>: {}", responseNode);
                throw new PYMTAssertionException(pymtValidationURL,
                        "[No <validations> node] " + responseNode.toString());
            }

            JsonNode countsNode = validationDetailsNode.get("count");
            if (countsNode == null || !countsNode.isObject() || ((ObjectNode) countsNode).size() == 0) {
                LOGGER.error("PYMT Validation response does not have valid validation-count: {}", responseNode);
                throw new PYMTAssertionException(pymtValidationURL,
                        "[No <validations-count> node] " + responseNode.toString());
            }

            JsonNode errorCountNode = countsNode.get("errors");
            if (errorCountNode == null) {
                LOGGER.error("PYMT Validation response does not have validation-error-count element: {}", responseNode);
                throw new PYMTAssertionException(pymtValidationURL, "[No <errors> node] " + responseNode.toString());
            }

            int errorCount = errorCountNode.asInt();
            if (errorCount > 0) {
                JsonNode errorsNode = validationDetailsNode.get("errors");
                if (errorsNode != null && errorsNode.size() != 0) {
                    Iterator<JsonNode> elements = errorsNode.elements();
                    while (elements.hasNext()) {
                        LOGGER.error("PYMT Validation error message:{}", elements.next());
                    }
                }
                LOGGER.error("PYMT Validation response has errors: {}", responseNode);
                throw new PYMTAssertionException(pymtValidationURL, "[<errors>] " + responseNode.toString());
            }
        } catch (IOException e) {
            LOGGER.error("Failed while reading the data from {} with exception ", pymtValidationURL, e);
            throw new PYMTAssertionException(pymtValidationURL, "[Call Failed]" + e.getMessage());
        }

    }

    private static class PYMTAssertionException extends TestExecutionBusinessException {

        private String pymtUrl;
        private String errorResponse;

        @Override
        public TestoniaExceptionReasonCode getReasonCode() {
            return TestoniaExceptionReasonCode.FAILURE_PYMT_ASSERTION;
        }

        private PYMTAssertionException(String pymtUrl, String errorResponse) {
            super("URL:" + pymtUrl + " Response:" + errorResponse);
            this.pymtUrl = pymtUrl;
            this.errorResponse = errorResponse;
        }

        @Override
        public String toString() {
            return "URL:" + pymtUrl + "\nResponse:" + errorResponse;
        }

    }

    @Override
    public long getTimeoutInMs() {
        return 20000;
    }

}
