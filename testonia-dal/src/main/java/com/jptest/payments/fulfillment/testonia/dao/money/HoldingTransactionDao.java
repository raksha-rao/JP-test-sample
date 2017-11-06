package com.jptest.payments.fulfillment.testonia.dao.money;

import java.util.Map;
import javax.inject.Singleton;


/**
 * DAO for HoldingTransaction table of MONEY DB
 */
@Singleton
public class HoldingTransactionDao extends MoneyDao {

    private static final String GET_WHOLDING_TRANSACTION_QUERY = "SELECT count(*) FROM HOLDING_TRANSACTION where account_number = {accountNumber}";

    // tokens
    private static final String ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{accountNumber}";

    /**
     * Queries WUSER_HOLDING table for input transaction
     *
     * @param transactions
     * @return
     */
    public Integer getCount(String accountNumber) {
        final String query = GET_WHOLDING_TRANSACTION_QUERY.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
                accountNumber);
        final Map<String, Object> queryResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
                query);
        final Integer count = Integer.parseInt(queryResult.get("COUNT(*)").toString());
        return count;
    }

}
