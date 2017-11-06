package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

/**
 * DAO for postpaid_tab_txn_map table of MONEY DB
 */
@Singleton
public class PostpaidTabTxnMapDao extends MoneyDao {

    private static final String GET_TRANSACTION_COUNT_QUERY = "select count(*) CNT from postpaid_tab_txn_map where account_number='{accountNumber}' "
            + "and POSTPAID_TAB_AMOUNT_TAB_TID='{tabId}' and TXN_TYPE='U' and TXN_ID in ({txnIds})";

    // tokens
    private static final String ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{accountNumber}";
    private static final String TAB_ID_REPLACEMENT_TOKEN = "{tabId}";
    private static final String TXN_IDS_REPLACEMENT_TOKEN = "{txnIds}";

    // Column names
    private static final String COUNT_COL = "CNT";

    public int getTransactionCount(BigInteger accountNumber, BigInteger tabId, List<BigInteger> transactionIds) {
        String query = GET_TRANSACTION_COUNT_QUERY.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
                .replace(TAB_ID_REPLACEMENT_TOKEN, tabId.toString())
                .replace(TXN_IDS_REPLACEMENT_TOKEN, mapIdToString(transactionIds,
                        transactionId -> new StringBuilder("'").append(transactionId).append("'").toString()));
        Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
        return getInteger(result.get(COUNT_COL));
    }

}
