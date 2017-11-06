package com.jptest.payments.fulfillment.testonia.business.component.task;

import com.jptest.payments.payment_message.PaymentMessagePb;

/**
 * Validating the PaymentMessage response
 */
public interface PaymentMessageResponseAsserter {

    void validateResponse(PaymentMessagePb.PaymentMessage paymentMessage, String validationString);
}
