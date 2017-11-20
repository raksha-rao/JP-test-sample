package com.jptest.payments.fulfillment.testonia.model.util;

public class ResourceUtil {

    public static final String MAIN_RESOURCE_LOCATION = "/src/main/resources/";
    public static final String TEST_RESOURCE_LOCATION = "/src/test/resources/";

    public static final String USER_DIR_PROPERTY = "user.dir";

    public static final String getTestResourceDirectory() {
        return System.getProperty(USER_DIR_PROPERTY) + TEST_RESOURCE_LOCATION;
    }

    public static final String getMainResourceDirectory() {
        return System.getProperty(USER_DIR_PROPERTY) + MAIN_RESOURCE_LOCATION;
    }
}
