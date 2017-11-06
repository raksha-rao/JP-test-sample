package com.jptest.payments.fulfillment.testonia.dao.money;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WCollateralLogDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WCOLLATERAL_LOG table of MONEY database
 */
@Singleton
public class WCollateralLogDao extends MoneyDao {

    private static final String GET_WCOLLATERAL_LOG_DETAILS_QUERY = "SELECT * FROM WCOLLATERAL_LOG where account_number = {senderAccountNumber} "
            + "and currency_code = '{currencyCode}' and time_created = "
            + "(select max (time_created) from  wcollateral_log where account_number = {senderAccountNumber} "
            + "and currency_code = '{currencyCode}' and time_created <= {currentTime}) order by id,time_created";
    private static final String CURRENCY_CODE_REPLACEMENT_TOKEN = "{currencyCode}";
    private static final String CURRENT_TIME_REPLACEMENT_TOKEN = "{currentTime}";
    private static final String NET_CHANGE_COL = "NET_CHANGE";

    /**
     * Queries WCOLLATERAL_LOG table for input transaction
     * @param transactions
     * @return
     */
    public List<WCollateralLogDTO> getCollateralLogDetails(List<WTransactionDTO> transactions) {
        String query = GET_WCOLLATERAL_LOG_DETAILS_QUERY.replace(SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
                transactions.get(0).getAccountNumber().toString())
                .replace(CURRENCY_CODE_REPLACEMENT_TOKEN, transactions.get(0).getCurrencyCode())
                .replace(CURRENT_TIME_REPLACEMENT_TOKEN, String.valueOf(System.currentTimeMillis()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WCollateralLogDTO> collateralLogs = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WCollateralLogDTO collateralLog = new WCollateralLogDTO();
            collateralLog.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            collateralLog.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            collateralLog.setCollateralAmount(getLong(result.get(COLLATERAL_AMOUNT_COL)));
            collateralLog.setNetChange(getLong(result.get(NET_CHANGE_COL)));
            collateralLogs.add(collateralLog);
        }
        return collateralLogs;
    }
}
