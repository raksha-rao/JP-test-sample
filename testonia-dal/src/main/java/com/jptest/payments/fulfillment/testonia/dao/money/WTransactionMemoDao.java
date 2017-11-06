package com.jptest.payments.fulfillment.testonia.dao.money;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionMemoDTO;

/**
 * Represents WTRANSACTION_MEMO table of MONEY database
 */
@Singleton
public class WTransactionMemoDao extends MoneyDao {

    private static final String GET_TRANSACTION_MEMO_DETAILS_QUERY = "SELECT * FROM WTRANSACTION_MEMO where shared_id = {sharedId} order by time_created";
    private static final String SHARED_ID_REPLACEMENT_TOKEN = "{sharedId}";
    private static final String RESTRICTION_ID_COL = "RESTRICTION_ID";

    /**
     * Queries WTRANSACTION_MEMO table for input sharedId
     * @param sharedId
     * @return
     */
    public List<WTransactionMemoDTO> getWTransactionMemoDetails(String sharedId) {
        String query = GET_TRANSACTION_MEMO_DETAILS_QUERY.replace(SHARED_ID_REPLACEMENT_TOKEN, sharedId);
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransactionMemoDTO> transactionMemos = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransactionMemoDTO transactionMemo = new WTransactionMemoDTO();
            transactionMemo.setMemo(getString(result.get(MEMO_COL)));
            transactionMemo.setRestrictionId(getBigInteger(result.get(RESTRICTION_ID_COL)));
            transactionMemo.setType(getByte(result.get(TYPE_COL)));
            transactionMemos.add(transactionMemo);
        }
        return transactionMemos;
    }
}
