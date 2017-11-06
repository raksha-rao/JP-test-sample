package com.jptest.payments.fulfillment.testonia.core.reporting;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionSystemException;
import com.jptest.payments.fulfillment.testonia.core.guice.GuiceInjector;
import com.jptest.payments.fulfillment.testonia.core.impl.AggregateAssertionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.impl.TestExecutionIdGenerator;
import com.jptest.payments.fulfillment.testonia.core.util.CoreServiceUtil;

/**
 * Guice injected TestNG Test listener for recording test results in elasticsearch
 * 
 * The only implemented methods are around test completion for success, fail and skip.
 * 
 * Test runs are identified using id parameter.
 * 
 * @JP Inc.
 * 
 */
@Singleton
public class TestResultSummaryReporter implements ITestListener, ISuiteListener, GuiceInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestResultSummaryReporter.class);

    private static final String TESTCASE_DOCUMENT_TYPE = "testcase";

    private static final String USER_NAME_SYSTEM_PROPERTY = "user.name";

    private static final String VM_PARAMS = "vmParams";

    private static final int MAX_KIBANA_MESSAGE_LENGTH = 32765;

    private Map<TestoniaExceptionReasonCode, ReasonCodeStats> reasonCodeMap;

    private String dashboardLink;

    private boolean disableReporting;

    private static final String EXECUTION_URL_VM_ARGS_PLACEHOLDER = "{vmArgs}";

    private static final String EXECUTION_URL_TARGET_STAGE_PLACEHOLDER = "{targetStage}";

    private static final String EXECUTION_URL_TESTS_PLACEHOLDER = "{tests}";

    private static final String ORIGINAL_CORR_ID_PARAM = "originalCorrId";

    private static final String ENCODED_HASH = "%23";

    private final String EXECUTION_ID_PLACEHOLDER = "executionId";

    @Inject
    private CoreServiceUtil coreServiceUtil;

    @Inject
    private LogstashClient logstashClient;

    @Inject
    private KafkaPublisher kafkaPublisher;

    @Inject
    private Configuration configuration;

    @Inject
    private SystemProperties systemProperties;

    // Suite start
    @Override
    public void onStart(ISuite suite) {
        reasonCodeMap = new ConcurrentHashMap<TestoniaExceptionReasonCode, ReasonCodeStats>();
    }

    // test case start
    @Override
    public void onStart(ITestContext context) {
        inject(context, this);
        dashboardLink = configuration.getString(CoreConfigKeys.DASHBOARD_LINK.getName());
        disableReporting = configuration.getBoolean(CoreConfigKeys.DISABLE_KIBANA_REPORTING.getName(), false);
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (disableReporting) {
            LOGGER.debug("Kibana reporting is disabled.");
            return;
        }

        LOGGER.info("*******************************************");
        String testExecutionId = TestExecutionIdGenerator.getTestId();
        LOGGER.info("Test execution id: {}", testExecutionId);
        LOGGER.info("Dashboard link for this execution: {}",
                URLDecoder.decode(
                        dashboardLink.replaceAll(CoreConfigKeys.EXECUTION_ID.getName(), testExecutionId)),
                "UTF-8");
        LOGGER.info("*******************************************");
        result.setAttribute(TestReporterAttributeKeys.TEST_EXECUTION_ID_KEY.getKeyName(),
                testExecutionId);
        String unifiedName = getUnifiedName(result);
        result.setAttribute(TestReporterAttributeKeys.UNIFIED_NAME_KEY.getKeyName(), unifiedName);
        MDC.put(TestReporterAttributeKeys.TEST_CASE_ID.getKeyName(), CoreLoggingUtil.getTestCaseName(result));
        MDC.put(TestReporterAttributeKeys.TEST_EXECUTION_ID_KEY.getKeyName(), testExecutionId);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logResult(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logResult(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logResult(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logResult(result);
    }

    // test case finish
    @Override
    public void onFinish(ITestContext context) {
        long totTime = context.getEndDate().getTime() - context.getStartDate().getTime();
        LOGGER.info("onFinish {} done in {} ms", context, totTime);
    }

    // test suite finish
    @Override
    public void onFinish(ISuite suite) {
        if (disableReporting) {
            LOGGER.info("Kibana reporting is disabled.");
            return;
        }
        LOGGER.info("********* FAILURE ANALYSIS ****************");
        for (Entry<TestoniaExceptionReasonCode, ReasonCodeStats> entry : reasonCodeMap.entrySet()) {
            LOGGER.info("Reason code: {} Count: {} test(s): {}", entry.getKey(), entry.getValue().getCount(),
                    entry.getValue().getTestcaseNames());
        }
        LOGGER.info("*******************************************");
    }

    /**
     * Builds MethodResult pojo, serializes and sends it to logstash using LogstashClient
     * @param result test method result
     */
    private void logResult(ITestResult result) {
        if (disableReporting) {
            return;
        }

        long totTime = result.getEndMillis() - result.getStartMillis();
        MethodResult mResult = new MethodResult();
        mResult.setIdentifier(
                (String) result.getAttribute(TestReporterAttributeKeys.TEST_EXECUTION_ID_KEY.getKeyName()));
        mResult.setTestHost(coreServiceUtil.getTargetStage());
        // logging user name triggering the test run
        mResult.setStartedBy(System.getProperty(USER_NAME_SYSTEM_PROPERTY));
        mResult.setClassName(result.getInstanceName());
        mResult.setMethodName(result.getName());
        mResult.setExecTime(totTime);
        mResult.setParams(CoreLoggingUtil.formatParameters(result));
        mResult.setStatus(statusToString(result.getStatus()));
        mResult.setCustomAttributes(collectCustomAttributes(result));
        mResult.setUnifiedName(getUnifiedName(result));
        mResult.setDocumentType(TESTCASE_DOCUMENT_TYPE);
        mResult.setCorrelationId((String) result.getAttribute(ContextKeys.CORRELATION_ID.getName()));
        mResult.setSuiteName(result.getTestContext().getSuite().getXmlSuite().getFileName());

        if (result.getThrowable() != null) {
            mResult.setCause(getCause(result));
            updateReasonCodeStats(mResult, result);
        }

        if (result.getMethod() != null && result.getMethod().getGroups() != null) {
            mResult.setGroups(result.getMethod().getGroups());
        }

        mResult.setRerunUrl(getRerunUrl(mResult));

        LOGGER.info("\n*******************************************");
        LOGGER.info("\nTest Execution Summary:\n{}", mResult.toString());
        LOGGER.info("\n*******************************************");
        publishToKafka(mResult);
        publishToLogstash(mResult);

    }

    private String getCause(ITestResult result) {
        String cause = result.getThrowable().toString();
        if (cause.length() > MAX_KIBANA_MESSAGE_LENGTH) {
            cause = cause.substring(0, MAX_KIBANA_MESSAGE_LENGTH - 4) + "...";
        }
        return cause;
    }

    /**
     * This method constructs and returns the URL to re-trigger the given test case
     * This assumes that we have a CI job with following params
     * 1. tests: This is a fully qualified class name (and optionally #<method_name>)
     * 2. vmArgs: The VM arguments for this test case execution
     * 3. targetStage: The stage against which this test case is executed.
     * 
     * This features also assumes that the CI job is correctly configured with
     * the appropriate GIT repo etc
     * This method gets the CI job base URL from the properties file
     * the format of this URL is as follows
     * https://host:port/<job_name>/buildWithParameters?token=<token_configured_in_the_job_config>
     * &tests={tests}&targetStage={targetStage}&vmArgs={vmArgs}
     * Note that the query param names can be anything as long as the placeholders are correctly placed.
     * @param mResult
     * @return
     */
    private String getRerunUrl(MethodResult mResult) {
        String vmParams = mResult.getCustomAttributes().get(VM_PARAMS);
        if (StringUtils.isEmpty(vmParams)) {
            vmParams = "";
        } else {
            vmParams = vmParams.replace(",", " ");
        }
        StringBuilder sb = new StringBuilder(vmParams);
        sb.append(" -D").append(ORIGINAL_CORR_ID_PARAM)
                .append("=").append(mResult.getCorrelationId())
                .append(" -D")
                .append(EXECUTION_ID_PLACEHOLDER).append("=")
                .append(mResult.getIdentifier())
                .append(" -D").append(USER_NAME_SYSTEM_PROPERTY).append("=").append(mResult.getStartedBy());
        String baseURL = URLDecoder.decode(configuration.getString(CoreConfigKeys.TEST_RERUN_URL.getName()));
        baseURL = baseURL
                .replace(EXECUTION_URL_TESTS_PLACEHOLDER,
                        mResult.getClassName() + ENCODED_HASH + mResult.getMethodName())
                .replace(EXECUTION_URL_TARGET_STAGE_PLACEHOLDER, mResult.getTestHost())
                .replace(EXECUTION_URL_VM_ARGS_PLACEHOLDER, sb.toString());
        LOGGER.info("URL : {}", baseURL);
        return baseURL;
    }


    private void publishToKafka(Object data) {
        try {
                kafkaPublisher.send(data);

        } catch (Exception e) {
            LOGGER.error("unable to publish to Kafka", e);
        }

    }

    private void publishToLogstash(Object data) {
        try {
            String message = getObjectMapper().writeValueAsString(data);
            LOGGER.info(message);

            logstashClient.write(message);
        } catch (JsonProcessingException e) {
            LOGGER.error("unable to send data to logstash", e);
        }
    }

    /**
     * Checks if the root exception is of {@link BaseTestoniaRuntimeException} 
     * and updates the reason code stats if we find such an exception
     * @param mResult 
     * @param result
     */
    private synchronized void updateReasonCodeStats(MethodResult mResult, ITestResult result) {
        TestExecutionException rootException = getRootCauseException(result.getThrowable());

        if (rootException instanceof AggregateAssertionException) {
            // handle aggregate exception
            AggregateAssertionException aggregateExpt = (AggregateAssertionException) rootException;
            for (TestExecutionException expt : aggregateExpt.getAssertions()) {
                updateReasonCodeStats(expt.getReasonCode(), result);
            }
            mResult.setFailureCause(aggregateExpt.getFailureCause());
        } else {
            // handle regular (non-aggregate) exception
            TestoniaExceptionReasonCode reasonCode = rootException != null ? rootException.getReasonCode()
                    : TestoniaExceptionReasonCode.FAILURE_GENERIC_ERROR;
            updateReasonCodeStats(reasonCode, result);
            mResult.setFailureCause(Arrays.asList(reasonCode.name()));
        }

    }

    private synchronized void updateReasonCodeStats(TestoniaExceptionReasonCode reasonCode, ITestResult result) {
        ReasonCodeStats stats = this.getReasonCodeStats(reasonCode);
        if (stats == null) {
            stats = new ReasonCodeStats();
            this.addReasonCodeStats(reasonCode, stats);
        }
        stats.incrementCount();
        stats.addTestCase(result.getInstanceName());
    }

    /**
     * @param throwable
     * @return
     */
    private TestExecutionException getRootCauseException(Throwable throwable) {
        List<Throwable> list = ExceptionUtils.getThrowableList(throwable);
        if (CollectionUtils.isEmpty(list)) {
            return throwable instanceof TestExecutionException ? (TestExecutionException) throwable : null;
        } else {
            for (Throwable th : Lists.reverse(list)) {
                if (th instanceof TestExecutionSystemException || th instanceof TestExecutionBusinessException)
                    return (TestExecutionException) th;
            }
        }
        return null;
    }

    private Map<String, String> collectCustomAttributes(ITestResult result) {
        Map<String, String> customAttributes = new HashMap<>();
        // display all vm arguments on kibana
        String commaSeparatedProperties = systemProperties.getCommaSeparatedProperties();
        customAttributes.put(VM_PARAMS, commaSeparatedProperties);

        // add all reporting attributes from context to custom attributes
        Context context = (Context) result.getAttribute(ContextKeys.CONTEXT_KEY.getName());
        if (context != null && MapUtils.isNotEmpty(context.getReportingAttributes())) {
            for (Map.Entry<String, Object> reportingAttribute : context.getReportingAttributes().entrySet()) {
                customAttributes.put(systemProperties.transformKey(reportingAttribute.getKey()),
                        reportingAttribute.getValue().toString());
            }
        }
        customAttributes.putAll(systemProperties.getReportingVMParameters());
        return customAttributes;
    }

    private ObjectMapper getObjectMapper() {
        // object mapper for serializing payload to logstash
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        jsonObjectMapper.setSerializationInclusion(Include.NON_EMPTY);
        jsonObjectMapper.setSerializationInclusion(Include.NON_NULL);
        jsonObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return jsonObjectMapper;
    }

    private String getUnifiedName(ITestResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(result.getInstanceName());
        sb.append(".");
        sb.append(result.getName());
        sb.append("(");
        sb.append(CoreLoggingUtil.formatParameters(result));
        sb.append(")");
        return sb.toString();
    }

    /**
     * Crappy test status mapper, unfortunatly testng does not expose it's internal enums
     * This is practically a copy from  org.testng.internal.TestResult.toString(int) 
     * minus the exception 
     * @param status
     * @return
     */
    private String statusToString(int status) {
        switch (status) {
        case ITestResult.SUCCESS:
            return "SUCCESS";
        case ITestResult.FAILURE:
            return "FAILURE";
        case ITestResult.SKIP:
            return "SKIP";
        case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
            return "SUCCESS WITHIN PERCENTAGE";
        case ITestResult.STARTED:
            return "STARTED";
        default:
            return "UNKNOWN";
        }
    }

    public ReasonCodeStats getReasonCodeStats(TestoniaExceptionReasonCode reasonCode) {
        return reasonCodeMap.get(reasonCode);
    }

    public void addReasonCodeStats(TestoniaExceptionReasonCode reasonCode, ReasonCodeStats stats) {

        reasonCodeMap.put(reasonCode, stats);
    }

    private static class ReasonCodeStats {
        private AtomicInteger count;
        private List<String> testcaseNames;

        private ReasonCodeStats() {
            super();
            count = new AtomicInteger();
            testcaseNames = new ArrayList<String>();
        }

        private void incrementCount() {
            count.incrementAndGet();
        }

        private void addTestCase(String testcase) {
            testcaseNames.add(testcase);
        }

        private AtomicInteger getCount() {
            return count;
        }

        private List<String> getTestcaseNames() {
            return testcaseNames;
        }

    }

}
