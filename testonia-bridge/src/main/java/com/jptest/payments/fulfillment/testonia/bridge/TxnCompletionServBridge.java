package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;
import com.jptest.payments.paymentcompletion.api.PaymentCompletionService;
import com.jptest.transfer.transfercompletion.api.TransferCompletionService;
import com.jptest.transfer.transfercompletion.model.CompleteTransferMessage;
import com.jptest.transfer.transfercompletion.model.CompleteTransferResponse;
import com.jptest.transfer.transfercompletion.model.ReverseTransferMessage;
import com.jptest.transfer.transfercompletion.model.ReverseTransferResponse;
import com.jptest.transfer.transfercompletion.model.completeTransferReviewMessage;
import com.jptest.transfer.transfercompletion.model.completeTransferReviewResponse;


@Singleton
public class TxnCompletionServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(TxnCompletionServBridge.class);

    @Inject
    @Named("txncompletionserv")
    private PaymentCompletionService paymentCompletion;

    @Inject
    @Named("txncompletionserv")
    private TransferCompletionService transfer;

    public PaymentMessage processPaymentCompletion(final PaymentMessage paymentMessage) {
        LOGGER.info("processPaymentCompletion request: {}", printJsonObject(paymentMessage));
        final PaymentMessage response = this.paymentCompletion.doPaymentCompletion(paymentMessage);
        LOGGER.info("processPaymentCompletion response : {}", printJsonObject(response));
        return response;
    }

    public CompleteTransferResponse completeTransfer(final CompleteTransferMessage completeTransferMessage) {
        LOGGER.info("completeTransfer request: {}", printJsonObject(completeTransferMessage));
        final CompleteTransferResponse response = this.transfer
                .completeTrasfer(completeTransferMessage);
        LOGGER.info("completeTransfer response: {}", printJsonObject(response));
        return response;

    }

    public completeTransferReviewResponse completeTransferReview(final completeTransferReviewMessage request) {
        LOGGER.info("completeTransferReview request: {}", printJsonObject(request));
        final completeTransferReviewResponse response = this.transfer.completeTransferReview(request);
        LOGGER.info("completeTransferReview response : {}", printJsonObject(response));
        return response;
    }

    public ReverseTransferResponse reverseTransfer(final ReverseTransferMessage request) {
        LOGGER.info("reverseTransfer request: {}", printJsonObject(request));
        final ReverseTransferResponse response = this.transfer.reverseTransfer(request);
        LOGGER.info("reverseTransfer request: {}", printJsonObject(response));
        return response;

    }
}
