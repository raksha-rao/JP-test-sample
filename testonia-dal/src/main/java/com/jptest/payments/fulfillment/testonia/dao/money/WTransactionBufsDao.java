package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionBufsDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WTRANSACTION_BUFS table of MONEY database
 */
@Singleton
public class WTransactionBufsDao extends MoneyDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(WTransactionBufsDao.class);

    private static final String GET_TRANSACTION_BUFS_DETAILS_QUERY = "SELECT * FROM WTRANSACTION_BUFS where transaction_id IN ({commaSeparatedIds}) and account_number = {senderAccountNumber} order by transaction_id";
    private static final String SOURCE_ID_COL = "SOURCE_ID";

    private static final String GET_TRANSCATION_BUFS_COUNT = "SELECT COUNT(*) CNT from WTRANSACTION_BUFS where account_number={accountNumber}";
    private static final String ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{accountNumber}";

    /**
     * Queries WTRANSACTION_BUFS table for input transaction
     * @param transactions
     * @return
     */
    public List<WTransactionBufsDTO> getTransactionBufsDetails(List<WTransactionDTO> transactions) {
        String query = GET_TRANSACTION_BUFS_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()))
                .replace(SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
                        transactions.get(0).getAccountNumber().toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransactionBufsDTO> transactionBufs = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransactionBufsDTO transactionBuf = new WTransactionBufsDTO();
            transactionBuf.setSourceId(getBigInteger(result.get(SOURCE_ID_COL)));
            transactionBuf.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            transactionBuf.setType(getByte(result.get(TYPE_COL)));
            transactionBufs.add(transactionBuf);
        }

        return transactionBufs;
    }

    /**
     * Returns count of BUFS for input account number
     *  
     * @param accountNumber
     * @return
     */
    public int getBufsCount(String accountNumber) {
        String query = GET_TRANSCATION_BUFS_COUNT.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber);
        Map<String, Object> queryResult = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
        int rowCount = getInteger(queryResult.get(COUNT_COL));
        LOGGER.info("Found {} BUFS entries for account number {}", rowCount, accountNumber);
        return rowCount;
    }
}
