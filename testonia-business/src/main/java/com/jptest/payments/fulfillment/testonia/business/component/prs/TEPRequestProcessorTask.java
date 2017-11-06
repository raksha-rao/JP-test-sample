package com.jptest.payments.fulfillment.testonia.business.component.prs;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.DataProcessingStatus;
import com.jptest.payments.ProcessDataRequest;
import com.jptest.payments.ProcessDataResponse;
import com.jptest.payments.fulfillment.testonia.bridge.TransactionEventProcessorBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;

/**
 * This class writes a process data request object into TE database, which later can be read using PRS APIs.
 * 
 * @JP Inc.
 *
 */
public class TEPRequestProcessorTask extends BaseTask<ProcessDataResponse> {

    @Inject
    TransactionEventProcessorBridge bridge;

    private static final Logger LOGGER = LoggerFactory.getLogger(TEPRequestProcessorTask.class);
    private ProcessDataRequest processDataRequest;

    public TEPRequestProcessorTask(ProcessDataRequest request) {
        this.processDataRequest = request;
    }

    private static enum TEPRequestProcessorOperations {
        FETCH_DATA_FROM_CONTEXT("FETCH_DATA_FROM_CONTEXT"),
        CALL_TEP("CALL_TEP");

        private TEPRequestProcessorOperations(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String toString() {
            return name;

        }
    }

    @Override
    public ProcessDataResponse process(Context context) {
        long startTime = 0;
        long timeTaken = 0;
        startTime = System.currentTimeMillis();
        if (processDataRequest == null) {
            throw new TEPRequestProcessorException("Error in fetching ProcessDataRequest object from context ....");
        }

        startTime = System.currentTimeMillis();
        ProcessDataResponse result = bridge.processData(processDataRequest);
        timeTaken = (System.currentTimeMillis() - startTime);
        LOGGER.info("Time taken for " + TEPRequestProcessorOperations.CALL_TEP + " : " + timeTaken);
        if (result == null || result.getStatusAsEnum() != DataProcessingStatus.PROCESSED) {
            LOGGER.info("process_data request: {}", printValueObject(processDataRequest));
            LOGGER.info("process_data response: {}", printValueObject(result));
            throw new TEPRequestProcessorException("Not able to process tep request");
        }
        return result;
    }

    private static class TEPRequestProcessorException extends TestExecutionBusinessException {

        private static final long serialVersionUID = -7144079061222946007L;

        /**
         * @param message
         */
        private TEPRequestProcessorException(final String message) {
            super(message);
        }

    }
}
