package com.jptest.sampletest;

import com.jptest.payments.fulfillment.testonia.business.component.XMLDiffTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.FileRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.util.DiffHtmlGenerator;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.diff.XmlDiff;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.inject.Inject;

@Component
public class SampleGoldenFileComparisonAsserter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleGoldenFileComparisonAsserter.class);

    private GoldenFileComparisonTaskInput input;

    public boolean isGoldenfileGenerationMode() {
        return goldenfileGenerationMode;
    }

    public void setGoldenfileGenerationMode(boolean goldenfileGenerationMode) {
        this.goldenfileGenerationMode = goldenfileGenerationMode;
    }

    public boolean goldenfileGenerationMode;

    @Inject
    public Configuration configuration;

    @Inject
    public SampleDiffHelper diffHelper;

    @Inject
    public FileRetrieverTask fileRetriever;

    @Inject
    public XMLDiffTask xmlDiffTask;

    protected Document goldenFileXMLContent;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public SampleDiffHelper getDiffHelper() {
        return diffHelper;
    }

    public void setDiffHelper(SampleDiffHelper diffHelper) {
        this.diffHelper = diffHelper;
    }

    public FileRetrieverTask getFileRetriever() {
        return fileRetriever;
    }

    public void setFileRetriever(FileRetrieverTask fileRetriever) {
        this.fileRetriever = fileRetriever;
    }

    public XMLDiffTask getXmlDiffTask() {
        return xmlDiffTask;
    }

    public void setXmlDiffTask(XMLDiffTask xmlDiffTask) {
        this.xmlDiffTask = xmlDiffTask;
    }

    public Document getGoldenFileXMLContent() {
        return goldenFileXMLContent;
    }

    public void setGoldenFileXMLContent(Document goldenFileXMLContent) {
        this.goldenFileXMLContent = goldenFileXMLContent;
    }

    public DiffHtmlGenerator getDiffHtmlGenerator() {
        return diffHtmlGenerator;
    }

    public void setDiffHtmlGenerator(DiffHtmlGenerator diffHtmlGenerator) {
        this.diffHtmlGenerator = diffHtmlGenerator;
    }

    @Inject
    private DiffHtmlGenerator diffHtmlGenerator;

    public SampleGoldenFileComparisonAsserter(GoldenFileComparisonTaskInput input) {
        this.input = input;
    }

    public void validate() {
        DiffResultData diffData = doExecute(null);
        if (!diffData.isNodiff()) {

            Assert.fail("Found difference in actual and expected XMLs for "+new DBDiffAssertionException(diffData.getDiffMessage()));
        }
    }

    private DiffResultData doExecute(Context context) {
        long startTime = System.nanoTime();

        String goldenFileLocation = input.getGoldenFileLocation();


        // If the test is run in golden file generation mode, then just generate the actual data and save it and return
        if (goldenfileGenerationMode) {
            LOGGER.info("Golden file generation mode ON; so only generating the file and no comparison will done");
            return processGoldenFileGeneration(context, input);
        }


        goldenFileXMLContent = fileRetriever.execute(goldenFileLocation);

        Document actualXML = getActualResponseXml(context);

        if (actualXML == null ) {
            LOGGER.warn("Ignoring {} assertion since its best effort");
            return new DiffResultData(Boolean.TRUE, null);
        }


        String outputFileLocation = "";

        // 6. perform diff
        XmlDiff diff = xmlDiffTask.performDiff(actualXML, goldenFileXMLContent, outputFileLocation);

        // 7. Log time
        long timeTaken = System.nanoTime() - startTime;
        LOGGER.info("{} golden file comparison completed successfully in {} ms.",
                timeTaken / 1000000);

        // 8. validate
        boolean noDiff = diff == null;
        String diffMessages = null;
        if (!noDiff) {
            // generates diff.html
            diffHtmlGenerator.generateHtml( "sample-actual.xml",  "sample-golden.xml",
                    getDiffHtmlLocation(outputFileLocation));

        }
        return new DiffResultData(noDiff, diffMessages);
    }

    private static String getDiffHtmlLocation(String outputFileLocation) {
        String diffHtmlFilePath = FilenameUtils.getFullPathNoEndSeparator(outputFileLocation) + "/diff.html";
        return FilenameUtils.separatorsToSystem(diffHtmlFilePath);
    }

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
                11399 + "/");
    }


    private DiffResultData processGoldenFileGeneration(Context context, GoldenFileComparisonTaskInput input) {
        Document goldenFileDoc = getGoldenFileResponseXml(context);
        if (goldenFileDoc == null) {
            return new DiffResultData(false,
                    "Actual file cannot be null for a mandatory validation task: ");

        }

        diffHelper.storeXmlOnFileSystem(goldenFileDoc, input.getGoldenFileLocationSource());
        return new DiffResultData(true, null);
    }

    protected Document getActualResponseXml(Context context) {
        Document actualXML;
        actualXML = fileRetriever.execute("sample-actual.xml");
       // replaceIDsValue(actualXML);
        return actualXML;
    }

    protected Document getGoldenFileResponseXml(Context context) {
        Document actualXML;
        actualXML = fileRetriever.execute("sample-golden.xml");
        // replaceIDsValue(actualXML);
        return actualXML;
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

    private class DBDiffAssertionException extends TestExecutionBusinessException {

        @Override
        public TestoniaExceptionReasonCode getReasonCode() {
            return TestoniaExceptionReasonCode.FAILURE_FULFILL_ACTIVITY_DBDIFF;
        }

        public DBDiffAssertionException(String message) {
            super(message);
        }

    }


}
