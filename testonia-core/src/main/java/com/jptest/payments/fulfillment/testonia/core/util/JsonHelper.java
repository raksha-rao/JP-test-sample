package com.jptest.payments.fulfillment.testonia.core.util;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;

public class JsonHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonHelper.class);

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerModule(new JaxbAnnotationModule());
    }

    public static String printJsonObject(Object object) {
        String output = null;
        try {
            output = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("Not a valid JSON object: ", e);
        }
        return output;
    }

    public static String printProtobufObject(MessageOrBuilder object) {
        String output = null;
        try {
            output = JsonFormat.printer().print(object);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Not a valid Protobuf object: ", e);
        }
        return output;
    }


    /**
     * Converts JSON string to a class object
     *
     * @param jsonString
     * @param inputClass
     * @param <T>
     * @return
     */
    public static <T> T convertJsonStringToObject(String jsonString, Class<T> inputClass) {
        T t = null;
        try {
            t = mapper.readValue(jsonString, inputClass);
        }
        catch (IOException e) {
            LOGGER.error("Exception occurred retrieving object from json", e);
        }
        return t;
    }
}
