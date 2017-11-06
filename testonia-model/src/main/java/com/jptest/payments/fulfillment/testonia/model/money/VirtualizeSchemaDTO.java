package com.jptest.payments.fulfillment.testonia.model.money;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class VirtualizeSchemaDTO {

    private Map<String, String> map;
    private Map<String, String> defaultValues;
    private Map<String, String> transform;
    private List<String> ignoreNodeList;
    private List<String> ignoreAttributeList;
    private List<String> excludeXPath;
    private Map<String, String> defaultValueMap;
    private List<String> ignoreZeroValueNodeList;
    private List<String> ignoreEmptyNodeList;
    private Map<String, String> unsetFlags;
    private Map<String, String> setIfNonEmpty;
    private Map<String, String> transformXPath;
    private List<String> ignoreXPath;
    private Map<String, String> setIfEmpty;


    public VirtualizeSchemaDTO() {

    }

    public VirtualizeSchemaDTO(final VirtualizeSchemaDTO another) {
        if (another.getMap() != null) {
            this.map = new LinkedHashMap<>(another.getMap());
        }
        if (another.getDefaultValues() != null) {
            this.defaultValues = new LinkedHashMap<>(anotdher.getDefaultValues());
        }
        if (another.getTransform() != null) {
            this.transform = new LinkedHashMap<>(another.getTransform());
        }
        if (another.getIgnoreNodeList() != null) {
            this.ignoreNodeList = new ArrayList<>(another.getIgnoreNodeList());
        }
        if (another.getExcludeXPath() != null) {
            this.excludeXPath = new ArrayList<>(another.getExcludeXPath());
        }
        if (another.getDefaultValueMap() != null) {
            this.defaultValueMap = new LinkedHashMap<>(another.getDefaultValueMap());
        }
        if (another.getIgnoreZeroValueNodeList() != null) {
            this.ignoreZeroValueNodeList = new ArrayList<>(another.getIgnoreZeroValueNodeList());
        }
        if (another.getIgnoreEmptyNodeList() != null) {
            this.ignoreEmptyNodeList = new ArrayList<>(another.getIgnoreEmptyNodeList());
        }
        if (another.getUnsetFlags() != null) {
            this.unsetFlags = new LinkedHashMap<>(another.getUnsetFlags());
        }
        if (another.getSetIfNonEmpty() != null) {
            this.setIfNonEmpty = new LinkedHashMap<>(another.getSetIfNonEmpty());
        }
        if (another.getTransformXPath() != null) {
            this.transformXPath = new LinkedHashMap<>(another.getTransformXPath());
        }
        if (another.getIgnoreXPath() != null) {
            this.ignoreXPath = new ArrayList<>(another.getIgnoreXPath());
        }
        if (another.getIgnoreAttributeList() != null) {
            this.ignoreAttributeList = new ArrayList<>(another.getIgnoreAttributeList());
        }

    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public Map<String, String> getDefaultValues() {
        return this.defaultValues;
    }

    public Map<String, String> getTransform() {
        return this.transform;
    }

    public List<String> getIgnoreNodeList() {
        return this.ignoreNodeList;
    }

    public List<String> getExcludeXPath() {
        return this.excludeXPath;
    }

    public Map<String, String> getDefaultValueMap() {
        return this.defaultValueMap;
    }

    public List<String> getIgnoreZeroValueNodeList() {
        return this.ignoreZeroValueNodeList;
    }

    public List<String> getIgnoreEmptyNodeList() {
        return this.ignoreEmptyNodeList;
    }

    public Map<String, String> getUnsetFlags() {
        return this.unsetFlags;
    }

    public Map<String, String> getSetIfNonEmpty() {
        return this.setIfNonEmpty;
    }

    public Map<String, String> getTransformXPath() {
        return this.transformXPath;
    }

    public List<String> getIgnoreXPath() {
        return this.ignoreXPath;
    }

    public void setIgnoreXPath(final List<String> ignoreXPath) {
        this.ignoreXPath = ignoreXPath;
    }

    public List<String> getIgnoreAttributeList() {
        return ignoreAttributeList;
    }

    public void setIgnoreAttributeList(List<String> ignoreAttributeList) {
        this.ignoreAttributeList = ignoreAttributeList;
    }
    
    public Map<String, String> getSetIfEmpty() {
        return setIfEmpty;
    }

    public void setSetIfEmpty(Map<String, String> setIfEmpty) {
        this.setIfEmpty = setIfEmpty;
    }
}
