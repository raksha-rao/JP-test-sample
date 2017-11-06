package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.google.inject.Inject;
import com.jptest.payments.fulfillment.testonia.business.component.validation.ActivityDetailsRetrieverTask;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.ActivityDetails;


/**
 * Get the activity details by Activity Id and set them to context
 */
public class SingleActivityDetailsSetterTask extends BaseTask<ActivityDetails> {

    @Inject
    private ActivityDetailsRetrieverTask activityDetailsRetrieverTask;
    @Inject
    private ActivityDetails resultActivityDetails;    
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleActivityDetailsSetterTask.class);

    private final String operationTypeKey;

    public SingleActivityDetailsSetterTask(ContextKeys operationTypeKey) {

        this.operationTypeKey = operationTypeKey.getName();

    }

    public SingleActivityDetailsSetterTask(String operationTypeKey) {

        this.operationTypeKey = operationTypeKey;

    }

    @Override
    public ActivityDetails process(Context context) {
        final BigInteger activityId = (BigInteger) this.getDataFromContext(context,
                this.operationTypeKey);
        
        // Account Level Reserves activity details will not be found since it will not mapped to the particular
        // transaction. Hence activityId would be empty. Empty will not be set in context hence activityId of those
        // kind of reserves set as BigInteger.ZERO
        
        if (activityId == null || activityId.equals(BigInteger.ZERO)) {
            return resultActivityDetails;
        }
        
        final ActivityDetails activityDetails = this.activityDetailsRetrieverTask.execute(activityId);
        Assert.assertNotNull(activityDetails, "actity-details is expected to be not-null.");
        resultActivityDetails = activityDetails;
        LOGGER.info("ACTIVITY ID :" + activityId);

        context.setData(ContextKeys.WTRANSACTION_LIST_ACTIVITY_DETAILS_KEY.getName(), resultActivityDetails);
        return resultActivityDetails;
    }

}
