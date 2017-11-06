package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.WAuctionItemP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WAuctionItemP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.WAuctionItemDTO;

/**
 * Unified WAuctionItemDao for 1.0 and 2.0
 */
@Singleton
public class WAuctionItemDao {

    @Inject
    private WAuctionItemP1Dao daoP10;

    @Inject
    private WAuctionItemP2Dao daoP20;

    public List<WAuctionItemDTO> getWAuctionItemDetails(String commaSeparatedAuctionItemIds) {
        List<WAuctionItemDTO> output = new ArrayList<>();
        output.addAll(daoP10.getWAuctionItemDetails(commaSeparatedAuctionItemIds));
        output.addAll(daoP20.getWAuctionItemDetails(commaSeparatedAuctionItemIds));
        return output;
    }
}
