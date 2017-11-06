package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.function.Function;

import com.jptest.payments.fulfillment.testonia.core.Context;

/**
 * This class can be used to run a method as a task as part of 
 * {@link ExecutionUnit} or {@link ConcurrentExecutionUnit}
 * This should help avoid having to do inline tasks / private static classes
 * extending BaseTask for scenarios where we just want to run a method as a task.
 */
public class FunctionExecutorTask<T> extends BaseTask<T> {

    private Function<Context, T> function;

    public FunctionExecutorTask(Function<Context, T> function) {
        super();
        this.function = function;
    }

    @Override
    public T process(Context context) {
        return function.apply(context);
    }

}
