package com.jptest.payments.fulfillment.testonia.business.component.validation;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import org.testng.Assert;

import com.google.inject.Inject;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.dao.pymt.PaymentSideReferenceDao;
import com.jptest.payments.fulfillment.testonia.dao.txn.EventOsAlternateIdDao;

/**
 * Retrieves list of ActivityIds from list of WTransactionVOs
 */
public class ActivityIdRetrieverTask {

    @Inject
    private PaymentSideReferenceDao paymentSideReferenceDao;
    @Inject
    private EventOsAlternateIdDao eventOsAlternateIdDao;

    /**
     * Calls PYMT or TXN DAO based on whether the transaction is cheetah transaction or not
     */
    public Set<BigInteger> execute(List<WTransactionVO> wTransactionList) {
        boolean isCheetahTransaction = wTransactionList.get(0).getFlags5().testBit(54);
        boolean isP20Transaction = wTransactionList.get(0).getFlags5().testBit(11)
                || isCheetahTransaction;
        Assert.assertTrue(isP20Transaction, "Transaction should be 2.0 transaction to do Activity Validation");

        // retrieve list of activities
        Set<BigInteger> activityIdSet = null;
        if (isCheetahTransaction) {
            activityIdSet = paymentSideReferenceDao.getActivityIdFromTransactionIds(
                    mapIdToString(wTransactionList, transaction -> transaction.getId().toString()));
        } else {
            activityIdSet = eventOsAlternateIdDao.getActivityIdFromTransactionIds(mapIdToString(wTransactionList,
                    transaction -> new StringBuilder("'").append(transaction.getId()).append("','")
                            .append(transaction.getEncryptedId()).append("','")
                            .append(transaction.getActivityId()).append("','")
                            .append(transaction.getSharedId()).append("'").toString()));
        }
        return activityIdSet;
    }

}
