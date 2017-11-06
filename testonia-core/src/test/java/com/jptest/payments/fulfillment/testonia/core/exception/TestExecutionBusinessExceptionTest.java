package com.jptest.payments.fulfillment.testonia.core.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;

/**
 * Unit test for {@link TestExecutionBusinessException}
 */
public class TestExecutionBusinessExceptionTest {
	
	@Test
	public void test() {
		new SomeBusinessException();
		new SomeBusinessException("test");
		new SomeBusinessException(new RuntimeException("test"));
		new SomeBusinessException("test", new RuntimeException("test"));
		Assert.assertEquals(new SomeBusinessException().getReasonCode(), TestoniaExceptionReasonCode.FAILURE_GENERIC_BUSINESS_ERROR);
	}

	
	private static class SomeBusinessException extends TestExecutionBusinessException {
		
		public SomeBusinessException() {
			
		}
		
		public SomeBusinessException(String message) {
			super(message);
		}

		public SomeBusinessException(Throwable cause) {
			super(cause);
		}

		public SomeBusinessException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
