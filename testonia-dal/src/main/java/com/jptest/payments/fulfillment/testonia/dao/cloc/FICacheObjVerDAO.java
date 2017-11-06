package com.jptest.payments.fulfillment.testonia.dao.cloc;

import javax.inject.Singleton;

/**
 * DAO for UPDATE FI_CACHE_OBJ_VER table of CLOC DB
 *
 * @JP Inc.
 */
@Singleton
public class FICacheObjVerDAO extends ClockDao {

	private static final String ACCOUNT_NUMBER = "{account_number}";

	private static final String UPDATE_FI_CACHE_OBJ_VER = "UPDATE FI_CACHE_OBJ_VER set value = value + 1 where account_number = {account_number}";

    /**
     * Updates FI_CACHE_OBJ_VER table for the given account number
     * @param accountNumber
     * @return number of rows impacted
     */
	public int updateFiCache(final String accountNumber) {
		final String updateQuery = UPDATE_FI_CACHE_OBJ_VER.replace(ACCOUNT_NUMBER, accountNumber);

		return this.dbHelper.executeUpdateQuery(this.getDatabaseName(), updateQuery);
	}
}
