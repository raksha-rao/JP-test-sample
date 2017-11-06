package com.jptest.payments.fulfillment.testonia.core.impl;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TestComponent;
import com.jptest.payments.fulfillment.testonia.core.guice.GuiceInjector;

/**
 * Provides default implementation for {@link TestComponent} behaviour
 */
public abstract class BaseTestComponent<V> implements TestComponent<V>, GuiceInjector {

    @Inject
    private Configuration config;

    public BaseTestComponent() {
        inject(this);
    }

    /**
     * Default implementation for TestComponent.shouldParticipate where 
     * we return true unless it is user creation mode , in that case return false
     * so that we can just create the users without running other units/ tasks etc
     */
    @Override
    public boolean shouldParticipate(Context context) {
        if (config != null && config.getBoolean(CoreConfigKeys.CREATE_CACHED_USER.getName())) {
            return false;
        } else {
            return true;
        }
    }

}
