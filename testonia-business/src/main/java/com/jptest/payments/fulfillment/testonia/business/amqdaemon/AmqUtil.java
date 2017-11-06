package com.jptest.payments.fulfillment.testonia.business.amqdaemon;

import com.jptest.infra.amq.subscriber.MessageID;
import com.jptest.infra.amq.subscriber.util.AMQUtil;
import com.jptest.payments.fulfillment.testonia.model.RawString;
import com.jptest.payments.fulfillment.testonia.model.amq.QueueDTO;

/**
 * Testonia version of {@link com.jptest.infra.amq.subscriber.util.AMQUtil}
 * <p>
 * It differs from original class in that - it populates QueueDTO as opposed to QueueDO
 */
public class AmqUtil extends AMQUtil {

    /*
    * split serialized message into chunks for DB storage
    */
    public static void split_chunk_into_multicolumn(final byte[] buf, QueueDTO vo_) {
        long pld_length = buf.length;

        vo_.setPlsize(pld_length);
        //use new method of splitting and populating payload
        if (pld_length <= AMQUtil.MAX_PAYLOAD_LENGTH_TOTAL) {
            int pld_length_processed = 0;
            int chunk_length = 0;
            while (pld_length_processed < pld_length) {
                chunk_length = (int) Math.min(pld_length - pld_length_processed, AMQUtil.RAW_PAYLOAD_LENGTH);
                byte[] b1 = new byte[chunk_length];
                System.arraycopy(buf, pld_length_processed, b1, 0, chunk_length);
                vo_.setPayloadB1(b1);
                pld_length_processed += chunk_length;
                if (pld_length_processed >= pld_length)
                    break;

                chunk_length = (int) Math.min(pld_length - pld_length_processed, AMQUtil.RAW_PAYLOAD_LENGTH);
                byte[] b2 = new byte[chunk_length];
                System.arraycopy(buf, pld_length_processed, b2, 0, chunk_length);
                vo_.setPayloadB2(b2);
                pld_length_processed += chunk_length;
                if (pld_length_processed >= pld_length)
                    break;

                chunk_length = (int) Math.min(pld_length - pld_length_processed, AMQUtil.RAW_PAYLOAD_LENGTH);
                byte[] b3 = new byte[chunk_length];
                System.arraycopy(buf, pld_length_processed, b3, 0, chunk_length);
                vo_.setPayloadB3(b3);
                pld_length_processed += chunk_length;
                if (pld_length_processed >= pld_length)
                    break;

                chunk_length = (int) Math.min(pld_length - pld_length_processed, AMQUtil.RAW_PAYLOAD_LENGTH);
                byte[] b4 = new byte[chunk_length];
                System.arraycopy(buf, pld_length_processed, b4, 0, chunk_length);
                vo_.setPayloadB4(b4);
                pld_length_processed += chunk_length;
                if (pld_length_processed >= pld_length)
                    break;
            }
        } else {
            vo_.setPayloadB(new RawString(buf, "UTF-8"));
        }
    }

    /*
    * Compose MessageID for enqueued/requeued message
    */
    public static MessageID compose_MessageID(final String queue_name_, final String qtable_name_, Long msg_type_id,
            QueueDTO vo_) {
        return new MessageID(queue_name_, (int) vo_.getPartitionId(), msg_type_id.intValue(), qtable_name_,
                vo_.getMessageType(), (int) vo_.getSubqueueId(), vo_.getDqTime(), vo_.getMessageId());
    }

}
