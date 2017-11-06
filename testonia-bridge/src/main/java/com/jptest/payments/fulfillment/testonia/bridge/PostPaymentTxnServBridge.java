package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.money.PaymentServ;
import com.jptest.money.RefundPaymentRequest;
import com.jptest.money.RefundPaymentResponse;


/**
 * Represents bridge for postpaymenttxnserv API calls
 */
@Singleton
public class PostPaymentTxnServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostPaymentTxnServBridge.class);

    @Inject
    @Named("postpaymenttxnserv")
    private PaymentServ client;

    public RefundPaymentResponse refundPayment(final RefundPaymentRequest request) {
        LOGGER.info("refund_payment request: {}", printValueObject(request));
        final RefundPaymentResponse response = this.client.refund_payment(request);
        LOGGER.info("refund_payment response: {}", printValueObject(response));
        return response;
    }

}
