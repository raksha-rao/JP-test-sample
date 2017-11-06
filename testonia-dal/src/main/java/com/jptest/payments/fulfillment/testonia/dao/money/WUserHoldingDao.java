package com.jptest.payments.fulfillment.testonia.dao.money;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WUserHoldingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO for wuser_holding table of MONEY DB
 */
@Singleton
public class WUserHoldingDao extends MoneyDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(WUserHoldingDao.class);
    private static final String GET_WUSER_HOLDING_QUERY = "SELECT * FROM WUSER_HOLDING where account_number = {senderAccountNumber}";

    private static final String PENDING_REVERSALS_COL = "PENDING_REVERSALS";

    private static final String UPDATE_BALANCE_QUERY = "update wuser_holding set balance = '{balance}' where account_number = '{accountNumber}' ";
    private static final String UPDATE_BALANCE_WITH_CURRENCY_CODE_QUERY = "update wuser_holding set balance = '{balance}' where account_number = '{accountNumber}' and currency_code = '{currencyCode}'";
    private static final String UPDATE_FLAGS_QUERY = "update wuser_holding set flags = '{flags}' where account_number = '{accountNumber}' and currency_code = '{currencyCode}'";
    private static final String GET_WUSER_HOLDING_BASED_ON_CURRENCY_CODE_QUERY = "SELECT * FROM WUSER_HOLDING where account_number = '{accountNumber}' and currency_code = '{currencyCode}'";
    private static final String GET_BALANCE_QUERY = "select balance BAL from WUSER_HOLDING where account_number='{accountNumber}' and CURRENCY_CODE='{currencyCode}'";

    // tokens
    private static final String ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{accountNumber}";
    private static final String BALANCE_TOKEN = "{balance}";
    private static final String CURRENCY_CODE_TOKEN = "{currencyCode}";
    private static final String FLAGS_TOKEN = "{flags}";
    // Column names
    private static final String BAL_COL = "BAL";

    /**
     * Queries WUSER_HOLDING table for input transaction
     *
     * @param transactions
     * @return
     */
    public List<WUserHoldingDTO> getWUserHoldingDetails(final List<WTransactionDTO> transactions) {
        final String query = GET_WUSER_HOLDING_QUERY.replace(SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
                transactions.get(0).getAccountNumber().toString());
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        final List<WUserHoldingDTO> userHoldings = new ArrayList<>();
        for (final Map<String, Object> result : queryResult) {
            final WUserHoldingDTO userHolding = new WUserHoldingDTO();
            userHolding.setAccountNumber(this.getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            userHolding.setBalance(this.getLong(result.get(BALANCE_COL)));
            userHolding.setCollateralAmount(this.getLong(result.get(COLLATERAL_AMOUNT_COL)));
            userHolding.setCurrencyCode(this.getString(result.get(CURRENCY_CODE_COL)));
            userHolding.setFlags(this.getLong(result.get(FLAGS_COL)));
            userHolding.setPendingReversals(this.getLong(result.get(PENDING_REVERSALS_COL)));
            userHoldings.add(userHolding);
        }

        return userHoldings;
    }

    public void updateBalance(final String accountNumber, final String balance) {
        final String query = UPDATE_BALANCE_QUERY.replace(BALANCE_TOKEN, balance)
                .replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber);
        this.dbHelper.executeUpdateQuery(this.getDatabaseName(), query);
    }

    /**
     * Queries WUSER_HOLDING table for input transaction
     *
     * @param accountNumber
     * @param currencyCode
     * @return List<WUserHoldingDTO>
     */
    public List<WUserHoldingDTO> getWUserHoldingDetails(final String accountNumber, final String currencyCode) {
        final String query = GET_WUSER_HOLDING_BASED_ON_CURRENCY_CODE_QUERY
                .replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber).replace(CURRENCY_CODE_TOKEN, currencyCode);
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        final List<WUserHoldingDTO> userHoldings = new ArrayList<>();
        for (final Map<String, Object> result : queryResult) {
            final WUserHoldingDTO userHolding = new WUserHoldingDTO();
            userHolding.setId(this.getBigInteger(result.get(ID_COL)));
            userHolding.setAccountNumber(this.getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            userHolding.setBalance(this.getLong(result.get(BALANCE_COL)));
            userHolding.setCollateralAmount(this.getLong(result.get(COLLATERAL_AMOUNT_COL)));
            userHolding.setCurrencyCode(this.getString(result.get(CURRENCY_CODE_COL)));
            userHolding.setFlags(this.getLong(result.get(FLAGS_COL)));
            userHolding.setPendingReversals(this.getLong(result.get(PENDING_REVERSALS_COL)));
            userHoldings.add(userHolding);
        }

        return userHoldings;
    }

    /**
     * Update flags column in the WUSER_HOLDING table for input accountNumber
     * and currencyCode
     *
     * @param accountNumber
     * @param currencyCode
     * @param flags
     */
    public void updateFlags(final String accountNumber, final String currencyCode, final String flags) {
        final String query = UPDATE_FLAGS_QUERY.replace(FLAGS_TOKEN, flags)
                .replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber).replace(CURRENCY_CODE_TOKEN, currencyCode);
        this.dbHelper.executeUpdateQuery(this.getDatabaseName(), query);
    }

    /**
     * Update balance column in the WUSER_HOLDING table for input accountNumber
     * and currencyCode
     *
     * @param accountNumber
     * @param balance
     * @param currencyCode
     */
    public void updateBalance(final String accountNumber, final String currencyCode, final String balance) {
        final String query = UPDATE_BALANCE_WITH_CURRENCY_CODE_QUERY.replace(BALANCE_TOKEN, balance)
                .replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber).replace(CURRENCY_CODE_TOKEN, currencyCode);
        this.dbHelper.executeUpdateQuery(this.getDatabaseName(), query);
    }

    public int getBalance(String accountNumber, String currencyCode) throws InterruptedException {
        String query = GET_BALANCE_QUERY.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())

                .replace(CURRENCY_CODE_TOKEN, currencyCode);

        Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
        LOGGER.info(" BALALNCE FOR THE ACCOUNT : {} from DB : {} ", result.get(BAL_COL), getDatabaseName());
        return getInteger(result.get(BAL_COL));
    }

}
