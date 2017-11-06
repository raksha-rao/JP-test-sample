package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.io.IOException;
import java.util.Random;
import javax.inject.Inject;
import com.jptest.payments.fulfillment.testonia.business.service.DisburseFundsUnilateralEmailPaymentMessageCreatorService;
import com.jptest.payments.fulfillment.testonia.business.service.DisburseFundsUnilateralPhoneNoPaymentMessageCreatorService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;


/**
 * This task will build the PaymentMessage request for DISBURSE_FUNDS Unilateral transaction with email id or phone no
 * and with no associated jptest account
 */
public class PaymentMessageUnilateralRequestBuilderTask extends BaseTask<PaymentMessage> {
	private final static String DISBURSE_FUNDS_OPERATION = "DISBURSE_FUNDS";
    private final static String EMAIL_ADDRESS = "EMAIL_ADDRESS";
    private final static String PHONE_NUMBER = "PHONE_NUMBER";

	@Inject
    protected DisburseFundsUnilateralEmailPaymentMessageCreatorService disburseFundsUnilateralEmailPaymentMessageCreatorService;

    @Inject
    protected DisburseFundsUnilateralPhoneNoPaymentMessageCreatorService disburseFundsUnilateralPhoneNoPaymentMessageCreatorService;

	private FulfillPaymentPlanOptions input;
    private String unilateralType;

    public PaymentMessageUnilateralRequestBuilderTask(final TestCaseInputData inputData, String unilateralType) {
		input = inputData.getFulfillPaymentPlanOptions();
		Random random = new Random();
		int randomNumber = random.nextInt(10000);
		input.setIdempotencyToken(
				"ECT:EC-0PY02594MV646805K.SHA1:1.0018.0000000000000187873554848" + randomNumber + ".");
        this.unilateralType = unilateralType;
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
        if (input.getFulfillmentType().value().equals(DISBURSE_FUNDS_OPERATION)) {
            if (unilateralType.equals(EMAIL_ADDRESS)) {
                output = disburseFundsUnilateralEmailPaymentMessageCreatorService.buildPaymentMessage(input, context);
            }
            else if (unilateralType.equals(PHONE_NUMBER)) {
                output = disburseFundsUnilateralPhoneNoPaymentMessageCreatorService.buildPaymentMessage(input, context);
            }
        }
        return output;
	}

}
