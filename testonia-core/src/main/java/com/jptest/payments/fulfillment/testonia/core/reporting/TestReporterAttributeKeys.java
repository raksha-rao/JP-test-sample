package com.jptest.payments.fulfillment.testonia.core.reporting;

/**
 * Enum representation of all the attributes used
 * in the {@link TestResultSummaryReporter} to report
 * test execution status in analysis module.
 */
public enum TestReporterAttributeKeys {

    JDBC_QUERY_ALERT_KEY("jdbc_query_alert"),
    TEST_EXECUTION_ID_KEY("TestId"),
    TEST_CASE_ID("testCaseId"),
    UNIFIED_NAME_KEY("UnifiedName"),
    CORRELATION_ID("corrId");

    private String keyName;

    private TestReporterAttributeKeys(final String name) {
        this.keyName = name;
    }

    public String getKeyName() {
        return this.keyName;
    }

}
