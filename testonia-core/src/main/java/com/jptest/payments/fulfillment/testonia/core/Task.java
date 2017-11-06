package com.jptest.payments.fulfillment.testonia.core;

import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionUnit;

/**
 * Represents a component that sets up the required data/ input for the test
 * case execution For more details of how this is used please refer to
 *
 * @see ExecutionUnit
 */
public interface Task<V> extends TestComponent<V> {

    @Override
    default V execute(Context context) {
        return process(context);
    }

    /**
     * Gets the instance of V class.
     */
    V process(Context context);

}
