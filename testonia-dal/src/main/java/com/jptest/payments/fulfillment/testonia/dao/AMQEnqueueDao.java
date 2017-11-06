package com.jptest.payments.fulfillment.testonia.dao;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.amq.QueueDTO;

/**
 * Common DAO class to be used to publish payload to AMQ Daemon
 * 
 * e.g. It can be used to insert record into
 * MONEY - AMQ_MONEYINTERNAL_Q
 * ENG - AMQ_FULFILLMENT_JRNL_Q
 */
@Singleton
public class AMQEnqueueDao extends BaseDao {

    private static final String INSERT_QUERY = "INSERT INTO {tableName} (MESSAGE_TYPE, SUBQUEUE_ID, DQ_TIME, MESSAGE_ID, PARTITION_ID, GROUP_ID, "
            + "MESSAGE_SOURCE, PLSIZE, DELETE_FLAG, ENQ_TIME, VERSION, PAYLOAD_1, PAYLOAD_C, PAYLOAD_B1, PAYLOAD_B2, PAYLOAD_B3, PAYLOAD_B4, PAYLOAD_B) "
            + "values ({messageType}, {subqueueId}, {dqTime}, {messageId}, {partitionId}, {groupId}, {messageSource}, {plSize}, {deleteFlag}, {enqueueTime}, {version}, "
            + "{payload1}, {payloadC}, ?, ?, ?, ?, ? )";

    private static final String TABLE_NAME_REPLACEMENT_TOKEN = "{tableName}";
    private static final String MESSAGE_TYPE_REPLACEMENT_TOKEN = "{messageType}";
    private static final String SUBQUEUE_ID_REPLACEMENT_TOKEN = "{subqueueId}";
    private static final String DQ_TIME_REPLACEMENT_TOKEN = "{dqTime}";
    private static final String MESSAGE_ID_REPLACEMENT_TOKEN = "{messageId}";
    private static final String PARTITION_ID_REPLACEMENT_TOKEN = "{partitionId}";
    private static final String GROUP_ID_REPLACEMENT_TOKEN = "{groupId}";
    private static final String MESSAGE_SOURCE_REPLACEMENT_TOKEN = "{messageSource}";
    private static final String PLSIZE_REPLACEMENT_TOKEN = "{plSize}";
    private static final String DELETE_FLAG_REPLACEMENT_TOKEN = "{deleteFlag}";
    private static final String ENQ_TIME_REPLACEMENT_TOKEN = "{enqueueTime}";
    private static final String VERSION_REPLACEMENT_TOKEN = "{version}";
    private static final String PAYLOAD1_REPLACEMENT_TOKEN = "{payload1}";
    private static final String PAYLOADC_REPLACEMENT_TOKEN = "{payloadC}";

    /**
     * Inserts queueDTO
     * @param queueDTO
     */
    public void insert(QueueDTO queueDTO) {
        String insertQuery = INSERT_QUERY.replace(TABLE_NAME_REPLACEMENT_TOKEN, queueDTO.getTableName())
                .replace(MESSAGE_TYPE_REPLACEMENT_TOKEN, quotedString(queueDTO.getMessageType()))
                .replace(SUBQUEUE_ID_REPLACEMENT_TOKEN, String.valueOf(queueDTO.getSubqueueId()))
                .replace(DQ_TIME_REPLACEMENT_TOKEN, String.valueOf(queueDTO.getDqTime()))
                .replace(MESSAGE_ID_REPLACEMENT_TOKEN, String.valueOf(queueDTO.getMessageId()))
                .replace(PARTITION_ID_REPLACEMENT_TOKEN, String.valueOf(queueDTO.getPartitionId()))
                .replace(GROUP_ID_REPLACEMENT_TOKEN, String.valueOf(queueDTO.getGroupId()))
                .replace(MESSAGE_SOURCE_REPLACEMENT_TOKEN, quotedString(queueDTO.getMessageSource()))
                .replace(PLSIZE_REPLACEMENT_TOKEN, String.valueOf(queueDTO.getPlsize()))
                .replace(DELETE_FLAG_REPLACEMENT_TOKEN, quotedString(String.valueOf(queueDTO.getDeleteFlag())))
                .replace(ENQ_TIME_REPLACEMENT_TOKEN, String.valueOf(queueDTO.getEnqTime()))
                .replace(VERSION_REPLACEMENT_TOKEN, quotedString(queueDTO.getVersion()))
                .replace(PAYLOAD1_REPLACEMENT_TOKEN, quotedString(queueDTO.getPayload1()))
                .replace(PAYLOADC_REPLACEMENT_TOKEN, quotedString(queueDTO.getPayloadC()));

        dbHelper.executeUpdateQueryForBlobData(queueDTO.getDbName(), insertQuery, queueDTO.getPayloadB1(),
                queueDTO.getPayloadB2(), queueDTO.getPayloadB3(), queueDTO.getPayloadB4(),
                queueDTO.getPayloadB() != null ? queueDTO.getPayloadB().getBytes() : null);
    }

    @Override
    protected String getDatabaseName() {
        throw new UnsupportedOperationException("Enter database name directly to the query");
    }

}
