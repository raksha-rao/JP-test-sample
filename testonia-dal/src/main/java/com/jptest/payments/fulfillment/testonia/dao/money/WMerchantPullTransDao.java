package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WMerchantPullTransDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WMERCHANT_PULL_TRANS table of MONEY database
 */
@Singleton
public class WMerchantPullTransDao extends MoneyDao {

    private static final String GET_MERCHANT_PULL_TRANSACTION_DETAILS_QUERY = "SELECT * FROM WMERCHANT_PULL_TRANS where TRANSACTION_ID in ({commaSeparatedIds}) order by transaction_id";

    /**
     * Queries WMERCHANT_PULL_TRANS table for input transaction
     * @param transactions
     * @return
     */
    public List<WMerchantPullTransDTO> getWMerchantPullTransactionsDetails(List<WTransactionDTO> transactions) {
        String query = GET_MERCHANT_PULL_TRANSACTION_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WMerchantPullTransDTO> merchantPullTransactions = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WMerchantPullTransDTO merchantPullTransaction = new WMerchantPullTransDTO();
            merchantPullTransaction.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            merchantPullTransaction.setAmount(getLong(result.get(AMOUNT_COL)));
            merchantPullTransaction.setBaId(getLong(result.get(BA_ID_COL)));
            merchantPullTransaction.setCounterpartyTransactionId(getBigInteger(result.get(CPARTY_TRANS_ID_COL)));
            merchantPullTransaction.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            merchantPullTransaction.setFlags(getLong(result.get(FLAGS_COL)));
            merchantPullTransaction.setMerchantAccountNumber(getBigInteger(result.get(MERCHANT_ACCOUNT_NUMBER_COL)));
            merchantPullTransaction.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            merchantPullTransaction.setTransDataMapId(getBigInteger(result.get(TRANS_DATA_MAP_ID_COL)));
            merchantPullTransactions.add(merchantPullTransaction);
        }
        return merchantPullTransactions;
    }

}
