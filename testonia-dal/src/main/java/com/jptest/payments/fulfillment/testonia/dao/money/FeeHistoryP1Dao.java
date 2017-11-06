package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.FeeHistoryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents FEE_HISTORY table of MONEY database
 */
@Singleton
public class FeeHistoryP1Dao extends MoneyDao {

    private static final String GET_FEE_HISTORY_DETAILS_QUERY = "SELECT * FROM FEE_HISTORY where transaction_id in ({commaSeparatedIds}) and transaction_type != 'R' order by time_created,transaction_id";
    private static final String ACTUAL_FIXED_FEE_COL = "ACTUAL_FIXED_FEE";
    private static final String ACTUAL_PERCENT_FEE_COL = "ACTUAL_PERCENT_FEE";
    private static final String TRANSACTION_TYPE_COL = "TRANSACTION_TYPE";

    /**
     * Queries FEE_HISTORY table for input transaction
     * @param transactions
     * @return
     */
    public List<FeeHistoryDTO> getFeeHistoryDetails(List<WTransactionDTO> transactions) {
        String query = GET_FEE_HISTORY_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<FeeHistoryDTO> feeHistoryList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            FeeHistoryDTO feeHistory = new FeeHistoryDTO();
            feeHistory.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            feeHistory.setActualFixedFee(getLong(result.get(ACTUAL_FIXED_FEE_COL)));
            feeHistory.setActualPercentFee(getBigDecimal(result.get(ACTUAL_PERCENT_FEE_COL)));
            feeHistory.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            feeHistory.setData(getString(result.get(DATA_COL)));
            feeHistory.setTotalFeeAmount(getLong(result.get(TOTAL_FEE_AMOUNT_COL)));
            feeHistory.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            feeHistory.setTransactionType(getByte(result.get(TRANSACTION_TYPE_COL)));
            feeHistoryList.add(feeHistory);
        }
        return feeHistoryList;
    }
}
