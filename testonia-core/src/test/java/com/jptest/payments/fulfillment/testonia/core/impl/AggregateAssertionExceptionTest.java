package com.jptest.payments.fulfillment.testonia.core.impl;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for {@link AggregateAssertionException}
 */

public class AggregateAssertionExceptionTest {
	
	private AggregateAssertionException aggregateAssertionException;

    @Test(dataProvider="testAggregateAssertDP")
    public void testAggregateAssert(List<TestExecutionException> inputAssertions, List<String> expectedFailureCause, String expectedToString) {
    	aggregateAssertionException = new AggregateAssertionException(inputAssertions);
        
    	// test
    	Assert.assertEquals(aggregateAssertionException.getAssertions(), inputAssertions);
    	Assert.assertEquals(aggregateAssertionException.getFailureCause(), expectedFailureCause);
    	Assert.assertEquals(aggregateAssertionException.toString(), expectedToString);
    }
    
    @DataProvider
    private static Object[][] testAggregateAssertDP() {
    	return new Object[][] {
    		{new ArrayList<>(), new ArrayList<>(), ""},
    		{Arrays.asList(new SomeBusinessException("error1")), Arrays.asList("FAILURE_GENERIC_ERROR"), "The following soft-assert(s) failed:\n1) error1"},
    		{Arrays.asList(new SomeBusinessException("error1"), new SomeBusinessException("error2")), 
    			Arrays.asList("FAILURE_GENERIC_ERROR","FAILURE_GENERIC_ERROR"), "The following soft-assert(s) failed:\n1) error1\n2) error2"},
    	};
    }
    
    private static class SomeBusinessException extends TestExecutionBusinessException {

        @Override
        public TestoniaExceptionReasonCode getReasonCode() {
        	return TestoniaExceptionReasonCode.FAILURE_GENERIC_ERROR;
        }
        
        public SomeBusinessException(String message) {
        	super(message);
        }

    }
    
    @Test(expectedExceptions=IllegalStateException.class)
    public void testAggregateAssertForGetReasonCode() {
    	aggregateAssertionException = new AggregateAssertionException(new ArrayList<>());
        
    	// test
    	aggregateAssertionException.getReasonCode();
    }

}
