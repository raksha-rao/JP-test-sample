package com.jptest.payments.fulfillment.testonia.core;

import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionUnit;

/**
 * Represents an entity used in this test framework identified
 * by its name. All the core interfaces like {@link Task}, {@link ExecutionUnit}
 * are essentially components.
 */
public interface Component {

    default String getName() {
        return this.getClass().getSimpleName();
    }
    

}
