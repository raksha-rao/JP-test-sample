package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionDetailsP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionDetailsP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDetailsDTO;

/**
 * Unified WTransactionDetailsDao for 1.0 and 2.0
 */
@Singleton
public class WTransactionDetailsDao {

    @Inject
    private WTransactionDetailsP1Dao daoP10;

    @Inject
    private WTransactionDetailsP2Dao daoP20;

    public List<WTransactionDetailsDTO> getWTransactionDetails(List<WTransactionDTO> transactions) {
        List<WTransactionDetailsDTO> output = new ArrayList<>();
        output.addAll(daoP10.getWTransactionDetails(transactions));
        output.addAll(daoP20.getWTransactionDetails(transactions));
        return output;
    }
}
