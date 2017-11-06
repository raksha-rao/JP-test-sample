package com.jptest.payments.fulfillment.testonia.core;

import com.jptest.payments.fulfillment.testonia.core.impl.AggregateAssert;

import java.util.Map;
//import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder;

/**
 * Represents the execution context used
 * to store and get data for the execution of a logical
 * entity like a test case.
 */
public interface Context {

    /**
     * Represents the unique identifier for the context.
     * Typically a test case name.
     */
    String getContextIdentifier();

    Object getData(String key);

    void setData(String key, Object value);

    void removeData(String key);

    /**
     * Configuration driven Assert which can act as either soft or hard assert.
     */
    AggregateAssert getAggregateAssert();

    String getCorrelationId();

    /**
     * Get attributes to display on kibana dashboard
     */
    Map<String, Object> getReportingAttributes();

    /**
     * To add attributes that will show up on kibana dashboard
     */
    void addReportingAttribute(String key, Object value);

    /**
     * To add dynamic value that will be used to replace placeholder in golden file
     */
    //void addDynamicValue(String key, PlaceHolder value);

    //Map<String, PlaceHolder> getDynamicValues();

}
