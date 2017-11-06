package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.google.inject.Inject;
import com.jptest.fulfillmentengine.FulfillmentActivityLogVO;
import com.jptest.fulfillmentengine.TaskState;
import com.jptest.fulfillmentengine.TaskVO;
import com.jptest.money.FulfillPaymentInternalRequest;
import com.jptest.money.MustangActivityLog;
import com.jptest.money.PaymentPlanVO;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.payments.fulfillment.testonia.model.ActivityDetails;

/**
 * Validates that input transactions are created for "Cheetah flow" on "M16 engine" and SETTLEMENT task is created
 *
 */
public class SettlementValidationTask extends BaseTask<ActivityDetails> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementValidationTask.class);

    private static final String SETTLEMENT_TASK = "SETTLEMENT";
    private static final String MESSAGE_ID = "msgId";

    private static final String ACTIVITY_NAME_PAY = "pay";
    private static final String ACTIVITY_NAME_IACH = "iach";
    private static final String ACTIVITY_NAME_ECHECK = "echeck";
    private static final String ACTIVITY_NAME_BANK = "bank";

    @Inject
    private ActivityIdRetrieverTask activityIdRetrieverTask;
    @Inject
    private ActivityDetailsRetrieverTask activityDetailsRetrieverTask;

    private List<String> realtimeTaskNamesToBeValidated;
    private List<String> asyncTasksToBeValidated;

    public SettlementValidationTask() {
    }

    public SettlementValidationTask(List<String> realtimeTaskNamesToBeValidated, List<String> asyncTasksToBeValidated) {
        this.realtimeTaskNamesToBeValidated = realtimeTaskNamesToBeValidated;
        this.asyncTasksToBeValidated = asyncTasksToBeValidated;
    }



    @Override
    public ActivityDetails process(Context context) {
        List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        ActivityDetails result = doExecute(context, wTransactionList);
        context.setData(ContextKeys.WTRANSACTION_LIST_ACTIVITY_DETAILS_KEY.getName(), result);
        return result;
    }

    private ActivityDetails doExecute(Context context, List<WTransactionVO> wTransactionList) {
        // retrieve list of activities
        Set<BigInteger> activityIdSet = activityIdRetrieverTask.execute(wTransactionList);

        if (activityIdSet.isEmpty()) {
            Assert.fail("activityIdSet can not be empty.");
        }

        context.addReportingAttribute(ReportingAttributes.ACTIVITY_ID_SET, activityIdSet);

        ActivityDetails resultActivityDetails = null;

        // for each activity, get activity details
        for (BigInteger activityId : activityIdSet) {
            ActivityDetails activityDetails = activityDetailsRetrieverTask.execute(activityId);
            Assert.assertNotNull(activityDetails, "actity-details is expected to be not-null.");
            // validate settlement task
            validateSettlementMessage(activityId, activityDetails);
            // validate activity log
            validateActivityLog(activityId, activityDetails.getActivityLogs());
            resultActivityDetails = activityDetails;
        }

        return resultActivityDetails;
    }

    /**
     * Validate settlement message details for given activityId
     *
     * @param activityId
     * @param activityDetails
     */
    private void validateSettlementMessage(BigInteger activityId, ActivityDetails activityDetails){

        String activityName = activityDetails.getActivityName().toLowerCase();

        activityDetails.getTaskList().get(SETTLEMENT_TASK).forEach(settlementTaskDetails -> {

        if (settlementTaskDetails == null) {
            LOGGER.warn("No SETTLEMENT task details for activity_id: {}", activityId);
            if (isSettlementCheckRequired(activityName)) {
                Assert.fail("Activity " + activityId + " should contain SETTLEMENT task");
            } else {
                return;
            }
        }

        TaskVO taskVO = settlementTaskDetails.getTaskVO();
        Assert.assertNotNull(taskVO, "Settlement Task should be present.");

        if (taskVO.getStateAsEnum() != TaskState.FINISHED) {
            Assert.fail("SETTLEMENT task is not in Finished state for activity " + activityId);
        }

        String jsonResponse = taskVO.getJsonResponse();
        if (jsonResponse == null) {
            Assert.fail("jsonResponse message is null for SETTLEMENT task of activity " + activityId);
        }

        Long messageId = null;
        try {
            messageId = (new JSONObject(jsonResponse)).getLong(MESSAGE_ID);
        } catch (JSONException e) {
            LOGGER.error("Exception occurred ", e);
            Assert.fail("Failed while getting msgId from SETTLEMENT task Json reponse");
        }

        if (messageId == null || messageId.equals(0L)) {
            Assert.fail("Settlement message is null or (0) for activity_id: " + activityId);
        }

        });
    }

    private static boolean isSettlementCheckRequired(final String activityName) {
        return activityName.contains(ACTIVITY_NAME_PAY) && (activityName.contains(ACTIVITY_NAME_IACH)
                || activityName.contains(ACTIVITY_NAME_ECHECK) || activityName
                .contains(ACTIVITY_NAME_BANK));
    }

    /**
     * Validate ActivityLog details for given activityId
     *
     * @param activityId
     * @param activityLogs
     */
    private void validateActivityLog(BigInteger activityId, List<MustangActivityLog> activityLogs) {
        FulfillmentActivityLogVO fulfillmentActivityLogVO = getFulfillmentActivityLogVO(
                activityLogs.get(0).getActivityLog().getSerializedData());
        FulfillPaymentInternalRequest fulfillPaymentInternalRequest = (FulfillPaymentInternalRequest) fulfillmentActivityLogVO
                .getFulfillmentRequest();
        Assert.assertNotNull(fulfillPaymentInternalRequest,
                "fulfillPaymentInternalRequest is null for activityId " + activityId);
        PaymentPlanVO paymentPlanVO = fulfillPaymentInternalRequest.getFulfillmentPlan()
                .getPaymentPlans().get(0);
        Assert.assertNotNull(paymentPlanVO, "PaymentPlanVO is null for activityId " + activityId);
        Assert.assertNotNull(paymentPlanVO.getFundingSources(), "FundingSources are null for activityId " + activityId);
        Assert.assertNotNull(paymentPlanVO.getFundingSinks(), "FundingSinks are null for activityId " + activityId);

        if (CollectionUtils.isNotEmpty(realtimeTaskNamesToBeValidated)) {
            for (String realtimeTaskNameToBeValidated : realtimeTaskNamesToBeValidated) {
                Assert.assertTrue(taskExists(fulfillmentActivityLogVO.getRealtimeTasks(), realtimeTaskNameToBeValidated), this.getClass().getSimpleName() + ".validateActivityLog() Expected realtime task " + realtimeTaskNameToBeValidated + " not found in activity log");
            }
        }

        if (CollectionUtils.isNotEmpty(asyncTasksToBeValidated)) {
            for (String asyncTasksToBeValidated : asyncTasksToBeValidated) {
                Assert.assertTrue(taskExists(fulfillmentActivityLogVO.getAsyncMessagesForAmqDaemon(), asyncTasksToBeValidated) || taskExists(fulfillmentActivityLogVO.getAsyncTasks(), asyncTasksToBeValidated), this.getClass().getSimpleName() +
                        ".validateActivityLog() Expected async task " + asyncTasksToBeValidated + " not found in activity log");
            }
        }
    }

    private boolean taskExists(List<TaskVO> taskList, String taskNameToBeValidated){
        if (CollectionUtils.isNotEmpty(taskList)) {
            return taskList.stream().anyMatch(taskVO -> taskNameToBeValidated.equals(taskVO.getName()));
        }
        return false;
    }

    private static FulfillmentActivityLogVO getFulfillmentActivityLogVO(
            byte[] serializedData) {
        try {
            return (FulfillmentActivityLogVO) VoHelper.deserializeByteArray2VO(serializedData);
        } catch (IOException ioe) {
            LOGGER.error("Failed while converting the string to ValueObject", ioe);
            Assert.fail("Failed while converting the string to ValueObject");
        }
        return null;
    }

}
