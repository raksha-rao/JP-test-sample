package com.jptest.payments.fulfillment.testonia.business.component.validation;

import com.jptest.payments.fulfillment.testonia.business.component.XMLDiffTask;
import com.jptest.payments.fulfillment.testonia.business.service.GoldenFileProcessor;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.business.util.DiffHtmlGenerator;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.impl.TestExecutionIdGenerator;
import com.jptest.payments.fulfillment.testonia.core.util.xml.diff.XMLDiffHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.diff.XmlDiff;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.inject.Inject;

/**
 * Parent class for all golden file comparison tasks
 *
 * @param //<I>
 */
public abstract class GoldenFileComparisonAsserter extends BaseAsserter implements TimeoutAwareComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoldenFileComparisonAsserter.class);

    protected static enum GolenFileAssertionType {
        E2E("e2e"),
        ALL("all"),
        COMPONENT("component");
        private final String type;

        GolenFileAssertionType(String type) {
            this.type = type;
        }

        protected String getType() {
            return this.type;
        }
    }

    @Inject
    private FileRetrieverTask fileRetriever;
    
    @Inject
    private GoldenFileProcessor goldenFileProcessor;

    @Inject
    private XMLDiffTask xmlDiffTask;

    @Inject
    private XMLDiffHelper diffHelper;

    @Inject
    private DiffHtmlGenerator diffHtmlGenerator;

    @Inject
    protected Configuration configuration;

    private GoldenFileComparisonTaskInput input;

    protected Document goldenFileXMLContent;

    public GoldenFileComparisonAsserter(GoldenFileComparisonTaskInput input) {
        this.input = input;
    }

    @Override
    public void validate(Context context) {
        DiffResultData diffData = doExecute(context);
        if (!diffData.isNodiff()) {
        	context.getAggregateAssert().fail("Found difference in actual and expected XMLs for " 
        			+ getValidationType(), new DBDiffAssertionException(diffData.getDiffMessage()));
        }
    }
    
    private class DBDiffAssertionException extends TestExecutionBusinessException {

        @Override
        public TestoniaExceptionReasonCode getReasonCode() {
            return getDiffReasonCode();
        }
        
        public DBDiffAssertionException(String message) {
            super(message);
        }

    }
    
    protected abstract TestoniaExceptionReasonCode getDiffReasonCode();

    private DiffResultData doExecute(Context context) {
        LOGGER.info("{} golden file comparison started ===> ", getValidationType());
        long startTime = System.nanoTime();

        String goldenFileLocation = input.getGoldenFileLocation();
        boolean goldenfileGenerationMode = configuration.getBoolean(BizConfigKeys.GOLDEN_FILE_GENERATION_MODE.getName(),
                Boolean.FALSE);

        // If the test is run in golden file generation mode, then just generate the actual data and save it and return
        if (goldenfileGenerationMode) {
            LOGGER.info("Golden file generation mode ON; so only generating the file and no comparison will done");
            return processGoldenFileGeneration(context, input);
        }

        // Reaching here means that we are in golden file comparison mode.

        // 1. get golden XML file
        goldenFileXMLContent = fileRetriever.execute(goldenFileLocation);
        // 2. Lets process golden file (replace placeholder etc)
        goldenFileProcessor.process(context, goldenFileXMLContent);
        // 3. get actual response XML
        Document actualXML = getActualResponseXml(context);

        // 4. When actual XML could not be retrieved, ignore the assertion for best-effort task
        if (actualXML == null && isBestEffort()) {
            LOGGER.warn("Ignoring {} assertion since its best effort", getValidationType());
            return new DiffResultData(Boolean.TRUE, null);
        }

        // 5. get output location
        String outputFileLocation = getFileOutputLocation(goldenFileLocation);

        // 6. perform diff
        XmlDiff diff = xmlDiffTask.performDiff(actualXML, goldenFileXMLContent, outputFileLocation);

        // 7. Log time
        long timeTaken = System.nanoTime() - startTime;
        LOGGER.info("{} golden file comparison completed successfully in {} ms.", getValidationType(),
                timeTaken / 1000000);

        // 8. validate
        boolean noDiff = diff.similar();
        String diffMessages = null;
        if (!noDiff) {
            // generates diff.html
            diffHtmlGenerator.generateHtml(diff.getActualFileLocation(), diff.getGoldenFileLocation(),
                    getDiffHtmlLocation(outputFileLocation));
            // retrieves diff messages
            diffMessages = diffHelper.getDiffs(diff);
            LOGGER.error("Found difference in actual and expected XMLs for {}", getValidationType());
        }
        return new DiffResultData(noDiff, diffMessages);
    }

    private DiffResultData processGoldenFileGeneration(Context context, GoldenFileComparisonTaskInput input) {
        Document actualXML = getActualResponseXml(context);
        if (actualXML == null) {
            if (isBestEffort()) {
                LOGGER.warn("Couldn't generate golden file for {} assertion but ignoring since its best effort",
                        getValidationType());
                return new DiffResultData(true, null);
            } else {
                return new DiffResultData(false,
                        "Actual file cannot be null for a mandatory validation task: " + getValidationType());
            }
        }

        diffHelper.storeXmlOnFileSystem(actualXML, input.getGoldenFileLocationSource());
        return new DiffResultData(true, null);
    }

    /**
     * Gives output location as &lt;test-execution-id&gt;-&lt;testcase_id&gt;-&lt;golden_file_name&gt;-&lt;corr_id&gt;
     * @param goldenFileLocation 
     *
     * @param //testExecutionId
     * @param //testcaseId
     * @param //correlationId
     * @param goldenFileLocation
     * @return
     */
    private String getFileOutputLocation(String goldenFileLocation) {

        // goldenFileLocation = ./golden/fmf/testSaleCCFMFFilterDeny/P20Validation.xml
        // outputFilePath = ./output/<unique_execution_id>/fmf/testSaleCCFMFFilterDeny/P20Validation.xml
        String outputFilePath = goldenFileLocation.replace(
                configuration.getString(ContextKeys.GOLDEN_FILE_INPUT_LOCATION.getName()),
                getUniqueOutputBaseDirectory(
                        configuration.getString(ContextKeys.GOLDEN_FILE_OUTPUT_LOCATION.getName())))
                .replace(configuration.getString(ContextKeys.TESTCASE_INPUT_DATA.getName()) + "/",
                        getUniqueOutputBaseDirectory(
                                configuration.getString(ContextKeys.GOLDEN_FILE_OUTPUT_LOCATION.getName())));
        // This is to make sure the separators are converted to system (OS) native separator
        outputFilePath = FilenameUtils.separatorsToSystem(outputFilePath);
        return outputFilePath;
    }

    private static String getUniqueOutputBaseDirectory(String outputBaseDirectory) {
        return FilenameUtils.concat(outputBaseDirectory,
                TestExecutionIdGenerator.getTestId() + "/");
    }

    /**
     * For input ./output/<unique_execution_id>/fmf/testSaleCCFMFFilterDeny/P20Validation.xml, 
     * it returns ./output/<unique_execution_id>/fmf/testSaleCCFMFFilterDeny/diff.html
     * 
     * @param outputFileLocation
     * @return
     */
    private static String getDiffHtmlLocation(String outputFileLocation) {
        String diffHtmlFilePath = FilenameUtils.getFullPathNoEndSeparator(outputFileLocation) + "/diff.html";
        return FilenameUtils.separatorsToSystem(diffHtmlFilePath);
    }

    protected abstract Document getActualResponseXml(Context context);

    public abstract String getValidationType();

    @Override
    public long getTimeoutInMs() {
        return configuration.getLong(CoreConfigKeys.DEFAULT_TASK_TIMEOUT_IN_MS.getName(), 3 * 60 * 1000);
    }

    /**
     * Override this method in subclass to mark it as best-effort asserter
     *
     * @return
     */
    protected boolean isBestEffort() {
        return false;
    }

    protected boolean isUserCreationMode() {
        return configuration.getBoolean(CoreConfigKeys.CREATE_CACHED_USER.getName());
    }

    private static class DiffResultData {
        private boolean nodiff;
        private String diffMessage;

        public boolean isNodiff() {
            return nodiff;
        }

        public String getDiffMessage() {
            return diffMessage;
        }

        private DiffResultData(boolean nodiff, String diffMessage) {
            super();
            this.nodiff = nodiff;
            this.diffMessage = diffMessage;
        }

    }

}
