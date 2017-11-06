/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.model.util;

/**
 * @JP Inc.
 *
 */
public class RecoupConstants {

	public enum RecoupAction {
		SCHEDULE_RECOUP("SCHEDULE_RECOUP"), 
		COMPLETE_RECOUP("COMPLETE_RECOUP"), 
		CANCEL_RECOUP("CANCEL_RECOUP");

		private String recoupAction;

		private RecoupAction(String recoupAction) {
			this.recoupAction = recoupAction;
		}

		public String getRecoupAction() {
			return recoupAction;
		}
	}
	
	public enum WaiveFeePolicy {
		RECOUP_WAIVE_PP_ALL_FEE('A'),
		RECOUP_WAIVE_NO_FEE('N');
		
		private char waiveFeePolicy;
		
		private WaiveFeePolicy(char waiveFeePolicy) {
			this.waiveFeePolicy = waiveFeePolicy;
		}
		
		public char getValue() {
			return waiveFeePolicy;
		}
		
		public byte getByte() {
			return (byte) waiveFeePolicy;
		}
	}
	
	public enum RecoupCancellationReason {
		RECOUP_CANCEL_REASON_EXPIRED_BY_jptest('X'),
		RECOUP_CANCEL_REASON_CANCELLED_BY_jpinc('C');
		
		private char recoupCancellationReason;
		
		private RecoupCancellationReason(char recoupCancellationReason) {
			this.recoupCancellationReason = recoupCancellationReason;
		}
		
		public char getValue() {
			return recoupCancellationReason;
		}
		
		public byte getByte() {
			return (byte) recoupCancellationReason;
		}
	}
	
	public enum RecoupStatus {
		RECOUP_STATUS_SCHEDULED('S'),
		RECOUP_STATUS_PROCESSING('P'),
		RECOUP_STATUS_FAILED('F'),
		RECOUP_STATUS_CANCELLED('C'),
		RECOUP_STATUS_EXPIRED('X'),
		RECOUP_STATUS_COMPLETED('Z');
		
		private char recoupStatus;

		private RecoupStatus(char recoupStatus) {
			this.recoupStatus = recoupStatus;
		}
		
		public char getValue() {
			return recoupStatus;
		}
		
		public byte getByte() {
			return (byte) recoupStatus;
		}
		
	}
	
}
