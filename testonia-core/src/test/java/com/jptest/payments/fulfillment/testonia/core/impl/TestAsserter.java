package com.jptest.payments.fulfillment.testonia.core.impl;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;

class TestAsserter extends BaseAsserter {

    private Boolean shouldFail;

    TestAsserter(Boolean shouldFail) {
        super();
        this.shouldFail = shouldFail;
    }

    @Override
    public void validate(Context context) {
        if (shouldFail)
            throw new RuntimeException("expected failure");
    }

}
