package com.jptest.payments.fulfillment.testonia.core.guice;

import org.apache.commons.configuration.Configuration;

import com.jptest.platform.test.api.Environment;

import net.sourceforge.cobertura.CoverageIgnore;

@CoverageIgnore
public class EnvironmentImpl implements Environment {

    Configuration config = null;

    EnvironmentImpl(Configuration config) {
        this.config = config;
    }

    @Override
    public boolean isCiExecution() {
        return config.getBoolean(ConfigurationHelper.CI_EXECUTION_FLAG, false);
    }
}
