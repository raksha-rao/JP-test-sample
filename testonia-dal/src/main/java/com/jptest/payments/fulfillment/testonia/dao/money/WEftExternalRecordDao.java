package com.jptest.payments.fulfillment.testonia.dao.money;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WEFTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WEFT_EXTERNAL_RECORD table of MONEY database
 */
@Singleton
public class WEftExternalRecordDao extends MoneyDao {

    private static final String GET_WEF_TRANSACTION_DETAILS_QUERY = "SELECT * FROM WEFT_EXTERNAL_RECORD where account_number = {senderAccountNumber} and type = 'U' order by time_created,id";
    private static final String EXTERNAL_ID_COL = "EXTERNAL_ID";
    private static final String GENERIC_ID_COL = "GENERIC_ID";
    private static final String VENDOR_COL = "VENDOR";

    /**
     * Queries WEFT_EXTERNAL_RECORD table for input transaction
     * @param transactions
     * @return
     */
    public List<WEFTransactionDTO> getWEFTransactionDetails(List<WTransactionDTO> transactions) {
        String query = GET_WEF_TRANSACTION_DETAILS_QUERY.replace(SENDER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
                transactions.get(0).getAccountNumber().toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WEFTransactionDTO> weftExternalRecords = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WEFTransactionDTO weftExternalRecord = new WEFTransactionDTO();
            weftExternalRecord.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            weftExternalRecord.setExternalId(getString(result.get(EXTERNAL_ID_COL)));
            weftExternalRecord.setGenericId(getLong(result.get(GENERIC_ID_COL)));
            weftExternalRecord.setStatus(getByte(result.get(STATUS_COL)));
            weftExternalRecord.setType(getByte(result.get(TYPE_COL)));
            weftExternalRecord.setVendor(getByte(result.get(VENDOR_COL)));
            weftExternalRecords.add(weftExternalRecord);
        }

        return weftExternalRecords;
    }
}
