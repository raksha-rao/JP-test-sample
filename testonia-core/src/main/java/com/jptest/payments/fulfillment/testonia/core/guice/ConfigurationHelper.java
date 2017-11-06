package com.jptest.payments.fulfillment.testonia.core.guice;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.platform.test.api.Environment;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * @JP Inc.
 */
@CoverageIgnore
public class ConfigurationHelper {
    private static Logger logger = LoggerFactory.getLogger(ConfigurationHelper.class);

    public final static String CONFIG_FILE_OVERRIDE = "config.file";
    public final static String CONFIG_FILE_TYPE_OVERRIDE = "config.file.type";

    private final static String ENVIRONMENT = "CI_ENVIRONMENT";
    private final static String DEFAULT_CONFIG_FILE = "test.properties";
    public final static String CI_EXECUTION_FLAG = "isciexecution";

    private static final String TESTONIA_BRIDGE_CONFIG = "testonia-bridge.properties";
    private static final String TESTONIA_DAL_CONFIG = "testonia-dal.properties";
    private static final String TESTONIA_CORE_CONFIG = "testonia-core.properties";
    private static final String TESTONIA_BUSINESS_CONFIG = "testonia-business.properties";

    private static Configuration testConfiguration = null;
    private static EnvironmentImpl environmentheper = null;

    public synchronized static void initializeEnvironmentConfiguration(String configoverride, String configtype) {
        CompositeConfiguration configuration = new CompositeConfiguration();
        //add environment configuration
        configuration.addConfiguration(new EnvironmentConfiguration());
        //add system property configurations
        configuration.addConfiguration(new SystemConfiguration());
        boolean isci = isCiEnvironment(configuration);
        String configfile = getConfigurationFileName(configuration, configoverride);
        loadUserConfig(configuration, configfile, isci);
        // add testonia specific configuration
        addTestoniaConfiguationFiles(configuration);
        testConfiguration = configuration;
        environmentheper = new EnvironmentImpl(testConfiguration);
    }

    private synchronized static void addTestoniaConfiguationFiles(CompositeConfiguration configuration) {
        addConfiguationFile(configuration, TESTONIA_DAL_CONFIG);
        addConfiguationFile(configuration, TESTONIA_BRIDGE_CONFIG);
        addConfiguationFile(configuration, TESTONIA_CORE_CONFIG);
        addConfiguationFile(configuration, TESTONIA_BUSINESS_CONFIG);
    }

    private synchronized static void addConfiguationFile(CompositeConfiguration configuration, String file) {
        try {
            configuration.addConfiguration(new PropertiesConfiguration(file));
            logger.info("{} configuration file loaded.", file);
        } catch (ConfigurationException ce) {
            logger.error(
                    "{} configuration file is missing. Make sure you have added testonia-business dependency in your project",
                    file, ce);
        }
    }

    public synchronized static Configuration getTestConfiguration() {
        if (testConfiguration == null) {
            throw new IllegalStateException("Configuration has not been initialized");
        }
        return testConfiguration;
    }

    public synchronized static Environment getExecutionEnvironment() {
        if (testConfiguration == null) {
            throw new IllegalStateException("Configuration has not been initialized");
        }
        return environmentheper;
    }

    private static void loadUserConfig(CompositeConfiguration parent, String configfile, boolean ci) {
        String envfile = "dev." + configfile;
        if (ci) {
            envfile = "ci." + configfile;
        }
        String isciexecution = Boolean.toString(ci);
        //TODO handle xml too for now just flat properties

        Configuration config = null;
        //attempt to load environment specific files if they are there, ignore any error
        try {
            config = new PropertiesConfiguration(envfile);
            logger.info("Adding environment specific properties to the configuration");
            parent.addConfiguration(config);
        } catch (ConfigurationException e) {
            logger.debug("No enviroment file found, ignoring it", e);
        }

        try {
            config = new PropertiesConfiguration(configfile);
            logger.debug("Adding the common properties to the configuration");
            config.addProperty(CI_EXECUTION_FLAG, isciexecution);
            parent.addConfiguration(config);
        } catch (ConfigurationException e) {
            logger.error("Unable to load configuration files for this test execution", e);
            throw new IllegalStateException(e);
        }
    }

    //determine the name to use for the configuration file, system or env variables trump the override
    private static String getConfigurationFileName(Configuration envconfig, String configoverride) {
        String ret = DEFAULT_CONFIG_FILE;
        String filename = envconfig.getString(CONFIG_FILE_OVERRIDE, "");
        //not overriden by environment or system check now a parameter override
        if (filename.isEmpty()) {
            if (configoverride != null && !configoverride.trim().isEmpty()) {
                ret = configoverride.trim();
            }
        } else {
            ret = filename.trim();
        }
        return ret;
    }

    public static boolean isCiEnvironment(Configuration config) {
        if (config.containsKey("JENKINS_HOME") || isJenkinsUser(config) || config.containsKey("JENKINS_URL")
                || config.containsKey("HUDSON_COOKIE")) {
            return true;
        }
        return config.containsKey(ENVIRONMENT);
    }

    private static boolean isJenkinsUser(Configuration config) {
        return config.containsKey("USER") && config.getString("USER", "").equalsIgnoreCase("jenkins");
    }
}
