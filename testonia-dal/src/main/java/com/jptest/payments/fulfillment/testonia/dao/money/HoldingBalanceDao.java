package com.jptest.payments.fulfillment.testonia.dao.money;

import java.util.Map;

import javax.inject.Singleton;

/**
 * Represents HOLDING_BALANCE table of MONEY database
 */
@Singleton
public class HoldingBalanceDao extends MoneyDao {

	private static final String GET_HOLDING_ID_FOR_ACCT_CURRENCY_AMOUNT_QUERY = "SELECT HOLDING_ID FROM HOLDING_BALANCE WHERE ACCOUNT_NUMBER = {account_number} AND CURRENCY_CODE = '{currency_code}' AND AMOUNT = {amount}";
	private static final String ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{account_number}";
	private static final String CURRENCY_CODE_REPLACEMENT_TOKEN = "{currency_code}";
	private static final String AMOUNT_REPLACEMENT_TOKEN = "{amount}";
	private static final String HOLDING_ID_COL = "HOLDING_ID";

	private static final String BALANCE_TYPE_TOKEN = "{holding_balance_type}";
	private static final String GET_COUNT_FOR_ACCT_TYPE_QUERY = "select count(*) as cnt from holding_balance where account_number = {account_number} "
			+ "and holding_balance_type = '{holding_balance_type}'";

	/**
	 * queries the HOLDING_BALANCE table and returns the table id back.
	 *
	 * @param accountNumber
	 * @param currencyCode
	 * @param amount
	 * @return
	 */
	public String getIdFromHoldingBalance(String accountNumber, String currencyCode, String amount) {
		final String query = GET_HOLDING_ID_FOR_ACCT_CURRENCY_AMOUNT_QUERY
				.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber).replace(AMOUNT_REPLACEMENT_TOKEN, amount)
				.replace(CURRENCY_CODE_REPLACEMENT_TOKEN, currencyCode);
		Map<String, Object> queryResult = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
		return queryResult != null ? getString(queryResult.get(HOLDING_ID_COL)) : null;
	}

	/**
	 * Gets count of rows in holding_balance table for given account number and
	 * balance type
	 *
	 * @param accountNumber
	 * @param balanceType
	 * @return
	 */
	public Integer getCount(String accountNumber, String balanceType) {
		final String query = GET_COUNT_FOR_ACCT_TYPE_QUERY.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber)
				.replace(BALANCE_TYPE_TOKEN, balanceType);
		final Map<String, Object> queryResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
				query);
		final Integer count = getInteger(queryResult.get(COUNT_COL));
		return count;
	}

}
