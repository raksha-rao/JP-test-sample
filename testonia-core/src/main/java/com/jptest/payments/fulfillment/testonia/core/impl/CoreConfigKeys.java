package com.jptest.payments.fulfillment.testonia.core.impl;

public enum CoreConfigKeys {

    PROTECTED_PACKAGE_LOCATION("protected_package_location"),
    TARGET_STAGE("targetStage"),
    IGNORE_TASK_TIMEOUTS("ignoreTaskTimeouts"),
    DEFAULT_TASK_TIMEOUT_IN_MS("defaultTaskTimeoutInMS"),
    AGGREGATE_ASSERT_MODE("aggregate.assert.mode"),
    XML_XPATH_EXTRACTOR_XSLT_LOCATION("xml.xpath.extractor.xslt.location"),
    TEST_RERUN_URL("test.rerun.url"),
    USE_CACHED_USER("use.cached.user"),
    CREATE_CACHED_USER("create.cached.user"),
    GOLDEN_FILE_ASSERTION_EXECUTION_MODE("golden.file.assertion.execution.mode"),
    DASHBOARD_LINK("kibana.dashboard.link"),
    DISABLE_KIBANA_REPORTING("disable.kibana.reporting"),
    EXECUTION_ID("executionId"),
    DASHBOARD_REPORTING_ATTRIBUTES("dashboard.reporting.attributes"),
    IGNORABLE_SYSTEM_PROPERTIES("ignorable.system.properties");

    private String name;

    private CoreConfigKeys(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
