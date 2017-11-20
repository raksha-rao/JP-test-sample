package com.jptest.payments.fulfillment.testonia.core.impl;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a scenario where assertion error is found in {@link AggregateAssert}
 * 
 * @see AggregateAssert
 * @see //AggregateAssertionExceptionTest
 */
public class AggregateAssertionException extends TestExecutionBusinessException {
	
	private List<TestExecutionException> assertions;
	
	public AggregateAssertionException(List<TestExecutionException> assertions) {
		this.assertions = assertions;
	}

    @Override
    public TestoniaExceptionReasonCode getReasonCode() {
        throw new IllegalStateException("This method is not supported");
    }
    
    public List<TestExecutionException> getAssertions() {
		return assertions;
	}
    
    public List<String> getFailureCause() {
    	return assertions.stream().map(assertion -> assertion.getReasonCode().name())
    			.collect(Collectors.toList());
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
        if (!assertions.isEmpty()) {
        	sb.append("The following soft-assert(s) failed:");
            
            for (int i = 1; i <= assertions.size(); i++) {
            	sb.append("\n").append(String.valueOf(i)).append(") ").append(assertions.get(i-1).getMessage());
            }
        }
        return sb.toString();
    }

}
