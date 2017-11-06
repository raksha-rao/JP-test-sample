package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.SkipException;
import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;


/**
 * This listener will generate placeholders for all existing golden files.
 * <p>
 * It will run the suite, generate placeholders and will skip execution of the testcases
 */
public class GoldenFilePlaceHolderPreGeneratorListener extends AbstractGoldenFilePlaceHolderGeneratorListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoldenFilePlaceHolderPreGeneratorListener.class);

    @Override
    public void onStart(ISuite suite) {
        super.onStart(suite);
        if (isGoldenFilePlaceHolderGenerationOnlyMode()) {
            System.setProperty(CoreConfigKeys.DISABLE_KIBANA_REPORTING.getName(), "true");
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        if (isGoldenFilePlaceHolderGenerationOnlyMode()) {
            printStats();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        if (isGoldenFilePlaceHolderGenerationOnlyMode()) {
            LOGGER.warn(
                    "***** Running test in GOLDEN_FILE_PLACEHOLDER_GENERATION_ONLY_MODE. It will skip the test execution *****");
            generatePlaceHolders(result);
            throw new SkipException("Skipping real execution");
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        // DO NOTHING
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // DO NOTHING
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // DO NOTHING
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // DO NOTHING
    }


    

}
