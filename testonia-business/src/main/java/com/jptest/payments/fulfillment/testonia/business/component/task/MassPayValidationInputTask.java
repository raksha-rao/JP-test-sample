package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * Input for MassPay validation
 */
public class MassPayValidationInputTask {

	private BigInteger decyrptTransactionId, debitPaymentSideId;

	private String validateStatus;

    private User sender, receiver;
	Context context;
    private FulfillPaymentPlanOptions fulfillmentOptions;

    public MassPayValidationInputTask(User sender, User receiver, BigInteger decyrptTransactionId,
			Context context, FulfillPaymentPlanOptions fulfillmentOptions, BigInteger debitPaymentSideId) {

        this.sender = sender;
        this.receiver = receiver;
        this.decyrptTransactionId = decyrptTransactionId;
        this.context = context;
        this.fulfillmentOptions = fulfillmentOptions;
        this.validateStatus = (String) context.getData("VALIDATION_STATUS");
		this.debitPaymentSideId = debitPaymentSideId;
    }

    public BigInteger getDecyrptTransactionId() {
        return decyrptTransactionId;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Context getContext() {
        return context;
    }

    public FulfillPaymentPlanOptions getFulfillmentOptions() {
        return fulfillmentOptions;
    }

    public String getValidateStatus() {
        return validateStatus;
    }

    public void setValidateStatus(String validateStatus) {
        this.validateStatus = validateStatus;
    }

	public BigInteger getDebitPaymentSideId() {
		return debitPaymentSideId;
	}

	public void setDebitPaymentSideId(BigInteger debitPaymentSideId) {
		this.debitPaymentSideId = debitPaymentSideId;
	}


}
