package com.jptest.payments.fulfillment.testonia.model;

//import static com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions.CurrencyData;

public class IncentiveDetails {
	
	private UserCreationTaskInput funder;
	//private CurrencyData incentiveAmount;
	private String type;
	
	/*
	 * Subtype for the incentive, e.g. ExternalIncentiveType values in case of external incentives, etc.
	 */
	private char subtype;
	private String externalIncentiveRefID;
	
	public static final String EXTERNAL_INCENTIVE_TYPE = "external";
	public static final String PRE_FUNDED_MSB_TYPE = "prefundedmsb";
	public static final String POST_FUNDED_MSB_TYPE = "postfundedmsb";
	public static final String PRE_FUNDED_PSB_TYPE = "prefundedpsb";
	public static final String POST_FUNDED_PSB_TYPE = "postfundedpsb";
	public static final String MPSB_TYPE = "mpsb";

	public UserCreationTaskInput getFunder() {
		return funder;
	}
	
	public void setFunder(UserCreationTaskInput funder) {
		this.funder = funder;
	}
	
	/*public CurrencyData getIncentiveAmount() {
		return incentiveAmount;
	}
	
	public void setIncentiveAmount(CurrencyData incentiveAmount) {
		this.incentiveAmount = incentiveAmount;
	}*/

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public char getSubtype() {
		return subtype;
	}

	public void setSubtype(char subtype) {
		this.subtype = subtype;
	}

	public String getExternalIncentiveRefID() {
		return externalIncentiveRefID;
	}

	public void setExternalIncentiveRefID(String externalIncentiveRefID) {
		this.externalIncentiveRefID = externalIncentiveRefID;
	}
}
