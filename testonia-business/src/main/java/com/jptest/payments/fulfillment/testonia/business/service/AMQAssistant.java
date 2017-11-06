package com.jptest.payments.fulfillment.testonia.business.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.dao.money.AmqTxnDao;
import com.jptest.vo.ValueObject;
import com.jptest.vo.serialization.Formats;
import com.jptest.vo.serialization.UniversalSerializer;

/**
 * This is a refactored AMQAssistant.java from bluefin - it provides AMQ related utility operations
 */
@Singleton
public class AMQAssistant {

    private static final Logger LOGGER = LoggerFactory.getLogger(AMQAssistant.class);

    @Inject
    private AmqTxnDao amqTxnDao;

    /**
     * Parses message Id and returns messageId payload from DB 
     * @param rawMsgId
     * @return
     * @throws Exception
     */
    public String getPayloadStringByMsgId(String messageId) {
        String[] splittedMessageId = messageId.split("-");
        try {
            ValueObject vo = amqTxnDao.getAMQBlob(splittedMessageId[1], splittedMessageId[2], splittedMessageId[3],
                    splittedMessageId[4]);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            new UniversalSerializer(Formats.XML).serialize(vo, outputStream);
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Exception occurred getting payload string for {}", messageId, e);
            throw new TestExecutionException(
                    "Exception occurred getting response payload string for AMQ message : " + e.getMessage());
        }
    }

    /**
     * returns messageId payload from DB using messageId and messageType
     * 
     * @param messageId
     * @param messageType
     * @return
     */
    public String getPayloadStringByMsgIdAndType(String messageId, String messageType) {
        try {
            ValueObject vo = amqTxnDao.getAMQBlob(messageType, messageId);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            new UniversalSerializer(Formats.XML).serialize(vo, outputStream);
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Exception occurred getting payload string for {}", messageId, e);
            throw new TestExecutionException(
                    "Exception occurred getting response payload string for AMQ message : " + e.getMessage());
        }
    }
}
