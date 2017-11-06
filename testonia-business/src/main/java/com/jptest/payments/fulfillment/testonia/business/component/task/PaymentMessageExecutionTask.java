package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.TestException;

import com.jptest.payments.fulfillment.testonia.bridge.TxnFulfillmentServBridge;
import com.jptest.payments.fulfillment.testonia.business.service.DisburseFundsPaymentMessageCreatorService;
import com.jptest.payments.fulfillment.testonia.business.service.ReserveFundsPaymentMessageCreatorService;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;
import com.jptest.payments.payment_message.TransactionPb.Transaction;

/**
 * This task will execute transfer & Disbursement PaymentMessage request and
 * will give PaymentMessge response.
 */
public class PaymentMessageExecutionTask extends BasePaymentExecutionTask<PaymentMessage> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentMessageExecutionTask.class);

	@Inject
	private TxnFulfillmentServBridge txnFulfillmentServBridge;
	@Inject
	protected ReserveFundsPaymentMessageCreatorService reserveFundsPaymentMessage;
	@Inject
	protected DisburseFundsPaymentMessageCreatorService disburseFundsPaymentMessage;

	@Override
	final protected PaymentMessage executeRequest(Context context) {
		try {

			PaymentMessage request = (PaymentMessage) getDataFromContext(context,
					ContextKeys.PAYMENT_MESSAGE_REQUEST_KEY.getName());
			Assert.assertNotNull(request);
			return txnFulfillmentServBridge.processPaymentMessage(request);
		} catch (Exception e) {
			LOGGER.error("Exception:", e);
			throw new TestExecutionException("Failed executing PaymentMessageExecutionTask", e);
		}

	}

	private Transaction getTransaction(PaymentMessage paymentMessage) {
		Transaction transaction = null;
		switch (paymentMessage.getMessageTypeCase()) {
		// Payment message type is same for Reserve & Release funds
		case TRANSFER:
			if (paymentMessage.getTransfer() != null) {
				transaction = paymentMessage.getTransfer().getTransactions(0);
			}
			break;
		case FINANCIAL_AUTHORIZATION:
			if (paymentMessage.getFinancialAuthorization() != null) {
				transaction = paymentMessage.getFinancialAuthorization().getTransactions(0);
			}
			break;

		default:
			break;
		}
		return transaction;
	}

	@Override
	final protected String getEncryptedTransactionId(PaymentMessage result) {
		Transaction transaction = getTransaction(result);
		if (transaction == null) {
			throw new TestException("transaction not found PaymentMessageExecutionTask");
		}
		return transaction.getTransactionHandle();
	}

}
