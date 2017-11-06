package com.jptest.payments.fulfillment.testonia.core.reporting;

import java.util.HashMap;
import java.util.Map;

/**
 * A POJO that holds test related basic information that needs to be reported 
 */
public class Result {

    private String identifier;
    private String testHost;

    private String className;

    private long execTime = -1L;

    private Map<String, String> customAttributes = new HashMap<String, String>();

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTestHost() {
        return testHost;
    }

    public void setTestHost(String host) {
        this.testHost = host;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getExecTime() {
        return execTime;
    }

    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }

    public Map<String, String> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(Map<String, String> customAttributes) {
        this.customAttributes = customAttributes;
    }

}
