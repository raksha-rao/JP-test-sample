package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;

/**
 * Generic asserter that validates object represented by input key is not null
 */
public class NotNullAsserter extends BaseAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotNullAsserter.class);
    private String contextKey;

    public NotNullAsserter(String key) {
        this.contextKey = key;
    }

    @Override
    public void validate(final Context context) {
        final Object value = this.getDataFromContext(context, this.contextKey);
        Assert.assertNotNull(value, value + " should not be null");
        LOGGER.info("Not Null check was successful for {}", value);
    }

}
