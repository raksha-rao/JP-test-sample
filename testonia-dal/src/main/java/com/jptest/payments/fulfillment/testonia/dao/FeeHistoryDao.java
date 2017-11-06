package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.FeeHistoryP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WFeeHistoryP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.FeeHistoryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Unified FeeHistoryDao for 1.0 and 2.0
 */
@Singleton
public class FeeHistoryDao {

    @Inject
    private FeeHistoryP1Dao daoP10;

    @Inject
    private WFeeHistoryP2Dao daoP20;

    public List<FeeHistoryDTO> getFeeHistoryDetails(List<WTransactionDTO> transactions) {
        List<FeeHistoryDTO> output = new ArrayList<>();
        output.addAll(daoP10.getFeeHistoryDetails(transactions));
        output.addAll(daoP20.getFeeHistoryDetails(transactions));
        return output;
    }
}
