package com.jptest.payments.fulfillment.testonia.business.amqdaemon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.configuration.Configuration;

import com.jpinc.kernel.cal.CalClientConfigFactory;
import com.jpinc.kernel.cal.api.CalEvent;
import com.jpinc.kernel.cal.api.CalTransaction;
import com.jpinc.kernel.cal.api.sync.CalEventFactory;
import com.jpinc.kernel.cal.api.sync.CalTransactionFactory;
import com.jpinc.kernel.cal.mxbean.CalClientConfigMXBean;
import com.jpinc.kernel.logger.Logger;
import com.jptest.infra.amq.publisher.AMQPublisherUtil;
import com.jptest.infra.amq.subscriber.MessageID;
import com.jptest.infra.amq.subscriber.util.AMQException;
import com.jptest.infra.amq.subscriber.util.AMQUtil;
import com.jptest.infra.util.LocalHost;
import com.jptest.payments.fulfillment.testonia.dao.AMQEnqueueDao;
import com.jptest.payments.fulfillment.testonia.model.amq.QueueDTO;
import com.jptest.vo.ValueObject;
import com.jptest.vo.serialization.UniversalSerializer;

/**
 * Testonia version of {@link com.jptest.infra.amq.publisher.AMQEnqueuer}.
 * <p>
 * It differs from original implementation in that - it internally uses testonia-dal (instead of jpinc DAL framework)
 * to insert record into AMQ Queue.
 * 
 */
@SuppressWarnings("PMD")
class AMQEnqueuer {
    private static final Logger logger = Logger.getInstance(AMQEnqueuer.class);

    private String qtable_name;
    private String message_type;
    private String sequence_name;
    private QueueConfigImpl m_q_config;

    private String originalMessageType;

    // occ name
    private String queueOccName;

    private AMQEnqueueDao dao;

    /**
     * @param messageType either exact messageType as in cdb
     * or composite of of messageType in cdb with occ name override delimited 
     * by colon ":" occ name is datasource name that should match what you 
     * have defined/generated into dsimport.xml
     * eg: ENG:occ-eng1
     */
    AMQEnqueuer(String messageType, AMQEnqueueDao dao, Configuration config) {
        this.dao = dao;

        // check if messageType is composite (does it contain :?)
        if (messageType.contains(":")) {
            String[] messageTypeComponents = messageType.split(":");
            messageType = messageTypeComponents[0];
            queueOccName = messageTypeComponents[1];
        }

        originalMessageType = messageType;
        List<String> messageList = new ArrayList<String>();
        messageList.add(messageType);

        m_q_config = new QueueConfigImpl(config, messageList);

        message_type = m_q_config.fix_message_type(messageType);

        qtable_name = String.format(AMQUtil.AMQ_FMT_QUEUE_TABLE_NAME, m_q_config.get_queue_name());
        long seq_num = m_q_config.get_seq_num(0);

        sequence_name = String.format(AMQUtil.AMQ_FMT_MSGTYPE_SEQ_NAME, m_q_config.get_queue_name(), seq_num);
        //List<String> sequenceList = new ArrayList<String>();
        //sequenceList.add(sequence_name);

        // if queueOccName is not set, let's fall back to config in cdb files
        if (queueOccName == null) {
            queueOccName = m_q_config.get_queue_occ_name();
        }

        AMQUtil.MarkerCALEvent("AMQ_PUBLISHER", "Q_TOUPLE_ADD" + "_" + queueOccName + "." + qtable_name, "0", null);

    }

    long compute_subqueueId(long dequeueTime, long groupID) {
        long subQueueId = 1;

        int subqueue_max = (int) (m_q_config.compute_subqueue_max(dequeueTime));

        if (groupID == 0) {
            ThreadLocalRandom r = ThreadLocalRandom.current();
            subQueueId = r.nextInt(0, subqueue_max) + 1;
        } else {
            subQueueId = (groupID % subqueue_max) + 1;
        }

        return subQueueId;

    }

    public MessageID enqueue(ValueObject payloadVO, int groupID, long dequeueDelay) {
        long delay = dequeueDelay;
        delay *= 1000;
        if ((dequeueDelay) < 0) {
            throw new AMQException("dequeueDelay=" + dequeueDelay + " is negative.");
        }

        if (delay > m_q_config.get_max_dequeue_delay()) {
            throw new AMQException("dequeueDelay=" + dequeueDelay + "exceeds max dequeue delay");
        }

        //        ICalService calService = CalServiceFactory.getCalService();
        //        String amq_publisher_id = CALUtils.getPoolName(calService);
        String amq_publisher_id = null;
        CalClientConfigMXBean calbean = CalClientConfigFactory.create();
        if (calbean != null) {
            amq_publisher_id = calbean.getPoolname();
        } else {
            amq_publisher_id = "";
        }

        CalTransaction ctenqueue = CalTransactionFactory.create(AMQUtil.AMQ_CAL_TRANS_ENQUEUE_Q);
        ctenqueue.setName(m_q_config.get_queue_name());
        ctenqueue.addData("publisher_id", amq_publisher_id);

        long enqueueTime = System.currentTimeMillis();
        long dequeueTime = enqueueTime + delay;

        // message source
        InetAddress addr = LocalHost.getInstance().getLocalHost();
        String hostName = ((addr == null) ? "" : addr.getHostName());
        String messageSource = AMQPublisherUtil.getCorrelationId() + "@";
        messageSource = messageSource + amq_publisher_id + "@" + hostName;
        if (messageSource.length() > 64) {
            messageSource = messageSource.substring(0, 64);
        }

        // groupid
        long groupId = groupID;

        // subqueue id
        long subqueueId = compute_subqueueId(dequeueTime, groupId);

        // version
        String version = "1.0";

        // partition id
        long partitionID = 0;
        partitionID = m_q_config.compute_partition_id(dequeueTime);

        // messsage id        
        long messageID = AMQUtil.build_message_uid();
        // messageID = AMQMessageSequenceGenDAO.getInstance().findNextSequenceNum(sequence_name);

        QueueDTO queueVO = new QueueDTO();
        queueVO.setMessageType(message_type);
        queueVO.setMessageSource(messageSource);
        queueVO.setSubqueueId(subqueueId);
        queueVO.setDqTime(dequeueTime);
        queueVO.setEnqTime(enqueueTime);
        queueVO.setGroupId(groupId);
        queueVO.setVersion(version);
        queueVO.setMessageId(messageID);
        queueVO.setPartitionId(partitionID);
        queueVO.setDeleteFlag('N');

        queueVO.setDbName(queueOccName);
        queueVO.setTableName(qtable_name);

        // Payload
        UniversalSerializer serializer = new UniversalSerializer(
                m_q_config.getSerializationFormatMap().get(originalMessageType));
        final ByteArrayOutputStream binaryPayload = new ByteArrayOutputStream();
        try {
            serializer.serialize(payloadVO, binaryPayload);
            binaryPayload.flush();
            AmqUtil.split_chunk_into_multicolumn(binaryPayload.toByteArray(), queueVO);
        } catch (IOException e) {
            throw new AMQException(e.toString());
        }

        if (logger.isDebugEnabled()) {
            StringBuilder publishMessage = new StringBuilder("Publishing message with ");
            publishMessage.append(" messageType=");
            publishMessage.append(message_type);
            publishMessage.append(" messageSource=");
            publishMessage.append(messageSource);
            publishMessage.append(" subqueueId=");
            publishMessage.append(subqueueId);
            publishMessage.append(" dequeueTime=");
            publishMessage.append(dequeueTime);
            publishMessage.append(" enqueueTime=");
            publishMessage.append(enqueueTime);
            publishMessage.append(" groupId=");
            publishMessage.append(groupId);
            publishMessage.append(" version=");
            publishMessage.append(version);
            publishMessage.append(" messageID=");
            publishMessage.append(messageID);
            publishMessage.append(" partitionID=");
            publishMessage.append(partitionID);
            logger.debug(publishMessage.toString());
        }

        StringBuilder sb = new StringBuilder();
        String message_metadata = sb.append("message_type=").append(message_type).append("&subqueueId=")
                .append(subqueueId).append("&dequeueTime=").append(dequeueTime).append("&version=").append(version)
                .append("&messageID=").append(messageID).append("&partitionId=").append(partitionID).toString();

        ctenqueue.addData(message_metadata);

        try {
            dao.insert(queueVO);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to publish the message");
            }
            CalEvent ce = CalEventFactory.create("ENQ_FAILED");
            ce.addData("error", e.getMessage());
            ce.setName(AMQUtil.AMQ_CAL_TRANS_ENQUEUE_Q);
            ce.setStatus("2.AMQPublisher.INTERNAL.ERROR");
            ce.completed();
            e.printStackTrace();
            throw new AMQException(e.toString());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Successfully published the message");
        }

        // Close CAL transaction
        ctenqueue.setStatus("0");
        ctenqueue.completed();

        MessageID mid = AmqUtil.compose_MessageID(m_q_config.get_queue_occ_name(), qtable_name,
                m_q_config.get_seq_num(0), queueVO);

        return mid;

    }

}
