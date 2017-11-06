package com.jptest.payments.fulfillment.testonia.model;

/**
@JP Inc.
 */
public class ValidationsInput {

	private String ledgerType;
	private String flags;
	private String paymentStatus;
	private String postPaymentStatus;
	private String buyerExpectedBalance;
	private String sellerExpectedBalance;
	private boolean buyerBalanceCheck = false;
	private boolean sellerBalanceCheck = false;

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPostPaymentStatus() {
		return postPaymentStatus;
	}

	public void setPostPaymentStatus(String postPaymentStatus) {
		this.postPaymentStatus = postPaymentStatus;
	}

	public String getBuyerExpectedBalance() {
		return buyerExpectedBalance;
	}

	public void setBuyerExpectedBalance(String buyerExpectedBalance) {
		this.buyerExpectedBalance = buyerExpectedBalance;
	}

	public String getSellerExpectedBalance() {
		return sellerExpectedBalance;
	}

	public void setSellerExpectedBalance(String sellerExpectedBalance) {
		this.sellerExpectedBalance = sellerExpectedBalance;
	}

	public String getLedgerType() {
		return ledgerType;
	}

	public void setLedgerType(String ledgerType) {
		this.ledgerType = ledgerType;
	}

	public boolean isBuyerBalanceCheck() {
		return buyerBalanceCheck;
	}

	public void setBuyerBalanceCheck(boolean buyerBalanceCheck) {
		this.buyerBalanceCheck = buyerBalanceCheck;
	}

	public boolean isSellerBalanceCheck() {
		return sellerBalanceCheck;
	}

	public void setSellerBalanceCheck(boolean sellerBalanceCheck) {
		this.sellerBalanceCheck = sellerBalanceCheck;
	}
}
