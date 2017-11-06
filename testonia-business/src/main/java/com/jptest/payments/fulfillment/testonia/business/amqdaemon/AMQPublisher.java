package com.jptest.payments.fulfillment.testonia.business.amqdaemon;

import org.apache.commons.configuration.Configuration;

import com.jptest.infra.amq.publisher.exception.TransportException;
import com.jptest.infra.amq.subscriber.MessageID;
import com.jptest.infra.amq.subscriber.util.AMQException;
import com.jptest.payments.fulfillment.testonia.dao.AMQEnqueueDao;
import com.jptest.vo.ValueObject;

/**
 * Testonia version of {@link com.jptest.infra.amq.publisher.AMQPublisher}
 * <p>
 * It differs from original implementation in that it uses testonia version of AMQEnqueuer to publish the message
 * 
 * @see AMQPublisherTest
 */
public class AMQPublisher {

    private AMQEnqueuer m_enqueuer = null;

    public AMQPublisher(String messageType, AMQEnqueueDao dao, Configuration config) {
        m_enqueuer = new AMQEnqueuer(messageType, dao, config);
    }

    //publish with groupID 0 and dequeueDelay 0
    public MessageID sendMessage(ValueObject message) throws TransportException {
        return sendMessage(message, 0, 0);
    }

    //publish with dequeueDelay
    public MessageID sendMessage(ValueObject message, long dequeueDelay) throws TransportException {
        return sendMessage(message, 0, dequeueDelay);
    }

    public MessageID sendMessage(ValueObject message, int groupID, long dequeueDelay) throws TransportException {

        MessageID mid = null;
        try {
            //Publishing code
            mid = m_enqueuer.enqueue(message, groupID, dequeueDelay);

        } catch (AMQException e1) {
            throw new TransportException(e1);
        } catch (Exception e) {
            throw new TransportException(e);
        }

        return mid;

    }
}
