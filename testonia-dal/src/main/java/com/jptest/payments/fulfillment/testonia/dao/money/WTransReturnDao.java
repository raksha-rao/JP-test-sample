package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransReturnDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WTRANS_RETURN table of MONEY database
 */
@Singleton
public class WTransReturnDao extends MoneyDao {

    private static final String GET_TRANS_RETURN_DETAILS_QUERY = "SELECT * FROM WTRANS_RETURN where transaction_id in ({commaSeparatedIds}) order by id";
    private static final String BOUNCED_AMOUNT_COL = "BOUNCED_AMOUNT";
    private static final String BOUNCED_AMOUNT_USD_COL = "BOUNCED_AMOUNT_USD";
    private static final String NACHA_RETURN_TYPE_COL = "NACHA_RETURN_TYPE";

    /**
     * Queries WTRANS_RETURN table for input transaction
     * @param transactions
     * @return
     */
    public List<WTransReturnDTO> getWTransactionReturnDetails(List<WTransactionDTO> transactions) {
        String query = GET_TRANS_RETURN_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransReturnDTO> transactionReturns = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransReturnDTO transactionReturn = new WTransReturnDTO();
            transactionReturn.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            transactionReturn.setBouncedAmount(getLong(result.get(BOUNCED_AMOUNT_COL)));
            transactionReturn.setBouncedAmountUsd(getLong(result.get(BOUNCED_AMOUNT_USD_COL)));
            transactionReturn.setCountryCode(getString(result.get(COUNTRY_CODE_COL)));
            transactionReturn.setFlags(getLong(result.get(FLAGS_COL)));
            transactionReturn.setNachaReturnType(getInteger(result.get(NACHA_RETURN_TYPE_COL)));
            transactionReturn.setTransactionId(getBigInteger(result.get(TRANSACTION_ID_COL)));
            transactionReturns.add(transactionReturn);
        }
        return transactionReturns;
    }
}
