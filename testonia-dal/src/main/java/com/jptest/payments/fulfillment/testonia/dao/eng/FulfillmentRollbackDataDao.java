package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.fulfillmentengine.TaskVO;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentRollbackDataDTO;
import com.jptest.vo.serialization.Formats;

/**
 * Represents FULFILLMENT_ROLLBACK_DATA table in ENG DB
 */
@Singleton
public class FulfillmentRollbackDataDao extends EngDao {

    private static final String GET_RECORD_BY_ACTIVITY_ID = "SELECT * FROM FULFILLMENT_ROLLBACK_DATA where activity_id in ({activityIds})";
    private static final String INSERT_QUERY = "INSERT INTO FULFILLMENT_ROLLBACK_DATA (PARTICIPANT_TRANSACTION_ID,ACTIVITY_ID,ROLLBACK_TASK,STATUS,TIME_UPDATED,TIME_CREATED) VALUES( '{participant_transaction_id}',{activity_id},?,{status},{time_updated},{time_created} )";
    private static final String PARTICIPANT_TRANSACTION_ID_REPLACEMENT_TOKEN = "{participant_transaction_id}";
    private static final String ACTIVITY_ID_REPLACEMENT_TOKEN = "{activity_id}";
    private static final String TIME_UPDATED_REPLACEMENT_TOKEN = "{time_updated}";
    private static final String TIME_CREATED_REPLACEMENT_TOKEN = "{time_created}";
    private static final String STATUS_REPLACEMENT_TOKEN = "{status}";
    private static final String ROLLBACK_TASK_COL = "ROLLBACK_TASK";

    /**
     * Returns list of records matching input activityId
     * 
     * @param activityId
     * @return
     */
    public List<FulfillmentRollbackDataDTO> getRecordsByActivityId(BigInteger activityId) {
        String query = GET_RECORD_BY_ACTIVITY_ID.replace(COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQueryUsingJDBC(getDBName(activityId), query);
        List<FulfillmentRollbackDataDTO> fulfillmentRollbackDataList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            FulfillmentRollbackDataDTO fulfillmentRollbackData = new FulfillmentRollbackDataDTO();
            fulfillmentRollbackData.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            fulfillmentRollbackData.setParticipantTransactionId(getString(result.get(PARTICIPANT_TRANSACTION_ID_COL)));
            fulfillmentRollbackData.setStatus(getInteger(result.get(STATUS_COL)));
            try {
                fulfillmentRollbackData
                        .setRollbackTask(blobToValueObject(result.get(ROLLBACK_TASK_COL), TaskVO.class));
            } catch (IOException e) {
                throw new TestExecutionException("Unable to retrieve ROLLBACK_TASK blob", e);
            }
            fulfillmentRollbackDataList.add(fulfillmentRollbackData);
        }
        return fulfillmentRollbackDataList;
    }
  
    /**
     * Inserts the fulfillment roll back tasks into ENG::FULFILLMENT_ROLLBACK_DATA table
     * 
     * @param fulfillmentRollbackDataDTO
     */
    public void insert(FulfillmentRollbackDataDTO fulfillmentRollbackDataDTO) {
        TaskVO voidTask = fulfillmentRollbackDataDTO.getRollbackTask();
        byte[] serializedVoidTask;
        try {
            serializedVoidTask = VoHelper.serializeValueObject(voidTask, Formats.BINARY, false);
        }
        catch (IOException e) {
            throw new TestExecutionException("Unable to serialize ROLLBACK_TASK.", e);
        }
        String query = INSERT_QUERY
                .replace(PARTICIPANT_TRANSACTION_ID_REPLACEMENT_TOKEN,
                        fulfillmentRollbackDataDTO.getParticipantTransactionId())
                .replace(ACTIVITY_ID_REPLACEMENT_TOKEN, String.valueOf(fulfillmentRollbackDataDTO.getActivityId()))
                .replace(STATUS_REPLACEMENT_TOKEN, String.valueOf(fulfillmentRollbackDataDTO.getStatus()))
                .replace(TIME_UPDATED_REPLACEMENT_TOKEN, String.valueOf(fulfillmentRollbackDataDTO.getTimeUpdated()))
                .replace(TIME_CREATED_REPLACEMENT_TOKEN, String.valueOf(fulfillmentRollbackDataDTO.getTimeCreated()));

        dbHelper.executeUpdateQueryForBlobData(getDBName(fulfillmentRollbackDataDTO.getActivityId()), query,
                serializedVoidTask);
    }

}
