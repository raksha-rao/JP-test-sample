package com.jptest.payments.fulfillment.testonia.dao.money;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WUserHoldingSubBalanceDTO;

/**
 * DAO for wuser_holding_subbalance table of MONEY DB
 */
public class WUserHoldingSubBalanceDao extends MoneyDao {

    private static final String GET_WUSER_HOLDING_SUB_BALANCE_QUERY = "SELECT * FROM WUSER_HOLDING_SUBBALANCE where account_number = {senderAccountNumber} order by time_created";

    public List<WUserHoldingSubBalanceDTO> getWUserHoldingSubBalanceDetails(final List<WTransactionDTO> transactions) {
        final String query = GET_WUSER_HOLDING_SUB_BALANCE_QUERY.replace(SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
                transactions.get(0).getAccountNumber().toString());
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        final List<WUserHoldingSubBalanceDTO> userHoldingSubBalances = new ArrayList<>();
        for (final Map<String, Object> result : queryResult) {
            final WUserHoldingSubBalanceDTO userHoldingSubBalance = new WUserHoldingSubBalanceDTO();
            userHoldingSubBalance.setAccountNumber(this.getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            userHoldingSubBalance.setBalance(this.getLong(result.get(BALANCE_COL)));
            userHoldingSubBalance.setCurrencyCode(this.getString(result.get(CURRENCY_CODE_COL)));
            userHoldingSubBalance.setFlags(this.getLong(result.get(FLAGS_COL)));
            userHoldingSubBalance.setTimeCreated(this.getLong(result.get(TIME_CREATED_COL)));
            userHoldingSubBalance.setType(this.getByte(result.get(TYPE_COL)));
            userHoldingSubBalances.add(userHoldingSubBalance);
        }

        return userHoldingSubBalances;
    }
}
