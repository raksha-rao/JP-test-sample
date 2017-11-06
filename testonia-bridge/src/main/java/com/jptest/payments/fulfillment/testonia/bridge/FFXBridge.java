package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;
import java.math.BigInteger;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableMap;
import com.google.inject.name.Named;
/*import com.jptest.fee.FeePaymentInfoVO;
import com.jptest.ffxfee.CalculateFeeAmountFromRequest;
import com.jptest.ffxfee.CalculateFeeAmountFromResponse;
import com.jptest.ffxfee.ffxFee;
import com.jptest.ffxfx.CurrencyConvertCustomerRequest;
import com.jptest.ffxfx.CurrencyConvertCustomerResponse;
import com.jptest.ffxfx.CurrencyConvertInterbankRequest;
import com.jptest.ffxfx.CurrencyConvertInterbankResponse;
import com.jptest.ffxfx.CurrencyConvertRefundCustomerRequest;
import com.jptest.ffxfx.CurrencyConvertRefundCustomerResponse;
import com.jptest.ffxfx.CurrencyConvertRefundInterbankRequest;
import com.jptest.ffxfx.CurrencyConvertRefundInterbankResponse;
import com.jptest.ffxfx.SerializeFxAuditTrailRequest;
import com.jptest.ffxfx.SerializeFxAuditTrailResponse;
import com.jptest.ffxfx.ffxFX;
import com.jptest.fx.FXAuditTrailVO;
import com.jptest.types.Currency;*/

/**
 * Represents a single point for all external calls that relate to operations
 * for generating FI from Testonia.
 */

@Singleton
public class FFXBridge {

	private static final Logger LOGGER = LoggerFactory.getLogger(FFXBridge.class);

	private static final String FX_FEE_TYPE = "fx";

	// Number representing recipient category (used by FFX)
	private static final String RECIPIENT_CATEGORY = "655360";

	private static final Map<String, Double> currencyConversionMap = ImmutableMap.<String, Double>builder()
			.put("AED", 3.6725).put("AFN", 68.220001).put("ALL", 119.800003).put("AMD", 481.200012).put("ANG", 1.79)
			.put("AOA", 165.089996).put("ARS", 16.139999).put("AUD", 1.3396).put("AWG", 1.79).put("AZN", 1.7015)
			.put("BAM", 1.752).put("BBD", 2.0).put("BDT", 80.669998).put("BGN", 1.7505).put("BHD", 0.3764)
			.put("BIF", 1706.650024).put("BMD", 1.0).put("BND", 1.385).put("BOB", 6.89).put("BRL", 3.257)
			.put("BRX", 3.2653).put("BSD", 1.0).put("BTN", 64.449997).put("BWP", 10.2831).put("BYN", 1.86)
			.put("BYR", 20020.0).put("BZD", 1.9977).put("CAD", 1.34684).put("CDF", 1389.0).put("CHF", 0.97529)
			.put("CLF", 0.02509).put("CLP", 674.5).put("CNH", 6.8239).put("CNY", 6.8531).put("COP", 2917.0)
			.put("CRC", 570.320007).put("CUP", 1.0).put("CVE", 98.639999).put("CYP", 0.51955).put("CZK", 23.6772)
			.put("DEM", 1.71745).put("DJF", 177.899994).put("DKK", 6.6526).put("DOP", 47.360001).put("DZD", 108.378998)
			.put("ECS", 25000.0).put("EGP", 18.049999).put("ERN", 15.29).put("ETB", 22.9).put("EUR", 0.8942)
			.put("FJD", 2.085).put("FKP", 0.7772).put("FRF", 1700.272217).put("GBP", 0.7807).put("GEL", 2.4174)
			.put("GHS", 4.2885).put("GIP", 0.7775).put("GMD", 44.900002).put("GNF", 9020.0).put("GTQ", 7.348)
			.put("GYD", 204.190002).put("HKD", 7.7926).put("HNL", 23.450001).put("HRK", 6.5992).put("HTG", 66.559998)
			.put("HUF", 275.299988).put("HUX", 276.519989).put("IDR", 13321.0).put("IEP", 0.699154).put("ILS", 3.5395)
			.put("INR", 64.599998).put("IQD", 1181.0).put("IRR", 32452.0).put("ISK", 99.800003).put("ITL", 1700.272217)
			.put("JMD", 129.139999).put("JOD", 0.7087).put("JPY", 110.856003).put("KES", 103.160004)
			.put("KGS", 68.024002).put("KHR", 4045.0).put("KMF", 439.649994).put("KPW", 900.0).put("KRW", 1123.030029)
			.put("KWD", 0.3033).put("KYD", 0.82).put("KZT", 311.779999).put("LAK", 8189.0).put("LBP", 1509.0)
			.put("LKR", 152.75).put("LRD", 92.0).put("LSL", 13.12).put("LTL", 3.0487).put("LVL", 0.62055)
			.put("LYD", 1.3965).put("MAD", 9.7632).put("MDL", 18.125).put("MGA", 3085.0).put("MKD", 54.779999)
			.put("MMK", 1366.0).put("MNT", 2393.0).put("MOP", 8.0261).put("MRO", 358.0).put("MUR", 34.700001)
			.put("MVR", 15.56).put("MWK", 710.0).put("MXN", 18.7188).put("MXV", 3.232533).put("MYR", 4.281)
			.put("MZN", 58.0).put("NAD", 13.13).put("NGN", 324.0).put("NIO", 29.5).put("NOK", 8.44585)
			.put("NPR", 102.800003).put("NZD", 1.4089).put("OMR", 0.3845).put("PAB", 1.0).put("PEN", 3.2767)
			.put("PGK", 3.283).put("PHP", 49.82).put("PKR", 104.660004).put("PLN", 3.734).put("PYG", 5574.0)
			.put("QAR", 3.6411).put("RON", 4.0879).put("RSD", 109.950302).put("RUB", 56.43).put("RWF", 819.669983)
			.put("SAR", 3.7496).put("SBD", 7.9026).put("SCR", 13.52).put("SDG", 6.6598).put("SEK", 8.72968)
			.put("SGD", 1.38576).put("SHP", 0.7775).put("SIT", 216.486755).put("SLL", 7500.0).put("SOS", 549.0)
			.put("SRD", 7.46).put("STD", 21918.09961).put("SVC", 8.7222).put("SYP", 514.97998).put("SZL", 13.13)
			.put("THB", 34.099998).put("TJS", 8.8195).put("TMT", 3.41).put("TND", 2.3995).put("TOP", 2.2927)
			.put("TRY", 3.5502).put("TTD", 6.7085).put("TWD", 30.195999).put("TZS", 2229.0).put("UAH", 26.27)
			.put("UGX", 3594.0).put("UYU", 28.209999).put("UZS", 3830.0).put("VEF", 9.975).put("VND", 22699.0)
			.put("VUV", 107.129997).put("WST", 2.5662).put("XAF", 586.26001).put("XCD", 2.7).put("XDR", 0.724326)
			.put("XOF", 588.190002).put("XPF", 106.160004).put("YER", 249.899994).put("ZAR", 13.1252).put("ZMW", 9.21)
			.put("ZWL", 322.355011).put("USD", 1.0).build();

	@Inject
	@Named("ffxserv_ca")
	private ffxFX ffxFXService;

	@Inject
	@Named("ffxserv_ca")
	private ffxFee ffxFeeService;

	public Currency getEquivalent(final String currencyCode, final long amount, final String newCurrencyCode)
			throws RuntimeException {
		if (!currencyConversionMap.containsKey(newCurrencyCode)) {
			throw new IllegalArgumentException("New Curency code " + newCurrencyCode + " is not supported");
		} else if (!currencyConversionMap.containsKey(currencyCode)) {
			throw new IllegalArgumentException(" Curency code " + currencyCode + " is not supported");
		}
		Double oldCurrencyRate = currencyConversionMap.get(currencyCode);
		Double newcurrencyRate = currencyConversionMap.get(newCurrencyCode);
		return new Currency(newCurrencyCode, (long) ((newcurrencyRate * amount) / oldCurrencyRate));
	}

	public Currency getUSDEquivalent(final String currencyCode, final long amount) throws RuntimeException {
		return getEquivalent(currencyCode, amount, "USD");
	}

	/**
	 * Returns USD equivalent by making currencyConvertInterbank() call.
	 * 
	 * @param currencyCode
	 * @param amount
	 * @return
	 */
	public CurrencyConvertInterbankResponse getUsdEquivalent(final String currencyCode, final long amount,
			boolean isReverse) {
		return currencyConvertInterbank(currencyCode, amount,
				Currency.Info.US_Dollar.getIsoCurrency().getCurrencyCode(), isReverse);
	}

	/**
	 * Calls currency_convert_interbank() for input data
	 * 
	 * @param fromCurrencyCode
	 * @param fromAmount
	 * @param toCurrencyCode
	 * @param reverse
	 * @return
	 */
	public CurrencyConvertInterbankResponse currencyConvertInterbank(final String fromCurrencyCode,
			final long fromAmount, String toCurrencyCode, boolean reverse) {
		CurrencyConvertInterbankRequest request = new CurrencyConvertInterbankRequest();
		Currency sourceCurrency = new Currency(fromCurrencyCode, fromAmount);
		request.setAmountIn(sourceCurrency);
		request.setCodeOut(toCurrencyCode);
		request.setIsReverse(reverse);
		LOGGER.info("currency_convert_interbank request: {}", printValueObject(request));
		CurrencyConvertInterbankResponse response = ffxFXService.currency_convert_interbank(request);
		LOGGER.info("currency_convert_interbank response: {}", printValueObject(response));
		return response;
	}

	/**
	 * Returns serialized version of FX audit trail for input VO
	 * 
	 * @param auditTrailVO
	 * @return
	 */
	public SerializeFxAuditTrailResponse serializeFxAuditTrail(FXAuditTrailVO auditTrailVO) {
		SerializeFxAuditTrailRequest request = new SerializeFxAuditTrailRequest();
		request.setVo(auditTrailVO);
		LOGGER.info("serialize_fx_audit_trail request: {}", printValueObject(request));
		SerializeFxAuditTrailResponse response = ffxFXService.serialize_fx_audit_trail(request);
		LOGGER.info("serialize_fx_audit_trail response: {}", printValueObject(response));
		return response;
	}

	/**
	 * Calls currency_convert_customer() for input data
	 * 
	 * <li>currencyIn + fromAmount = txn_amt in CAL</li>
	 * <li>currencyOut = sccy and dccy in CAL</li>
	 * <li>country = cntry in CAL</li>
	 * <li>reverse = whether conversion should be reversed</li>
	 * <li>conversionType = fxtype in CAL</li>
	 * 
	 * @param currencyIn
	 * @param fromAmount
	 * @param currencyOut
	 * @param country
	 * @param reverse
	 * @param conversionType
	 * @return
	 */
	public CurrencyConvertCustomerResponse currencyConvertCustomer(final String currencyIn, final long fromAmount,
			String currencyOut, String country, boolean reverse, String conversionType) {
		CurrencyConvertCustomerRequest request = new CurrencyConvertCustomerRequest();
		Currency sourceCurrency = new Currency(currencyIn, fromAmount);
		request.setAmountIn(sourceCurrency);
		request.setCodeOut(currencyOut);
		request.setIsReverse(reverse);

		FeePaymentInfoVO feePaymentInfo = new FeePaymentInfoVO();
		feePaymentInfo.setRecipientCountry(country);
		feePaymentInfo.setSenderCountry(country);
		feePaymentInfo.setAllowAuditTrailSerialize(true);
		feePaymentInfo.setRecipientTransAmount(sourceCurrency);
		feePaymentInfo.setSourceCurrency(currencyIn);
		feePaymentInfo.setDestinationCurrency(currencyOut);
		feePaymentInfo.setFeeType(FX_FEE_TYPE);
		feePaymentInfo.setFxConversionType(conversionType);
		request.setFeePaymentInfo(feePaymentInfo);

		LOGGER.info("currency_convert_customer request: {}", printValueObject(request));
		CurrencyConvertCustomerResponse response = ffxFXService.currency_convert_customer(request);
		LOGGER.info("currency_convert_customer response: {}", printValueObject(response));
		return response;
	}

	/**
	 * Calls calculate_fee_amount_from() for input data
	 * 
	 * <li>currencyIn + fromAmount = txn_amt in CAL</li>
	 * <li>senderCountry = s_cntry in CAL</li>
	 * <li>currencyOut = vol in CAL</li>
	 * <li>recipientCountry = cntry in CAL</li>
	 * <li>feeType = fee_type in CAL</li>
	 * <li>fundingSource = fsrc in CAL</li>
	 * 
	 * @param currencyIn
	 * @param fromAmount
	 * @param currencyOut
	 * @param country
	 * @param conversionType
	 * @return
	 */
	public CalculateFeeAmountFromResponse calculateFeeAmountFrom(final String currencyIn, final long fromAmount,
            String senderCountry, String currencyOut, String recipientCountry, String feeType, String fundingSource,
            String chargeBackGrade) {
		CalculateFeeAmountFromRequest request = new CalculateFeeAmountFromRequest();

		FeePaymentInfoVO feePaymentInfo = new FeePaymentInfoVO();
		feePaymentInfo.setFeeType(feeType);
		feePaymentInfo.setRecipientCategoryId(new BigInteger(RECIPIENT_CATEGORY));
		feePaymentInfo.setRecipientCountry(recipientCountry);
		feePaymentInfo.setRecipientVolume(new Currency(currencyOut, 0));
		feePaymentInfo.setSenderCountry(senderCountry);
		feePaymentInfo.setAllowAuditTrailSerialize(true);
		feePaymentInfo.setRecipientTransAmount(new Currency(currencyIn, fromAmount));
		feePaymentInfo.setFundingSource(fundingSource);
        feePaymentInfo.setCbGrade(chargeBackGrade);
		request.setFeePaymentInfo(feePaymentInfo);

		LOGGER.info("calculate_fee_amount_from request: {}", printValueObject(request));
		CalculateFeeAmountFromResponse response = ffxFeeService.calculate_fee_amount_from(request);
		LOGGER.info("calculate_fee_amount_from response: {}", printValueObject(response));
		return response;
	}

	/**
	 * Calls currency_convert_refund_customer() for input data. It retrieves
	 * audit trail by internally calling currency_convert_customer().
	 * 
	 * <li>currencyIn + fromAmount = txn_amt in CAL</li>
	 * <li>currencyOut = sccy and dccy in CAL</li>
	 * <li>country = cntry in CAL</li>
	 * <li>reverse = whether conversion should be reversed</li>
	 * <li>conversionType = fxtype in CAL</li>
	 * 
	 * @param currencyIn
	 * @param fromAmount
	 * @param currencyOut
	 * @param country
	 * @param reverse
	 * @param conversionType
	 * @return
	 */
	public CurrencyConvertRefundCustomerResponse currencyConvertRefundCustomer(final String currencyIn,
            final long fromAmount, String currencyOut, String country, boolean reverse, String conversionType) {
		CurrencyConvertRefundCustomerRequest request = new CurrencyConvertRefundCustomerRequest();
		Currency sourceCurrency = new Currency(currencyIn, fromAmount);
		request.setAmountIn(sourceCurrency);
		request.setCodeOut(currencyOut);

		FeePaymentInfoVO feePaymentInfo = new FeePaymentInfoVO();
		feePaymentInfo.setRecipientCountry(country);
		feePaymentInfo.setSenderCountry(country);
		feePaymentInfo.setAllowAuditTrailSerialize(true);
		feePaymentInfo.setRecipientTransAmount(sourceCurrency);
		feePaymentInfo.setSourceCurrency(currencyIn);
		feePaymentInfo.setDestinationCurrency(currencyOut);
		feePaymentInfo.setFeeType(FX_FEE_TYPE);
		feePaymentInfo.setFxConversionType(conversionType);
		request.setFeePaymentInfo(feePaymentInfo);

		// get audit trail from original
		CurrencyConvertCustomerResponse forwardMovementResponse = currencyConvertCustomer(currencyIn, fromAmount,
				currencyOut, country, reverse, conversionType);
		request.setFxAuditTrail(forwardMovementResponse.getConversionInfoOut().getFxAuditTrail());

        // set rateId from FX call
        request.setOriginalRateIdTo(forwardMovementResponse.getConversionInfoOut().getFxAuditTrail()
                .getConversionDetails().getDestXr().getExchangeRateId());

		LOGGER.info("currency_convert_refund_customer request: {}", printValueObject(request));
		CurrencyConvertRefundCustomerResponse response = ffxFXService.currency_convert_refund_customer(request);
		LOGGER.info("currency_convert_refund_customer response: {}", printValueObject(response));
		return response;
	}

    /**
     * Calls currency_convert_refund_interbank() for input data. It retrieves audit trail by internally calling
     * currency_convert_interbank().
     * <li>currencyIn + fromAmount = txn_amt in CAL</li>
     * <li>currencyOut = sccy and dccy in CAL</li>
     * <li>country = cntry in CAL</li>
     * <li>reverse = whether conversion should be reversed</li>
     * <li>conversionType = fxtype in CAL</li>
     * 
     * @param currencyIn
     * @param fromAmount
     * @param currencyOut
     * @param country
     * @param reverse
     * @param conversionType
     * @return
     */
    public CurrencyConvertRefundInterbankResponse currencyConvertInterBankRefundCustomer(final String currencyIn,
            final long fromAmount, String currencyOut, boolean reverse) {

        CurrencyConvertRefundInterbankRequest request = new CurrencyConvertRefundInterbankRequest();
        Currency sourceCurrency = new Currency(currencyIn, fromAmount);
        request.setAmountIn(sourceCurrency);
        request.setCodeOut(currencyOut);

        // get audit trail from original
        CurrencyConvertInterbankResponse forwardMovementResponse = currencyConvertInterbank(currencyIn, fromAmount,
                currencyOut, reverse);
        request.setFxAuditTrail(forwardMovementResponse.getConversionInfoOut().getFxAuditTrail());

        if (reverse) {
            request.setOriginalRateIdFrom(forwardMovementResponse.getConversionInfoOut().getRateIdFrom());
        }
        else {
            request.setOriginalRateIdTo(forwardMovementResponse.getConversionInfoOut().getRateIdTo());
        }

        LOGGER.info("currency_convert_refund_interbank request: {}", printValueObject(request));
        CurrencyConvertRefundInterbankResponse response = ffxFXService.currency_convert_refund_interbank(request);
        LOGGER.info("currency_convert_refund_interbank response: {}", printValueObject(response));
        return response;
    }

}
