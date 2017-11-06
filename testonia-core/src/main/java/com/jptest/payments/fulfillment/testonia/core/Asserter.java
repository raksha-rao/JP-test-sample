package com.jptest.payments.fulfillment.testonia.core;

/**
 * Represents the type that does validations on one or more objects available in
 * the {@link Context}
 */
public interface Asserter extends TestComponent<Void> {

    void validate(Context context);

    @Override
    default Void execute(Context context) {
        validate(context);
        return null;
    }

}
