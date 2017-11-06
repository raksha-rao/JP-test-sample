package com.jptest.payments.fulfillment.testonia.dao.money;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WAuctionItemAddlAttrDTO;

/**
 * Represents WAUCTION_ITEM_ADDL_ATTR table of MONEY database
 */
@Singleton
public class WAuctionItemAddlAttrDao extends MoneyDao {

    private static final String GET_WAUCTION_ITEM_ADDL_ATTR_DETAILS_QUERY = "SELECT * FROM WAUCTION_ITEM_ADDL_ATTR where auction_item_id in ({auctionItemIds}) order by auction_item_id";
    private static final String ADDL_SHIPPING_COL = "ADDL_SHIPPING";
    private static final String BASE_SHIPPING_COL = "BASE_SHIPPING";
    private static final String HANDLING_COL = "HANDLING";
    private static final String OPTIONS_COL = "OPTIONS";
    private static final String TAX_COL = "TAX";

    /**
     * Queries WAUCTION_ITEM_ADDL_ATTR table for input transaction
     * @param transactions
     * @return
     */
    public List<WAuctionItemAddlAttrDTO> getWAuctionItemAdditionalAttributesDetails(
            String commaSeparatedAuctionItemIds) {
        String query = GET_WAUCTION_ITEM_ADDL_ATTR_DETAILS_QUERY.replace(AUCTION_ITEM_ID_REPLACEMENT_TOKEN,
                commaSeparatedAuctionItemIds);
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        List<WAuctionItemAddlAttrDTO> auctionItemAddlAttrs = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            WAuctionItemAddlAttrDTO auctionItemAddlAttr = new WAuctionItemAddlAttrDTO();
            auctionItemAddlAttr.setAdditionalShipping(getLong(result.get(ADDL_SHIPPING_COL)));
            auctionItemAddlAttr.setAuctionItemId(getBigInteger(result.get(AUCTION_ITEM_ID_COL)));
            auctionItemAddlAttr.setBaseShipping(getLong(result.get(BASE_SHIPPING_COL)));
            auctionItemAddlAttr.setHandling(getLong(result.get(HANDLING_COL)));
            auctionItemAddlAttr.setOptions(getString(result.get(OPTIONS_COL)));
            auctionItemAddlAttr.setTax(getLong(result.get(TAX_COL)));
            auctionItemAddlAttrs.add(auctionItemAddlAttr);
        }
        return auctionItemAddlAttrs;
    }
}
