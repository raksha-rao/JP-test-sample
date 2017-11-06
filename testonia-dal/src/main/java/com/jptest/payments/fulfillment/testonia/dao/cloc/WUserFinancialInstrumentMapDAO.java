package com.jptest.payments.fulfillment.testonia.dao.cloc;

import javax.inject.Singleton;

/**
 * DAO for wuser_financial_instrument_map table of CLOC DB
 *
 * @JP Inc.
 */
@Singleton
public class WUserFinancialInstrumentMapDAO extends ClockDao {

	private static final String ACCOUNT_NUMBER = "{account_number}";
	private static final String ACH_ID = "{ach_id}";

	// make the given ach-id default instrument
	private static final String INSERT_DEFAULT_INSTRUMENT_QUERY = "INSERT INTO wuser_financial_instrument_map "
			+ "(id, account_number, financial_instrument_id, financial_instrument_type, rank, status) "
			+ "VALUES (wuser_fin_instr_map_seq.nextval" + ", {account_number}, {ach_id}, 'R', 1, 'A')";

	/**
	 * Inserts the given ach-id into wuser_financial_instrument_map table to
	 * make it the as default instrument for the given account number.
	 *
	 * @param accountNumber
	 * @param achId
	 * @return number of rows impacted
	 */
	public int updateDefaultInstrument(final String accountNumber, final String achId) {
		final String updateQuery = INSERT_DEFAULT_INSTRUMENT_QUERY.replace(ACCOUNT_NUMBER, accountNumber)
				.replace(ACH_ID, achId);

		return this.dbHelper.executeUpdateQuery(this.getDatabaseName(), updateQuery);
	}
}
