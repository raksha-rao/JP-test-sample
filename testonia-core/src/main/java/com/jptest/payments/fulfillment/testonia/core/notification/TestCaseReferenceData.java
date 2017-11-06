package com.jptest.payments.fulfillment.testonia.core.notification;

public class TestCaseReferenceData {

    private String testSuitId;

    private String testCaseName;

    private String executionPlan;

    private long timeInMs;

    public TestCaseReferenceData(String testSuitId, String testCaseName, String executionPlan, long time) {
        super();
        this.testSuitId = testSuitId;
        this.testCaseName = testCaseName;
        this.executionPlan = executionPlan;
        timeInMs = time;
    }

    public String getTestSuitId() {
        return testSuitId;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public String getExecutionPlan() {
        return executionPlan;
    }

    public long getStartTime() {
        return timeInMs;
    }

}
