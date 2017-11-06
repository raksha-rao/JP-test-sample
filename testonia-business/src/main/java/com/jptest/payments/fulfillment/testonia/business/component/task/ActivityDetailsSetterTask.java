package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;
import com.google.inject.Inject;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.ActivityDetailsRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.ActivityIdRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.ActivityDetails;

/**
 * Get the list of Activity Ids and set them to context
 */
public class ActivityDetailsSetterTask extends BaseTask<ActivityDetails> {

    @Inject
    private ActivityIdRetrieverTask activityIdRetrieverTask;
    @Inject
    private ActivityDetailsRetrieverTask activityDetailsRetrieverTask;

    @Override
    public ActivityDetails process(Context context) {
        @SuppressWarnings("unchecked")
        final List<WTransactionVO> wTransactionList = (List<WTransactionVO>) this.getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        final ActivityDetails result = this.execute(context, wTransactionList);
        context.setData(ContextKeys.WTRANSACTION_LIST_ACTIVITY_DETAILS_KEY.getName(), result);
        return result;
    }

    public ActivityDetails execute(Context context, List<WTransactionVO> wTransactionList) {
        // retrieve list of activities
        final Set<BigInteger> activityIdSet = new RetriableActivityIdRetrieverTask(activityIdRetrieverTask)
                .execute(wTransactionList);

        if (activityIdSet.isEmpty()) {
            Assert.fail("activityIdSet can not be empty.");
        }
        
        context.addReportingAttribute(ReportingAttributes.ACTIVITY_ID_SET, activityIdSet);

        ActivityDetails resultActivityDetails = null;

        // for each activity, get activity details
        for (final BigInteger activityId : activityIdSet) {
            final ActivityDetails activityDetails = activityDetailsRetrieverTask.execute(activityId);
            Assert.assertNotNull(activityDetails, "actity-details is expected to be not-null.");
            resultActivityDetails = activityDetails;
        }

        return resultActivityDetails;
    }

    private static class RetriableActivityIdRetrieverTask extends RetriableTask<List<WTransactionVO>, Set<BigInteger>> {

        private ActivityIdRetrieverTask activityIdRetrieverTask;

        private RetriableActivityIdRetrieverTask(ActivityIdRetrieverTask activityIdRetrieverTask) {
            super();
            this.activityIdRetrieverTask = activityIdRetrieverTask;
        }

        @Override
        public boolean isDesiredOutput(Set<BigInteger> output) {
            return CollectionUtils.isNotEmpty(output);
        }

        @Override
        public Set<BigInteger> retriableExecute(List<WTransactionVO> input) {
            return activityIdRetrieverTask.execute(input);
        }

        @Override
        protected Set<BigInteger> onSuccess(List<WTransactionVO> input, Set<BigInteger> output) {
            return output;
        }

        @Override
        protected Set<BigInteger> onFailure(List<WTransactionVO> input, Set<BigInteger> output) {
            return null;
        }

    }

}
