package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.WCartDetailsP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WCartDetails2P2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.WCartDetailsDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Unified WxClickUrlDao for 1.0 and 2.0
 */
@Singleton
public class WCartDetailsDao {

    @Inject
    private WCartDetailsP1Dao daoP10;

    @Inject
    private WCartDetails2P2Dao daoP20;

    public List<WCartDetailsDTO> getCartDetails(List<WTransactionDTO> transactions) {
        List<WCartDetailsDTO> output = new ArrayList<>();
        output.addAll(daoP10.getCartDetails(transactions));
        output.addAll(daoP20.getCartDetails(transactions));
        return output;
    }
}
