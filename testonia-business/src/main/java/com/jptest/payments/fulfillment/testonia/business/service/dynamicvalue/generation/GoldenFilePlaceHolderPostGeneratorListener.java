package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import org.testng.ITestResult;


/**
 * This listener will generate placeholders for new golden files.
 * <p>
 * If golden file generation mode is set, it will generate the placeholder at the end of the test run
 */
public class GoldenFilePlaceHolderPostGeneratorListener  {

    public void onTestSuccess(ITestResult result) {
        //generatePlaceHolders(result);
    }

    public void onTestFailure(ITestResult result) {
        //generatePlaceHolders(result);
    }

    public void onTestSkipped(ITestResult result) {
        // DO NOTHING
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub
      //  generatePlaceHolders(result);
    }

    protected void generatePlaceHolders(ITestResult result) {
      /*  if (isGoldenFileGenerationMode()) {
            super.generatePlaceHolders(result);
        }*/
    }

}
