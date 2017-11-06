package com.jptest.payments.fulfillment.testonia.dao.cloc;

import java.util.Map;

import javax.inject.Singleton;

/**
 * Represents wnotify_add table of CLOC database
 */

@Singleton
public class WNotifyAddressDAO extends ClockDao {

    private static final String NEXTVAL_WNOTIFY_ADDRESS = "select wnotify_addr_id_seq.nextval from wdual";
    private static final String SELECT_WNOTIFY_ADDRESS = "select id from wnotify_addr where account_number = '{acc_number}'";
    private static final String UPDATE_WNOTIFY_ADDRESS = "update wnotify_addr set address = '{ipnURL}' where account_number = '{acc_number}'";
    private static final String INSERT_WNOTIFY_ADDRESS = "insert into wnotify_addr (account_number, time_created, type, address, flags, id)"
            + "values ('{acc_number}','{unix_time}',1,'{ipnURL}',0,'{id}')";
    private static final String IPN_URL = "{ipnURL}";
    private static final String ACCOUNT_NUMBER = "{acc_number}";
    private static final String NEXTVAL_ID = "{id}";
    private static final String UNIX_TIME = "{unix_time}";

    // Column names
    private static final String NEXTVAL_COL = "NEXTVAL";

    public Map<String, Object> selectIPNURL(String accountNumber) {
        String selectQuery = SELECT_WNOTIFY_ADDRESS.replace(ACCOUNT_NUMBER, accountNumber);
        return dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), selectQuery);
    }

    public void updateIPNURL(String ipnURL, String accountNumber) {
        String updateQuery = UPDATE_WNOTIFY_ADDRESS.replace(IPN_URL, ipnURL)
                .replace(ACCOUNT_NUMBER, accountNumber);
        dbHelper.executeUpdateQuery(getDatabaseName(), updateQuery);
    }

    public void insertIPNURL(String ipnURL, String accountNumber) {

        String unixTime = Long.toString(System.currentTimeMillis() / 1000L);
        String NextVal = getNextVal();
        String InsertQuery = INSERT_WNOTIFY_ADDRESS.replace(ACCOUNT_NUMBER, accountNumber)
                .replace(UNIX_TIME, unixTime)
                .replace(IPN_URL, ipnURL)
                .replace(NEXTVAL_ID, NextVal);
        dbHelper.executeUpdateQuery(getDatabaseName(), InsertQuery);
    }

    public String getNextVal() {
        String selectQuery = NEXTVAL_WNOTIFY_ADDRESS;
        Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), selectQuery);
        return result != null ? getString(result.get(NEXTVAL_COL)) : null;
    }
}
