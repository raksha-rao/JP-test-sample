package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.jptest.payments.fulfillment.testonia.bridge.StoredValueServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.sv.api.rest.resources.CreditRequest;
import com.jptest.sv.api.rest.resources.CurrentAccount;
import com.jptest.sv.api.rest.resources.LegacyBalanceFields;
import com.jptest.sv.api.rest.resources.LegalEntity;
import com.jptest.sv.api.rest.resources.Money;
import com.jptest.sv.api.rest.resources.SubBalance;

public class GoalAccountCreditTask extends BaseTask<CurrentAccount> {

	/**
	 * Adding a amount to the holding account for user with the specified
	 * currency code.
	 */

	@Inject
	private StoredValueServBridge storedValueServBridge;

	@Override
	public CurrentAccount process(Context context) {

		CurrentAccount currentAccount = (CurrentAccount) context
				.getData(ContextKeys.GOAL_ACCOUNT_CREATION_TASK_KEY.getName());

		CreditRequest creditRequest = new CreditRequest();

		String accountId = currentAccount.getId();
		SubBalance credit = new SubBalance();
		credit.setSubBalanceType("AVAILABLE");
		Money amount = new Money();
		amount.setValue("100.00");
		amount.setCurrencyCode("USD");
		credit.setAmount(amount);
		credit.setAffectsAggregate(true);
		List<SubBalance> credits = new ArrayList<>();
		credits.add(credit);
		creditRequest.setCredits(credits);
		LegacyBalanceFields legacyBalanceFields = new LegacyBalanceFields();
		legacyBalanceFields.setLegalEntity(LegalEntity.INC);
		legacyBalanceFields.setLogNegativeBalance(false);
		creditRequest.setLegacyBalanceFields(legacyBalanceFields);

		return storedValueServBridge.creditAccount(currentAccount.getAccountNumber(), accountId, creditRequest);
	}

}
