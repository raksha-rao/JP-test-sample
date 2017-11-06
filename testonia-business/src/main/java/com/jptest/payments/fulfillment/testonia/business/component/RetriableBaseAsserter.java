package com.jptest.payments.fulfillment.testonia.business.component;

import javax.inject.Inject;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;


/**
 * Parent class of all retriable tasks. This class has the sole responsibility of performing retry.
 * <p>
 * It keeps retrying until either
 * <li>We got desired output</li>
 * <li>or we exceeded retry time limit</li> All subclasses are required to override isDesiredOutput() and
 * retriableExecute() method
 *
 * @param <O>
 * @see also checkout {@link RetriableBaseTask} and {@link RetriableTask}
 */
public abstract class RetriableBaseAsserter<O> extends BaseAsserter implements TimeoutAwareComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetriableBaseAsserter.class);

    private static final long MAX_WAIT_IN_MS = 180 * 1000L;
    private static final long REPEAT_INTERVAL_IN_MS = 15 * 1000L;

    private final long maxWaitInMs;
    private final long repeatIntervalInMs;

    @Inject
    private Configuration config;

    public RetriableBaseAsserter() {
        this.maxWaitInMs = MAX_WAIT_IN_MS;
        this.repeatIntervalInMs = REPEAT_INTERVAL_IN_MS;
    }

    public RetriableBaseAsserter(long maxWaitInMs, long repeatIntervalInMs) {
        this.maxWaitInMs = maxWaitInMs;
        this.repeatIntervalInMs = repeatIntervalInMs;
    }

    public RetriableBaseAsserter(String maxWaitInMsKey, String repeatIntervalInMsKey) {
        this.maxWaitInMs = this.config.getLong(maxWaitInMsKey);
        this.repeatIntervalInMs = this.config.getLong(repeatIntervalInMsKey);
    }

    @Override
    public void validate(Context context) {
        final long startTime = System.currentTimeMillis();
        O output = null;
        boolean hasTimeToRetry = true;
        do {
            // execute task
            output = this.retriableExecute(context);

            // check if got desired output. If not, continue.
            if (this.isDesiredOutput(output)) {
                this.onSuccess(context, output);
                return;
            }

            // wait for pre-defined time interval
            try {
                Thread.sleep(this.repeatIntervalInMs);
            }
            catch (final InterruptedException iexpt) {
                this.onFailure(context, output);
            }

            // check if out-of-time
            hasTimeToRetry = this.hasNotExpired(startTime);
            if (hasTimeToRetry) {
                LOGGER.debug("Retrying task: {}", this.getClass());
            }

        }
        while (hasTimeToRetry);

        this.onFailure(context, output);
    }

    private boolean hasNotExpired(long startTime) {
        return (System.currentTimeMillis() - startTime) <= this.maxWaitInMs;
    }

    @Override
    public long getTimeoutInMs() {
        return this.maxWaitInMs + this.repeatIntervalInMs;
    }

    public abstract boolean isDesiredOutput(O output);

    public abstract O retriableExecute(Context context);

    public abstract void onSuccess(Context context, O output);

    public abstract void onFailure(Context context, O output);

}
