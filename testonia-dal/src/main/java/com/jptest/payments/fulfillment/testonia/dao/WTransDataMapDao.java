package com.jptest.payments.fulfillment.testonia.dao;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransDataMapP1Dao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransDataMapP2Dao;
import com.jptest.payments.fulfillment.testonia.model.money.WTransDataMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;


/**
 * Unified WTransDataMapDao for 1.0 and 2.0
 */
@Singleton
public class WTransDataMapDao {

    @Inject
    private WTransDataMapP1Dao daoP10;

    @Inject
    private WTransDataMapP2Dao daoP20;

    public List<WTransDataMapDTO> getTransDataMapDetails(List<WTransactionDTO> transactions) {
        List<WTransDataMapDTO> output = new ArrayList<>();
        output.addAll(daoP10.getTransDataMapDetails(transactions));
        output.addAll(daoP20.getTransDataMapDetails(transactions));
        return output;
    }
}
