package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionUrlDTO;

/**
 * Represents WTRANSACTION_URL table of MONEY database
 */
@Singleton
public class WTransactionUrlP1Dao extends MoneyDao {

    private static final String GET_TRANSACTION_URL_DETAILS_QUERY = "SELECT * FROM WTRANSACTION_URL where trans_data_map_id in "
            + "(select MAP_ID from WTRANS_DATA_MAP where transactionlike_id in ({commaSeparatedIds}) and transactionlike_type = 'T' and BITAND(FLAGS, {wTransactionUrlFlag}) > 0) "
            + "order by trans_data_map_id";
    private static final String TRANSACTION_URL_FLAG_REPLACEMENT_TOKEN = "{wTransactionUrlFlag}";
    private static final String TRANSACTION_URL_FLAG_VALUE = "16";
    private static final String URL_COL = "URL";

    /**
     * Queries WTRANSACTION_URL table for input transaction
     * @param transactions
     */
    public List<WTransactionUrlDTO> getWTransactionUrlDetails(List<WTransactionDTO> transactions) {
        String query = GET_TRANSACTION_URL_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()))
                .replace(TRANSACTION_URL_FLAG_REPLACEMENT_TOKEN, TRANSACTION_URL_FLAG_VALUE);
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransactionUrlDTO> transactionUrls = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransactionUrlDTO transactionUrl = new WTransactionUrlDTO();
            transactionUrl.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            transactionUrl.setUrl(getString(result.get(URL_COL)));
            transactionUrls.add(transactionUrl);
        }
        return transactionUrls;
    }
}
