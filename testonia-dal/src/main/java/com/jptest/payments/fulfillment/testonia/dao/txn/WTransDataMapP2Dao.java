package com.jptest.payments.fulfillment.testonia.dao.txn;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import com.jptest.payments.fulfillment.testonia.model.money.WTransDataMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;


/*
 * Represents WTRANS_DATA_MAP_P2 in TXN DB
 */
@Singleton
public class WTransDataMapP2Dao extends TxnDao {

    private static final String GET_WTRANS_DATA_MAP_P2 = "select * from WTRANS_DATA_MAP_P2 where transactionlike_id in ({commaSeparatedIds}) and transactionlike_type = 'T' and BITAND(FLAGS, 32) > 0 order by map_id";

    private static final String TRANSACTIONLIKE_ID_COL = "TRANSACTIONLIKE_ID";
    private static final String TRANSACTIONLIKE_TYPE_COL = "TRANSACTIONLIKE_TYPE";
    private static final String MAP_ID_COL = "MAP_ID";
    private static final String FLAGS_COL = "FLAGS";
    private static final String TIME_CREATED_COL = "TIME_CREATED";

    /**
     * Queries WTRANS_DATA_MAP table for input transaction
     *
     * @param transactions
     * @return
     */
    public List<WTransDataMapDTO> getTransDataMapDetails(List<WTransactionDTO> transactions) {
        String query = GET_WTRANS_DATA_MAP_P2.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WTransDataMapDTO> wTransDataMapList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WTransDataMapDTO wTransDataMap = new WTransDataMapDTO();
            wTransDataMap.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            wTransDataMap.setTimeCreated(getLong(result.get(TIME_CREATED_COL)));
            wTransDataMap.setTransactionLikeId(getBigInteger(result.get(TRANSACTIONLIKE_ID_COL)));
            wTransDataMap.setTransactionLikeType(getByte(result.get(TRANSACTIONLIKE_TYPE_COL)));
            wTransDataMap.setFlags(getLong(result.get(FLAGS_COL)));
            wTransDataMap.setMapId(getBigInteger(result.get(MAP_ID_COL)));
            wTransDataMapList.add(wTransDataMap);
        }
        return wTransDataMapList;
    }
}
