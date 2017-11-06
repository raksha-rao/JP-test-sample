package com.jptest.payments.fulfillment.testonia.model;

import org.apache.commons.lang3.StringUtils;
import com.jptest.payments.fulfillment.testonia.model.util.ResourceUtil;


/**
 * @see //GoldenFileComparisonTaskInputTest
 */
public class GoldenFileComparisonTaskInput {

    private String goldenFileLocation;

    public String getGoldenFileLocation() {
        return goldenFileLocation;
    }

    public void setGoldenFileLocation(String goldenFileLocation) {
        this.goldenFileLocation = goldenFileLocation;
    }

    /**
     * Returns source location of golden file
     * 
     * @return
     */
    public String getGoldenFileLocationSource() {
        return getGoldenFileLocationSource(goldenFileLocation);
    }

    public static String getGoldenFileLocationSource(String goldenFileLocation) {
        return StringUtils.isBlank(goldenFileLocation) ? goldenFileLocation
                : ResourceUtil.getTestResourceDirectory() + goldenFileLocation;
    }

}
