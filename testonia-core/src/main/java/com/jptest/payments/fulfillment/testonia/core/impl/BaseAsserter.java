package com.jptest.payments.fulfillment.testonia.core.impl;

import com.jptest.payments.fulfillment.testonia.core.Asserter;
import com.jptest.payments.fulfillment.testonia.core.Context;

public abstract class BaseAsserter extends BaseTestComponent<Void> implements Asserter {

    @Override
    public final Void execute(Context context) {
        return Asserter.super.execute(context);
    }

}
