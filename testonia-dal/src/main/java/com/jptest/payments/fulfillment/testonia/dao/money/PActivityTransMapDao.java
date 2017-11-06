package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.PActivityTransMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents PACTIVITY_TRANS_MAP table of MONEY database
 */
@Singleton
public class PActivityTransMapDao extends MoneyDao {

    private static final String GET_PACTIVITY_TRANS_MAP_DETAILS_QUERY = "SELECT * FROM PACTIVITY_TRANS_MAP where txn_id in ({commaSeparatedIds}) and "
            + "account_number in ({senderAccountNumber}) order by ledger_type, time_created";
    private static final String LEDGER_TYPE_COL = "LEDGER_TYPE";
    private static final String OPERATION_TYPE_COL = "OPERATION_TYPE";
    private static final String TXN_ID_COL = "TXN_ID";

    private static final String GET_PACTIVITY_TRANS_MAP_DETAILS_FROM_ACCNUMBER_QUERY = "SELECT * FROM PACTIVITY_TRANS_MAP where "
            + "txn_id in ({commaSeparatedIds}) order by time_created";

    /**
     * Queries PACTIVITY_TRANS_MAP table for input transaction
     * 
     * @param transactions
     * @return
     */
    public List<PActivityTransMapDTO> getPActivityTransMapDetails(List<WTransactionDTO> transactions) {
        String query = GET_PACTIVITY_TRANS_MAP_DETAILS_QUERY
                .replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                        mapIdToString(transactions, transaction -> transaction.getId().toString()))
                .replace(SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN, transactions.get(0).getAccountNumber().toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<PActivityTransMapDTO> pActivityTransMaps = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            pActivityTransMaps.add(mapPActivityTransMapDetails(result));
        }
        return pActivityTransMaps;
    }

    public List<PActivityTransMapDTO> getPActivityTransMapDetails(Set<String> txnIds) {
        String query = GET_PACTIVITY_TRANS_MAP_DETAILS_FROM_ACCNUMBER_QUERY
                .replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN, mapIdToString(txnIds, txnId -> txnId));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<PActivityTransMapDTO> pActivityTransMaps = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            pActivityTransMaps.add(mapPActivityTransMapDetails(result));
        }
        return pActivityTransMaps;
    }

    private PActivityTransMapDTO mapPActivityTransMapDetails(Map<String, Object> result) {
        PActivityTransMapDTO pActivityTransMapDTO = new PActivityTransMapDTO();
        pActivityTransMapDTO.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
        pActivityTransMapDTO.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
        pActivityTransMapDTO.setLedgerType(getByte(result.get(LEDGER_TYPE_COL)));
        pActivityTransMapDTO.setOperationType(getByte(result.get(OPERATION_TYPE_COL)));
        pActivityTransMapDTO.setTransactionId(getBigInteger(result.get(TXN_ID_COL)));
        return pActivityTransMapDTO;
    }
}
