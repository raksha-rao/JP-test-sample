package com.jptest.payments.fulfillment.testonia.dao.cloc;

import com.google.inject.Singleton;

@Singleton
public class WMerchantSettlementDAO extends ClockDao {

    private static final String INSERT_WMERCHANT_SETTLE = "insert into wmerchant_settlement(account_number,flags,type,time_created,PYPL_TIME_TOUCHED) values ('{account_number}',4,'N','{unix_time}','{unix_time}')";
    private static final String ACCOUNT_NUMBER = "{account_number}";
    private static final String UNIX_TIME = "{unix_time}";

    /**
     * Inserts WMERCHANT_SETTLEMENT table for input transaction
     *
     * @param accountNumber
     * @return
     */
    public void insertWMerchantSettlement(final String accountNumber) {
        final String unixTime = Long.toString(System.currentTimeMillis() / 1000L);
        final String insertQuery = INSERT_WMERCHANT_SETTLE.replace(ACCOUNT_NUMBER, accountNumber).replace(UNIX_TIME,
                unixTime);
        this.dbHelper.executeUpdateQuery(this.getDatabaseName(), insertQuery);
    }

}
