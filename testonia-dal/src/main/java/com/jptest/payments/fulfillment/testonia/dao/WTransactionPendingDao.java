package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionPendingP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionPendingP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionPendingDTO;

/**
 * Unified WTransactionPendingDao for 1.0 and 2.0
 */
@Singleton
public class WTransactionPendingDao {

    @Inject
    private WTransactionPendingP1Dao daoP10;

    @Inject
    private WTransactionPendingP2Dao daoP20;

    public List<WTransactionPendingDTO> getPendingTransactionDetails(
            List<WTransactionDTO> transactions) {
        List<WTransactionPendingDTO> output = new ArrayList<>();
        output.addAll(daoP10.getPendingTransactionDetails(transactions));
        output.addAll(daoP20.getPendingTransactionDetails(transactions));
        return output;
    }
}
