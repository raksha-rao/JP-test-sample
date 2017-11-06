package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.Map;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder;

public class TestCaseLocalContext implements Context {

    private Context globalContext;
    private String inputKey;

    public TestCaseLocalContext(Context globalContext, String inputKey) {
        this.globalContext = globalContext;
        this.inputKey = inputKey;
    }

    @Override
    public String getContextIdentifier() {
        return globalContext.getContextIdentifier();
    }

    @Override
    public Object getData(String key) {
        return globalContext.getData(key);
    }

    @Override
    public void setData(String key, Object value) {
        globalContext.setData(key, value);
    }

    @Override
    public void removeData(String key) {
        globalContext.removeData(key);
    }

    public String getInputKey() {
        return inputKey;
    }

    @Override
    public AggregateAssert getAggregateAssert() {
        return globalContext.getAggregateAssert();
    }

    @Override
    public String getCorrelationId() {
        return globalContext.getCorrelationId();
    }

    /**
     * Get attributes to display on kibana dashboard
     */
    @Override
    public Map<String, Object> getReportingAttributes() {
        return globalContext.getReportingAttributes();
    }

    /**
     * To add attributes that will show up on kibana dashboard
     */
    @Override
    public void addReportingAttribute(String key, Object value) {
        globalContext.addReportingAttribute(key, value);
    }

    @Override
    public Map<String, PlaceHolder> getDynamicValues() {
        return globalContext.getDynamicValues();
    }

    /**
     * To add dynamic value that will be used to replace placeholder in golden file
     */
    @Override
    public void addDynamicValue(String key, PlaceHolder value) {
        globalContext.addDynamicValue(key, value);
    }
}
