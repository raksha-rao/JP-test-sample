package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.fulfillmentengine.FulfillmentActivityLogVO;
import com.jptest.fulfillmentengine.TaskVO;
import com.jptest.money.GetMustangActivityLogsResponse;
import com.jptest.payments.fulfillment.testonia.bridge.MustangEngineAdminBridge;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.payments.fulfillment.testonia.model.ActivityDetails;
import com.jptest.payments.fulfillment.testonia.model.TaskDetails;

/**
 * Populates Activity Details for input activityId
 *
 */
public class ActivityDetailsRetrieverTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityDetailsRetrieverTask.class);

    @Inject
    private MustangEngineAdminBridge mustangEngineAdminBridge;

    public ActivityDetails execute(BigInteger activityId) {
        try {
            return getActivityDetails(activityId);
        } catch (IOException e) {
            LOGGER.error("Error occurred retrieving activity-logs for activityId {}", activityId, e);
        }

        return null;
    }

    private ActivityDetails getActivityDetails(BigInteger activityId) throws IOException {
        GetMustangActivityLogsResponse response = mustangEngineAdminBridge.getMustangActivityLogs(activityId);
        if (!CollectionUtils.isEmpty(response.getExceptions())) {
            response.getExceptions().forEach(expt -> LOGGER.error("Mustang Engine returned exception: {}", expt));
            Assert.fail(
                    "Unable to retrieve ActivityDetails for activityId " + activityId + " from Mustang engine. "
                    		+ "Chances are that M16 flag is not enabled in DB. Please enable it for this test to succeed");
        }
        ActivityDetails activityDetails = new ActivityDetails();
        activityDetails.setActivityId(activityId);
        activityDetails.setMustangActivity(true);
        activityDetails.setActivityName(response.getActivity().getActivityType());
        activityDetails.setTaskList(getTaskDetailsMap(getTasksFrom(response)));
        activityDetails.setActivityLogs(response.getActivityLogs());
        return activityDetails;
    }

    private Multimap<String, TaskDetails> getTaskDetailsMap(List<TaskVO> tasks) {
        Multimap<String, TaskDetails> list = HashMultimap.create();
        for (TaskVO mustangTask : tasks) {
                TaskDetails taskDetails = new TaskDetails();
                taskDetails.setParticipantTransactionId(mustangTask
                        .getParticipantTxnId());
                taskDetails.setTaskDetailsVO(null);
                taskDetails.setTaskVO(mustangTask);
                taskDetails.setTaskName(mustangTask.getName());
                list.put(mustangTask.getName(), taskDetails);

        }
        return list;
    }

    private static List<TaskVO> getTasksFrom(GetMustangActivityLogsResponse response) {
        List<TaskVO> tasks = new ArrayList<TaskVO>();
        updateMustangTasksFromOpaqueData(tasks, response.getActivityLogs().get(0).getActivityLog().getSerializedData());
        int listLastRecordIndex = response.getActivityLogs().size() - 1;
        updateMustangTasksFromOpaqueData(tasks,
                response.getActivityLogs().get(listLastRecordIndex).getActivityLog().getSerializedData());
        return tasks;
    }

    private static void updateMustangTasksFromOpaqueData(List<TaskVO> tasks,
            byte[] serializedData) {

        FulfillmentActivityLogVO activitylog = getFulfillmentActivityLogVO(serializedData);
        if (activitylog.getRealtimeTasks() != null) {
            tasks.addAll(activitylog.getRealtimeTasks());
        }
        if (activitylog.getAsyncTasks() != null) {
            tasks.addAll(activitylog.getAsyncTasks());
        }

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
