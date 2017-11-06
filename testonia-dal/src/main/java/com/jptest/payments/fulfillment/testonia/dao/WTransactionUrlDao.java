package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionUrlP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionUrlP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionUrlDTO;

/**
 * Unified WTransactionUrlDao for 1.0 and 2.0
 */
@Singleton
public class WTransactionUrlDao {

    @Inject
    private WTransactionUrlP1Dao daoP10;

    @Inject
    private WTransactionUrlP2Dao daoP20;

    public List<WTransactionUrlDTO> getWTransactionUrlDetails(List<WTransactionDTO> transactions) {
        List<WTransactionUrlDTO> output = new ArrayList<>();
        output.addAll(daoP10.getWTransactionUrlDetails(transactions));
        output.addAll(daoP20.getWTransactionUrlDetails(transactions));
        return output;
    }
}
