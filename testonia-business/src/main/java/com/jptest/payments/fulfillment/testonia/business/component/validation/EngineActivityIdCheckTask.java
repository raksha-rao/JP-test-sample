package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.math.BigInteger;
import java.util.List;
import com.google.inject.Inject;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableBaseTask;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.dao.eng.FulfillmentActivityDao;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityDTO;


/**
 * Checks Engine ActivityId is present in the payment side reference table . This is a retriable task. Retries/waits
 * until the activity is async Complete
 */

public class EngineActivityIdCheckTask extends RetriableBaseTask<BigInteger> {

    @Inject
    private FulfillmentActivityDao fulfillmentActivityDao;

    private BigInteger activityId;

    private static final int ASYNC_COMPLETE = 2;

    public EngineActivityIdCheckTask(final BigInteger activityId) {
        this.activityId = activityId;
    }

    public EngineActivityIdCheckTask(final long maxWaitInMs, final long repeatIntervalInMs,
            final BigInteger activityId) {
        super(maxWaitInMs, repeatIntervalInMs);
        this.activityId = activityId;
    }

    /**
     * Calls PYMT or TXN DAO to retrieve activity ID
     */

    private BigInteger checkActivityId() {

        final List<FulfillmentActivityDTO> fulfillmentActivityIds = this.fulfillmentActivityDao
                .getRecordsByActivityId(this.activityId);

        for (final FulfillmentActivityDTO faDto : fulfillmentActivityIds) {
            if (faDto.getStatus() == ASYNC_COMPLETE) {
                return faDto.getActivityId();

            }

        }
        return null;
    }

    @Override
    protected boolean isDesiredOutput(final BigInteger output) {
        return output != null;
    }

    @Override
    protected BigInteger retriableExecute(final Context context) {
        return this.checkActivityId();
    }

    @Override
    protected BigInteger onSuccess(final Context context, final BigInteger output) {
        return output;
    }

    @Override
    protected BigInteger onFailure(final Context context, final BigInteger output) {
        throw new TestExecutionException("Timed out before ASYNC_COMPLETE status");
    }

}
