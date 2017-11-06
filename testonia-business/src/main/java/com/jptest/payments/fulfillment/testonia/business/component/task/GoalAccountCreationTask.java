package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import com.jptest.payments.fulfillment.testonia.bridge.StoredValueServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.sv.api.rest.resources.CurrentAccount;
import com.jptest.sv.api.rest.resources.CurrentAccountCreate;

/**
 * Adding a holding account for user with the specified currency code.
 */

public class GoalAccountCreationTask extends BaseTask<CurrentAccount> {
	@Inject
	private StoredValueServBridge storedValueServBridge;

	@Override
	public CurrentAccount process(Context context) {

		User user = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());

		CurrentAccountCreate createRequest = new CurrentAccountCreate();
		createRequest.setAccountType("STANDARD_GOAL_ACCOUNT");
		createRequest.setCurrencyCode("USD");

		CurrentAccount currentAccount = storedValueServBridge.createAccount(user.getAccountNumber(), createRequest);

		return currentAccount;
	}
}
