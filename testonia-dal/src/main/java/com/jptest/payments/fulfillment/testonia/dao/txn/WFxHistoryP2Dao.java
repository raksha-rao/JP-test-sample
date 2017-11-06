package com.jptest.payments.fulfillment.testonia.dao.txn;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.FXHistoryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents W_FX_HISTORY_P2 table of TXN database
 */
@Singleton
public class WFxHistoryP2Dao extends TxnDao {

    private static final String GET_W_FX_HISTORY_P2_DETAILS_QUERY = "SELECT * FROM W_FX_HISTORY_P2 where transaction_id in ({commaSeparatedIds}) order by time_created,transaction_id";
    private static final String AMOUNT_FROM_COL = "AMOUNT_FROM";
    private static final String AMOUNT_TO_COL = "AMOUNT_TO";
    private static final String EXCHANGE_RATE_COL = "EXCHANGE_RATE";
    private static final String CURRENCY_CODE_FROM_COL = "CURRENCY_CODE_FROM";
    private static final String CURRENCY_CODE_TO_COL = "CURRENCY_CODE_TO";

    /**
     * Queries W_FX_HISTORY_P2 table for input transaction
     * @param transactions
     * @return
     */
    public List<FXHistoryDTO> getFXHistoryDetails(List<WTransactionDTO> transactions) {
        String query = GET_W_FX_HISTORY_P2_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<FXHistoryDTO> fXHistoryList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            FXHistoryDTO fXHistory = new FXHistoryDTO();
            fXHistory.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            fXHistory.setAmountFrom(getLong(result.get(AMOUNT_FROM_COL)));
            fXHistory.setAmountTo(getLong(result.get(AMOUNT_TO_COL)));
            fXHistory.setCurrencyCodeFrom(getString(result.get(CURRENCY_CODE_FROM_COL)));
            fXHistory.setCurrencyCodeTo(getString(result.get(CURRENCY_CODE_TO_COL)));
            fXHistory.setData(getString(result.get(DATA_COL)));
            fXHistory.setExchangeRate(getBigDecimal(result.get(EXCHANGE_RATE_COL)));
            fXHistory.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            fXHistoryList.add(fXHistory);
        }
        return fXHistoryList;
    }

}
