package com.jptest.payments.fulfillment.testonia.model.dynamicvalue;

//import com.jptest.ffxfx.CurrencyConvertCustomerResponse;
/**
 * Placeholder for FX related dynamic values e.g.
 * ${FX:AMOUNT-FROM(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}
 * 
 * <p>
 * Parameters have below meaning:
 * <li>USD1000 - amount to be converted (txn_amt field in CAL)</li>
 * <li>CAD - currencyOut (sccy and dccy in CAL)</li>
 * <li>CA - country for currencyOut (cntry in CAL)</li>
 * <li>TRUE - if conversion should be reversed</li>
 * <li>CONVERT_SEND - if conversion should happen on sender or recipient side
 * (fxtype field in CAL). Other possible value is CONVERT_RECEIVE</li>
 * 
 * <p>
 * The values map will contain entries such as:
 * <li>${FX:AMOUNT-FROM(USD,1000,CAD,CA,TRUE,CONVERT_SEND)} : some-value</li>
 * <li>${FX:AMOUNT-TO(USD,1000,CAD,CA,TRUE,CONVERT_SEND)} : some-value</li>
 * <li>${FX:EXCHANGE-RATE(USD,1000,CAD,CA,TRUE,CONVERT_SEND)} : some-value</li>
 * <li>${FX:AUDIT-TRAIL(USD,1000,CAD,CA,TRUE,CONVERT_SEND)} : some-value</li>
 */
public class FXPlaceHolder extends AbstractFXPlaceHolder {

	// placeholder prefix for FX is "${FX:"
	public static final String FUNCTION_NAME = "FX";
	public static final String FX_PLACEHOLDER_PREFIX = PLACEHOLDER_PREFIX + FUNCTION_NAME + FIELD_DELIMITER;

	public static final String FIELD_AMOUNT_FROM = "AMOUNT-FROM";
	public static final String FIELD_AMOUNT_TO = "AMOUNT-TO";
	public static final String FIELD_USD_AMOUNT_FROM = "USD-AMOUNT-FROM";
	public static final String FIELD_USD_AMOUNT_TO = "USD-AMOUNT-TO";
	public static final String FIELD_EXCHANGE_RATE = "EXCHANGE-RATE";
	public static final String FIELD_AUDIT_TRAIL = "AUDIT-TRAIL";
	public static final String FIELD_FEE_AUDIT_TRAIL = "FEE-AUDIT-TRAIL";
	public static final String FIELD_ACTUAL_FIXED_FEE = "ACTUAL-FIXED-FEE";
	public static final String FIELD_ACTUAL_PERCENT_FEE = "ACTUAL-PERCENT-FEE";
	public static final String FIELD_TOTAL_FEE_AMOUNT = "TOTAL-FEE-AMOUNT";

	private String currencyFrom;
	private Long amountFrom;
	private String currencyOut;
	private boolean reverse;
	private String country;
	private String conversionType;

	public FXPlaceHolder(String placeholder) {
		super(placeholder);

		this.currencyFrom = getParams().get(0).trim();
		this.amountFrom = Long.parseLong(getParams().get(1).trim());
		this.currencyOut = getParams().get(2).trim();
		this.country = getParams().get(3).trim();
		this.reverse = Boolean.parseBoolean(getParams().get(4).trim());
		this.conversionType = getParams().get(5).trim().toUpperCase();
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

	public boolean isReverse() {
		return reverse;
	}

	public String getFxConversionType() {
		return conversionType;
	}

	@Override
	public String buildKey(String fieldName) {
		return FX_PLACEHOLDER_PREFIX + fieldName + FUNCTION_PREFIX + getParameters() + FUNCTION_SUFFIX
				+ PLACEHOLDER_SUFFIX;
	}

	private String getParameters() {
		return String.join(PARAMETER_DELIMITER, currencyFrom, String.valueOf(amountFrom), currencyOut, country,
				String.valueOf(reverse).toUpperCase(), conversionType);
	}

	/**
	 * Set FX related values in map from input response
	 * 
	 * @param response
	 */
	/*public void setValues(CurrencyConvertCustomerResponse response) {
		addKeyValue(FIELD_USD_AMOUNT_FROM,
				String.valueOf(response.getConversionInfoOut().getUsdAmountFrom().getAmount()));
		addKeyValue(FIELD_USD_AMOUNT_TO, String.valueOf(response.getConversionInfoOut().getUsdAmountTo().getAmount()));
		addKeyValue(FIELD_AMOUNT_FROM, String.valueOf(response.getConversionInfoOut().getAmountFrom().getAmount()));
		addKeyValue(FIELD_AMOUNT_TO, String.valueOf(response.getConversionInfoOut().getAmountTo().getAmount()));
		addKeyValue(FIELD_EXCHANGE_RATE,
				FXDataTrimmer.trimExchangeRate(String.valueOf(response.getConversionInfoOut().getExchangeRate())));
		addKeyValue(FIELD_AUDIT_TRAIL, FXDataTrimmer
				.trimAuditTrail(response.getConversionInfoOut().getFxAuditTrail().getSerializedAuditTrail()));
		addKeyValue(FIELD_FEE_AUDIT_TRAIL, FXDataTrimmer.trimFeeAuditTrail(response.getConversionInfoOut()
				.getFxAuditTrail().getConversionDetails().getFeeAuditTrail().getSerializedAuditTrail()));
		addKeyValue(FIELD_ACTUAL_FIXED_FEE, String.valueOf(response.getConversionInfoOut().getFxAuditTrail()
				.getConversionDetails().getFeeAuditTrail().getFeeItems().get(0).getActualFixedFee().getAmount()));
		addKeyValue(FIELD_ACTUAL_PERCENT_FEE, String.valueOf(response.getConversionInfoOut().getFxAuditTrail()
				.getConversionDetails().getFeeAuditTrail().getFeeItems().get(0).getActualPercentFee()));
		addKeyValue(FIELD_TOTAL_FEE_AMOUNT, String.valueOf(response.getConversionInfoOut().getFxAuditTrail()
				.getConversionDetails().getFeeAuditTrail().getFeeItems().get(0).getTotalFee().getAmount()));
	}*/

}
