package com.jptest.payments.fulfillment.testonia.core.reporting;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


import org.apache.commons.configuration.Configuration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Kafka producer for logging test results
 */

@Singleton
public class KafkaPublisher {



    @Inject
    private Configuration configuration;
    /**
     * Kafka producer properties, kakfa.host.port and kafka.topic can be changed,
     * the others should remain default but could be changed.
     */
    private static final String KAFKA_CONFIG_HOST_PORT_CFG= "kafka.host.port";
    private static final String KAFKA_TOPIC_NAME = "kafka.topic.name";
    private static final String KAFKA_BOOSTRAP_SERVERS = "bootstrap.servers";
    private static final String KAFKA_ACKS = "acks";
    private static final String KAFKA_ACKS_CFG = "kafka.acks";
    private static final String KAFKA_RETRIES = "retries";
    private static final String KAFKA_RETRIES_CFG = "kafka.retries";
    private static final String KAFKA_BATCH = "batch.size";
    private static final String KAFKA_BATCH_CFG ="kafka.batch.size" ;
    private static final String KAFKA_LINGER = "linger.ms";
    private static final String KAFKA_LINGER_CFG = "kafka.linger.ms";
    private static final String KAFKA_BUFF_MEM = "buffer.memory";
    private static final String KAFKA_BUFF_MEM_CFG = "kafka.buffer.mem";
    private static final String KAFKA_KEY_SERIALIZE = "key.serializer";
    private static final String KAFKA_KEY_SERIALIZE_CFG = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String KAFKA_VALUE_SERIALIZE = "value.serializer";
    private static final String KAFKA_VALUE_SERIALIZE_CFG = "org.apache.kafka.common.serialization.StringSerializer";







    private Properties KAFKA_PROPS = new Properties();

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPublisher.class);


    @Inject
    public void init(){

        KAFKA_PROPS.put(KAFKA_BOOSTRAP_SERVERS, configuration.getString(KAFKA_CONFIG_HOST_PORT_CFG));
        KAFKA_PROPS.put(KAFKA_ACKS,configuration.getString(KAFKA_ACKS_CFG));
        KAFKA_PROPS.put( KAFKA_RETRIES, configuration.getInt(KAFKA_RETRIES_CFG));
        KAFKA_PROPS.put(KAFKA_BATCH, configuration.getInt(KAFKA_BATCH_CFG));
        KAFKA_PROPS.put(KAFKA_LINGER, configuration.getInt(KAFKA_LINGER_CFG));
        KAFKA_PROPS.put(KAFKA_BUFF_MEM, configuration.getInt(KAFKA_BUFF_MEM_CFG));
        KAFKA_PROPS.put(KAFKA_KEY_SERIALIZE,KAFKA_KEY_SERIALIZE_CFG );
        KAFKA_PROPS.put(KAFKA_VALUE_SERIALIZE, KAFKA_VALUE_SERIALIZE_CFG);

    }

    /**
     * Assembly of object in specific format to be used with Searchserv ES Cluster, ships message after
     * object has been assembled.
     */
    public void send(Object data) {

        JSONObject kafkaMsg = new JSONObject();
        JSONObject testResultMsg = new JSONObject(data);

        testResultMsg.put("@timestamp", getTimestamp("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        kafkaMsg.put("store","demo_testonia-"+ getTimestamp("yyyy-MM-dd"));
        kafkaMsg.put("entity","testcase");
        kafkaMsg.put("tenant","demo");
        kafkaMsg.put("data", testResultMsg);


        try {
            Producer<String, String> producer = new KafkaProducer<>(KAFKA_PROPS);
            producer.send(new ProducerRecord<>(configuration.getString(KAFKA_TOPIC_NAME), kafkaMsg.toString()));
            LOGGER.info("Message sent to Kafka successfully");
            producer.close();
        } catch (Exception e){
            LOGGER.error("Message to kafka failed: ",e);
        }
    }
    /**
     * Builds timestamp in format based on String passed to method
     * timestamp is required in multiple use-cases, ES cluster index and message to be sent which
     * is also used part of searching indexes.
     */
    public String getTimestamp(String sdf){
        Date date =  new Date();
        DateFormat dateFormat = new SimpleDateFormat(sdf);
        String timestamp = dateFormat.format(date);

        return timestamp;
    }
}
