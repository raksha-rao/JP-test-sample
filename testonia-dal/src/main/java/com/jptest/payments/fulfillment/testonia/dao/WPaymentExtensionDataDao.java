package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.dao.money.WPaymentExtensionDataP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WPaymentExtensionDataP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.PActivityTransMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WPaymentExtensionDataDTO;

/**
 * Unified WPaymentExtensionDataDao for 1.0 and 2.0
 */
@Singleton
public class WPaymentExtensionDataDao {

    @Inject
    private WPaymentExtensionDataP1Dao daoP10;

    @Inject
    private WPaymentExtensionDataP2Dao daoP20;

    public List<WPaymentExtensionDataDTO> getWPaymentExtensionDataDetails(
            List<PActivityTransMapDTO> paymentActivityTransactionMap) {
        List<WPaymentExtensionDataDTO> output = new ArrayList<>();
        output.addAll(daoP10.getWPaymentExtensionDataDetails(paymentActivityTransactionMap));
        output.addAll(daoP20.getWPaymentExtensionDataDetails(paymentActivityTransactionMap));
        return output;
    }
}
