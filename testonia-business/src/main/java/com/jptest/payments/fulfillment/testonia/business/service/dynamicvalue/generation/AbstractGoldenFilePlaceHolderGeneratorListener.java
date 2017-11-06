package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.dataprovider.TestData;
import com.jptest.payments.fulfillment.testonia.core.guice.GuiceInjector;
import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderGenerationStats;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.internal.TestNGMethod;

import javax.inject.Inject;
import javax.xml.transform.TransformerException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Create a new suite file in your project to replace placeholders
 * <p>
 * Please include in the suite file:
 * <li>a listener of type {@link GoldenFilePlaceHolderPreGeneratorListener} or {@link GoldenFilePlaceHolderPostGeneratorListener}</li>
 * <li>the test class for which you want to generate placeholders</li>
 * 
 * @see GoldenFilePlaceHolderPreGeneratorListener - to generate placeholder for existing golden files
 * @see GoldenFilePlaceHolderPostGeneratorListener - to generate placeholders for new golden files
 */
public abstract class AbstractGoldenFilePlaceHolderGeneratorListener implements ITestListener, ISuiteListener, GuiceInjector
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGoldenFilePlaceHolderGeneratorListener.class);

    private GoldenFilePlaceHolderGenerationStats stats;

    @Inject
    private Configuration config;

    @Inject
    private GoldenFilePlaceHolderGeneratorService goldenFilePlaceHolderGeneratorService;

    @Override
    public void onStart(ISuite suite) {
        stats = new GoldenFilePlaceHolderGenerationStats();
    }

    @Override
    public void onFinish(ISuite suite) {

    }
    
    @Override
    public void onTestStart(ITestResult result) {

        //inject(result.getTestContext(), this);
    }

    @Override
    public void onStart(ITestContext context) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFinish(ITestContext context) {
        // TODO Auto-generated method stub
    }

    protected void generatePlaceHolders(ITestResult result) {
        Path jsonFilePath = getJsonFilePath(result);
        String testName = result.getInstanceName() + "#" + result.getName();

        try {
            goldenFilePlaceHolderGeneratorService.generatePlaceHolders(testName, jsonFilePath, getData(result),
                    stats);
        }
        catch (TransformerException e) {
            LOGGER.error("Error occurred executing placeholders for {}", jsonFilePath);
        }
    }

    protected boolean isGoldenFileGenerationMode() {
        return config.getBoolean(BizConfigKeys.GOLDEN_FILE_GENERATION_MODE.getName(), false);
    }

    protected boolean isGoldenFilePlaceHolderGenerationOnlyMode() {
        //ConfigurationHelper.getTestConfiguration()
      //          .getBoolean(BizConfigKeys.GOLDEN_FILE_PLACEHOLDER_GENERATION_ONLY_MODE.getName(), false);
        return false;
    }

    protected void printStats() {
        stats.print();
    }

    private Path getJsonFilePath(ITestResult result) {
        TestData testDataAnnotation = ((TestNGMethod) result.getMethod()).getMethod().getAnnotation(TestData.class);
        return Paths.get(getFileForInputData(testDataAnnotation.filename()));
    }

    private TestCaseInputData getData(ITestResult result) {
        return (TestCaseInputData) result.getParameters()[0];
    }

    private String getFileForInputData(String fileName) {
        String baseDir = config
                .getString(BizConfigKeys.TEST_DATA_FILE_LOCATION.getName());
        return FilenameUtils.concat(baseDir, fileName);
    }


    

}
