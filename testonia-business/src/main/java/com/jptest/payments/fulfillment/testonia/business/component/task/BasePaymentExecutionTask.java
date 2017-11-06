package com.jptest.payments.fulfillment.testonia.business.component.task;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * Basic PaymentMessage Request Builder Builds the request based on Input
 */
public abstract class BasePaymentExecutionTask<T> extends BaseTask<T> {

	@Override
	public T process(Context context) {
		T result = executeRequest(context);
		String txnId = getEncryptedTransactionId(result);
		context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), txnId);
		return result;
	}

	protected abstract T executeRequest(Context context);

	protected abstract String getEncryptedTransactionId(T result);
}
