package com.jptest.payments.fulfillment.testonia.core;

import com.jptest.payments.fulfillment.testonia.core.impl.ComponentExecutor;

/**
 * Represents a component which is timeout sensitive and thus implements a
 * method that returns a expected duration for the execution. This is used in
 * {@link ComponentExecutor} to wait for the given duration for execution.
 */
public interface TimeoutAwareComponent {

    long getTimeoutInMs();

}
