package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.transform.TransformerException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.xpath.XPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.FXInterbankPlaceHolderResolver;
import com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.FXPlaceHolderResolver;
import com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.FXRefundPlaceHolderResolver;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.AbstractFXPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FXInterbankPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FXPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FXRefundPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderDTO;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.util.FXDataTrimmer;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;

/**
 * This class helps in generating {@link FXPlaceHolder} from input JSON file and
 * sender/recipient golden file. It returns FXPlaceHolder object with values
 * retrieved from FFX bridge call
 * <p>
 * FXPlaceHolder object can then be used to compare FX data in golden file with
 * data retrieved from FX call.
 * <p>
 * The class exposes methods to do these comparisons and replace FX field values
 * with placeholder strings.
 * <p>
 * <p>
 * It replaces below "fee_history" rows of type 'X' in sender and recipient
 * golden files with placeholder strings:
 * <li>data - ${FX:FEE-AUDIT-TRAIL()}</li>
 * <li>actual_fixed_fee - ${FX:ACTUAL-FIXED-FEE()}</li>
 * <li>actual_percent_fee - ${FX:ACTUAL-PERCENT-FEE}</li>
 * <li>total_fee_amount - ${FX:TOTAL-FEE-AMOUNT()}</li>
 * <p>
 * It also replaces below "fx_history" fields in sender and recipient golden
 * files with placeholder strings:
 * <li>data - ${FX:AUDIT-TRAIL()}</li>
 * <li>exchange_rate - ${FX:EXCHANGE-RATE()}</li>
 * <li>amount_to - ${FX:AMOUNT-TO()}</li>
 * <li>amount_from - ${FX:AMOUNT-FROM()}</li>
 */
@Singleton
public class FXPlaceHolderGenerator extends PlaceHolderGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(FXPlaceHolderGenerator.class);

	private static final String XPATH_FOR_FX_HISTORY = "//validation_fixture/*/fx_history/item[*]";
	private static final String XPATH_FOR_FEE_HISTORY_F_ROW = "//validation_fixture/*/fee_history/item[transaction_type/text() = 'X']";

	private static final String AMOUNT_TO_FIELD = "amount_to";
	private static final String AMOUNT_FROM_FIELD = "amount_from";
	private static final String CURRENCY_CODE_TO_FIELD = "currency_code_to";
	private static final String CURRENCY_CODE_FROM_FIELD = "currency_code_from";
	private static final String EXCHANGE_RATE_FIELD = "exchange_rate";
	private static final String AUDIT_TRAIL_FIELD = "data";
	private static final String ACTUAL_FIXED_FEE_FIELD = "actual_fixed_fee";
	private static final String ACTUAL_PERCENT_FEE_FIELD = "actual_percent_fee";
	private static final String TOTAL_FEE_AMOUNT_FIELD = "total_fee_amount";
	private static final String CURRENCY_CODE_FIELD = "currency_code";
	private static final String AMOUNT_FIELD = "amount";
	private static final String USD_AMOUNT_FIELD = "usd_amount";

    private static final String SUBTYPE_FIELD = "subtype";

	private static final String TEXT_ATTRIBUTE = "text()";

	private static final String AUDIT_TRAIL_XPATH_FOR_FX_HISTORY = XPATH_FOR_FX_HISTORY + "/" + AUDIT_TRAIL_FIELD + "/"
			+ TEXT_ATTRIBUTE;
	private static final String EXCHANGE_RATE_XPATH_FOR_FX_HISTORY = XPATH_FOR_FX_HISTORY + "/" + EXCHANGE_RATE_FIELD
			+ "/" + TEXT_ATTRIBUTE;
	private static final String AMOUNT_FROM_XPATH_FOR_FX_HISTORY = XPATH_FOR_FX_HISTORY + "/" + AMOUNT_FROM_FIELD + "/"
			+ TEXT_ATTRIBUTE;
	private static final String AMOUNT_TO_XPATH_FOR_FX_HISTORY = XPATH_FOR_FX_HISTORY + "/" + AMOUNT_TO_FIELD + "/"
			+ TEXT_ATTRIBUTE;

	private static final String ACTUAL_FIXED_FEE_XPATH_FOR_FEE_HISTORY_X_ROW = XPATH_FOR_FEE_HISTORY_F_ROW + "/"
			+ ACTUAL_FIXED_FEE_FIELD + "/" + TEXT_ATTRIBUTE;
	private static final String ACTUAL_PERCENT_FEE_XPATH_FOR_FEE_HISTORY_X_ROW = XPATH_FOR_FEE_HISTORY_F_ROW + "/"
			+ ACTUAL_PERCENT_FEE_FIELD + "/" + TEXT_ATTRIBUTE;
	private static final String TOTAL_FEE_AMOUNT_XPATH_FOR_FEE_HISTORY_X_ROW = XPATH_FOR_FEE_HISTORY_F_ROW + "/"
			+ TOTAL_FEE_AMOUNT_FIELD + "/" + TEXT_ATTRIBUTE;
	private static final String AUDIT_TRAIL_XPATH_FOR_FEE_HISTORY_X_ROW = XPATH_FOR_FEE_HISTORY_F_ROW + "/"
			+ AUDIT_TRAIL_FIELD + "/" + TEXT_ATTRIBUTE;

	private static final String XPATH_FOR_WTRANSACTION_X_ROW = "//validation_fixture/*/wtransaction/item[type/text() = 'X']";

    private static final String RATE_INVERSE = "I";
    private static final String RATE_INTERBANK = "B";

	@Inject
	private FXPlaceHolderResolver fxPlaceHolderResolver;

    @Inject
    private FXInterbankPlaceHolderResolver fxInterbankPlaceHolderResolver;

    @Inject
    private FXRefundPlaceHolderResolver fxRefundPlaceHolderResolver;

    public List<AbstractFXPlaceHolder> getFXPlaceHolders(GoldenFilePlaceHolderDTO dto, User user,
            Currency transactionAmount,
			boolean isReverse, String conversionType) throws TransformerException {
		// 1. create FX placeholder from input
        List<AbstractFXPlaceHolder> fxPlaceHolders = createFXPlaceHolders(dto, user, transactionAmount, isReverse,
                conversionType);
        if (CollectionUtils.isNotEmpty(fxPlaceHolders)) {
            for (AbstractFXPlaceHolder fxPlaceHolder : fxPlaceHolders) {
                try {
                    // 2. resolve placeholder by making FX bridge call
                    if (fxPlaceHolder instanceof FXPlaceHolder) {
                        fxPlaceHolderResolver.setPlaceHolderValues((FXPlaceHolder) fxPlaceHolder);
                    }
                    else if (fxPlaceHolder instanceof FXInterbankPlaceHolder) {
                        fxInterbankPlaceHolderResolver.setPlaceHolderValues((FXInterbankPlaceHolder) fxPlaceHolder);
                    }
                    else if (fxPlaceHolder instanceof FXRefundPlaceHolder) {
                        fxRefundPlaceHolderResolver.setPlaceHolderValues((FXRefundPlaceHolder) fxPlaceHolder);
                    }
                }
                catch (Exception e) {
                    LOGGER.error("Error occurred getting value for placeholder {} in {}", fxPlaceHolder.buildKey(""),
                            dto.getPath());
                    throw e;
                }
            }
        }
        return fxPlaceHolders;
	}

	/**
	 * Create placeholder string from input and return FXPlaceHolder object.
	 * <p>
	 * Need to rely on golden file for transaction amount and currency. Using
	 * the amount from input JSON file requires complex logic handling (e.g.
	 * when part of the funding is done through holding and remaining through
	 * bank)
	 * <p>
	 * Disadvantage of relying on golden file for transaction amount and
	 * currency is, can't calculate amount after fee
	 */
    private List<AbstractFXPlaceHolder> createFXPlaceHolders(GoldenFilePlaceHolderDTO dto, User user,
            Currency transactionAmount, boolean isReverse, String conversionType) throws TransformerException {
        List<AbstractFXPlaceHolder> fxPlaceHolders = new ArrayList<>();
		NodeList fxNodes = XPathAPI.selectNodeList(dto.getGoldenFileDocument(), XPATH_FOR_FX_HISTORY);
		if (fxNodes.getLength() > 0) {
            fxPlaceHolders.add(createFXPlaceHolder(fxNodes.item(0), user, transactionAmount, isReverse,
                    conversionType));
		}
		if (fxNodes.getLength() > 1) {
            dto.getStats().incrementMoreThan1XRow();
            String subType = getSubType(dto, 1);
            if (RATE_INVERSE.equalsIgnoreCase(subType)) {
                fxPlaceHolders.add(createFXRefundPlaceHolder(fxNodes.item(1), user, transactionAmount, isReverse,
                        conversionType));
            }
            else if (RATE_INTERBANK.equalsIgnoreCase(subType)) {
                fxPlaceHolders.add(createFXInterbankPlaceHolder(fxNodes.item(1), user, transactionAmount, isReverse,
                        conversionType));
            }
            else {
                throw new IllegalStateException(
                        "Didn't find any FXRefund placeholder for " + dto.getPath() + " for subtype " + subType);
            }
        }
        return fxPlaceHolders;
	}

    private String getSubType(GoldenFilePlaceHolderDTO dto, int rowIndex) throws TransformerException {
        NodeList wTransactionXRowNodes = XPathAPI.selectNodeList(dto.getGoldenFileDocument(),
                XPATH_FOR_WTRANSACTION_X_ROW);
        return getNodeValue(wTransactionXRowNodes.item(2 * rowIndex), SUBTYPE_FIELD);
    }

    private FXPlaceHolder createFXPlaceHolder(Node fxNode, User user, Currency transactionAmount,
            boolean isReverse, String conversionType) {
        String fromAmount = null;
        String fromCurrency = null;
        String toCurrency = null;
        if (AbstractFXPlaceHolder.isSenderSideConversion(conversionType)) {
            // sender validation
            fromAmount = getNodeValue(fxNode, AMOUNT_TO_FIELD);
            fromCurrency = getNodeValue(fxNode, CURRENCY_CODE_TO_FIELD);
            toCurrency = getNodeValue(fxNode, CURRENCY_CODE_FROM_FIELD);
        }
        else {
            // recipient validation
            fromAmount = getNodeValue(fxNode, AMOUNT_FROM_FIELD);
            fromCurrency = getNodeValue(fxNode, CURRENCY_CODE_FROM_FIELD);
            toCurrency = getNodeValue(fxNode, CURRENCY_CODE_TO_FIELD);
        }

        LOGGER.debug("transactionAmount:{}", transactionAmount);

        StringBuilder sb = new StringBuilder(FXPlaceHolder.FX_PLACEHOLDER_PREFIX + PlaceHolder.FUNCTION_PREFIX)
                .append(fromCurrency).append(PlaceHolder.PARAMETER_DELIMITER) // currencyIn
                .append(fromAmount).append(PlaceHolder.PARAMETER_DELIMITER) // fromAmount
                .append(toCurrency).append(PlaceHolder.PARAMETER_DELIMITER) // currency
                .append(user.getCountry()).append(PlaceHolder.PARAMETER_DELIMITER) // country
                .append(String.valueOf(isReverse).toUpperCase()).append(PlaceHolder.PARAMETER_DELIMITER) // reverse
                .append(conversionType).append(PlaceHolder.FUNCTION_SUFFIX).append(PlaceHolder.PLACEHOLDER_SUFFIX); // conversionType
        return new FXPlaceHolder(sb.toString());
    }

    private FXInterbankPlaceHolder createFXInterbankPlaceHolder(Node fxNode, User user, Currency transactionAmount,
            boolean isReverse, String conversionType) {
        String fromAmount = null;
        String fromCurrency = null;
        String toCurrency = null;
        if (AbstractFXPlaceHolder.isSenderSideConversion(conversionType)) {
            // sender validation
            fromAmount = getNodeValue(fxNode, AMOUNT_FROM_FIELD);
            fromCurrency = getNodeValue(fxNode, CURRENCY_CODE_FROM_FIELD);
            toCurrency = getNodeValue(fxNode, CURRENCY_CODE_TO_FIELD);
        }
        else {
            // recipient validation
            fromAmount = getNodeValue(fxNode, AMOUNT_TO_FIELD);
            fromCurrency = getNodeValue(fxNode, CURRENCY_CODE_TO_FIELD);
            toCurrency = getNodeValue(fxNode, CURRENCY_CODE_FROM_FIELD);
        }
        isReverse = !isReverse;

        LOGGER.debug("transactionAmount:{}", transactionAmount);

        StringBuilder sb = new StringBuilder(
                FXInterbankPlaceHolder.FX_INTERBANK_PLACEHOLDER_PREFIX + PlaceHolder.FUNCTION_PREFIX)
                        .append(fromCurrency).append(PlaceHolder.PARAMETER_DELIMITER) // currencyIn
                        .append(fromAmount).append(PlaceHolder.PARAMETER_DELIMITER) // fromAmount
                        .append(toCurrency).append(PlaceHolder.PARAMETER_DELIMITER) // currency
                        .append(String.valueOf(isReverse).toUpperCase()).append(PlaceHolder.PARAMETER_DELIMITER) // reverse
                        .append(conversionType).append(PlaceHolder.FUNCTION_SUFFIX) // conversionType
                        .append(PlaceHolder.PLACEHOLDER_SUFFIX);
        return new FXInterbankPlaceHolder(sb.toString());
    }

    private FXRefundPlaceHolder createFXRefundPlaceHolder(Node fxNode, User user, Currency transactionAmount,
            boolean isReverse, String conversionType) {
        String fromAmount = null;
        String fromCurrency = null;
        String toCurrency = null;
        if (AbstractFXPlaceHolder.isSenderSideConversion(conversionType)) {
            fromAmount = getNodeValue(fxNode, AMOUNT_FROM_FIELD);
            fromCurrency = getNodeValue(fxNode, CURRENCY_CODE_FROM_FIELD);
            toCurrency = getNodeValue(fxNode, CURRENCY_CODE_TO_FIELD);
        }
        else {
            fromAmount = getNodeValue(fxNode, AMOUNT_TO_FIELD);
            fromCurrency = getNodeValue(fxNode, CURRENCY_CODE_TO_FIELD);
            toCurrency = getNodeValue(fxNode, CURRENCY_CODE_FROM_FIELD);
        }

        LOGGER.debug("transactionAmount:{}", transactionAmount);

        StringBuilder sb = new StringBuilder(
                FXRefundPlaceHolder.FX_REFUND_PLACEHOLDER_PREFIX + PlaceHolder.FUNCTION_PREFIX).append(fromCurrency)
                        .append(PlaceHolder.PARAMETER_DELIMITER) // currencyIn
                        .append(fromAmount).append(PlaceHolder.PARAMETER_DELIMITER) // fromAmount
                        .append(toCurrency).append(PlaceHolder.PARAMETER_DELIMITER) // currency
                        .append(user.getCountry()).append(PlaceHolder.PARAMETER_DELIMITER) // country
                        .append(String.valueOf(isReverse).toUpperCase()).append(PlaceHolder.PARAMETER_DELIMITER) // reverse
                        .append(conversionType).append(PlaceHolder.FUNCTION_SUFFIX)
                        .append(PlaceHolder.PLACEHOLDER_SUFFIX); // conversionType
        return new FXRefundPlaceHolder(sb.toString());
    }

	/**
	 * checks correctness of fxplaceholder formula by matching audit trail in
	 * golden file with the one retrieved by formula
	 */
    public void compareAndReplaceAuditTrail(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            int rowIndex) {
		try {
			Node fxAuditTrailNode = XPathAPI
                    .selectNodeList(dto.getGoldenFileDocument(), AUDIT_TRAIL_XPATH_FOR_FX_HISTORY).item(rowIndex);
			String expectedAuditTrail = fxAuditTrailNode.getTextContent();
			String auditTrailKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_AUDIT_TRAIL);
			String actualAuditTrail = fxPlaceHolder.getValue(auditTrailKey);

			String actualString = expectedAuditTrail.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? auditTrailKey
					: actualAuditTrail;

            if (!expectedAuditTrail.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
                expectedAuditTrail = FXDataTrimmer.trimAuditTrail(expectedAuditTrail);
            }

			if (expectedAuditTrail.equals(actualString)) {
				dto.getStats().incrementMatchingFXAuditTrail();
				if (!expectedAuditTrail.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] FX Audit Trail\n{} {}\n{} {}",
							new Object[] { expectedAuditTrail, dto.getPath(), actualString, auditTrailKey });
					replaceValue(dto, fxAuditTrailNode, auditTrailKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingFXAuditTrail();
				LOGGER.error("[Not Matched] FX Audit Trail\n{} {}\n{} {}",
						new Object[] { expectedAuditTrail, dto.getPath(), actualString, auditTrailKey });
				replaceValue(dto, fxAuditTrailNode, auditTrailKey, false);
			}
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing audit trail for {}", dto.getPath(), expt);
		}
	}

	/**
	 * checks correctness of fxplaceholder formula by matching exchange rate in
	 * golden file with the one retrieved by formula
	 */
    public void compareAndReplaceExchangeRate(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            int rowIndex) {
		try {
			Node fxExchangeRateNode = XPathAPI
                    .selectNodeList(dto.getGoldenFileDocument(), EXCHANGE_RATE_XPATH_FOR_FX_HISTORY).item(rowIndex);
			String expectedExchangeRate = fxExchangeRateNode.getTextContent();
			String exchangeRateKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_EXCHANGE_RATE);
			String actualExchangeRate = fxPlaceHolder.getValue(exchangeRateKey);

			String actualString = expectedExchangeRate.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? exchangeRateKey
					: actualExchangeRate;

            if (!expectedExchangeRate.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
                expectedExchangeRate = FXDataTrimmer.trimExchangeRate(expectedExchangeRate);
            }

			if (expectedExchangeRate.equals(actualString)) {
				dto.getStats().incrementMatchingFXExchangeRate();
				if (!expectedExchangeRate.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] FX Exchange rate\n{} {}\n{} {}",
							new Object[] { expectedExchangeRate, dto.getPath(), actualString, exchangeRateKey });
					replaceValue(dto, fxExchangeRateNode, exchangeRateKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingFXExchangeRate();
				LOGGER.error("[Not Matched] FX Exchange rate\n{} {}\n{} {}",
						new Object[] { expectedExchangeRate, dto.getPath(), actualString, exchangeRateKey });
				replaceValue(dto, fxExchangeRateNode, exchangeRateKey, false);
			}
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing exchange rate for {}", dto.getPath(), expt);
		}
	}

	/**
	 * checks correctness of fxplaceholder formula by matching "amount from" in
	 * golden file with the one retrieved by formula
	 */
    public void compareAndReplaceAmountFrom(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            int rowIndex) {
		try {
			Node fxAmountFromNode = XPathAPI
                    .selectNodeList(dto.getGoldenFileDocument(), AMOUNT_FROM_XPATH_FOR_FX_HISTORY).item(rowIndex);
			String expectedAmountFrom = fxAmountFromNode.getTextContent();
			String amountFromKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_AMOUNT_FROM);
			String actualAmountFrom = fxPlaceHolder.getValue(amountFromKey);

			String actualString = expectedAmountFrom.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? amountFromKey
					: actualAmountFrom;

			if (expectedAmountFrom.equals(actualString)) {
				dto.getStats().incrementMatchingFXAmountFrom();
				if (!expectedAmountFrom.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] FX Amount from\n{} {}\n{} {}",
							new Object[] { expectedAmountFrom, dto.getPath(), actualString, amountFromKey });
					replaceValue(dto, fxAmountFromNode, amountFromKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingFXAmountFrom();
				LOGGER.error("[Not Matched] FX Amount from\n{} {}\n{} {}",
						new Object[] { expectedAmountFrom, dto.getPath(), actualString, amountFromKey });
				replaceValue(dto, fxAmountFromNode, amountFromKey, false);
			}
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing amount-from for {}", dto.getPath(), expt);
		}
	}

	/**
	 * checks correctness of fxplaceholder formula by matching "amount from" in
	 * golden file with the one retrieved by formula
	 */
    public void compareAndReplaceAmountTo(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            int rowIndex) {
		try {
			Node fxAmoutToNode = XPathAPI.selectNodeList(dto.getGoldenFileDocument(), AMOUNT_TO_XPATH_FOR_FX_HISTORY)
                    .item(rowIndex);
			String expectedAmountTo = fxAmoutToNode.getTextContent();
			String amountToKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_AMOUNT_TO);
			String actualAmountTo = fxPlaceHolder.getValue(amountToKey);

			String actualString = expectedAmountTo.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? amountToKey
					: actualAmountTo;

			if (expectedAmountTo.equals(actualString)) {
				dto.getStats().incrementMatchingFXAmountTo();
				if (!expectedAmountTo.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] FX Amount to\n{} {}\n{} {}",
							new Object[] { expectedAmountTo, dto.getPath(), actualString, amountToKey });
					replaceValue(dto, fxAmoutToNode, amountToKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingFXAmountTo();
				LOGGER.error("[Not Matched] FX Amount to\n{} {}\n{} {}",
						new Object[] { expectedAmountTo, dto.getPath(), actualString, amountToKey });
				replaceValue(dto, fxAmoutToNode, amountToKey, false);
			}
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing amount-to for {}", dto.getPath(), expt);
		}
	}

	/**
	 * checks correctness of fxplaceholder formula by matching actual fixed fee
	 * in golden file with the one retrieved by formula
	 */
    public void compareAndReplaceActualFixedFee(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            int rowIndex) {
        if (fxPlaceHolder instanceof FXInterbankPlaceHolder) {
            return;
        }
		try {
			NodeList fRowFixedFeeNodes = XPathAPI.selectNodeList(dto.getGoldenFileDocument(),
					ACTUAL_FIXED_FEE_XPATH_FOR_FEE_HISTORY_X_ROW);

            Node fRowFixedFeeNode = fRowFixedFeeNodes.item(rowIndex);
			String expectedFixedFee = fRowFixedFeeNode.getTextContent();
			String fixedFeeKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_ACTUAL_FIXED_FEE);
			String fixedFeeAmount = fxPlaceHolder.getValue(fixedFeeKey);

			String actualString = expectedFixedFee.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? fixedFeeKey
					: fixedFeeAmount;

			if (expectedFixedFee.equals(actualString)) {
				dto.getStats().incrementMatchingXActualFixedFeeAmount();
				if (!expectedFixedFee.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] X Row Fixed Fee \n{} {}\n{} {}",
							new Object[] { expectedFixedFee, dto.getPath(), actualString, fixedFeeKey });
					replaceValue(dto, fRowFixedFeeNode, fixedFeeKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingXActualFixedFeeAmount();
				LOGGER.error("[Not Matched] X Row Fixed Fee \n{} {}\n{} {}",
						new Object[] { expectedFixedFee, dto.getPath(), actualString, fixedFeeKey });
				replaceValue(dto, fRowFixedFeeNode, fixedFeeKey, false);
			}

		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing fixed fee for {}", dto.getPath(), expt);
		}

	}

	/**
	 * checks correctness of fxplaceholder formula by matching actual percent
	 * fee in golden file with the one retrieved by formula
	 */
    public void compareAndReplaceActualPercentFee(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            int rowIndex) {
        if (fxPlaceHolder instanceof FXInterbankPlaceHolder) {
            return;
        }
		try {
			NodeList fRowPercentFeeNodes = XPathAPI.selectNodeList(dto.getGoldenFileDocument(),
					ACTUAL_PERCENT_FEE_XPATH_FOR_FEE_HISTORY_X_ROW);

            Node fRowPercentFeeNode = fRowPercentFeeNodes.item(rowIndex);
			String expectedPercentFee = fRowPercentFeeNode.getTextContent();
			String percentFeeKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_ACTUAL_PERCENT_FEE);
			String percentFeeAmount = fxPlaceHolder.getValue(percentFeeKey);

			String actualString = expectedPercentFee.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? percentFeeKey
					: percentFeeAmount;

			if (expectedPercentFee.equals(actualString)) {
				dto.getStats().incrementMatchingXActualPercentFeeAmount();
				if (!expectedPercentFee.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] X Row Percent Fee \n{} {}\n{} {}",
							new Object[] { expectedPercentFee, dto.getPath(), actualString, percentFeeKey });
					replaceValue(dto, fRowPercentFeeNode, percentFeeKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingXActualPercentFeeAmount();
				LOGGER.error("[Not Matched] X Row Percent Fee \n{} {}\n{} {}",
						new Object[] { expectedPercentFee, dto.getPath(), actualString, percentFeeKey });
				replaceValue(dto, fRowPercentFeeNode, percentFeeKey, false);
			}

		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing percent fee for {}", dto.getPath(), expt);
		}

	}

	/**
	 * checks correctness of fxplaceholder formula by matching fee amount in
	 * golden file with the one retrieved by formula
	 */
    public void compareAndReplaceTotalFeeAmount(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            int rowIndex) {
        if (fxPlaceHolder instanceof FXInterbankPlaceHolder) {
            return;
        }
		try {
			NodeList fRowFeeAmountNodes = XPathAPI.selectNodeList(dto.getGoldenFileDocument(),
					TOTAL_FEE_AMOUNT_XPATH_FOR_FEE_HISTORY_X_ROW);

            Node fRowFeeAmountNode = fRowFeeAmountNodes.item(rowIndex);
			String expectedTotalFeeAmount = fRowFeeAmountNode.getTextContent();
			String totalFeeAmountKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_TOTAL_FEE_AMOUNT);
			String actualTotalFeeAmount = fxPlaceHolder.getValue(totalFeeAmountKey);

			String actualString = expectedTotalFeeAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? totalFeeAmountKey
					: actualTotalFeeAmount;

			if (expectedTotalFeeAmount.equals(actualString)) {
				dto.getStats().incrementMatchingXTotalFeeAmount();
				if (!expectedTotalFeeAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] X Row Fee Amount\n{} {}\n{} {}",
							new Object[] { expectedTotalFeeAmount, dto.getPath(), actualString, totalFeeAmountKey });
					replaceValue(dto, fRowFeeAmountNode, totalFeeAmountKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingXTotalFeeAmount();
				LOGGER.error("[Not Matched] X Row Fee Amount\n{} {}\n{} {}",
						new Object[] { expectedTotalFeeAmount, dto.getPath(), actualString, totalFeeAmountKey });
				replaceValue(dto, fRowFeeAmountNode, totalFeeAmountKey, false);
			}
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing total fee amount for {}", dto.getPath(), expt);
		}

	}

	/**
	 * checks correctness of fxplaceholder formula by matching audit trail in
	 * golden file with the one retrieved by formula
	 */
    public void compareAndReplaceXRowAuditTrail(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            int rowIndex) {
        if (fxPlaceHolder instanceof FXInterbankPlaceHolder) {
            return;
        }
		try {
			NodeList feeAuditTrailNodes = XPathAPI.selectNodeList(dto.getGoldenFileDocument(),
					AUDIT_TRAIL_XPATH_FOR_FEE_HISTORY_X_ROW);

            Node feeAuditTrailNode = feeAuditTrailNodes.item(rowIndex);
			String expectedAuditTrail = feeAuditTrailNode.getTextContent();
			String auditTrailKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_FEE_AUDIT_TRAIL);
			String actualAuditTrail = fxPlaceHolder.getValue(auditTrailKey);

			String actualString = expectedAuditTrail.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? auditTrailKey
					: actualAuditTrail;

            if (!expectedAuditTrail.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
                expectedAuditTrail = FXDataTrimmer.trimFeeAuditTrail(expectedAuditTrail);
            }

			if (expectedAuditTrail.equals(actualString)) {
				dto.getStats().incrementMatchingFeeXAuditTrail();
				if (!expectedAuditTrail.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] X Row Audit Trail:\n{} {}\n{} {}",
							new Object[] { expectedAuditTrail, dto.getPath(), actualString, auditTrailKey });
					replaceValue(dto, feeAuditTrailNode, auditTrailKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingFeeXAuditTrail();
				LOGGER.error("[Not Matched] X Row Audit Trail:\n{} {}\n{} {}",
						new Object[] { expectedAuditTrail, dto.getPath(), actualString, auditTrailKey });
				replaceValue(dto, feeAuditTrailNode, auditTrailKey, false);
			}

		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing audit trail for {}", dto.getPath(), expt);
		}
	}

	/**
	 * checks correctness of fxplaceholder formula by matching amount and
	 * usd-amount in golden file with the one retrieved by formula
	 */
    public void compareAndReplaceAmountAndUsdAmount(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            int rowIndex) {
		try {
			NodeList wTransactionXRowNodes = XPathAPI.selectNodeList(dto.getGoldenFileDocument(),
					XPATH_FOR_WTRANSACTION_X_ROW);

            compareAndReplaceAmountAndUsdAmount(dto, fxPlaceHolder, wTransactionXRowNodes.item(2 * rowIndex));
            compareAndReplaceAmountAndUsdAmount(dto, fxPlaceHolder, wTransactionXRowNodes.item(2 * rowIndex + 1));
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing amount and usd-amount for {}", dto.getPath(), expt);
		}
	}

    private void compareAndReplaceAmountAndUsdAmount(GoldenFilePlaceHolderDTO dto, AbstractFXPlaceHolder fxPlaceHolder,
            Node xRowNode) {
        if (fxPlaceHolder instanceof FXPlaceHolder) {
            compareAndReplaceAmountAndUsdAmountForForwardX(dto, fxPlaceHolder, xRowNode);
        }
        else {
            compareAndReplaceAmountAndUsdAmountForBackwardX(dto, fxPlaceHolder, xRowNode);
        }
    }

    private void compareAndReplaceAmountAndUsdAmountForForwardX(GoldenFilePlaceHolderDTO dto,
            AbstractFXPlaceHolder fxPlaceHolder,
			Node xRowNode) {
		String currencyCode = getNodeValue(xRowNode, CURRENCY_CODE_FIELD);
		if (currencyCode.equals(fxPlaceHolder.getCurrencyFrom())) {
            if (fxPlaceHolder.isSenderSideConversion()) {
				// usd amount
				String usdAmountFieldKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_USD_AMOUNT_TO);
				String usdAmountValue = fxPlaceHolder.getValue(usdAmountFieldKey);
				compareAndReplaceUsdAmount(dto, getChildNode(xRowNode, USD_AMOUNT_FIELD), usdAmountFieldKey,
						usdAmountValue);
			} else {
				// get negated value and key for usd-amount
				String[] negatedUsdAmountFieldAndValue = getNegatedFieldAndValue(dto, fxPlaceHolder,
						fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_USD_AMOUNT_FROM));
				compareAndReplaceUsdAmount(dto, getChildNode(xRowNode, USD_AMOUNT_FIELD),
						negatedUsdAmountFieldAndValue[0], negatedUsdAmountFieldAndValue[1]);
			}
		} else {
            if (fxPlaceHolder.isSenderSideConversion()) {
				// get negated value and key for amount
				String[] negatedAmountFieldAndValue = getNegatedFieldAndValue(dto, fxPlaceHolder,
						fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_AMOUNT_FROM));
				compareAndReplaceAmount(dto, getChildNode(xRowNode, AMOUNT_FIELD), negatedAmountFieldAndValue[0],
						negatedAmountFieldAndValue[1]);

				// get negated value and key for usd-amount
				String[] negatedUsdAmountFieldAndValue = getNegatedFieldAndValue(dto, fxPlaceHolder,
						fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_USD_AMOUNT_FROM));
				compareAndReplaceUsdAmount(dto, getChildNode(xRowNode, USD_AMOUNT_FIELD),
						negatedUsdAmountFieldAndValue[0], negatedUsdAmountFieldAndValue[1]);
			} else {
				// amount
				String amountFieldKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_AMOUNT_TO);
				String amountValue = fxPlaceHolder.getValue(amountFieldKey);
				compareAndReplaceAmount(dto, getChildNode(xRowNode, AMOUNT_FIELD), amountFieldKey, amountValue);

				// usd amount
				String usdAmountFieldKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_USD_AMOUNT_TO);
				String usdAmountValue = fxPlaceHolder.getValue(usdAmountFieldKey);
				compareAndReplaceUsdAmount(dto, getChildNode(xRowNode, USD_AMOUNT_FIELD), usdAmountFieldKey,
						usdAmountValue);
			}
		}
	}

    private void compareAndReplaceAmountAndUsdAmountForBackwardX(GoldenFilePlaceHolderDTO dto,
            AbstractFXPlaceHolder fxPlaceHolder,
            Node xRowNode) {
        String currencyCode = getNodeValue(xRowNode, CURRENCY_CODE_FIELD);
        if (currencyCode.equals(fxPlaceHolder.getCurrencyFrom())) {
            if (fxPlaceHolder.isSenderSideConversion()) {
                // get negated value and key for amount
                String[] negatedAmountFieldAndValue = getNegatedFieldAndValue(dto, fxPlaceHolder,
                        fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_AMOUNT_FROM));
                compareAndReplaceAmount(dto, getChildNode(xRowNode, AMOUNT_FIELD), negatedAmountFieldAndValue[0],
                        negatedAmountFieldAndValue[1]);

                // get negated value and key for usd-amount
                String[] negatedUsdAmountFieldAndValue = getNegatedFieldAndValue(dto, fxPlaceHolder,
                        fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_USD_AMOUNT_FROM));
                compareAndReplaceUsdAmount(dto, getChildNode(xRowNode, USD_AMOUNT_FIELD),
                        negatedUsdAmountFieldAndValue[0], negatedUsdAmountFieldAndValue[1]);
            }
            else {
                // amount
                String amountFieldKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_AMOUNT_TO);
                String amountValue = fxPlaceHolder.getValue(amountFieldKey);
                compareAndReplaceAmount(dto, getChildNode(xRowNode, AMOUNT_FIELD), amountFieldKey, amountValue);

                // usd amount
                String usdAmountFieldKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_USD_AMOUNT_TO);
                String usdAmountValue = fxPlaceHolder.getValue(usdAmountFieldKey);
                compareAndReplaceUsdAmount(dto, getChildNode(xRowNode, USD_AMOUNT_FIELD), usdAmountFieldKey,
                        usdAmountValue);
            }
        }
        else {
            if (fxPlaceHolder.isSenderSideConversion()) {
                // amount
                String amountFieldKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_AMOUNT_TO);
                String amountValue = fxPlaceHolder.getValue(amountFieldKey);
                compareAndReplaceAmount(dto, getChildNode(xRowNode, AMOUNT_FIELD), amountFieldKey, amountValue);

                // usd amount
                String usdAmountFieldKey = fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_USD_AMOUNT_TO);
                String usdAmountValue = fxPlaceHolder.getValue(usdAmountFieldKey);
                compareAndReplaceUsdAmount(dto, getChildNode(xRowNode, USD_AMOUNT_FIELD), usdAmountFieldKey,
                        usdAmountValue);
            }
            else {
                // get negated value and key for amount
                String[] negatedAmountFieldAndValue = getNegatedFieldAndValue(dto, fxPlaceHolder,
                        fxPlaceHolder.buildKey(FXPlaceHolder.FIELD_AMOUNT_FROM));
                compareAndReplaceAmount(dto, getChildNode(xRowNode, AMOUNT_FIELD), negatedAmountFieldAndValue[0],
                        negatedAmountFieldAndValue[1]);
            }
        }
    }

	private void compareAndReplaceAmount(GoldenFilePlaceHolderDTO dto, Node amountNode, String amountFieldKey,
			String actualAmount) {
		String expectedAmount = amountNode.getTextContent();

		String actualString = expectedAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? amountFieldKey : actualAmount;

		if (expectedAmount.equals(actualString)) {
			dto.getStats().incrementMatchingFXAmount();
			if (!expectedAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
				LOGGER.error("[Matched] X Row Amount:\n{} {}\n{} {}",
						new Object[] { expectedAmount, dto.getPath(), actualString, amountFieldKey });
				replaceValue(dto, amountNode, amountFieldKey, true);
			}
		} else {
			dto.getStats().incrementNonMatchingFXAmount();
			LOGGER.error("[Not Matched] X Row Amount:\n{} {}\n{} {}",
					new Object[] { expectedAmount, dto.getPath(), actualString, amountFieldKey });
			replaceValue(dto, amountNode, amountFieldKey, false);
		}
	}

	private void compareAndReplaceUsdAmount(GoldenFilePlaceHolderDTO dto, Node usdAmountNode, String usdAmountFieldKey,
			String actualUsdAmount) {
		String expectedUsdAmount = usdAmountNode.getTextContent();

		String actualString = expectedUsdAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? usdAmountFieldKey
				: actualUsdAmount;

		if (expectedUsdAmount.equals(actualString)) {
			dto.getStats().incrementMatchingFXUsdAmount();
			if (!expectedUsdAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
				LOGGER.error("[Matched] X Row Usd-Amount:\n{} {}\n{} {}",
						new Object[] { expectedUsdAmount, dto.getPath(), actualString, usdAmountFieldKey });
				replaceValue(dto, usdAmountNode, usdAmountFieldKey, true);
			}
		} else {
			dto.getStats().incrementNonMatchingFXUsdAmount();
			LOGGER.error("[Not Matched] X Row Usd-Amount:\n{} {}\n{} {}",
					new Object[] { expectedUsdAmount, dto.getPath(), actualString, usdAmountFieldKey });
			replaceValue(dto, usdAmountNode, usdAmountFieldKey, false);
		}
	}
}
