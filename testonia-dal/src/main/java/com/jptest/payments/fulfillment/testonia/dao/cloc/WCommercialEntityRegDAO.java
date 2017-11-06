package com.jptest.payments.fulfillment.testonia.dao.cloc;

import java.util.Map;

import javax.inject.Singleton;

/**
 * DAO for wcommercial_entity_reg table of CLOC DB
 */
@Singleton
public class WCommercialEntityRegDAO extends ClockDao {

	private static final String NEXTVAL_WCOMMERCIAL_ID = "select WCOMMERCIAL_ENTITY_REG_SEQ.nextval from dual";
	private static final String INSERT_WCOMMERCIAL_ENTITY_REG = "insert into wcommercial_entity_reg"
			+ "(id,account_number,time_created,status_code,risk_cat,reason_flags,flags,registration_flags,tou_flags,mask,wf_return_status_code,hsbc_return_status_code)"
			+ "values('{id}','{account_number}','{unix_time}',268435456,0,0,56,null,7,15,0,0)";

	private static final String ACCOUNT_NUMBER = "{account_number}";
	private static final String UNIX_TIME = "{unix_time}";
	private static final String NEXTVAL_ID = "{id}";


	public int insertCommercialEntity(final String accountNumber) {

		String unixTime = Long.toString(System.currentTimeMillis() / 1000L);
		String NextVal = getNextVal();

		final String isertQuery = INSERT_WCOMMERCIAL_ENTITY_REG.replace(ACCOUNT_NUMBER, accountNumber)
				.replace(UNIX_TIME, unixTime).replace(NEXTVAL_ID, NextVal);

		return this.dbHelper.executeUpdateQuery(this.getDatabaseName(), isertQuery);
	}

	public String getNextVal() {
		String selectQuery = NEXTVAL_WCOMMERCIAL_ID;
		Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), selectQuery);
		return result != null ? getString(result.get("NEXTVAL")) : null;
	}

}
