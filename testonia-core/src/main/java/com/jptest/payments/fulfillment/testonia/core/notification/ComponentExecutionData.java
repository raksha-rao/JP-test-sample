package com.jptest.payments.fulfillment.testonia.core.notification;

import com.jptest.payments.fulfillment.testonia.core.ComponentType;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionResult.ComponentExecutionStatus;

public class ComponentExecutionData {
    private String name;
    private ComponentType type;
    private ComponentExecutionStatus status;
    private Exception executionError;
    private String stageName;

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Exception getExecutionError() {
        return executionError;
    }

    public String getName() {
        return name;
    }

    public ComponentType getType() {
        return type;
    }

    public ComponentExecutionStatus getStatus() {
        return status;
    }

    public ComponentExecutionData(String name, ComponentType type, ComponentExecutionStatus status) {
        super();
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public ComponentExecutionData(String name, ComponentType type, ComponentExecutionStatus status,
            Exception exception) {
        super();
        this.name = name;
        this.type = type;
        this.status = status;
        executionError = exception;
    }

}
