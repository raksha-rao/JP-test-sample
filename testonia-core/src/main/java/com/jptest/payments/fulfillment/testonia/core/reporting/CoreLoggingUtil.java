package com.jptest.payments.fulfillment.testonia.core.reporting;

import org.testng.ITestResult;

/**
 * Convenience methods that are used in logging test case specific data
 */
public class CoreLoggingUtil {

    /**
     * returns the unique test case id in this format
     * <test_class_name>_<test_method_name>_<test_case_id_in_Json>
     * e.g. FulfillPaymentFMFTest_testFMFCCDeny_SaleIAchFMFFilterDeny
     * @param result
     * @return
     */
    public static String getTestCaseName(ITestResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(result.getInstanceName().substring(result.getInstanceName().lastIndexOf(".") + 1));
        sb.append("_").append(result.getName()).append("_").append(formatParameters(result));
        return sb.toString();
    }

    public static String formatParameters(ITestResult result) {
        if (result.getParameters() != null && result.getParameters().length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object parameter : result.getParameters()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                if (parameter != null) {
                    sb.append(parameter.toString());
                } else {
                    sb.append(" ");
                }
            }
            return sb.toString();
        }
        return null;
    }

}
