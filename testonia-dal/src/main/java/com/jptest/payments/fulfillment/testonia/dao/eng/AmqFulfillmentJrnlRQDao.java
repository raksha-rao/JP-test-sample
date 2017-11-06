package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.model.amq.QueueDTO;

/**
 * DAO for AMQ_FULFILLMENT_JRNL_RQ  table
 */
@Singleton
public class AmqFulfillmentJrnlRQDao extends EngDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmqFulfillmentJrnlRQDao.class);

    private static final String FIND_IN_QUEUE_BY_MESSAGE_ID = "SELECT MESSAGE_TYPE,MESSAGE_ID,NUM_RETRY FROM AMQ_FULFILLMENT_JRNL_RQ   WHERE message_id = {message_id}";

    private static final String MESSAGE_ID_TOKEN = "{message_id}";

    private static final String MESSAGE_TYPE_COL = "MESSAGE_TYPE";
    private static final String MESSAGE_ID_COL = "MESSAGE_ID";
    private static final String NUM_RETRY_COL = "NUM_RETRY";

    /**
        * Query AMQ_FULFILLMENT_JRNL_RQ  for given messageId
        * @param messageId
        * @param activityId to determine the DB
        * @return QueueDTO if exists else return null
     */
    public QueueDTO findByMessageId(Long messageId, BigInteger activityId) {
        QueueDTO queueDTO = null;
        if (messageId != null && activityId != null) {
            String query = FIND_IN_QUEUE_BY_MESSAGE_ID.replace(MESSAGE_ID_TOKEN,
                    String.valueOf(messageId));
            List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
            if (!CollectionUtils.isEmpty(queryResult)) {
                queueDTO = readQueueDetails(queryResult.get(0));
                LOGGER.info("Found message in AMQ_FULFILLMENT_JRNL_RQ  for message id - " + messageId);
            } else {
                LOGGER.info("Not found message in AMQ_FULFILLMENT_JRNL_RQ  for message id - " + messageId);
            }
        }
        return queueDTO;
    }

    private QueueDTO readQueueDetails(Map<String, Object> result) {
        QueueDTO queueDTO = new QueueDTO();
        queueDTO.setMessageId(getLong(result.get(MESSAGE_ID_COL)));
        queueDTO.setMessageType(getString(result.get(MESSAGE_TYPE_COL)));
        queueDTO.setRetry(getInteger(result.get(NUM_RETRY_COL)));
        return queueDTO;
    }

}