package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.Map;

import org.slf4j.MDC;

import com.jpinc.kernel.cal.api.CalEvent;
import com.jpinc.kernel.cal.api.sync.CalEventFactory;
import com.jpinc.kernel.executor.CallableTask;
import com.jpinc.kernel.executor.TaskExecutor;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TestComponent;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;

/**
 * This is the executor class for {@link TestComponent} which wraps the
 * TestComponent.execute with a call() method that runs in a separate thread
 * thus making the TestComponent execution parallel. This is used in
 * {@link ComponentExecutor} which executes a list of {@link TestComponent}
 * instances by submitting them to {@link TaskExecutor}. If a particular
 * implementation of this class throws an exception the
 * {@link ComponentExecutor} handles it by logging it and by rethrowing it by
 * wrapping inside {@link TestExecutionException}
 */
public class CallableComponent<V> implements CallableTask<V> {

    private TestComponent<V> component;

    private static final String CAL_EVENT_SUCCESS = "0";

    private Context context;

    private Map<String, String> mdcMap;

    public CallableComponent(TestComponent<V> component, Context context) {
        this.component = component;
        this.context = context;
        // This is done to make sure that the new thread inherits all the 
        // contextual data from MDC (used in reporting / logging)
        mdcMap = MDC.getCopyOfContextMap();
    }

    @Override
    public String getName() {
        return component.getName();
    }

    public TestComponent<V> getComponent() {
        return component;
    }

    @Override
    public V call() throws Exception {

        initializeContextualLoggingData();
        CalEvent event = CalEventFactory.create("EXECUTION_UNIT");
        try {
            event.setName(getName());
            event.addData(getName(), "START");
            V result = component.execute(context);
            event.addData(getName(), "END");
            event.setStatus(CAL_EVENT_SUCCESS);
            return result;
        } catch (Exception e) {
            event.setStatus(e);
            throw e;
        } finally {
            event.completed();
            clearContextualLoggingData();
        }
    }

    private void clearContextualLoggingData() {
        MDC.clear();
    }

    private void initializeContextualLoggingData() {
        if (mdcMap != null)
            MDC.setContextMap(mdcMap);
    }

}
