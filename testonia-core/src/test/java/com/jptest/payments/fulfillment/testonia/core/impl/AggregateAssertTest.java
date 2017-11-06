package com.jptest.payments.fulfillment.testonia.core.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;

/**
 * Unit test for {@link AggregateAssert}
 */
public class AggregateAssertTest {

    private AggregateAssert aggregateAssert;

    @Test
    public void testAggregateAssertForSoftAssert() {

        aggregateAssert = new AggregateAssert(true); // isSoftAssert=true
        try {
        	// test
            aggregateAssert.fail("", new SomeBusinessException());
            Assert.assertTrue(aggregateAssert.hasSoftErrors());
            Assert.assertNotNull(aggregateAssert.getAggregateException());
        } catch (AssertionError ex) {
            Assert.fail("In soft mode, it should'nt throw error");
        }
    }

    @Test
    public void testAggregateAssertForHardAssert() {

        aggregateAssert = new AggregateAssert(false); // isSoftAssert = false
        try {
            aggregateAssert.fail("", new SomeBusinessException());
            Assert.fail("In hard mode, it should've thrown the error right away");
        } catch (Throwable ex) {
        	Assert.assertFalse(aggregateAssert.hasSoftErrors());
            Assert.assertTrue(ex instanceof TestExecutionException);
        }
    }
    
    private static class SomeBusinessException extends TestExecutionBusinessException {

        @Override
        public TestoniaExceptionReasonCode getReasonCode() {
        	return TestoniaExceptionReasonCode.FAILURE_GENERIC_ERROR;
        }

    }

}
