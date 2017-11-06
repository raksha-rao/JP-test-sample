package com.jptest.payments.fulfillment.testonia.business.component.validation;

import javax.inject.Inject;

import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.HoldingBalanceDao;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions.CurrencyData;
import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * This component queries the MONEY DB and checks whether the entry has been
 * added to HOLDING_BALANCE Table for the transaction which has a hold on it.
 * This check basically represents whether we are correctly holding the money in
 * this satellite table till the hold has been released.
 */
public class HoldingBalanceIdStatusAsserter extends BaseAsserter implements TimeoutAwareComponent {
	CurrencyData txnAmount;
	@Inject
	private RetriableHoldingBalanceAsserter innerAsserter;

	public HoldingBalanceIdStatusAsserter(final TestCaseInputData inputData) {
		txnAmount = inputData.getFulfillPaymentPlanOptions().getTxnAmount();

	}

	@Override
	public void validate(Context context) {

		User buyer = (User) getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
		String accountNumber = buyer.getAccountNumber();

		String currency = txnAmount.getCurrencyCode();
		String amount = txnAmount.getAmount();
		innerAsserter.execute(new HoldingBalanceInput(accountNumber, currency, amount));
	}

	/**
	 * Adds the retry facet for executing query to get the id since the db entry
	 * is made by a daemon and it can take some time for it to kick in
	 * 
	 * @see RetriableTask for details of how the retry is performed.
	 */
	private static class RetriableHoldingBalanceAsserter extends RetriableTask<HoldingBalanceInput, String> {

		@Inject
		HoldingBalanceDao holdingBalanceDao;

		@Override
		public boolean isDesiredOutput(String output) {
			if (output != null) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String retriableExecute(HoldingBalanceInput input) {
			return holdingBalanceDao.getIdFromHoldingBalance(input.getAcctNum(), input.getCurrency(),
					input.getAmount());

		}

		@Override
		protected String onSuccess(HoldingBalanceInput input, String output) {
			return output;
		}

		@Override
		protected String onFailure(HoldingBalanceInput input, String output) {
			return output;
		}

	}

	private static class HoldingBalanceInput {
		private String acctNum, currency, amount;

		private HoldingBalanceInput(String acctNum, String currency, String amount) {
			super();
			this.acctNum = acctNum;
			this.currency = currency;
			this.amount = amount;
		}

		public String getAcctNum() {
			return acctNum;
		}

		public String getCurrency() {
			return currency;
		}

		public String getAmount() {
			return amount;
		}

	}

	@Override
	public long getTimeoutInMs() {
		// It can take this long to get the db entry in the table
		return 60 * 3000;
	}

}
