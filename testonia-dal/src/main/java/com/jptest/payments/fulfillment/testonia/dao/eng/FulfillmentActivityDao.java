package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityDTO;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Represents FULFILLMENT_ACTIVITY table in ENG DB
 */
@Singleton
public class FulfillmentActivityDao extends EngDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FulfillmentActivityDao.class);
    
    private static final String GET_RECORD_BY_ACTIVITY_ID = "SELECT * FROM FULFILLMENT_ACTIVITY where activity_id in ({activityIds})";
    private static final String INSERT_QUERY = "INSERT INTO FULFILLMENT_ACTIVITY (ACTIVITY_ID,ACTIVITY_TYPE,STATUS,TASK_STATUS,TIME_UPDATED,TIME_CREATED) VALUES( {activity_id},'{activity_type}',{status},{task_status},{time_updated},{time_created} )";

    private static final String ACTIVITY_TYPE_COL = "ACTIVITY_TYPE";
    private static final String ACTIVITY_ID_REPLACEMENT_TOKEN = "{activity_id}";
    private static final String ACTIVITY_TYPE_REPLACEMENT_TOKEN = "{activity_type}";
    private static final String STATUS_REPLACEMENT_TOKEN = "{status}";
    private static final String TASK_STATUS_REPLACEMENT_TOKEN = "{task_status}";

    private static final String TIME_UPDATED_REPLACEMENT_TOKEN = "{time_updated}";
    private static final String TIME_CREATED_REPLACEMENT_TOKEN = "{time_created}";
    
    private static final String TASK_STATUS_COL = "TASK_STATUS";
    private static final String GET_UNIQUE_ACTIVITIES = "SELECT ACTIVITY_TYPE ACT_TYPE, MAX(ACTIVITY_ID) ID FROM FULFILLMENT_ACTIVITY WHERE STATUS={status} AND ACTIVITY_TYPE IN ('{activity_type}') GROUP BY ACTIVITY_TYPE";
    
    private static final String ENG01 = "ENG01"; //Represent ENG 1
    private static final String ID = "ID";
    private static final String ACT_TYPE = "ACT_TYPE";
    /**
     * Returns list of records matching input activityId
     * 
     * @param activityId
     * @return
     */
    public List<FulfillmentActivityDTO> getRecordsByActivityId(BigInteger activityId) {
        String query = GET_RECORD_BY_ACTIVITY_ID.replace(COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<FulfillmentActivityDTO> fulfillmentActivities = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            FulfillmentActivityDTO fulfillmentActivity = new FulfillmentActivityDTO();
            fulfillmentActivity.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            fulfillmentActivity.setActivityType(getString(result.get(ACTIVITY_TYPE_COL)));
            fulfillmentActivity.setStatus(getInteger(result.get(STATUS_COL)));
            fulfillmentActivity.setTaskStatus(getLong(result.get(TASK_STATUS_COL)));
            fulfillmentActivities.add(fulfillmentActivity);
        }
        return fulfillmentActivities;
    }
    
    /**
     * Inserts fulfillment activity into ENG::FULFILLMENT_ACTIVITY table
     *
     * @param fulfillmentActivityDto
     */
    public void insert(FulfillmentActivityDTO fulfillmentActivityDto) {
        String query = INSERT_QUERY
                .replace(ACTIVITY_ID_REPLACEMENT_TOKEN, String.valueOf(fulfillmentActivityDto.getActivityId()))
                .replace(ACTIVITY_TYPE_REPLACEMENT_TOKEN, fulfillmentActivityDto.getActivityType())
                .replace(STATUS_REPLACEMENT_TOKEN, String.valueOf(fulfillmentActivityDto.getStatus()))
                .replace(TASK_STATUS_REPLACEMENT_TOKEN, String.valueOf(fulfillmentActivityDto.getTaskStatus()))
                .replace(TIME_UPDATED_REPLACEMENT_TOKEN, String.valueOf(fulfillmentActivityDto.getTimeUpdated()))
                .replace(TIME_CREATED_REPLACEMENT_TOKEN, String.valueOf(fulfillmentActivityDto.getTimeCreated()));

        dbHelper.executeUpdateQuery(getDBName(fulfillmentActivityDto.getActivityId()), query);
    }

    /**
     * Fetcing unique activities based on configuration  
     * @param activityTypeKeys
     * @param status
     * @return
     */
    public Map<String,String> fetchUniqueActivities(List<String> activityTypeKeys, Integer status) {
        Map<String,String> res = null;
        final String query = GET_UNIQUE_ACTIVITIES.replace(STATUS_REPLACEMENT_TOKEN, status.toString())
                .replace(ACTIVITY_TYPE_REPLACEMENT_TOKEN, String.join("','", activityTypeKeys));
        LOGGER.info("fetchUniqueActivities query: {} ", query);
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(ENG01, query);
        if (!CollectionUtils.isEmpty(queryResult)) {
            res = new HashMap<>();
            for (Map<String, Object> result : queryResult) {
                res.put((String)result.get(ID), (String)result.get(ACT_TYPE));
            }
        }
        return res;
    }
}
