package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WAuctionItemTransMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WAUCTION_ITEM_TRANS_MAP table of MONEY database
 */
@Singleton
public class WAuctionItemTransMapP1Dao extends MoneyDao {

    private static final String GET_WAUCTION_ITEM_TRANS_MAP_DETAILS_QUERY = "SELECT * FROM WAUCTION_ITEM_TRANS_MAP where payer_transaction_id in ({commaSeparatedIds}) order by payer_transaction_id";
    private static final String PAYEE_TRANSACTION_ID_COL = "PAYEE_TRANSACTION_ID";
    private static final String PAYER_TRANSACTION_ID_COL = "PAYER_TRANSACTION_ID";

    /**
     * Queries WAUCTION_ITEM_TRANS_MAP table for input transaction
     * @param transactions
     * @return
     */
    public List<WAuctionItemTransMapDTO> getWAuctionItemTransMapDetails(List<WTransactionDTO> transactions) {
        String query = GET_WAUCTION_ITEM_TRANS_MAP_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WAuctionItemTransMapDTO> auctionItemTransMapList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WAuctionItemTransMapDTO auctionItemTransMap = new WAuctionItemTransMapDTO();
            auctionItemTransMap.setAuctionItemId(getBigInteger(result.get(AUCTION_ITEM_ID_COL)));
            auctionItemTransMap.setPayeeTransactionId(getBigInteger(result.get(PAYEE_TRANSACTION_ID_COL)));
            auctionItemTransMap.setPayerTransactionId(getBigInteger(result.get(PAYER_TRANSACTION_ID_COL)));
            auctionItemTransMapList.add(auctionItemTransMap);
        }
        return auctionItemTransMapList;
    }
}
