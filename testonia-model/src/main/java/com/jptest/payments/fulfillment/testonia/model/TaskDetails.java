package com.jptest.payments.fulfillment.testonia.model;

import com.jptest.fulfillmentengine.TaskVO;
import com.jptest.money.TaskDetailVO;

public class TaskDetails {
    private TaskVO taskVO;
    private TaskDetailVO taskDetailsVO;
    private String taskName;
    private String participantTransactionId;

    public TaskDetails() {
    }

    public TaskVO getTaskVO() {
        return this.taskVO;
    }

    public void setTaskVO(TaskVO taskVO) {
        this.taskVO = taskVO;
    }

    public TaskDetailVO getTaskDetailsVO() {
        return this.taskDetailsVO;
    }

    public void setTaskDetailsVO(TaskDetailVO taskDetailsVO) {
        this.taskDetailsVO = taskDetailsVO;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getParticipantTransactionId() {
        return this.participantTransactionId;
    }

    public void setParticipantTransactionId(String participantTransactionId) {
        this.participantTransactionId = participantTransactionId;
    }

    public String toString() {
        return " { Name: " + this.taskName + ", Participant Transaction Id: " + this.participantTransactionId + "}";
    }
}
