package com.jptest.payments.fulfillment.testonia.dao.cloc;

import javax.inject.Singleton;

/**
 * DAO for wuser table of CLOC DB
 */
@Singleton
public class WUserDAO extends ClockDao {

    private static final String ACCOUNT_NUMBER = "{account_number}";
    private static final String FIRST_NAME = "{first_name}";

    private static final String UPDATE_FIRST_NAME_QUERY  = 
    		"update wuser set first_name = '{first_name}' where account_number = '{account_number}'";

    public void updateFirstName(final String accountNumber, final String firstName) {
    	final String updateQuery = UPDATE_FIRST_NAME_QUERY.replace(ACCOUNT_NUMBER, accountNumber).
    			replace(FIRST_NAME, firstName);
    	this.dbHelper.executeUpdateQuery(this.getDatabaseName(), updateQuery);
    }
}
