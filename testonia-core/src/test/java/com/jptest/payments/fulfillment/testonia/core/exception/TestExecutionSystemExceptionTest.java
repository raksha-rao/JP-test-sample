package com.jptest.payments.fulfillment.testonia.core.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;

/**
 * Unit test for {@link TestExecutionSystemException}
 */
public class TestExecutionSystemExceptionTest {
	
	@Test
	public void test() {
		new SomeSystemException();
		new SomeSystemException("test");
		new SomeSystemException(new RuntimeException("test"));
		new SomeSystemException("test", new RuntimeException("test"));
		Assert.assertEquals(new SomeSystemException().getReasonCode(), TestoniaExceptionReasonCode.FAILURE_GENERIC_SYSTEM_ERROR);
	}

	
	private static class SomeSystemException extends TestExecutionSystemException {
		
		public SomeSystemException() {
			
		}
		
		public SomeSystemException(String message) {
			super(message);
		}

		public SomeSystemException(Throwable cause) {
			super(cause);
		}

		public SomeSystemException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
