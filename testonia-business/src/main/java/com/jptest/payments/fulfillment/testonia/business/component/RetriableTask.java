package com.jptest.payments.fulfillment.testonia.business.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent class of all retriable tasks. This class has the sole responsibility of performing retry.
 * <p>
 * It keeps retrying until either
 * <li>We got desired output</li>
 * <li>or we exceeded retry time limit</li>
 *
 * All subclasses are required to override isDesiredOutput() and retriableExecute() method
 *
 * @param <I>
 * @param <O>
 * 
 * @see also checkout {@link RetriableBaseTask} and {@link RetriableBaseAsserter}
 */
public abstract class RetriableTask<I, O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetriableTask.class);

    private static final long MAX_WAIT_IN_MS = 180 * 1000;
    private static final long REPEAT_INTERVAL_IN_MS = 15 * 1000;

    private long maxWaitInMs;
    private long repeatIntervalInMs;

    public RetriableTask() {
        this.maxWaitInMs = MAX_WAIT_IN_MS;
        this.repeatIntervalInMs = REPEAT_INTERVAL_IN_MS;
    }

    public RetriableTask(long maxWaitInMs, long repeatIntervalInMs) {
        this.maxWaitInMs = maxWaitInMs;
        this.repeatIntervalInMs = repeatIntervalInMs;
    }

    public void setMaxWaitInMs(long maxWaitInMs) {
        this.maxWaitInMs = maxWaitInMs;
    }

    public void setRepeatIntervalInMs(long repeatIntervalInMs) {
        this.repeatIntervalInMs = repeatIntervalInMs;
    }

    public O execute(I input) {
        long startTime = System.currentTimeMillis();
        O output = null;
        boolean hasTimeToRetry = true;
        do {
            // execute task
            output = retriableExecute(input);

            // check if got desired output. If not, continue.
            if (isDesiredOutput(output)) {
                return onSuccess(input, output);
            }

            // wait for pre-defined time interval
            try {
                Thread.sleep(repeatIntervalInMs);
            } catch (InterruptedException iexpt) {
                return onFailure(input, output);
            }

            // check if out-of-time
            hasTimeToRetry = hasNotExpired(startTime);
            if (hasTimeToRetry) {
                LOGGER.debug("Retrying task: {}", this.getClass());
            }
        } while (hasTimeToRetry);

        return onFailure(input, output);
    }

    private boolean hasNotExpired(long startTime) {
        return (System.currentTimeMillis() - startTime) <= maxWaitInMs;
    }

    protected abstract boolean isDesiredOutput(O output);

    protected abstract O retriableExecute(I input);

    protected abstract O onSuccess(I input, O output);

    protected abstract O onFailure(I input, O output);

}
