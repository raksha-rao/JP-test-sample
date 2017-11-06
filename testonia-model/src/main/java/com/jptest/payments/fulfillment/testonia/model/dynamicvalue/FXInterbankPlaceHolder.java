package com.jptest.payments.fulfillment.testonia.model.dynamicvalue;

import com.jptest.ffxfx.CurrencyConvertRefundInterbankResponse;
import com.jptest.ffxfx.SerializeFxAuditTrailResponse;
import com.jptest.payments.fulfillment.testonia.model.util.FXDataTrimmer;


/**
 * Placeholder for FX-Interbank related dynamic values e.g. ${FX-INTERBANK:AMOUNT-FROM(USD,1000,CAD,TRUE)}
 * <p>
 * Parameters have below meaning:
 * <li>USD1000 - amount to be converted (txn_amt field in CAL)</li>
 * <li>CAD - currencyOut (sccy and dccy in CAL)</li>
 * <li>TRUE - if conversion should be reversed</li>
 * <p>
 * The values map will contain entries such as:
 * <li>${FX-INTERBANK:AMOUNT-FROM(USD,1000,CAD,TRUE)} : some-value</li>
 * <li>${FX-INTERBANK:AMOUNT-TO(USD,1000,CAD,TRUE)} : some-value</li>
 * <li>${FX-INTERBANK:EXCHANGE-RATE(USD,1000,CAD,TRUE)} : some-value</li>
 * <li>${FX-INTERBANK:AUDIT-TRAIL(USD,1000,CAD,TRUE)} : some-value</li>
 */
public class FXInterbankPlaceHolder extends AbstractFXPlaceHolder {

    // placeholder prefix for FX-Interbank is "${FX-INTERBANK:"
    public static final String FUNCTION_NAME = "FX-INTERBANK";
    public static final String FX_INTERBANK_PLACEHOLDER_PREFIX = PLACEHOLDER_PREFIX + FUNCTION_NAME + FIELD_DELIMITER;

	public static final String FIELD_AMOUNT_FROM = "AMOUNT-FROM";
	public static final String FIELD_AMOUNT_TO = "AMOUNT-TO";
	public static final String FIELD_USD_AMOUNT_FROM = "USD-AMOUNT-FROM";
	public static final String FIELD_USD_AMOUNT_TO = "USD-AMOUNT-TO";
	public static final String FIELD_EXCHANGE_RATE = "EXCHANGE-RATE";
	public static final String FIELD_AUDIT_TRAIL = "AUDIT-TRAIL";

	private String currencyFrom;
	private Long amountFrom;
	private String currencyOut;
	private boolean reverse;
    private String conversionType;

	public FXInterbankPlaceHolder(String placeholder) {
		super(placeholder);

		this.currencyFrom = getParams().get(0).trim();
		this.amountFrom = Long.parseLong(getParams().get(1).trim());
		this.currencyOut = getParams().get(2).trim();
        this.reverse = Boolean.parseBoolean(getParams().get(3).trim());
        this.conversionType = getParams().get(4).trim().toUpperCase();
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

	public boolean isReverse() {
		return reverse;
	}

    public String getFxConversionType() {
        return conversionType;
    }

	@Override
	public String buildKey(String fieldName) {
        return FX_INTERBANK_PLACEHOLDER_PREFIX + fieldName + FUNCTION_PREFIX + getParameters() + FUNCTION_SUFFIX
				+ PLACEHOLDER_SUFFIX;
	}

	private String getParameters() {
        return String.join(PARAMETER_DELIMITER, currencyFrom, String.valueOf(amountFrom), currencyOut,
                String.valueOf(reverse).toUpperCase(), conversionType);
	}

	/**
	 * Set FX related values in map from input response
	 * 
	 * @param response
	 */
    public void setValues(CurrencyConvertRefundInterbankResponse currencyConvertInterbankResponse,
            SerializeFxAuditTrailResponse fxAuditTrailResponse) {
		addKeyValue(FIELD_USD_AMOUNT_FROM,
                String.valueOf(currencyConvertInterbankResponse.getConversionInfoOut().getUsdAmountFrom().getAmount()));
        addKeyValue(FIELD_USD_AMOUNT_TO,
                String.valueOf(currencyConvertInterbankResponse.getConversionInfoOut().getUsdAmountTo().getAmount()));
        addKeyValue(FIELD_AMOUNT_FROM,
                String.valueOf(currencyConvertInterbankResponse.getConversionInfoOut().getAmountFrom().getAmount()));
        addKeyValue(FIELD_AMOUNT_TO,
                String.valueOf(currencyConvertInterbankResponse.getConversionInfoOut().getAmountTo().getAmount()));
		addKeyValue(FIELD_EXCHANGE_RATE,
                FXDataTrimmer.trimExchangeRate(
                        String.valueOf(currencyConvertInterbankResponse.getConversionInfoOut().getExchangeRate())));
		addKeyValue(FIELD_AUDIT_TRAIL, FXDataTrimmer
                .trimAuditTrail(fxAuditTrailResponse.getFxAuditTrail()));
	}

}
