package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityLogDTO;
import com.jptest.vo.ValueObject;
import com.jptest.vo.serialization.Formats;

/**
 * Represents FULFILLMENT_ACTIVITY_LOG table of ENG database
 */
@Singleton
public class FulfillmentActivityLogDao extends EngDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FulfillmentActivityLogDao.class);
    private static final String GET_ACTIVITY_LOG_QUERY = "SELECT ACTIVITY_LOG FROM FULFILLMENT_ACTIVITY_LOG where activity_id = {activityId} AND LOG_NUMBER = 0";
    private static final String GET_LATEST_ACTIVITY_LOG_QUERY = "select ACTIVITY_LOG from (select ACTIVITY_LOG from fulfillment_activity_log where activity_id = {activityId} and LOG_NUMBER>0 order by log_number) where rownum<2";
    private static final String ACTIVITY_LOG_COL = "ACTIVITY_LOG";
    private static final String TIME_CREATED_COL = "TIME_CREATED";
    private static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    private static final String ACTIVITY_ID_TOKEN = "{activityId}";

    private static final String GET_ALL_ACTIVITY_LOGS_GT_GIVEN_LOG_NUMBER_QUERY = "SELECT ACTIVITY_LOG FROM FULFILLMENT_ACTIVITY_LOG where activity_id = {activityId} AND LOG_NUMBER >= {log_number} ORDER BY LOG_NUMBER";
    private static final String LOG_NUMBER_TOKEN = "{log_number}";
    private static final String GET_RECENT_LOG_FOR_ACTIVITY = "SELECT A.ACTIVITY_LOG AS ACTIVITY_LOG, B.TIME_CREATED AS TIME_CREATED, B.ACTIVITY_TYPE from FULFILLMENT_ACTIVITY_LOG A, FULFILLMENT_ACTIVITY B WHERE A.ACTIVITY_ID = {activityId} AND A.ACTIVITY_ID = B.ACTIVITY_ID AND ROWNUM < 2 ORDER BY A.LOG_NUMBER desc";
    private static final String ENG01 = "ENG01";
    private static final String ENG02 = "ENG02";
    /**
     * Queries FULFILLMENT_ACTIVITY_LOG table for input activity-id
     * @param activityId
     * @return FulfillmentActivityLogDTO containing the activity log with log_number 0
     */
    public FulfillmentActivityLogDTO getActivityLog(BigInteger activityId) {
        String query = GET_ACTIVITY_LOG_QUERY.replace(ACTIVITY_ID_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQueryUsingJDBC(getDBName(activityId), query);
        List<String> activityLogs = new ArrayList<String>();
        FulfillmentActivityLogDTO dto = new FulfillmentActivityLogDTO();
        if (!CollectionUtils.isEmpty(queryResult)) {
            for (Map<String, Object> result : queryResult) {
                try {
                    ValueObject vo = blobToValueObject(result.get(ACTIVITY_LOG_COL), ValueObject.class);
                    String activityLog = new String(VoHelper.serializeValueObject(vo, Formats.XML, false), StandardCharsets.UTF_8);
                    activityLogs.add(activityLog);
                } catch (IOException e) {
                    LOGGER.error(
                            "Error getting the activity log from FULLFILLMENT_ACTIVITY_LOG table for activity id {}",
                            activityId);
                }
            }
        }
        dto.setActivityLogs(activityLogs);
        return dto;
    }

    /**
     * Queries FULFILLMENT_ACTIVITY_LOG table for input activity-id
     * @param activityId
     * @return FulfillmentActivityLogDTO containing the activity log with latest log_number
     */
    public FulfillmentActivityLogDTO getLatestActivityLog(BigInteger activityId) {
        String query = GET_LATEST_ACTIVITY_LOG_QUERY.replace(ACTIVITY_ID_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQueryUsingJDBC(getDBName(activityId), query);
        List<String> activityLogs = new ArrayList<String>();
        FulfillmentActivityLogDTO dto = new FulfillmentActivityLogDTO();
        if (!CollectionUtils.isEmpty(queryResult)) {
            for (Map<String, Object> result : queryResult) {
                try {
                    ValueObject vo = blobToValueObject(result.get(ACTIVITY_LOG_COL), ValueObject.class);
                    String activityLog = new String(VoHelper.serializeValueObject(vo, Formats.XML, false), StandardCharsets.UTF_8);
                    activityLogs.add(activityLog);
                } catch (IOException e) {
                    LOGGER.error(
                            "Error getting the activity log from FULLFILLMENT_ACTIVITY_LOG table for activity id {}",
                            activityId);
                }
            }
        }
        dto.setActivityLogs(activityLogs);
        return dto;
    }


    /**
     * Queries FULFILLMENT_ACTIVITY_LOG table for input activity-id, returns all FulfillmentActivityLog greater than or equal to given lognumber
     * @param activityId
     * @param lognumber 
     * @return FulfillmentActivityLogDTO containing all the activity logs great
     */
    public FulfillmentActivityLogDTO getAllActivityLogsGTEGivenLognumber(BigInteger activityId, int lognumber) {
        String query = GET_ALL_ACTIVITY_LOGS_GT_GIVEN_LOG_NUMBER_QUERY.replace(ACTIVITY_ID_TOKEN,
                activityId.toString()).replace(LOG_NUMBER_TOKEN, String.valueOf(lognumber));
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQueryUsingJDBC(getDBName(activityId), query);
        List<String> activityLogs = new ArrayList<String>();
        FulfillmentActivityLogDTO dto = new FulfillmentActivityLogDTO();
        if (!CollectionUtils.isEmpty(queryResult)) {
            for (Map<String, Object> result : queryResult) {
                try {
                    ValueObject vo = blobToValueObject(result.get(ACTIVITY_LOG_COL), ValueObject.class);
                    String activityLog = new String(VoHelper.serializeValueObject(vo, Formats.XML, false), StandardCharsets.UTF_8);
                    activityLogs.add(activityLog);
                } catch (IOException e) {
                    LOGGER.error(
                            "Error getting the activity log from FULLFILLMENT_ACTIVITY_LOG table for activity id {}",
                            activityId);
                }
            }
        }
        dto.setActivityLogs(activityLogs);
        return dto;
    }
    
    public Object [] getRecentActivityLog(String activityId) {
        String query = GET_RECENT_LOG_FOR_ACTIVITY.replace(ACTIVITY_ID_TOKEN, activityId);
        Object [] activityLogTimeCreated = new Object[3];
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQueryUsingJDBC(ENG01, query);
        /**
         * In case the activity log is not found in ENG01 DB
         */
        if(CollectionUtils.isEmpty(queryResult))
            queryResult = dbHelper.executeSelectQueryUsingJDBC(ENG02, query);
        if (!CollectionUtils.isEmpty(queryResult)) {
            for (Map<String, Object> result : queryResult) {
                   try {
                       activityLogTimeCreated[0] = blobToValueObject(result.get(ACTIVITY_LOG_COL), ValueObject.class);
                       activityLogTimeCreated[1] = result.get(TIME_CREATED_COL);
                       activityLogTimeCreated[2] = result.get(ACTIVITY_TYPE);
                   } catch (IOException e) {
                        LOGGER.error(
                                   "Error getting recent activity log from FULLFILLMENT_ACTIVITY_LOG table for activity id:",
                                   activityId);
                   }
            }
        }
        return activityLogTimeCreated;
   }
}
