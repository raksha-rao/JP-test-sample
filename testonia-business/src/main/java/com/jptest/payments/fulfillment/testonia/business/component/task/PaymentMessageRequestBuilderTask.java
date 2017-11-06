package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.io.IOException;
import java.util.Random;

import javax.inject.Inject;

import com.jptest.payments.fulfillment.testonia.business.service.DisburseFundsPaymentMessageCreatorService;
import com.jptest.payments.fulfillment.testonia.business.service.ReleaseFundsPaymentMessageCreatorService;
import com.jptest.payments.fulfillment.testonia.business.service.ReserveFundsPaymentMessageCreatorService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;

/**
 * This task will build the PaymentMessage request for the given input dat for
 * the following API RESERVE_FUNDS,"DISBURSE_FUNDS", & RELEASE_FUNDS.
 * 
 */
public class PaymentMessageRequestBuilderTask extends BaseTask<PaymentMessage> {
	private final static String RESERVE_FUNDS_OPERATION = "RESERVE_FUNDS";
	private final static String DISBURSE_FUNDS_OPERATION = "DISBURSE_FUNDS";
	private final static String RELEASE_FUNDS_OPERATION = "RELEASE_FUNDS";

	@Inject
	protected ReserveFundsPaymentMessageCreatorService reserveFundsPaymentMessage;

	@Inject
	protected DisburseFundsPaymentMessageCreatorService disburseFundsPaymentMessage;

	@Inject
	protected ReleaseFundsPaymentMessageCreatorService releaseFundsPaymentMessage;

	private FulfillPaymentPlanOptions input;

	public PaymentMessageRequestBuilderTask(final TestCaseInputData inputData) {
		input = inputData.getFulfillPaymentPlanOptions();
		Random random = new Random();
		int randomNumber = random.nextInt(10000);
		input.setIdempotencyToken(
				"ECT:EC-0PY02594MV646805K.SHA1:1.0018.0000000000000187873554848KAVI" + randomNumber + ".");
	}

	@Override
	public PaymentMessage process(Context context) {
		try {
			return buildRequest(input, context);
		} catch (Exception e) {
			throw new TestExecutionException("Failed creating PaymentMessageRequest", e);
		}

	}

	private PaymentMessage buildRequest(FulfillPaymentPlanOptions input, Context context) throws IOException {
		return getPaymentMessage(input, context);
	}

	private PaymentMessage getPaymentMessage(FulfillPaymentPlanOptions input, Context context) {
		PaymentMessage output = null;
		switch (input.getFulfillmentType().value()) {

		case RESERVE_FUNDS_OPERATION:
			output = reserveFundsPaymentMessage.buildPaymentMessage(input, context);
			break;
		case DISBURSE_FUNDS_OPERATION:
			output = disburseFundsPaymentMessage.buildPaymentMessage(input, context);
			break;
		case RELEASE_FUNDS_OPERATION:
			output = releaseFundsPaymentMessage.buildPaymentMessage(input, context);
			break;
		default:
			break;
		}
		return output;
	}

}
