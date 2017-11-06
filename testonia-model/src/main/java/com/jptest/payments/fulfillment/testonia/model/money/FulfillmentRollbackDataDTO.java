package com.jptest.payments.fulfillment.testonia.model.money;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jptest.fulfillmentengine.TaskVO;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBVOToXMLAdapter;

/**
 * Represents FULFILLMENT_ROLLBACK_DATA table record
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FulfillmentRollbackDataDTO {

    @XmlElement(name = "activity_id")
    private BigInteger activityId;

    @XmlElement(name = "participant_transaction_id")
    private String participantTransactionId;

    @XmlElement(name = "rollback_task")
    @XmlJavaTypeAdapter(JAXBVOToXMLAdapter.class)
    private TaskVO rollbackTask;

    @XmlElement(name = "status")
    private int status;

    @XmlElement(name = "time_created")
    private Long timeCreated;

    @XmlElement(name = "time_updated")
    private Long timeUpdated;

    public BigInteger getActivityId() {
        return activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    public String getParticipantTransactionId() {
        return participantTransactionId;
    }

    public void setParticipantTransactionId(String participantTransactionId) {
        this.participantTransactionId = participantTransactionId;
    }

    public TaskVO getRollbackTask() {
        return rollbackTask;
    }

    public void setRollbackTask(TaskVO rollbackTask) {
        this.rollbackTask = rollbackTask;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }
}
