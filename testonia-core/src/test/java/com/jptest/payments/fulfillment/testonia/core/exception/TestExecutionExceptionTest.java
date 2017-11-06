package com.jptest.payments.fulfillment.testonia.core.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;

/**
 * Unit test for {@link TestExecutionException}
 */
public class TestExecutionExceptionTest {
	
	@Test
	public void test() {
		new SomeTestExecutionException();
		new SomeTestExecutionException("test");
		new SomeTestExecutionException(new RuntimeException("test"));
		new SomeTestExecutionException("test", new RuntimeException("test"));
		Assert.assertEquals(new SomeTestExecutionException().getReasonCode(), TestoniaExceptionReasonCode.FAILURE_GENERIC_ERROR);
	}

	
	private static class SomeTestExecutionException extends TestExecutionException {
		
		public SomeTestExecutionException() {
			
		}
		
		public SomeTestExecutionException(String message) {
			super(message);
		}

		public SomeTestExecutionException(Throwable cause) {
			super(cause);
		}

		public SomeTestExecutionException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
