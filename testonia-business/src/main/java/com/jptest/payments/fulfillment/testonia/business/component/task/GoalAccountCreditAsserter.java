package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.testng.Assert;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.sv.api.rest.resources.CurrentAccount;

/**
 * Validating holding account .
 */
public class GoalAccountCreditAsserter extends BaseAsserter {

	@Override
	public void validate(Context context) {
		Object obj = getDataFromContext(context, ContextKeys.GOAL_ACCOUNT_CREDIT_TASK_KEY.getName());
		Assert.assertTrue(obj instanceof CurrentAccount);
		CurrentAccount goalAccount = (CurrentAccount) obj;
		Assert.assertNotNull(goalAccount.getSubBalances());
		Assert.assertTrue(Double.parseDouble(goalAccount.getSubBalances().get(0).getAmount().getValue()) > 0);
	}

}
