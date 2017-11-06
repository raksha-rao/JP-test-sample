/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.model.money;

import com.jptest.money.DisputePayoutRecoupRequest;
import com.jptest.payments.fulfillment.testonia.model.util.RecoupConstants.RecoupAction;
import com.jptest.payments.fulfillment.testonia.model.util.RecoupConstants.RecoupCancellationReason;
import com.jptest.payments.fulfillment.testonia.model.util.RecoupConstants.WaiveFeePolicy;
import com.jptest.types.Currency;

/**
 * @JP Inc.
 *
 *         Wrapper class for recoup request
 *
 */
public class RecoupRequest {

	private RecoupAction recoupAction;
	private DisputePayoutRecoupRequest disputePayoutRecoupRequest;
	private String invoiceId;
	private Currency minimumAmountToRecoup;
	private WaiveFeePolicy waiveFeePolicy;
	private RecoupCancellationReason recoupCancellationReason;
	
	public RecoupRequest() {
		this.recoupAction = RecoupAction.SCHEDULE_RECOUP;
		this.waiveFeePolicy = WaiveFeePolicy.RECOUP_WAIVE_NO_FEE;
		this.recoupCancellationReason = RecoupCancellationReason.RECOUP_CANCEL_REASON_EXPIRED_BY_jptest;
		this.disputePayoutRecoupRequest = new DisputePayoutRecoupRequest();
	}

	public RecoupAction getRecoupAction() {
		return recoupAction;
	}

	public void setRecoupAction(RecoupAction recoupAction) {
		this.recoupAction = recoupAction;
	}

	public DisputePayoutRecoupRequest getDisputePayoutRecoupRequest() {
		return disputePayoutRecoupRequest;
	}

	public void setDisputePayoutRecoupRequest(DisputePayoutRecoupRequest disputePayoutRecoupRequest) {
		this.disputePayoutRecoupRequest = disputePayoutRecoupRequest;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Currency getMinimumAmountToRecoup() {
		return minimumAmountToRecoup;
	}

	public void setMinimumAmountToRecoup(Currency minimumAmountToRecoup) {
		this.minimumAmountToRecoup = minimumAmountToRecoup;
	}

	public WaiveFeePolicy getWaiveFeePolicy() {
		return waiveFeePolicy;
	}

	public void setWaiveFeePolicy(WaiveFeePolicy waiveFeePolicy) {
		this.waiveFeePolicy = waiveFeePolicy;
	}

	public RecoupCancellationReason getRecoupCancellationReason() {
		return recoupCancellationReason;
	}

	public void setRecoupCancellationReason(RecoupCancellationReason recoupCancellationReason) {
		this.recoupCancellationReason = recoupCancellationReason;
	}

}
