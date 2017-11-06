package com.jptest.payments.fulfillment.testonia.dao.money;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import com.jptest.payments.fulfillment.testonia.model.money.WAuctionItemDTO;


/**
 * Represents WAUCTION_ITEM table of MONEY database
 */
@Singleton
public class WAuctionItemP1Dao extends MoneyDao {

    private static final String GET_WAUCTION_ITEM_DETAILS_QUERY = "SELECT * FROM WAUCTION_ITEM where id in ({auctionItemIds}) order by id";
    private static final String AUCTION_TIME_COL = "AUCTION_TIME";
    private static final String BUYER_PROTECTION_COL = "BUYER_PROTECTION";
    private static final String INSURANCE_AMOUNT_COL = "INSURANCE_AMOUNT";
    private static final String TAX_AMOUNT_COL = "TAX_AMOUNT";
    private static final String ORDER_ID_COL = "ORDER_ID";

    /**
     * Queries WAUCTION_ITEM table for input transaction
     *
     * @param transactions
     * @return
     */
    public List<WAuctionItemDTO> getWAuctionItemDetails(String commaSeparatedAuctionItemIds) {
        String query = GET_WAUCTION_ITEM_DETAILS_QUERY.replace(AUCTION_ITEM_ID_REPLACEMENT_TOKEN,
                commaSeparatedAuctionItemIds);
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WAuctionItemDTO> auctionItems = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WAuctionItemDTO auctionItem = new WAuctionItemDTO();
            auctionItem.setAuctionTime(getLong(result.get(AUCTION_TIME_COL)));
            auctionItem.setBidAmount(getLong(result.get(BID_AMOUNT_COL)));
            auctionItem.setBuyerProtection(getBigInteger(result.get(BUYER_PROTECTION_COL)));
            auctionItem.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            auctionItem.setFlags(getLong(result.get(FLAGS_COL)));
            auctionItem.setId(getBigInteger(result.get(ID_COL)));
            auctionItem.setInsuranceAmount(getLong(result.get(INSURANCE_AMOUNT_COL)));
            auctionItem.setQuantity(getInteger(result.get(QUANTITY_COL)));
            auctionItem.setShippingAmount(getLong(result.get(SHIPPING_AMOUNT_COL)));
            auctionItem.setTaxAmount(getLong(result.get(TAX_AMOUNT_COL)));
            auctionItem.setOrderId(getString(result.get(ORDER_ID_COL)));
            auctionItems.add(auctionItem);
        }
        return auctionItems;
    }
}
