package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.payments.fulfillment.testonia.model.RawString;
import com.jptest.payments.fulfillment.testonia.model.amq.QueueDTO;
import com.jptest.vo.ValueObject;
import com.jptest.vo.serialization.Formats;


/**
 * DAO for AMQ_FULFILLMENT_JRNL_EX table
 */
@Singleton
public class AmqFulfillmentJrnlExDao extends EngDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmqFulfillmentJrnlExDao.class);

    private static final String FIND_IN_QUEUE_BY_MESSAGE_ID = "SELECT * FROM AMQ_FULFILLMENT_JRNL_EX  WHERE message_id = {message_id}";

    private static final String MESSAGE_ID_TOKEN = "{message_id}";

    private static final String MESSAGE_TYPE_COL = "MESSAGE_TYPE";
    private static final String MESSAGE_ID_COL = "MESSAGE_ID";
    private static final String NUM_RETRY_COL = "NUM_RETRY";
    private static final String PAYLOAD_C_COL = "PAYLOAD_C";
    private static final String PAYLOAD_B_COL = "PAYLOAD_B";
    private static final String SUBQUEUE_ID_COL = "SUBQUEUE_ID";
    private static final String PLSIZE_COL = "PLSIZE";
    private static final String GROUP_ID_COL = "GROUP_ID";
    private static final String PARTITION_ID_COL = "PARTITION_ID";
    private static final String ENQ_TIME_COL = "ENQ_TIME";
    private static final String MESSAGE_SOURCE_COL = "MESSAGE_SOURCE";
    private static final String DQ_TIME_COL = "DQ_TIME";
    private static final String VERSION_COL = "VERSION";

    /**
     * Query AMQ_FULFILLMENT_JRNL_EX for given messageId
     * 
     * @param messageId
     * @param activityId
     *            to determine the DB
     * @return QueueDTO if exists else return null
     */
    public QueueDTO findByMessageId(Long messageId, BigInteger activityId) {
        QueueDTO queueDTO = null;
        if (messageId != null && activityId != null) {
            String query = FIND_IN_QUEUE_BY_MESSAGE_ID.replace(MESSAGE_ID_TOKEN,
                    String.valueOf(messageId));
            List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
            if (!CollectionUtils.isEmpty(queryResult)) {
                queueDTO = readQueueDetails(queryResult.get(0), activityId);
                LOGGER.info("Found message in AMQ_FULFILLMENT_JRNL_EX  for message id - " + messageId);
            }
            else {
                LOGGER.info("Not found message in AMQ_FULFILLMENT_JRNL_EX  for message id - " + messageId);
            }
        }
        return queueDTO;
    }

    private QueueDTO readQueueDetails(Map<String, Object> result, BigInteger activityId) {
        QueueDTO queueDTO = new QueueDTO();
        queueDTO.setMessageId(getLong(result.get(MESSAGE_ID_COL)));
        queueDTO.setMessageType(getString(result.get(MESSAGE_TYPE_COL)));
        queueDTO.setRetry(getInteger(result.get(NUM_RETRY_COL)));
        queueDTO.setMessageSource(getString(result.get(MESSAGE_SOURCE_COL)));
        queueDTO.setSubqueueId(getLong(result.get(SUBQUEUE_ID_COL)));
        queueDTO.setPlsize(getLong(result.get(PLSIZE_COL)));
        queueDTO.setGroupId(getLong(result.get(GROUP_ID_COL)));
        queueDTO.setPartitionId(getLong(result.get(PARTITION_ID_COL)));
        queueDTO.setEnqTime(getLong(result.get(ENQ_TIME_COL)));
        queueDTO.setDqTime(getLong(result.get(DQ_TIME_COL)));
        queueDTO.setPayloadC(getString(result.get(PAYLOAD_C_COL)));
        queueDTO.setVersion(getString(result.get(VERSION_COL)));

        try {
            queueDTO.setPayloadB(new RawString(new String(VoHelper.serializeValueObject(
                    blobToValueObject(result.get(PAYLOAD_B_COL), ValueObject.class), Formats.XML, false),
                    StandardCharsets.UTF_8)));
        }
        catch (IOException e) {
            LOGGER.error("Error getting PAYLOAD_B from AMQ_FULFILLMENT_JRNL_EX table for activity id {}",
                    activityId, e);
        }
        return queueDTO;
    }

}
