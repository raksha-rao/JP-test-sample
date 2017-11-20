package com.jptest.payments.fulfillment.testonia.business.util;

public enum BizConfigKeys {

    TEST_DATA_FILE_LOCATION("test.data.file.location"),

    DIFF_HTML_COMPARE_SCRIPT_LOCATION("diff.html.compare.script.location"),

    TRANSACTION_EXISTS_CHECK_WAIT_TIME_IN_MS("transaction.exists.check.wait.time.in.ms"),
    TRANSACTION_EXISTS_CHECK_RETRY_INTERVAL_IN_MS("transaction.exists.check.retry.interval.in.ms"),

    HOLDING_TRANSACTION_EXISTS_CHECK_WAIT_TIME_IN_MS("holding.transaction.exists.check.wait.time.in.ms"),
    PAYMENT_RECIPIENT_VIRTUALIZE_SCHEMA_FILE_LOCATION("payment.recipient.virtualize.schema.file.location"),
    FINANCIAL_JOURNAL_VIRTUALIZE_SCHEMA_FILE_LOCATION("financial.journal.virtualize.schema.file.location"),
    PAYMENT_SIDE_VIRTUALIZE_SCHEMA_FILE_LOCATION("payment.side.virtualize.schema.file.location"),
    IPN_VIRTUALIZE_SCHEMA_FILE_LOCATION("ipn.virtualize.schema.file.location"),
    PAYLOAD_VIRTUALIZE_SCHEMA_FILE_LOCATION("payload.virtualize.schema.file.location"),
    PAYMENT_SENDER_VIRTUALIZE_SCHEMA_FILE_LOCATION("payment.sender.virtualize.schema.file.location"),
    FULFILLMENT_ACTIVITY_VIRTUALIZE_SCHEMA_FILE_LOCATION("fulfillment.activity.virtualize.schema.file.location"),
    PAYMENT_READSERV_VIRTUALIZE_SCHEMA_FILE_LOCATION("payment.readserv.virtualize.schema.file.location"),
    ACTIVITY_LOG_SOR_VIRTUALIZE_SCHEMA_FILE_LOCATION(
            "fulfillment.activity.log.sor.virtualize.schema.file.location"),
    ACTIVITY_LOG_LEGACY_EVENTS_VIRTUALIZE_SCHEMA_FILE_LOCATION(
            "fulfillment.activity.log.legacy.events.virtualize.schema.file.location"),
    ACTIVITY_LOG_IPN_VIRTUALIZE_SCHEMA_FILE_LOCATION(
            "fulfillment.activity.log.ipn.virtualize.schema.file.location"),
    ACTIVITY_LOG_SETTLEMENT_VIRTUALIZE_SCHEMA_FILE_LOCATION(
            "fulfillment.activity.log.settlement.virtualize.schema.file.location"),
    PYMT_VALIDATION_TOOL_URL("pymtValidationToolUrl"),
    HOLDING_TRANSACTION_EXISTS_CHECK_RETRY_INTERVAL_IN_MS("holding.transaction.exists.check.retry.interval.in.ms"),
    HOLDING_BALANCE_EXISTS_CHECK_WAIT_TIME_IN_MS("holding.balance.exists.check.wait.time.in.ms"),
    HOLDING_BALANCE_EXISTS_CHECK_RETRY_INTERVAL_IN_MS("holding.balance.exists.check.retry.interval.in.ms"),
    XSL_SORT_TEMPLATE_FILE_LOCATION("xsl.sort.template.file.location"),
    ACTIVITY_LOG_XSL_SOR_TEMPLATE_FILE_LOCATION("activity.log.xsl.sor.template.file.location"),
    DEFAULT_IGNORABLE_FLAGS_LOCATION("default.ignorable.flags.location"),
    PRS_RETRY_MODE("prs.retry.mode"),
    PAYMENT_MESSAGE_VIRTUALIZE_SCHEMA_FILE_LOCATION("payment.message.virtualize.schema.file.location"),
    POST_PAYMENT_RESPONSE_VIRTUALIZE_SCHEMA_FILE_LOCATION("post.payment.response.virtualize.schema.file.location"),
    PRS_RESPONSE_VIRTUALIZE_SCHEMA_FILE_LOCATION("prs.response.virtualize.schema.file.location"),
    PYMTVALIDATION_ENABLE("pymtvalidation.enable"),
    GOLDEN_FILE_GENERATION_MODE("golden.file.generation.mode"),
    GOLDEN_FILE_PLACEHOLDER_GENERATION_ONLY_MODE("golden.file.placeholder.generation.only.mode");

    private String name;

    private BizConfigKeys(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
