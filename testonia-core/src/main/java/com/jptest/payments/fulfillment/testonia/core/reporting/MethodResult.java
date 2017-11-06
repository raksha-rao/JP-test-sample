package com.jptest.payments.fulfillment.testonia.core.reporting;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * POJO that holds test result for individual test-method.
 */
public class MethodResult extends Result {

    private String methodName;
    private String status;
    private String params;
    private String cause;
    private String[] groups;
    private String unifiedName;
    private String documentType;
    private String startedBy;
    private String correlationId;
    private List<String> failureCause;
    private String suiteName;
    private String rerunUrl;

    public String getRerunUrl() {
        return rerunUrl;
    }

    public void setRerunUrl(String rerunUrl) {
        this.rerunUrl = rerunUrl;
    }

    public List<String> getFailureCause() {
        return failureCause;
    }

    public void setFailureCause(List<String> failureCause) {
        this.failureCause = failureCause;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    public String getUnifiedName() {
        return unifiedName;
    }

    public void setUnifiedName(String unifiedName) {
        this.unifiedName = unifiedName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getSuiteName() {
        return suiteName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Test Case: ").append(this.getClassName()).append("_")
                .append(StringUtils.isEmpty(this.getMethodName()) ? "Not available" : this.getMethodName())
                .append("\n")
                .append("Status: ").append(StringUtils.isEmpty(this.getStatus()) ? "N/A" : this.getStatus())
                .append("\n")
                .append("Root Cause: ").append(StringUtils.isEmpty(this.getCause()) ? "N/A" : this.getCause())
                .append("\n")
                .append("Execution time: ").append(this.getExecTime())
                .append("\n").append("Corr id: ")
                .append(StringUtils.isEmpty(this.getCorrelationId()) ? "N/A" : this.getCorrelationId());
        return builder.toString();
    }

}
