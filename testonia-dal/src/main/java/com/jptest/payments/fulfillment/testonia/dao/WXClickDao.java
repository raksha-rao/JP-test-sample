package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.WXClickP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WXClickP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WxClickDTO;

/**
 * Unified WXClickDao for 1.0 and 2.0
 */
@Singleton
public class WXClickDao {

    @Inject
    private WXClickP1Dao daoP10;

    @Inject
    private WXClickP2Dao daoP20;

    public List<WxClickDTO> getWxClickDetails(List<WTransactionDTO> transactions) {
        List<WxClickDTO> output = new ArrayList<>();
        output.addAll(daoP10.getWxClickDetails(transactions));
        output.addAll(daoP20.getWxClickDetails(transactions));
        return output;
    }
}
