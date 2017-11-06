package com.jptest.payments.fulfillment.testonia.core.impl;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.Task;

/**
 * Base implementation of a retriever for common implementation
 */
public abstract class BaseTask<V> extends BaseTestComponent<V> implements Task<V> {

    @Override
    public final V execute(Context context) {
        return Task.super.execute(context);
    }

}
