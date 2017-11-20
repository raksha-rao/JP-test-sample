package com.jptest.payments.fulfillment.testonia.core.impl;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is a configuration driven implementation of {@link Assertion} where it acts as either Soft or a Hard assert
 * based on the configuration aggregate.assert.mode=HARD|SOFT
 * The default value is "SOFT" so it will aggregate all assertion failures added to the instance of this class
 * and will return all soft errors through method getAllSoftErrors(). It is the framework's responsibility to check
 * for the presence of errors at the appropriate time in the test case execution. Adding the asserts alone to this class
 * won't ensure the test failure because of assertion errors.
 * IF the value is set to "HARD", then the first occurance of {@link AssertionError} will be thrown so that the 
 * test case fails right away without any delay.
 * 
 * @see AggregateAssertionException
 * @see //AggregateAssertTest
 */
public class AggregateAssert extends Assertion {
    // LinkedHashMap to preserve the order
    private List<TestExecutionException> softErrors = new CopyOnWriteArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateAssert.class);

    public void fail(String s, Exception e) {

    }

    private enum AssertMode {
        SOFT,
        HARD;
    }

    @Inject
    private Configuration configuration;

    private boolean isSoftAssert;

    @Inject
    public void init() {
        String assertMode = configuration.getString(CoreConfigKeys.AGGREGATE_ASSERT_MODE.getName(),
                AssertMode.SOFT.name());
        this.isSoftAssert = assertMode.equalsIgnoreCase(AssertMode.SOFT.name());
        LOGGER.info("The aggregate assert is configured to be in {} mode", assertMode);
    }

    public AggregateAssert() {
        super();
    }

    // For unit test
    AggregateAssert(boolean isSoftAssert) {
        super();
        this.isSoftAssert = isSoftAssert;
    }

    @Override
    protected void doAssert(IAssert<?> a) {
        onBeforeAssert(a);
        try {
            a.doAssert();
            onAssertSuccess(a);
        } catch (AssertionError ex) {
            onAssertFailure(a, ex);
            if (isSoftAssert) {
                softErrors.add((TestExecutionException)ex.getCause());
            } else {
                LOGGER.warn(
                        "Aggregate assert mode is set to HARD, "
                                + "If you want the test case to continue inspite of this error, then"
                                + "change the property 'aggregate.assert.mode' and set it to SOFT");
                throw (TestExecutionException)ex.getCause();
            }
        } finally {
            onAfterAssert(a);
        }
    }

    /** 
     * This method will be called only in SOFT mode to see if 
     * there are any errors at the end of the test case. 
     * @return
     */
    public boolean hasSoftErrors() {
        return !softErrors.isEmpty();
    }

    /**
     * This method will return all the aggregated assertion errors. 
     * This method will get called only in SOFT mode.
     * @return
     */
    public AggregateAssertionException getAggregateException() {
        return new AggregateAssertionException(softErrors);
    }

}
