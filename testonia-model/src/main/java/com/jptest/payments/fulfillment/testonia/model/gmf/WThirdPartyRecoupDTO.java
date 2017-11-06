/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.model.gmf;

import java.math.BigInteger;

/**
 * @JP Inc.
 *
 *         Represents wthird_party_recoup table
 *
 */
public class WThirdPartyRecoupDTO {

	private Long id;
	private String payoutTransactionId;
	private BigInteger sellerAccountNumber;
	private BigInteger payoutFundingAccountNumber;
	private BigInteger recoupFundingAccountNumber;
	private String invoiceId;
	private Long recoupScheduledDate;
	private Long recoupAskAmount;
	private String recoupAskAmountCurr;
	private Byte waiveFeePolicy;
	private Long minRecoupAmount;
	private String minRecoupAmountCurr;
	private Long amountRecovered;
	private String amountRecoveredCurr;
	private Byte recoupStatus;
	private Byte recoupRecoveryType;
	private Long recoupEndDate;
	private Byte cancelReasonCode;
	private Long pyplTimeTouched;
	private Long timeCreated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPayoutTransactionId() {
		return payoutTransactionId;
	}

	public void setPayoutTransactionId(String payoutTransactionId) {
		this.payoutTransactionId = payoutTransactionId;
	}

	public BigInteger getSellerAccountNumber() {
		return sellerAccountNumber;
	}

	public void setSellerAccountNumber(BigInteger sellerAccountNumber) {
		this.sellerAccountNumber = sellerAccountNumber;
	}

	public BigInteger getPayoutFundingAccountNumber() {
		return payoutFundingAccountNumber;
	}

	public void setPayoutFundingAccountNumber(BigInteger payoutFundingAccountNumber) {
		this.payoutFundingAccountNumber = payoutFundingAccountNumber;
	}

	public BigInteger getRecoupFundingAccountNumber() {
		return recoupFundingAccountNumber;
	}

	public void setRecoupFundingAccountNumber(BigInteger recoupFundingAccountNumber) {
		this.recoupFundingAccountNumber = recoupFundingAccountNumber;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Long getRecoupScheduledDate() {
		return recoupScheduledDate;
	}

	public void setRecoupScheduledDate(Long recoupScheduledDate) {
		this.recoupScheduledDate = recoupScheduledDate;
	}

	public Long getRecoupAskAmount() {
		return recoupAskAmount;
	}

	public void setRecoupAskAmount(Long recoupAskAmount) {
		this.recoupAskAmount = recoupAskAmount;
	}

	public String getRecoupAskAmountCurr() {
		return recoupAskAmountCurr;
	}

	public void setRecoupAskAmountCurr(String recoupAskAmountCurr) {
		this.recoupAskAmountCurr = recoupAskAmountCurr;
	}

	public Byte getWaiveFeePolicy() {
		return waiveFeePolicy;
	}

	public void setWaiveFeePolicy(Byte waiveFeePolicy) {
		this.waiveFeePolicy = waiveFeePolicy;
	}

	public Long getMinRecoupAmount() {
		return minRecoupAmount;
	}

	public void setMinRecoupAmount(Long minRecoupAmount) {
		this.minRecoupAmount = minRecoupAmount;
	}

	public String getMinRecoupAmountCurr() {
		return minRecoupAmountCurr;
	}

	public void setMinRecoupAmountCurr(String minRecoupAmountCurr) {
		this.minRecoupAmountCurr = minRecoupAmountCurr;
	}

	public Long getAmountRecovered() {
		return amountRecovered;
	}

	public void setAmountRecovered(Long amountRecovered) {
		this.amountRecovered = amountRecovered;
	}

	public String getAmountRecoveredCurr() {
		return amountRecoveredCurr;
	}

	public void setAmountRecoveredCurr(String amountRecoveredCurr) {
		this.amountRecoveredCurr = amountRecoveredCurr;
	}

	public Byte getRecoupStatus() {
		return recoupStatus;
	}

	public void setRecoupStatus(Byte recoupStatus) {
		this.recoupStatus = recoupStatus;
	}

	public Byte getRecoupRecoveryType() {
		return recoupRecoveryType;
	}

	public void setRecoupRecoveryType(Byte recoupRecoveryType) {
		this.recoupRecoveryType = recoupRecoveryType;
	}

	public Long getRecoupEndDate() {
		return recoupEndDate;
	}

	public void setRecoupEndDate(Long recoupEndDate) {
		this.recoupEndDate = recoupEndDate;
	}

	public Byte getCancelReasonCode() {
		return cancelReasonCode;
	}

	public void setCancelReasonCode(Byte cancelReasonCode) {
		this.cancelReasonCode = cancelReasonCode;
	}

	public Long getPyplTimeTouched() {
		return pyplTimeTouched;
	}

	public void setPyplTimeTouched(Long pyplTimeTouched) {
		this.pyplTimeTouched = pyplTimeTouched;
	}

	public Long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Long timeCreated) {
		this.timeCreated = timeCreated;
	}

}
