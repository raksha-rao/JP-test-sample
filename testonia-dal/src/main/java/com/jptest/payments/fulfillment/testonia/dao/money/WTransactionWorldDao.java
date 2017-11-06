package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionWorldDTO;

/**
 * Represents WTRANSACTION_WORLD table of MONEY database
 */
@Singleton
public class WTransactionWorldDao extends MoneyDao {

    private static final String GET_TRANSACTION_WORLD_DETAILS_QUERY = "SELECT * FROM WTRANSACTION_WORLD where transaction_id IN ({commaSeparatedIds}) order by transaction_id";
    private static final String LOCAL_CURRENCY_CODE_COL = "LOCAL_CURRENCY_CODE";
    private static final String AMOUNT_IN_LOCAL_CURRENCY_COL = "AMOUNT_IN_LOCAL_CURRENCY";
    private static final String EXCHANGE_RATE_ID_COL = "EXCHANGE_RATE_ID";
    private static final String INTERBANK_RATE_COL = "INTERBANK_RATE";
    private static final String COUNTRY_COL = "COUNTRY";

    /**
     * Queries WTRANSACTION_WORLD table for input transaction
     * @param transactions
     * @return
     */
    public List<WTransactionWorldDTO> getTransactionWorldDetails(List<WTransactionDTO> transactions) {
        String query = GET_TRANSACTION_WORLD_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransactionWorldDTO> transactionWorldList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransactionWorldDTO transactionWorld = new WTransactionWorldDTO();
            transactionWorld.setAmountInLocalCurrency(getBigInteger(result.get(AMOUNT_IN_LOCAL_CURRENCY_COL)));
            transactionWorld.setCountry(getString(result.get(COUNTRY_COL)));
            transactionWorld.setLocalCurrencyCode(getString(result.get(LOCAL_CURRENCY_CODE_COL)));
            transactionWorld.setExchangeRate(getBigDecimal(result.get(EXCHANGE_RATE_COL)));
            transactionWorld.setExchangeRateId(getInteger(result.get(EXCHANGE_RATE_ID_COL)));
            transactionWorld.setFlags(getLong(result.get(FLAGS_COL)));
            transactionWorld.setInterBankRate(getBigDecimal(result.get(INTERBANK_RATE_COL)));
            transactionWorld.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            transactionWorldList.add(transactionWorld);
        }

        return transactionWorldList;
    }
}
