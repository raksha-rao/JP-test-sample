package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.WxClickUrlP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WxClickUrlP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WxClickUrlDTO;

/**
 * Unified WxClickUrlDao for 1.0 and 2.0
 */
@Singleton
public class WxClickUrlDao {

    @Inject
    private WxClickUrlP1Dao daoP10;

    @Inject
    private WxClickUrlP2Dao daoP20;

    public List<WxClickUrlDTO> getWxClickUrlDetails(List<WTransactionDTO> transactions) {
        List<WxClickUrlDTO> output = new ArrayList<>();
        output.addAll(daoP10.getWxClickUrlDetails(transactions));
        output.addAll(daoP20.getWxClickUrlDetails(transactions));
        return output;
    }
}
