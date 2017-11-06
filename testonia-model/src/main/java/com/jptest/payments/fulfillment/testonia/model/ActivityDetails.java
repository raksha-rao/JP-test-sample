package com.jptest.payments.fulfillment.testonia.model;

import com.google.common.collect.Multimap;
import com.jptest.money.MustangActivityLog;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

public class ActivityDetails {
    private BigInteger activityId;
    private String activityName;
    private boolean isMustangActivity;
    private Multimap<String, TaskDetails> taskList;
    private List<MustangActivityLog> activityLogs;

    public ActivityDetails() {
    }

    public BigInteger getActivityId() {
        return this.activityId;
    }

    public void setActivityId(BigInteger activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public boolean isMustangActivity() {
        return this.isMustangActivity;
    }

    public void setMustangActivity(boolean isMustangActivity) {
        this.isMustangActivity = isMustangActivity;
    }

    public Multimap<String, TaskDetails> getTaskList() {
        return this.taskList;
    }

    public void setTaskList(Multimap<String, TaskDetails> taskList) {
        this.taskList = taskList;
    }

    public List<MustangActivityLog> getActivityLogs() {
        return this.activityLogs;
    }

    public void setActivityLogs(List<MustangActivityLog> activityLogs) {
        this.activityLogs = activityLogs;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{").append("ActivityId: ").append(this.activityId).append(", ActivityName: ").append(this.activityName).append(", isMustangActivity: ").append(this.isMustangActivity).append(", TaskList: ");
        Iterator var2 = this.getTaskList().keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            sb.append(this.taskList.get(key).toString());
        }

        return sb.toString();
    }
}
