package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.PaymentDataProcessor;
import com.jptest.payments.ProcessDataRequest;
import com.jptest.payments.ProcessDataResponse;

/**
 * Bridge to persist the ProcessDataRequest object into the database using transactioneventprocessor
 * service.
 * @JP Inc.
 * 
 */
@Named
public class TransactionEventProcessorBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionEventProcessorBridge.class);

    @Inject
    @Named("transactioneventprocessorserv")
    private PaymentDataProcessor paymentDataProcessor;

    public ProcessDataResponse processData(ProcessDataRequest request) {
        LOGGER.debug("process_data request: {}", printValueObject(request));
        ProcessDataResponse response = paymentDataProcessor.process_data(request);
        LOGGER.debug("process_data response: {}", printValueObject(response));
        return response;
    }

}
