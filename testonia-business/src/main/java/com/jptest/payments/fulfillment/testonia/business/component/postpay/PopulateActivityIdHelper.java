package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.ActivityDetailsRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.dao.pymt.PaymentSideReferenceDao;
import com.jptest.payments.fulfillment.testonia.model.ActivityDetails;


public class PopulateActivityIdHelper {

    @Inject
    private PaymentSideReferenceDao paymentSideReferenceDao;

    @Inject
    private CryptoUtil cryptoUtil;

    @Inject
    private ActivityDetailsRetrieverTask activityDetailsRetrieverTask;

    private static final String ACTIVITY_ID_COL = "ACTIVITY_ID";
    private static final Logger LOGGER = LoggerFactory.getLogger(PopulateActivityIdHelper.class);

    /**
     * This function takes the decrypted Sub Balance Handle and Sub Balance Type, using those input it try to query the
     * Payment Side Reference table and get the list of activity id's. Loop through the activity list for getting the
     * specific Sub balance activity type provided in the input.
     *
     * @param decryptedSubBalanceHandle
     * @param subBalanceActivityType
     * @return activityId
     */

    public BigInteger getActivityIdFromSubBalanceHandle(final String decryptedSubBalanceHandle,
            final String subBalanceActivityType) {
        if (decryptedSubBalanceHandle != null) {
            final List<Map<String, Object>> queryResults = this.paymentSideReferenceDao
                    .getActivityIdsFromHoldsHandle(decryptedSubBalanceHandle);
            for (final Map<String, Object> result : queryResults) {
                final String activityId = (String) result.get(ACTIVITY_ID_COL);
                final String activityType = this.getActivityTypeUsingActivityID(new BigInteger(activityId));
                if (activityType.equals(subBalanceActivityType)) {
                    return new BigInteger(activityId);
                }
            }
        }
        return null;

    }

    /**
     * Calls cryptoUtil to retrieve decrypted Transaction ID
     *
     * @param encryptedSubBalanceHandle
     * @return decryptedHoldsHandle
     */

    public String getdecryptedSubBalanceHandle(final String encryptedSubBalanceHandle) {
        if (encryptedSubBalanceHandle != null) {
            String decryptedHoldsHandle = null;
            try {
                decryptedHoldsHandle = Long.toString(this.cryptoUtil.decryptTxnId(encryptedSubBalanceHandle));
            }
            catch (final Exception e) {
                LOGGER.warn("Unable to retrive decrypted id from using CrytoUtill:{}", e);
            }
            return decryptedHoldsHandle;
        }
        return null;
    }

    /**
     * Calls ActivityDetailsRetrieverTask to get the Activity Name to populate specific Sub Balance transaction Activity
     *
     * @param activityID
     * @return activityId.getActivityName()
     */

    private String getActivityTypeUsingActivityID(final BigInteger activityID) {
        if (activityID != null) {
            final ActivityDetails activityId = this.activityDetailsRetrieverTask
                    .execute(activityID);
            return activityId.getActivityName();
        }
        return null;
    }
    
    /**
     * Returns latest activityId for input payment side reference value
     *
     * @param referenceValue
     *         (payment side reference value)
     * @return
     */
    public BigInteger getLatestActivityIdForReferenceValue(final String referenceValue) {

        RetriableLatestActivityIdForReferenceValueTask retriableLatestActivityIdForReferenceValueTask =
                new RetriableLatestActivityIdForReferenceValueTask(this.paymentSideReferenceDao);
        List<BigInteger> activityIdList = retriableLatestActivityIdForReferenceValueTask.execute(referenceValue);
        return activityIdList.get(0);
    }

    private static class RetriableLatestActivityIdForReferenceValueTask
            extends RetriableTask<String, List<BigInteger>> {

        private PaymentSideReferenceDao paymentSideReferenceDao;

        RetriableLatestActivityIdForReferenceValueTask(PaymentSideReferenceDao paymentSideReferenceDao) {
            super(60 * 1000, 10 * 1000);
            this.paymentSideReferenceDao = paymentSideReferenceDao;
        }

        @Override
        protected boolean isDesiredOutput(List<BigInteger> output) {
            return !output.isEmpty();
        }

        @Override
        protected List<BigInteger> retriableExecute(String referenceValue) {
            return paymentSideReferenceDao.getLatestActivityIdForReferenceValue(referenceValue);
        }

        @Override
        protected List<BigInteger> onSuccess(String encryptedId, List<BigInteger> output) {
            return output;
        }

        @Override
        protected List<BigInteger> onFailure(String encryptedId, List<BigInteger> output) {
            return new ArrayList<>();
        }
    }
}
