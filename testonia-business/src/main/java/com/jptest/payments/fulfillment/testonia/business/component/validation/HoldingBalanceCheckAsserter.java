package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.Objects;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.money.WUserHoldingDao;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions.CurrencyData;
import com.jptest.payments.fulfillment.testonia.model.ValidationsInput;
import com.jptest.qi.rest.domain.pojo.Fund;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * This component queries the MONEY DB and checks whether the entry has been
 * added to HOLDING_BALANCE Table for the transaction which has a hold on it.
 * This check basically represents whether we are correctly holding the money in
 * this satellite table till the hold has been released.
 */
public class HoldingBalanceCheckAsserter extends BaseAsserter implements TimeoutAwareComponent {
	private static final Logger LOGGER = LoggerFactory.getLogger(HoldingBalanceCheckAsserter.class);
	CurrencyData txnAmount;
	Fund fund;
	Integer buyerExpBalance = null;
	Integer sellerExpBalance = null;
	boolean buyerBalanceCheck = false;
	boolean sellerBalanceCheck = false;
	FulfillPaymentPlanOptions fulfillmentOptions;
	private final static String RESERVE_FUNDS_OPERATION = "RESERVE_FUNDS";
	private final static String DISBURSE_FUNDS_OPERATION = "DISBURSE_FUNDS";
	private final static String RELEASE_FUNDS_OPERATION = "RELEASE_FUNDS";

	@Inject
	private RetriableHoldingBalanceAsserter innerAsserter;

	public HoldingBalanceCheckAsserter(final FulfillPaymentPlanOptions fulfillPaymentPlanOptions,
			final ValidationsInput validationsInput) {
		this.fulfillmentOptions = fulfillPaymentPlanOptions;
		this.txnAmount = fulfillmentOptions.getTxnAmount();
		this.buyerBalanceCheck = validationsInput.isBuyerBalanceCheck();
		this.sellerBalanceCheck = validationsInput.isSellerBalanceCheck();
		if (buyerBalanceCheck)
			this.buyerExpBalance = Integer.valueOf(validationsInput.getBuyerExpectedBalance());
		if (sellerBalanceCheck)
			this.sellerExpBalance = Integer.valueOf(validationsInput.getSellerExpectedBalance());


	}

	@Override
	public void validate(Context context) {

		User sender = (User) getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
		User receiver = (User) getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());
		String buyerAccountNumber = sender.getAccountNumber();
		String sellerAccountNumber = null;
		if (receiver != null)
			sellerAccountNumber = receiver.getAccountNumber();
		fund = sender.getFund().get(0);
		if (Objects.isNull(buyerExpBalance))

			switch (fulfillmentOptions.getFulfillmentType().value()) {
			case RESERVE_FUNDS_OPERATION:
				buyerExpBalance = (fund.getFundsInCents() - Integer.parseInt(txnAmount.getAmount()));
				break;
			case DISBURSE_FUNDS_OPERATION:
				buyerExpBalance = fund.getFundsInCents();
				break;
			case RELEASE_FUNDS_OPERATION:
				buyerExpBalance = fund.getFundsInCents() + Integer.parseInt(txnAmount.getAmount());
				break;
			default:
				break;
			}
		if (buyerBalanceCheck)
			innerAsserter.execute(new HoldingBalanceInput(buyerAccountNumber, fund, buyerExpBalance));
		if (sellerBalanceCheck)
			innerAsserter.execute(new HoldingBalanceInput(sellerAccountNumber, fund, sellerExpBalance));

	}

	/**
	 * Adds the retry facet for executing query to get the id since the db entry
	 * is made by a daemon and it can take some time for it to kick in
	 * 
	 * @see RetriableTask for details of how the retry is performed.
	 */
	private static class RetriableHoldingBalanceAsserter
			extends RetriableTask<HoldingBalanceInput, HoldingBalanceInput> {

		@Inject
		WUserHoldingDao holdingBalanceDao;

		@Override
		protected boolean isDesiredOutput(HoldingBalanceInput output) {
			return output.getBalance() != null && output.getBalance().equals(output.getExpBalance());
		}

		@Override
		public HoldingBalanceInput retriableExecute(HoldingBalanceInput input) {
			Integer balance = 0;

			try {
				balance = holdingBalanceDao.getBalance(input.getAcctNum(), input.getFund().getCurrency());
				input.setBalance(balance);
			} catch (InterruptedException e) {
				LOGGER.warn("Printing exception : " + e.toString());
			}

			return input;

		}

		@Override
		protected HoldingBalanceInput onSuccess(HoldingBalanceInput input, HoldingBalanceInput output) {
			LOGGER.info("Balance is Matching.And Balance is:{}", output.balance);
			return output;
		}

		@Override
		protected HoldingBalanceInput onFailure(HoldingBalanceInput input, HoldingBalanceInput output) {
			LOGGER.warn("Balance is not Matching. And Balance is:{}", output.balance);
			return output;
		}

	}

	private static class HoldingBalanceInput {
		private String acctNum;
		private Fund fund;
		private Integer expBalance;
		private Integer balance;

		private HoldingBalanceInput(String acctNum, Fund fund, Integer expBalance) {
			super();
			this.fund = fund;
			this.acctNum = acctNum;
			this.expBalance = expBalance;

		}

		public String getAcctNum() {
			return acctNum;
		}

		public Fund getFund() {
			return fund;
		}

		public void setBalance(Integer balance) {
			this.balance = balance;
		}

		public Integer getBalance() {
			return balance;
		}

		public Integer getExpBalance() {
			return expBalance;
		}
	}

	@Override
	public long getTimeoutInMs() {
		// It can take this long to get the db entry in the table
		return 60 * 3000;
	}

}
