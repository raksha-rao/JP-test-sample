package com.jptest.payments.fulfillment.testonia.model.money;

import java.util.List;
import java.util.Map;

public class IPNVirtualizeSchemaDTO {

    private Map<String, String> map;
    private Map<String, String> defaultValues;
    private Map<String, String> transform;
    private List<String> ignoreNodeList;
    private List<String> excludeXPath;
    private Map<String, String> defaultValueMap;
    private List<String> ignoreZeroValueNodeList;
    private List<String> ignoreEmptyNodeList;
    private Map<String, String> unsetFlags;

    public Map<String, String> getMap() {
        return map;
    }

    public Map<String, String> getDefaultValues() {
        return defaultValues;
    }

    public Map<String, String> getTransform() {
        return transform;
    }

    public List<String> getIgnoreNodeList() {
        return ignoreNodeList;
    }

    public List<String> getExcludeXPath() {
        return excludeXPath;
    }

    public Map<String, String> getDefaultValueMap() {
        return defaultValueMap;
    }

    public List<String> getIgnoreZeroValueNodeList() {
        return ignoreZeroValueNodeList;
    }

    public List<String> getIgnoreEmptyNodeList() {
        return ignoreEmptyNodeList;
    }

    public Map<String, String> getUnsetFlags() {
        return unsetFlags;
    }
}
