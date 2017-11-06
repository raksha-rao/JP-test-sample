package com.jptest.payments.fulfillment.testonia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jptest.payments.fulfillment.testonia.model.group.PaymentFlowType;
import com.jptest.payments.fulfillment.testonia.model.group.PaymentSubtype;

import java.util.List;

//import com.jptest.money.FulfillmentType;
//import com.jptest.money.FundingMethodType;
//import com.jptest.types.Currency;

/**
 * Part of the test case input data that represents the payment related
 * information used in the test case. This is used within
 * {@link TestCaseInputData} to represent entire input data for the test case.
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FulfillPaymentPlanOptions {

//	private FulfillmentType fulfillmentType;

    private PaymentFlowType paymentFlowType;
    
    private PaymentSubtype paymentSubtype;

	private CurrencyData txnAmount;

	private String disbursementCurrencyCode; // This is used for SinglePartyTransfer Liftoff Goal to Goal transfer

	private CurrencyData ledgerAmount;

	private CurrencyData holdingAmount;

	private String memo;

	private String fundingSource;

	private boolean useBalance;

	//private FundingMethodType backupFundingType;

	private boolean forceUserSpecifiedFundingInstrument;

	private String actionId;

    private boolean unilateral;

	private String idempotencyToken;

    private boolean s2F;
	private boolean allowNegativeBalance = false;
	private String ledger;
	private String ledgerType;

	private String contingency;

	/**
	 * incentives details to create incentive for the funder account
	 * type, amount and currency code
	 */
	private List<IncentiveDetails> incentives;

	/**
	 * if true, the txn amount will be disbursed to the mpsb funding instrument
	 */
	private boolean mpsbFundingSink;

	/**
	 * if true, mpsb will be used to fund the txn
	 */
	private boolean mpsbFundingSource;

    private boolean isMobile;
    
    private AuthOptions authOptions;

	/**
	 * GSP (Multi-Leg Payment)
	 */
	private boolean delayPostProcessing;

	/**
	 * Risk reserve during payment
	 */
	private boolean reserveDuringPayment;

	/**
	 * MLPFinalizeActionType
	 */
	private int mlpAction;

	public boolean isUseBalance() {
		return this.useBalance;
	}

	/*public FulfillmentType getFulfillmentType() {
		return this.fulfillmentType;
	}*/

	public PaymentFlowType getPaymentFlowType() {
		return this.paymentFlowType;
	}

	public CurrencyData getTxnAmount() {
		return this.txnAmount;
	}

    public String getDisbursementCurrencyCode() {
        return disbursementCurrencyCode;
    }

	public CurrencyData getLedgerAmount() {
		return this.ledgerAmount;
	}

	public CurrencyData getHoldingAmount() {
		return this.holdingAmount;
	}

	public String getMemo() {
		return this.memo;
	}

	public String getFundingSource() {
		return this.fundingSource;
	}

	/*	public FundingMethodType getBackupFundingType() {
		return this.backupFundingType;
	}
	*/
	public boolean isForceUserSpecifiedFundingInstrument() {
		return this.forceUserSpecifiedFundingInstrument;
	}

	public String getActionId() {
		return this.actionId;
	}
	
    public AuthOptions getAuthOptions() {
        return authOptions;
    }

	public static class CurrencyData {
		private String currencyCode;
		private String amount;

		private CurrencyData() {
			super();
		}

		public String getCurrencyCode() {
			return this.currencyCode;
		}

		public void setCurrencyCode(final String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public void setAmount(final String amount) {
			this.amount = amount;
		}

		public String getAmount() {
			return this.amount;
		}

		/*public Currency getCurrency() {
			return new Currency(currencyCode, Long.parseLong(amount));
		}*/
	}

    /**
     * Authorization related input data
     * 
     */
    public static class AuthOptions {

        private CurrencyData authAmount;
        private boolean avoidUsingBalance;
        private String memo;

        private AuthOptions() {
            super();
        }

        public CurrencyData getAuthAmount() {
            return authAmount;
        }

        public void setAuthAmount(CurrencyData authAmount) {
            this.authAmount = authAmount;
        }

        public boolean avoidUsingBalance() {
            return avoidUsingBalance;
        }

        public void setAvoidUsingBalance(boolean avoidUsingBalance) {
            this.avoidUsingBalance = avoidUsingBalance;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

    }

	public boolean isUnilateral() {
        return this.unilateral;
	}

    public void setUnilateral(final boolean unilateral) {
        this.unilateral = unilateral;
	}

    /**
     * Fix for some JSON which uses "isUnilateral" property name
     * 
     * @param unilateral
     */
    public void setIsUnilateral(final boolean unilateral) {
        this.unilateral = unilateral;
    }

	public String getIdempotencyToken() {
		return this.idempotencyToken;
	}

	public void setIdempotencyToken(String idempotencyToken) {
		this.idempotencyToken = idempotencyToken;
	}

    public void setS2F(boolean s2F) {
        this.s2F = s2F;
	}

	public boolean isS2F() {
        return this.s2F;
	}

	public boolean isAllowNegativeBalance() {
		return allowNegativeBalance;
	}

	public void setAllowNegativeBalance(boolean allowNegativeBalance) {
		this.allowNegativeBalance = allowNegativeBalance;
	}

	public CurrencyData setHoldingAmount() {
		return this.holdingAmount;
	}

	public String getLedger() {
		return ledger;
	}

	public void setLedger(String ledger) {
		this.ledger = ledger;
	}

	public String getLedgerType() {
		return ledgerType;
	}

	public void setLedgerType(String ledgerType) {
		this.ledgerType = ledgerType;
	}

	public PaymentSubtype getPaymentSubtype() {
		return paymentSubtype;
	}

	public void setPaymentSubtype(PaymentSubtype paymentSubtype) {
		this.paymentSubtype = paymentSubtype;
	}

	public String getContingency() {
		return contingency;
	}

	public void setContingency(String contingency) {
		this.contingency = contingency;
	}

	public List<IncentiveDetails> getIncentives() {
		return incentives;
	}

	public void setIncentives(List<IncentiveDetails> incentives) {
		this.incentives = incentives;
	}

	public boolean isMpsbFundingSink() {
		return mpsbFundingSink;
	}

	public void setMpsbFundingSink(boolean mpsbFundingSink) {
		this.mpsbFundingSink = mpsbFundingSink;
	}

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean isMobile) {
        this.isMobile = isMobile;
    }

	public boolean isMpsbFundingSource() {
		return mpsbFundingSource;
	}

	public void setMpsbFundingSource(boolean mpsbFundingSource) {
		this.mpsbFundingSource = mpsbFundingSource;
	}

	public boolean isDelayPostProcessing() {
		return delayPostProcessing;
	}

	public void setDelayPostProcessing(boolean delayPostProcessing) {
		this.delayPostProcessing = delayPostProcessing;
	}

	public boolean isReserveDuringPayment() {
		return reserveDuringPayment;
	}

	public void setReserveDuringPayment(boolean reserveDuringPayment) {
		this.reserveDuringPayment = reserveDuringPayment;
	}

	public int getMlpAction() {
		return mlpAction;
	}

	public void setMlpAction(int mlpAction) {
		this.mlpAction = mlpAction;
	}

}
