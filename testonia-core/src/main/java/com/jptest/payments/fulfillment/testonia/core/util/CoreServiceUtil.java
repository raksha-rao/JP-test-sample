package com.jptest.payments.fulfillment.testonia.core.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.Configuration;

import com.jpinc.kernel.cal.api.sync.CalTransactionHelper;
import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;

/**
 * Util class for convenience methods 
 * related to core functionalities
 */
@Singleton
public class CoreServiceUtil {

    @Inject
    private Configuration config;

    void setConfig(Configuration config) {
        this.config = config;
    }

    public String getTargetStage() {
        return config.getString(CoreConfigKeys.TARGET_STAGE.getName());
    }

    public String getTopCorrelationId() {
        return CalTransactionHelper.getTopTransaction().getCorrelationId();
    }

}
