package com.jptest.payments.fulfillment.testonia.core.reporting;

import org.testng.Reporter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;

/**
 * To print logs in emailable-report.html, follow below steps:
 * <li>Add below entry in logback.xml</li>
 * <pre>
    &lt;appender name="TESTNG" class="com.jptest.payments.fulfillment.testonia.reporting.TestNGAppender"&gt;
        &lt;layout class="ch.qos.logback.classic.PatternLayout"&gt;
            &lt;pattern&gt;%d{HH:mm:ss.SSS} [%thread] %level %logger %msg%n&lt;/pattern&gt;
        &lt;/layout&gt;
    &lt;/appender>
 *  </pre>
 *  <li>Add appender to your logger
 *  <pre>
 *  &lt;root level="WARN""&gt;
 *      &lt;appender-ref ref="TESTNG" /&gt;
 *  &lt;/root"&gt;
 *  </pre>
 *  </li>
 */
public class TestNGAppender extends AppenderBase<ILoggingEvent> {

    private Layout<ILoggingEvent> layout;

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        Reporter.log(layout.doLayout(eventObject));
    }

}
