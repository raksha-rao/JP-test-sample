package com.jptest.payments.fulfillment.testonia.dao.txn;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionPendingDTO;

/**
 * Represents WTRANSACTION_PENDING_P2 table of TXN database
 */
@Singleton
public class WTransactionPendingP2Dao extends TxnDao {

    private static final String GET_PENDING_TRANSACTION_DETAILS_QUERY = "SELECT * FROM WTRANSACTION_PENDING_P2 where transaction_id IN ({commaSeparatedIds}) and account_number = {senderAccountNumber} and status not in ('Q') order by time_created,transaction_id";
    private static final String SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{senderAccountNumber}";

    /**
     * Queries WTRANSACTION_PENDING table for input transaction
     * @param transactions
     * @return
     */
    public List<WTransactionPendingDTO> getPendingTransactionDetails(List<WTransactionDTO> transactions) {
        String query = GET_PENDING_TRANSACTION_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()))
                .replace(SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
                        transactions.get(0).getAccountNumber().toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransactionPendingDTO> pendingTransactions = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransactionPendingDTO pendingTransaction = new WTransactionPendingDTO();
            pendingTransaction.setType(getByte(result.get(TYPE_COL)));
            pendingTransaction.setStatus(getByte(result.get(STATUS_COL)));
            pendingTransaction.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            pendingTransaction.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            pendingTransactions.add(pendingTransaction);
        }

        return pendingTransactions;
    }
}
