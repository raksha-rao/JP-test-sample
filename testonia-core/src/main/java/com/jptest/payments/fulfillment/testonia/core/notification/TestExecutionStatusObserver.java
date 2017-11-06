package com.jptest.payments.fulfillment.testonia.core.notification;

//import com.jptest.payments.fulfillment.testonia.core.Unit;

/**
 * Represents a component that is interested to receive real-time status of a
 * test case execution. This gets 3 events from the framework <br>
 * 1. start of the test case execution <br>
 * 2. End of the test case execution<br>
 * 3. Change of Status for each individual {@link //Unit} within a test case.
 */
public interface TestExecutionStatusObserver {

    void start(TestCaseReferenceData data);

    void finish(TestCaseReferenceData data);

   // void newStatus(TestCaseReferenceData refData, ComponentExecutionData execData);

}
