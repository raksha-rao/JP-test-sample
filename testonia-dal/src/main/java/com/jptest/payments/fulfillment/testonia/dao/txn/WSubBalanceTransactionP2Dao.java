package com.jptest.payments.fulfillment.testonia.dao.txn;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WSubBalanceTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WSUBBALANCE_TRANSACTION_P2 table of TXN database
 */
@Singleton
public class WSubBalanceTransactionP2Dao extends TxnDao {

    private static final String GET_WSUBBALANCE_TRANSACTION_P2_DETAILS_QUERY = "SELECT * FROM WSUBBALANCE_TRANSACTION_P2 where transaction_id in ({commaSeparatedIds}) order by time_created,id";
    private static final String GET_SUBBALANCE_TYPE = "SELECT * FROM WSUBBALANCE_TRANSACTION_P2 where ACCOUNT_NUMBER='{acc_number}' and REASON_CODE = '{code}'";
    private static final String AVAILABLE_BALANCE_COL = "AVAILABLE_BALANCE";
    private static final String SUBBALANCE_TYPE_COL = "SUBBALANCE_TYPE";
    private static final String REASON_CODE_COL = "REASON_CODE";
    private static final String ACCOUNT_NUMBER = "{acc_number}";
    private static final String REASON_CODE = "{code}";

    /**
     * Queries WSUBBALANCE_TRANSACTION_P2 table for input transaction
     * @param transactions
     * @return
     */
    public List<WSubBalanceTransactionDTO> getWSubBalanceTransactionDetails(List<WTransactionDTO> transactions) {
        String query = GET_WSUBBALANCE_TRANSACTION_P2_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WSubBalanceTransactionDTO> subBalanceTransactions = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WSubBalanceTransactionDTO subBalanceTransaction = new WSubBalanceTransactionDTO();
            subBalanceTransaction.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            subBalanceTransaction.setAmount(getLong(result.get(AMOUNT_COL)));
            subBalanceTransaction.setAvailableBalance(getLong(result.get(AVAILABLE_BALANCE_COL)));
            subBalanceTransaction.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            subBalanceTransaction.setSubBalanceType(getByte(result.get(SUBBALANCE_TYPE_COL)));
            subBalanceTransaction.setFlags(getLong(result.get(FLAGS_COL)));
            subBalanceTransaction.setReasonCode(getByte(result.get(REASON_CODE_COL)));
            subBalanceTransaction.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            subBalanceTransactions.add(subBalanceTransaction);
        }
        return subBalanceTransactions;
    }

    public String getSubBalanceType(String accountNumber, String reasonCode) {
        String selectQuery = GET_SUBBALANCE_TYPE.replace(ACCOUNT_NUMBER, accountNumber)
                .replace(REASON_CODE, reasonCode);
        Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), selectQuery);
        return result != null ? getString(result.get(SUBBALANCE_TYPE_COL)) : null;
    }
}
