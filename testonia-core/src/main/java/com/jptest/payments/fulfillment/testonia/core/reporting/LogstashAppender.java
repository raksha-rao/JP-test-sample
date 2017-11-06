package com.jptest.payments.fulfillment.testonia.core.reporting;

import javax.inject.Inject;

import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.jpinc.kernel.cal.api.sync.CalTransactionHelper;
import com.jptest.payments.fulfillment.testonia.core.guice.GuiceInjector;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;

/**
 * SLF4J Appender that logs messages to logstash
 */
public class LogstashAppender extends AppenderBase<ILoggingEvent> implements GuiceInjector {

    private static final String USER_NAME_SYSTEM_PROPERTY = "user.name";

    private Layout<ILoggingEvent> layout;

    private boolean initialized = false;

    @Inject
    private LogstashClient logstashClient;

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!isInitialized()) {
            return;
        }

        logstashClient.write(createJson(layout.doLayout(eventObject)).toString());
    }

    /**
     * Won't be initialized till 1st test method execution starts. 
     * @return
     */
    private boolean isInitialized() {
        if (!initialized) {
            ITestResult result = Reporter.getCurrentTestResult();
            if (result != null) {
                ITestContext testContext = result.getTestContext();
                inject(testContext, this);
                initialized = true;
            }
        }
        return initialized;
    }

    private JSONObject createJson(String logMessage) {
        JSONObject json = new JSONObject();
        json.put("message", logMessage);
        json.put("identifier",
                (String) Reporter.getCurrentTestResult()
                        .getAttribute(TestReporterAttributeKeys.TEST_EXECUTION_ID_KEY.getKeyName()));
        json.put("unifiedName",
                (String) Reporter.getCurrentTestResult()
                        .getAttribute(TestReporterAttributeKeys.UNIFIED_NAME_KEY.getKeyName()));
        json.put("startedBy", System.getProperty(USER_NAME_SYSTEM_PROPERTY));
        json.put("correlationId", CalTransactionHelper.getTopTransaction() != null
                ? CalTransactionHelper.getTopTransaction().getCorrelationId() : "null");
        json.put("documentType", "logs");
        return json;
    }

}
