package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.function.Function;

import com.jptest.payments.fulfillment.testonia.core.Context;

/**
 * This class can be used to run a method as an asserter as part of 
 * {@link ExecutionUnit}
 * This should help avoid having to do inline asserters / private static classes
 * extending BaseAsserter for scenarios where we just want to run a method as a task.
 */
public class FunctionExecutorAsserter extends BaseAsserter {

    private Function<Context, Void> function;

    public FunctionExecutorAsserter(Function<Context, Void> function) {
        super();
        this.function = function;
    }

    @Override
    public void validate(Context context) {
        function.apply(context);
    }

}
