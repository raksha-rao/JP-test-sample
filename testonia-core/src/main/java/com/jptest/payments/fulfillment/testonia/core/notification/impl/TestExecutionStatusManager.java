package com.jptest.payments.fulfillment.testonia.core.notification.impl;

import com.jptest.payments.fulfillment.testonia.core.notification.ComponentExecutionData;
import com.jptest.payments.fulfillment.testonia.core.notification.TestCaseReferenceData;
import com.jptest.payments.fulfillment.testonia.core.notification.TestExecutionStatusObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestExecutionStatusManager implements TestExecutionStatusObserver {

    private Map<String, List<ComponentExecutionData>> executionStatusData = new ConcurrentHashMap<String, List<ComponentExecutionData>>();

    // TODO this is temporary , will find a way to make it injectable
    private static TestExecutionStatusManager instance = new TestExecutionStatusManager();

    List<TestExecutionStatusObserver> subscribers = new ArrayList<TestExecutionStatusObserver>();

    private TestExecutionStatusManager() {

    }

    public static TestExecutionStatusManager getInstance() {
        return instance;
    }

    private TestExecutionStatusObserver statusWriter = null;

    @Override
    public void start(TestCaseReferenceData data) {
        statusWriter = new TestExecutionStatusLogger();
        executionStatusData.clear();
        statusWriter.start(data);

    }

    @Override
    public void finish(TestCaseReferenceData data) {
        statusWriter.finish(data);
    }

   /* @Override
    public void newStatus(TestCaseReferenceData referenceData, ComponentExecutionData executionData) {
        statusWriter.newStatus(referenceData, executionData);
    }
*/
}
