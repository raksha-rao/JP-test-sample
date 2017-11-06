package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.FxHistoryP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WFxHistoryP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.FXHistoryDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Unified FxHistoryDao for 1.0 and 2.0
 */
@Singleton
public class FxHistoryDao {

    @Inject
    private FxHistoryP1Dao daoP10;

    @Inject
    private WFxHistoryP2Dao daoP20;

    public List<FXHistoryDTO> getFXHistoryDetails(List<WTransactionDTO> transactions) {
        List<FXHistoryDTO> output = new ArrayList<>();
        output.addAll(daoP10.getFXHistoryDetails(transactions));
        output.addAll(daoP20.getFXHistoryDetails(transactions));
        return output;
    }
}
