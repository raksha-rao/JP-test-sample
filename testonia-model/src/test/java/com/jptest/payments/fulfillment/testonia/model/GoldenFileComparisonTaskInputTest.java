package com.jptest.payments.fulfillment.testonia.model;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


/**
 * Unit test for {@link GoldenFileComparisonTaskInput}
 */
public class GoldenFileComparisonTaskInputTest {

    @Test(dataProvider = "testGetGoldenFileLocationSourceForSuccessDP")
    public void testGetGoldenFileLocationSourceForSuccess(String inputLocation) {
        GoldenFileComparisonTaskInput input = new GoldenFileComparisonTaskInput();
        input.setGoldenFileLocation(inputLocation);
        input.getGoldenFileLocationSource();
    }

    @DataProvider
    private static Object[][] testGetGoldenFileLocationSourceForSuccessDP() {
        return new Object[][] {
                { null },
                { "" },
                { "./golden/IPNValidation.xml" },
                { "./golden/testDirectory/IPNValidation.xml" }

        };
    }

}
