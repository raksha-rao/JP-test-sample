package com.jptest.payments.fulfillment.testonia.model.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @see //FXDataTrimmerTest
 */
public class FXDataTrimmer {

	private static final int FEE_AUDIT_TRAIL_SUBSTRING_LENGTH = 33;

	/**
	 * Reduces precision of exchange rate in audit trail
	 * <p>
	 * e.g. converts
	 * "V:1:USD:1609:EUR:1000:0.62162125612600000000;C:0:M:USD;D:8402:0.6408466558:0.6413000000:0.6416847800;"
	 * to
	 * "V:1:USD:1609:EUR:1000:0.6216212561...;C:0:M:USD;D:8402:0.6408466558:0.6413000000:0.6416847800;"
	 * 
	 * @param inputAuditTrail
	 * @return
	 */
	public static String trimAuditTrail(String inputAuditTrail) {
		if (StringUtils.isNotBlank(inputAuditTrail)) {

			int firstSemicolonIndex = inputAuditTrail.indexOf(";");
			if (firstSemicolonIndex > 0) {
				int lastColonIndex = inputAuditTrail.substring(0, firstSemicolonIndex).lastIndexOf(":");
				if (lastColonIndex > -1 && lastColonIndex + 1 < firstSemicolonIndex) {
					String exchangeRateSubstring = inputAuditTrail.substring(lastColonIndex + 1, firstSemicolonIndex);
					String trimmedExchangeRate = trimExchangeRate(exchangeRateSubstring);
					inputAuditTrail = inputAuditTrail.replace(":" + exchangeRateSubstring + ";",
							":" + trimmedExchangeRate + ";");
				}

			}
		}
		return inputAuditTrail;
	}

	/**
	 * Truncates exchange rate to 10 digits after decimal e.g. converts
	 * "0.123456789012" to "0.1234567890"
	 * 
	 * @param //input
	 * @return
	 */
	public static String trimExchangeRate(String exchangeRateString) {
		return String.format("%.10f", new BigDecimal(exchangeRateString));
	}

	/**
	 * Reduces audit trail to first "FEE_AUDIT_TRAIL_SUBSTRING_LENGTH"
	 * characters
	 * <p>
	 * e.g. converts
	 * "V:6:USD:10000;B:10285:320:0.02900:30::30000:30:0.02900::::::0:::::::;max:15695:30000:;"
	 * to "V:6:USD:10000;B:10285:320:0.02900..."
	 * 
	 * @param //inputAuditTrail
	 * @return
	 */
	public static String trimFeeAuditTrail(String inputFeeAuditTrail) {
		if (StringUtils.isNotBlank(inputFeeAuditTrail)
				&& inputFeeAuditTrail.length() > FEE_AUDIT_TRAIL_SUBSTRING_LENGTH) {
			inputFeeAuditTrail = inputFeeAuditTrail.substring(0, FEE_AUDIT_TRAIL_SUBSTRING_LENGTH) + "...";
		}
		return inputFeeAuditTrail;
	}

}
