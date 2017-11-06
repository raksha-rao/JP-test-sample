package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WwaxTransactionDTO;

/**
 * Represents WWAX_TRANSACTION table of MONEY database
 */
@Singleton
public class WwaxTransactionDao extends MoneyDao {

    private static final String GET_WWAX_TRANSACTION_DETAILS_QUERY = "SELECT * FROM WWAX_TRANSACTION where trans_data_map_id in "
            + "(select MAP_ID from WTRANS_DATA_MAP where transactionlike_id in ({commaSeparatedIds}) and transactionlike_type = 'T' and BITAND(FLAGS, {wwaxTransaction}) > 0 ) "
            + "order by trans_data_map_id";
    private static final String WWAX_TRANSACTION_FLAG_REPLACEMENT_TOKEN = "{wwaxTransaction}";
    private static final String WWAX_TRANSACTION_FLAG_VALUE = "2";
    private static final String ALIAS_ID_COL = "ALIAS_ID";
    private static final String CC_LOGIN_ACCOUNT_NUMBER_COL = "CC_LOGIN_ACCOUNT_NUMBER";
    private static final String EMAIL_LOGIN_ACCOUNT_NUMBER_COL = "EMAIL_LOGIN_ACCOUNT_NUMBER";
    private static final String RECEIPT_ID_COL = "RECEIPT_ID";

    /**
     * Queries WWAX_TRANSACTION table for input transaction
     * @param transactions
     * @return
     */
    public List<WwaxTransactionDTO> getWwaxTransactionDetails(List<WTransactionDTO> transactions) {
        String query = GET_WWAX_TRANSACTION_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()))
                .replace(WWAX_TRANSACTION_FLAG_REPLACEMENT_TOKEN, WWAX_TRANSACTION_FLAG_VALUE);
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WwaxTransactionDTO> wwaxTransactions = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WwaxTransactionDTO wwaxTransaction = new WwaxTransactionDTO();
            wwaxTransaction.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            wwaxTransaction.setAliasId(getBigInteger(result.get(ALIAS_ID_COL)));
            wwaxTransaction.setCcLoginAccountNumber(getBigInteger(result.get(CC_LOGIN_ACCOUNT_NUMBER_COL)));
            wwaxTransaction.setEmailLoginAccountNumber(getBigInteger(result.get(EMAIL_LOGIN_ACCOUNT_NUMBER_COL)));
            wwaxTransaction.setFlags(getLong(result.get(FLAGS_COL)));
            wwaxTransaction.setReceiptId(getBigInteger(result.get(RECEIPT_ID_COL)));
            wwaxTransaction.setTimeCreated(getLong(result.get(TIME_CREATED_COL)));
            wwaxTransaction.setTransDataMapId(getBigInteger(result.get(TRANS_DATA_MAP_ID_COL)));
            wwaxTransactions.add(wwaxTransaction);
        }
        return wwaxTransactions;
    }
}
