package com.jptest.payments.fulfillment.testonia.business.service;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;

/**
 * A common interface to generate PaymentMessage for reserve_funds, release_funds and disburse_funds API
 */
public interface PaymentMessageCreatorService {

	PaymentMessage buildPaymentMessage(FulfillPaymentPlanOptions input, final Context context);
}
