package com.jptest.payments.fulfillment.testonia.model.dynamicvalue;

//import com.jptest.ffxfee.CalculateFeeAmountFromResponse;
/**
 * Placeholder for Fee related dynamic values e.g.
 * ${FEE:AUDIT-TRAIL(CAD,1000,CA,USD,US,RECEIVER,ECHECK)}
 * 
 * <p>
 * Parameters have below meaning:
 * <li>CAD1000 - amount used to calculate fee (txn_amt field in CAL)</li>
 * <li>CA - sender-country (s_cntry field in CAL)</li>
 * <li>USD - currencyOut (vol field in CAL)</li>
 * <li>US - recipient country (cntry field in CAL)</li>
 * <li>RECEIVER - fee type (fee_type field in CAL). Other possible value is P2P
 * (when sender pays fee)</li>
 * <li>ECHECK - funding source (fsrc field in CAL). Other possible values are
 * BALANCE, IACH, MEFT</li>
 * 
 * <p>
 * The values map will contain entries such as:
 * <li>${FEE:ACTUAL-FIXED-FEE(CAD,1000,CA,USD,US,P2P,ECHECK)} : some-value</li>
 * <li>${FEE:ACTUAL-PERCENT-FEE(CAD,1000,CA,USD,US,P2P,ECHECK)} :
 * some-value</li>
 * <li>${FEE:TOTAL-FEE-AMOUNT(CAD,1000,CA,USD,US,P2P,ECHECK)} : some-value</li>
 * <li>${FEE:AUDIT-TRAIL(CAD,1000,CA,USD,US,P2P,ECHECK)} : some-value</li>
 */
public class FeePlaceHolder extends PlaceHolder {

	// placeholder prefix is "${FEE:"
	public static final String FUNCTION_NAME = "FEE";
	public static final String FEE_PLACEHOLDER_PREFIX = PLACEHOLDER_PREFIX + FUNCTION_NAME + FIELD_DELIMITER;

	public static final String FIELD_ACTUAL_FIXED_FEE = "ACTUAL-FIXED-FEE";
	public static final String FIELD_ACTUAL_PERCENT_FEE = "ACTUAL-PERCENT-FEE";
	public static final String FIELD_TOTAL_FEE_AMOUNT = "TOTAL-FEE-AMOUNT";
	public static final String FIELD_TOTAL_FEE_USD_AMOUNT = "TOTAL-FEE-USD-AMOUNT";
	public static final String FIELD_AUDIT_TRAIL = "AUDIT-TRAIL";
    public static final String FIELD_WSUBBALANCE_AMOUNT = "WSUBBALANCE-AMOUNT";

    public static final String SENDER_FEE_TYPE = "P2P";
    public static final String RECIPIENT_FEE_TYPE = "RECEIVER";

	private String currencyFrom;
	private Long amountFrom;
	private String senderCountry;
	private String currencyOut;
	private String country;
	private String type;
	private String fundingSource;
    private String chargeBackGrade;

	public FeePlaceHolder(String placeholder) {
		super(placeholder);
		this.currencyFrom = getParams().get(0).trim();
		this.amountFrom = Long.parseLong(getParams().get(1).trim());
		this.senderCountry = getParams().get(2).trim();
		this.currencyOut = getParams().get(3).trim();
		this.country = getParams().get(4).trim();
		this.type = getParams().get(5).trim().toUpperCase();
		this.fundingSource = getParams().get(6).trim().toUpperCase();
        // set optional chargeback grade if specified
        if (getParams().size() > 7) {
            this.chargeBackGrade = getParams().get(7).trim();
        }
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public Long getAmountFrom() {
		return amountFrom;
	}

	public String getCurrencyOut() {
		return currencyOut;
	}

	public String getCountry() {
		return country;
	}

	public String getSenderCountry() {
		return senderCountry;
	}

	public String getFeeType() {
		return type;
	}

    public boolean isSenderSideFee() {
        return SENDER_FEE_TYPE.equals(type);
    }

	public String getFundingSource() {
		return fundingSource;
	}

    public String getChargeBackGrade() {
        return chargeBackGrade;
    }

	@Override
	public String buildKey(String fieldName) {
		return FEE_PLACEHOLDER_PREFIX + fieldName + FUNCTION_PREFIX + getParameters() + FUNCTION_SUFFIX
				+ PLACEHOLDER_SUFFIX;
	}

	private String getParameters() {
        if (chargeBackGrade == null) {
            return String.join(PARAMETER_DELIMITER, currencyFrom, String.valueOf(amountFrom), senderCountry,
                    currencyOut,
                    country, type, fundingSource);
        }
        else {
            return String.join(PARAMETER_DELIMITER, currencyFrom, String.valueOf(amountFrom), senderCountry,
                    currencyOut,
                    country, type, fundingSource, chargeBackGrade);
        }
	}

	/**
	 * Set Fee related values in map from input response
	 * 
	 * @param response
	 */
	/*public void setValues(CalculateFeeAmountFromResponse response, long usdAmount) {
		addKeyValue(FIELD_ACTUAL_FIXED_FEE,
				String.valueOf(response.getAuditTrail().getFeeItems().get(0).getActualFixedFee().getAmount()));
		addKeyValue(FIELD_ACTUAL_PERCENT_FEE,
				String.valueOf(response.getAuditTrail().getFeeItems().get(0).getActualPercentFee()));
		addKeyValue(FIELD_TOTAL_FEE_AMOUNT, String.valueOf(response.getFeeAmount().getAmount()));
		addKeyValue(FIELD_AUDIT_TRAIL,
				FXDataTrimmer.trimFeeAuditTrail(response.getAuditTrail().getSerializedAuditTrail()));
		addKeyValue(FIELD_TOTAL_FEE_USD_AMOUNT, String.valueOf(usdAmount));
        addKeyValue(FIELD_WSUBBALANCE_AMOUNT, String.valueOf(amountFrom - response.getFeeAmount().getAmount()));
	}*/

}
