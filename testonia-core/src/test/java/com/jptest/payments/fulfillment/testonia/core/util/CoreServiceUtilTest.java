package com.jptest.payments.fulfillment.testonia.core.util;

import org.apache.commons.configuration.Configuration;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CoreServiceUtilTest {

    @Test
    public void testGetTargetStage() {
        CoreServiceUtil util = getUtilWithMockConfig("msmaster");
        Assert.assertEquals(util.getTargetStage(), "msmaster");
    }

    private CoreServiceUtil getUtilWithMockConfig(String expectedValue) {
        CoreServiceUtil util = new CoreServiceUtil();
        Configuration config = Mockito.mock(Configuration.class);
        Mockito.when(config.getString(Mockito.anyString())).thenReturn(expectedValue);
        util.setConfig(config);
        return util;
    }

}
