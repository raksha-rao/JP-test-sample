package com.jptest.payments.fulfillment.testonia.dao.txn;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WxClickUrlDTO;

/**
 * Represents WXCLICK_URL_P2 table of TXN database
 */
@Singleton
public class WxClickUrlP2Dao extends TxnDao {

    private static final String GET_WXCLICK_URL_P2_DETAILS_QUERY = "SELECT * FROM WXCLICK_URL_P2 where trans_data_map_id in "
            + "(select MAP_ID from WTRANS_DATA_MAP_P2 where transactionlike_id in ({commaSeparatedIds}) and transactionlike_type = 'T' and BITAND(FLAGS, 2) > 0) "
            + "order by trans_data_map_id";

    private static final String REFERRAL_URL_COL = "REFERRAL_URL";
    private static final String RETURN_URL_COL = "RETURN_URL";

    /**
     * Queries WXCLICK_P2 table for input transaction
     * @param transactions
     * @return
     */
    public List<WxClickUrlDTO> getWxClickUrlDetails(List<WTransactionDTO> transactions) {
        String query = GET_WXCLICK_URL_P2_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WxClickUrlDTO> wxClickUrls = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WxClickUrlDTO wxClickUrl = new WxClickUrlDTO();
            wxClickUrl.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            wxClickUrl.setReferralUrl(getString(result.get(REFERRAL_URL_COL)));
            wxClickUrl.setReturnUrl(getString(result.get(RETURN_URL_COL)));
            wxClickUrl.setTransDataMapId(getBigInteger(result.get(TRANS_DATA_MAP_ID_COL)));
            wxClickUrls.add(wxClickUrl);
        }
        return wxClickUrls;
    }
}
