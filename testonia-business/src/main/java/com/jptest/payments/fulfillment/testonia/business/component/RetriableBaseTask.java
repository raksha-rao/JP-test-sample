package com.jptest.payments.fulfillment.testonia.business.component;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;

/**
 * Parent class of all retriable tasks. This class has the sole responsibility of performing retry.
 * <p>
 * It keeps retrying until either
 * <li>We got desired output</li>
 * <li>or we exceeded retry time limit</li>
 *
 * All subclasses are required to override isDesiredOutput() and retriableExecute() method
 *  
 * @param <O>
 * 
 *  @see also checkout {@link RetriableBaseAsserter} and {@link RetriableTask}
 */
public abstract class RetriableBaseTask<O> extends BaseTask<O> implements TimeoutAwareComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetriableBaseTask.class);

    private static final long MAX_WAIT_IN_MS = 180 * 1000L;
    private static final long REPEAT_INTERVAL_IN_MS = 15 * 1000L;

    private long maxWaitInMs;
    private long repeatIntervalInMs;

    @Inject
    protected Configuration config;

    public RetriableBaseTask() {
        this.maxWaitInMs = MAX_WAIT_IN_MS;
        this.repeatIntervalInMs = REPEAT_INTERVAL_IN_MS;
    }

    public RetriableBaseTask(long maxWaitInMs, long repeatIntervalInMs) {
        this.maxWaitInMs = maxWaitInMs;
        this.repeatIntervalInMs = repeatIntervalInMs;
    }

    public RetriableBaseTask(String maxWaitInMsKey, String repeatIntervalInMsKey) {
        this.maxWaitInMs = config.getLong(maxWaitInMsKey);
        this.repeatIntervalInMs = config.getLong(repeatIntervalInMsKey);
    }

    @Override
    public O process(Context context) {
        long startTime = System.currentTimeMillis();
        O output = null;
        boolean hasTimeToRetry = true;
        do {
            // execute task
            output = retriableExecute(context);

            // check if got desired output. If not, continue.
            if (isDesiredOutput(output)) {
                return onSuccess(context, output);
            }

            // wait for pre-defined time interval
            try {
                Thread.sleep(repeatIntervalInMs);
            } catch (InterruptedException iexpt) {
                return onFailure(context, output);
            }

            // check if out-of-time
            hasTimeToRetry = hasNotExpired(startTime);
            if (hasTimeToRetry) {
                LOGGER.debug("Retrying task: {}", this.getClass());
            }
        } while (hasTimeToRetry);

        return onFailure(context, output);
    }

    private boolean hasNotExpired(long startTime) {
        return (System.currentTimeMillis() - startTime) <= maxWaitInMs;
    }

    @Override
    public long getTimeoutInMs() {
        // time out (with respect to execution unit) needs to be a little bit more than 
        // the maxtime + repeat interval to leave room for the retriable thread to come back 
        // and timeout on it's own rather than the execution unit timing out on the retriable task.
        // that's why double the repeat interval.
        return maxWaitInMs + (2 * repeatIntervalInMs);
    }

    protected abstract boolean isDesiredOutput(O output);

    protected abstract O retriableExecute(Context context);

    protected abstract O onSuccess(Context context, O output);

    protected abstract O onFailure(Context context, O output);

}
