package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import org.testng.Assert;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;
import com.jptest.payments.product.PaymentServicesCategoryPb.PaymentServicesCategory;

public class BasicPaymentMessageResponseAsserter extends BaseAsserter {

	private String validationString;

	/**
	 * Validating the PamentMessage response is success.
	 */
	@Inject
	protected ReserveFundsResponseAsserter reserveFundsResponseAsserter;

	@Inject
	protected DisburseFundsResponseAsserter disburseFundsResponseAsserter;

	@Inject
	protected ReleaseFundsResponseAsserter releaseFundsResponseAsserter;

	public BasicPaymentMessageResponseAsserter(final String validationString) {
		this.validationString = validationString;
	}

	@Override
	public void validate(Context context) {
		PaymentMessage paymentMessageResponse = (PaymentMessage) getDataFromContext(context,
				ContextKeys.PAYMENT_MESSAGE_RESPONSE_KEY.getName());
		Assert.assertNotNull(paymentMessageResponse, this.getClass().getSimpleName()
				+ ". validateResponse() failed for paymentMessageResponse should not be null");

		validateResponse(paymentMessageResponse);

	}

	private void validateResponse(PaymentMessage paymentMessageResponse) {

		switch (paymentMessageResponse.getMessageTypeCase()) {

		case TRANSFER:
			// Payment message type is same for Reserve & Release funds
			PaymentServicesCategory paymentServicesCategory = paymentMessageResponse.getTransfer().getPaymentContext()
					.getProductConfiguration().getPaymentServicesCategory();
			if (null != paymentServicesCategory && PaymentServicesCategory.RESERVE_FUNDS == paymentServicesCategory) {
				reserveFundsResponseAsserter.validateResponse(paymentMessageResponse, validationString);
			} else if (null != paymentServicesCategory
					&& PaymentServicesCategory.RELEASE_FUNDS == paymentServicesCategory) {
				releaseFundsResponseAsserter.validateResponse(paymentMessageResponse, validationString);
			}
			break;
		case FINANCIAL_AUTHORIZATION:
			disburseFundsResponseAsserter.validateResponse(paymentMessageResponse, validationString);
			break;
		default:
			break;

		}

	}

}
