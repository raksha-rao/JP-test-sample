package com.jptest.payments.fulfillment.testonia.business.component.task;



import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import com.jptest.payments.payment_message.PaymentMessagePb;

/**
 * Validating the ReserveFunds PaymentMessage response
 */

public class ReserveFundsResponseAsserter implements PaymentMessageResponseAsserter {

    public void validateResponse(PaymentMessagePb.PaymentMessage paymentMessage, String validationString) {
        Assert.assertNotNull(
                StringUtils.trimToNull(paymentMessage.getTransfer().getTransactions(0).getTransactionHandle()),
				this.getClass().getSimpleName()
						+ ". validateResponse() failed for Transaction Handle should not be null");
        Assert.assertNotNull(paymentMessage.getTransfer().getTransactions(0).getStatusInfo().getTransactionStatusInfo(),
				this.getClass().getSimpleName()
						+ ". validateResponse() failed for Transaction Status Info should not be null");
        Assert.assertEquals(paymentMessage.getStatus().toString(), validationString);
        Assert.assertNotNull(
                paymentMessage.getTransfer().getTransactions(0).getPaymentInstructions(1).getInstrumentHolder(),
				this.getClass().getSimpleName()
						+ ". validateResponse() failed for InstrumentHolder should not be null");
        Assert.assertNotNull(
                paymentMessage.getTransfer().getTransactions(0).getPaymentInstructions(1).getReferenceTransactionId(),
				this.getClass().getSimpleName()
						+ ". validateResponse() failed for ReferenceTransactionId should not be null");
        Assert.assertNotNull(
                paymentMessage.getTransfer().getTransactions(0).getPaymentInstructions(1).getReferenceTransactionTime(),
				this.getClass().getSimpleName()
						+ ". validateResponse() failed for ReferenceTransactionTime should not be null");
        Assert.assertNotNull(paymentMessage.getTransfer().getTransactions(0).getPaymentInstructions(1)
				.getIntermediaryAgentReference(),
				this.getClass().getSimpleName()
						+ ". validateResponse() failed for IntermediaryAgentReferencee should not be null");
        Assert.assertNotNull(paymentMessage.getTransfer().getTransactions(0).getPaymentInstructions(1)
				.getInstructionContext().getParticipantTransactionId(),
				this.getClass().getSimpleName()
						+ ". validateResponse() failed for ParticipantTransactionIde should not be null");

    }
}
