package com.jptest.payments.fulfillment.testonia.core.guice;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.infra.protectedpkg.ProtectedPackageUtil;
import com.jptest.payments.fulfillment.testonia.core.guice.rest.ClientRegistrationHelper;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * Suite bootstrapper that will create the main configuration object and setup all the required clients under the covers
 *
 * @JP Inc.
 */
@CoverageIgnore
public class ClientBootStrapper {

    private static final String KEYMAKER_APPNAME_PROPERTY_KEY = "keymaker.appname";

    private static Logger logger = LoggerFactory.getLogger(ClientBootStrapper.class);

    public static void init(String configfile, String filetype) {
        logger.info("Bootstrapping any clients that have been configured");
        //get the configuration going
        ConfigurationHelper.initializeEnvironmentConfiguration(configfile, filetype);
        setKeyMakerAppName();
        initializeProtected();
        //not bootstrap the clients that are configured
        ClientRegistrationHelper.registerClients();
        //we are done all clients should be registered now
    }

    public static void setKeyMakerAppName() {
        Configuration config = ConfigurationHelper.getTestConfiguration();
        String appName = config.getString(KEYMAKER_APPNAME_PROPERTY_KEY);

        if (appName == null) {
            String errorMessage = "KeyMaker test application name has not been set. Please make sure property '"
                    + KEYMAKER_APPNAME_PROPERTY_KEY + "' is present and properly set in test.properties file";
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        } else {
            System.setProperty("keymaker.test.appname", appName);
            logger.info("Setting KeyMaker test application name to " + appName);
        }
    }

    private static void initializeProtected() {
        System.setProperty("SHARE_PASSWORD_0", "aardvark");
        ProtectedPackageUtil.setupForInputViaSystemProperty();
        logger.info("Protected initialized");
    }

}
