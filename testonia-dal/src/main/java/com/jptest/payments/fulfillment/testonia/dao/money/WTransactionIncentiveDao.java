package com.jptest.payments.fulfillment.testonia.dao.money;

import java.math.BigInteger;
import java.util.Map;

import javax.inject.Singleton;

/**
 * DAO for wtransaction_incentive table of MONEY DB
 */
@Singleton
public class WTransactionIncentiveDao extends MoneyDao {

    private static final String GET_TRANSACTION_COUNT_QUERY = "select count(*) CNT from wtransaction_incentive where (transaction_id, PARENT_TRANS_ID) = "
            + "(select ID,PARENT_ID from WTRANSACTION where account_number = '{accountNumber}' and type='C' and AMOUNT='{amount}') and type='M' "
            + "and account_number= '{accountNumber}' and auth_id = '{authId}' and incentive_id = '{msbId}' and amount_used = '{amount}'";

    // tokens
    private static final String ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{accountNumber}";
    private static final String AMOUNT_REPLACEMENT_TOKEN = "{amount}";
    private static final String AUTH_ID_REPLACEMENT_TOKEN = "{authId}";
    private static final String MSB_ID_REPLACEMENT_TOKEN = "{msbId}";

    // Column names
    private static final String COUNT_COL = "CNT";

    public int getTransactionCount(BigInteger accountNumber, Long amount, String authId, BigInteger msbId) {
        String query = GET_TRANSACTION_COUNT_QUERY
                .replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
                .replace(AMOUNT_REPLACEMENT_TOKEN, amount.toString())
                .replace(AUTH_ID_REPLACEMENT_TOKEN, authId)
                .replace(MSB_ID_REPLACEMENT_TOKEN, msbId.toString());
        Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
        return getInteger(result.get(COUNT_COL));
    }
}
