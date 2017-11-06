package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDetailsDTO;

/**
 * Represents WTRANSACTION_DETAILS table of MONEY database
 */
@Singleton
public class WTransactionDetailsP1Dao extends MoneyDao {

    private static final String GET_TRANSACTION_DETAILS_QUERY = "SELECT * FROM WTRANSACTION_DETAILS where TRANS_DATA_MAP_ID in "
            + "(select MAP_ID from WTRANS_DATA_MAP where transactionlike_id in ({commaSeparatedIds}) and transactionlike_type = 'T' and BITAND(FLAGS, 512) > 0) "
            + "order by time_created,id";

    private static final String HOSTED_BUTTON_KEYS_TEXT_COL = "HOSTED_BUTTON_KEYS_TEXT";
    private static final String MARKET_COL = "MARKET";
    private static final String WT_DETAILS_CHAR1_COL = "WT_DETAILS_CHAR1";
    private static final String WT_DETAILS_CHAR2_COL = "WT_DETAILS_CHAR2";
    private static final String WT_DETAILS_CHAR3_COL = "WT_DETAILS_CHAR3";
    private static final String WT_DETAILS_CHAR4_COL = "WT_DETAILS_CHAR4";
    private static final String WT_DETAILS_CHAR5_COL = "WT_DETAILS_CHAR5";

    private static final String WT_DETAILS_NUM1_COL = "WT_DETAILS_NUM1";
    private static final String WT_DETAILS_NUM2_COL = "WT_DETAILS_NUM2";
    private static final String WT_DETAILS_NUM3_COL = "WT_DETAILS_NUM3";
    private static final String WT_DETAILS_NUM4_COL = "WT_DETAILS_NUM4";
    private static final String WT_DETAILS_NUM5_COL = "WT_DETAILS_NUM5";

    /**
    * Queries WTRANSACTION_DETAILS table for input transaction
    * @param transactions
    * @return
    */
    public List<WTransactionDetailsDTO> getWTransactionDetails(List<WTransactionDTO> transactions) {
        String query = GET_TRANSACTION_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransactionDetailsDTO> transactionDetails = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransactionDetailsDTO transactionDetail = new WTransactionDetailsDTO();
            transactionDetail.setFlags(getLong(result.get(FLAGS_COL)));
            transactionDetail.setHostedButtonKeysText(getString(result.get(HOSTED_BUTTON_KEYS_TEXT_COL)));
            transactionDetail.setId(getBigInteger(result.get(ID_COL)));
            transactionDetail.setMarket(getLong(result.get(MARKET_COL)));
            transactionDetail.setTransDataMapId(getBigInteger(result.get(TRANS_DATA_MAP_ID_COL)));
            transactionDetail.setWtDetailsChar1(getString(result.get(WT_DETAILS_CHAR1_COL)));
            transactionDetail.setWtDetailsChar2(getString(result.get(WT_DETAILS_CHAR2_COL)));
            transactionDetail.setWtDetailsChar3(getString(result.get(WT_DETAILS_CHAR3_COL)));
            transactionDetail.setWtDetailsChar4(getString(result.get(WT_DETAILS_CHAR4_COL)));
            transactionDetail.setWtDetailsChar5(getString(result.get(WT_DETAILS_CHAR5_COL)));
            transactionDetail.setWtDetailsNum1(getBigInteger(result.get(WT_DETAILS_NUM1_COL)));
            transactionDetail.setWtDetailsNum2(getBigInteger(result.get(WT_DETAILS_NUM2_COL)));
            transactionDetail.setWtDetailsNum3(getBigInteger(result.get(WT_DETAILS_NUM3_COL)));
            transactionDetail.setWtDetailsNum4(getBigInteger(result.get(WT_DETAILS_NUM4_COL)));
            transactionDetail.setWtDetailsNum5(getBigInteger(result.get(WT_DETAILS_NUM5_COL)));
            transactionDetails.add(transactionDetail);
        }
        return transactionDetails;
    }

}
