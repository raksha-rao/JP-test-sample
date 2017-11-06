package com.jptest.payments.fulfillment.testonia.model.money;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents FULFILLMENT_ACTIVITY_LOG table activity_log blob column
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FulfillmentActivityLogDTO {

    @XmlElement(name = "activity_log")
    private List<String> activityLogs;

    public List<String> getActivityLogs() {
        return activityLogs;
    }

    public void setActivityLogs(List<String> activityLogs) {
        this.activityLogs = activityLogs;
    }

}
