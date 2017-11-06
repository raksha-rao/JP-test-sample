package com.jptest.payments.fulfillment.testonia.core.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonHelperTest {

    @Test
    public void readContent() {
        DummyClass instance = new DummyClass("somestring");
        String jsonContent = JsonHelper.printJsonObject(instance);
        Assert.assertNotNull(jsonContent, "JSON content can not be null");
        Assert.assertEquals(jsonContent, "{\"property\":\"somestring\"}");

    }

    private static class DummyClass {
        private String property;

        public DummyClass(String property) {
            this.property = property;
        }

        public String getProperty() {
            return property;
        }
    }
}
