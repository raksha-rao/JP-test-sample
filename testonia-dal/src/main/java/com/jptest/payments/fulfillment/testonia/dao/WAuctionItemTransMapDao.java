package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.WAuctionItemTransMapP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WAuctionItemTransMapP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.WAuctionItemTransMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Unified WAuctionItemTransMapDao for 1.0 and 2.0
 */
@Singleton
public class WAuctionItemTransMapDao {

    @Inject
    private WAuctionItemTransMapP1Dao daoP10;

    @Inject
    private WAuctionItemTransMapP2Dao daoP20;

    public List<WAuctionItemTransMapDTO> getWAuctionItemTransMapDetails(List<WTransactionDTO> transactions) {
        List<WAuctionItemTransMapDTO> output = new ArrayList<>();
        output.addAll(daoP10.getWAuctionItemTransMapDetails(transactions));
        output.addAll(daoP20.getWAuctionItemTransMapDetails(transactions));
        return output;
    }
}
