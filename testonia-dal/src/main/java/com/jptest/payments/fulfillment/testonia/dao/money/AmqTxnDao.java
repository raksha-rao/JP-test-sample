package com.jptest.payments.fulfillment.testonia.dao.money;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.vo.ValueObject;

/**
 * DAO for AMQ_TXN_Q table of MONEY DB
 */
@Singleton
public class AmqTxnDao extends MoneyDao {

    // AMQ table
    private static final String GET_AMQ_BLOB_QUERY = "select payload_b from amq_txn_q where message_type = '{messageType}' and "
            + "subqueue_id = '{subqueueId}' and dq_time = {dqTime} and message_id = {messageId} and partition_id >=0";
    private static final String MESSAGE_TYPE_REPLACEMENT_TOKEN = "{messageType}";
    private static final String SUBQUEUE_ID_REPLACEMENT_TOKEN = "{subqueueId}";
    private static final String DQ_TIME_REPLACEMENT_TOKEN = "{dqTime}";
    private static final String MESSAGE_ID_REPLACEMENT_TOKEN = "{messageId}";
    private static final String PAYLOAD_BLOB_COL = "PAYLOAD_B";

    private static final String GET_AMQ_BLOB_QUERY_BY_MESSAGE_ID_AND_TYPE = "select payload_b from amq_txn_q where message_type = '{messageType}' and "
            + "message_id = {messageId} and partition_id >=0";

    private static final String GET_MIN_MESSAGE_ID = "SELECT message_id FROM amq_txn_q WHERE ROWID=(SELECT MIN(ROWID) FROM amq_txn_q where message_type='TXNA')";
    private static final String GET_MAX_MESSAGE_ID = "SELECT message_id FROM amq_txn_q WHERE ROWID=(SELECT MAX(ROWID) FROM amq_txn_q where message_type='TXNA')";
    private static final String MESSAGE_ID_COL = "MESSAGE_ID";

    /**
     * Queries AMQ table and returns payload-blob VO for given messageId
     *
     * @param messageType
     * @param subqueueId
     * @param dqTime
     * @param messageId
     * @return
     * @throws IOException
     */
    public ValueObject getAMQBlob(String messageType, String subqueueId, String dqTime, String messageId)
            throws IOException {
        ValueObject output = null;
        String query = GET_AMQ_BLOB_QUERY
                .replace(MESSAGE_TYPE_REPLACEMENT_TOKEN, messageType)
                .replace(SUBQUEUE_ID_REPLACEMENT_TOKEN, subqueueId)
                .replace(DQ_TIME_REPLACEMENT_TOKEN, dqTime)
                .replace(MESSAGE_ID_REPLACEMENT_TOKEN, messageId);
        List<Map<String, Object>> dbResults = dbHelper.executeSelectQueryUsingJDBC(getDatabaseName(), query);
        for (Map<String, Object> dbResult : dbResults) {
            output = blobToValueObject(dbResult.get(PAYLOAD_BLOB_COL), ValueObject.class);
        }
        return output;
    }

    /**
     * Queries AMQ table and returns payload-blob VO for given messageId
     *
     * @param messageType
     * @param subqueueId
     * @param dqTime
     * @param messageId
     * @return
     * @throws IOException
     */
    public ValueObject getAMQBlob(String messageType, String messageId)
            throws IOException {
        ValueObject output = null;
        String query = GET_AMQ_BLOB_QUERY_BY_MESSAGE_ID_AND_TYPE
                .replace(MESSAGE_TYPE_REPLACEMENT_TOKEN, messageType)
                .replace(MESSAGE_ID_REPLACEMENT_TOKEN, messageId);
        List<Map<String, Object>> dbResults = dbHelper.executeSelectQueryUsingJDBC(getDatabaseName(), query);
        for (Map<String, Object> dbResult : dbResults) {
            output = blobToValueObject(dbResult.get(PAYLOAD_BLOB_COL), ValueObject.class);
        }
        return output;
    }

    public String getSmallestMessageID() {
        Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), GET_MIN_MESSAGE_ID);
        return result != null ? getString(result.get(MESSAGE_ID_COL)) : null;
    }

    public String getLargestMessageID() {
        Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), GET_MAX_MESSAGE_ID);
        return result != null ? getString(result.get(MESSAGE_ID_COL)) : null;
    }

}
