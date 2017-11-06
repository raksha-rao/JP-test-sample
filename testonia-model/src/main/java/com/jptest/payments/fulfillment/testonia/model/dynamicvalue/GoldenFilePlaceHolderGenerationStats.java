package com.jptest.payments.fulfillment.testonia.model.dynamicvalue;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class carries general statistic information about:
 * <li>senderSideCurrencyConversionCount - how many golden files have sender
 * side currency conversions</li>
 * <li>recipientSideCurrencyConversionCount - how many golden files have
 * recipient side currency conversions</li>
 * <li>senderPaysFeeCount - how many golden files have sender charged for
 * fee</li>
 * <li>recipientPaysFeeCount - how many golden files have recipient charged for
 * fee</li>
 * <li>moreThan1XRow - how many golden files have more than 1 X row entry in
 * fee_history table</li>
 * <li>moreThan1FRow - how many golden files have more than 1 F row entry in
 * fee_history table</li>
 * <p>
 * It also has statistics specifically required for placeholder string
 * generation in golden file
 * <li>matchingFeeAuditTrail - how many golden files have fee-audit-trail field
 * whose value matches with value retrieved from FEE call</li>
 * <li>nonMatchingFeeAuditTrail - how many golden files have fee-audit-trail
 * field whose value does not match value retrieved from FEE call</li>
 * <li>matchingFXExchangeRate - how many golden files have exchange-rate field
 * whose value matches with value retrieved from FX call</li>
 * <li>nonMatchingFXExchangeRate - how many golden files have exchange-rate
 * field whose value does not match value retrieved from FX call</li> etc..
 * 
 * <p>
 * 
 * 
 */
public class GoldenFilePlaceHolderGenerationStats {

	private static final Logger LOGGER = LoggerFactory.getLogger(GoldenFilePlaceHolderGenerationStats.class);

	// general statistics
	private AtomicInteger exceptionCount = new AtomicInteger();
	private AtomicInteger senderPaysFeeCount = new AtomicInteger();
	private AtomicInteger recipientPaysFeeCount = new AtomicInteger();
	private AtomicInteger senderSideCurrencyConversionCount = new AtomicInteger();
	private AtomicInteger recipientSideCurrencyConversionCount = new AtomicInteger();
	private AtomicInteger moreThan1FRow = new AtomicInteger();
	private AtomicInteger moreThan1XRow = new AtomicInteger();

	// statistics for fee F row fields (during placeholder generation)
	private AtomicInteger matchingFeeAuditTrail = new AtomicInteger();
	private AtomicInteger nonMatchingFeeAuditTrail = new AtomicInteger();
	private AtomicInteger matchingTotalFeeAmount = new AtomicInteger();
	private AtomicInteger nonMatchingTotalFeeAmount = new AtomicInteger();
	private AtomicInteger matchingActualFixedFeeAmount = new AtomicInteger();
	private AtomicInteger nonMatchingActualFixedFeeAmount = new AtomicInteger();
	private AtomicInteger matchingActualPercentFeeAmount = new AtomicInteger();
	private AtomicInteger nonMatchingActualPercentFeeAmount = new AtomicInteger();

	// statistics for FX fields (during placeholder generation)
	private AtomicInteger matchingFXAuditTrail = new AtomicInteger();
	private AtomicInteger nonMatchingFXAuditTrail = new AtomicInteger();
	private AtomicInteger matchingFXExchangeRate = new AtomicInteger();
	private AtomicInteger nonMatchingFXExchangeRate = new AtomicInteger();
	private AtomicInteger matchingFXAmountFrom = new AtomicInteger();
	private AtomicInteger nonMatchingFXAmountFrom = new AtomicInteger();
	private AtomicInteger matchingFXAmountTo = new AtomicInteger();
	private AtomicInteger nonMatchingFXAmountTo = new AtomicInteger();
	private AtomicInteger matchingFXAmount = new AtomicInteger();
	private AtomicInteger nonMatchingFXAmount = new AtomicInteger();
	private AtomicInteger matchingFXUsdAmount = new AtomicInteger();
	private AtomicInteger nonMatchingFXUsdAmount = new AtomicInteger();

	// statistics for FX-Refund fields (during placeholder generation)
	private AtomicInteger matchingFXRefundAuditTrail = new AtomicInteger();
	private AtomicInteger nonMatchingFXRefundAuditTrail = new AtomicInteger();
	private AtomicInteger matchingFXRefundExchangeRate = new AtomicInteger();
	private AtomicInteger nonMatchingFXRefundExchangeRate = new AtomicInteger();
	private AtomicInteger matchingFXRefundAmountFrom = new AtomicInteger();
	private AtomicInteger nonMatchingFXRefundAmountFrom = new AtomicInteger();
	private AtomicInteger matchingFXRefundAmountTo = new AtomicInteger();
	private AtomicInteger nonMatchingFXRefundAmountTo = new AtomicInteger();
	private AtomicInteger matchingFXRefundAmount = new AtomicInteger();
	private AtomicInteger nonMatchingFXRefundAmount = new AtomicInteger();
	private AtomicInteger matchingFXRefundUsdAmount = new AtomicInteger();
	private AtomicInteger nonMatchingFXRefundUsdAmount = new AtomicInteger();

	// statistics for fee X row fields (during placeholder generation)
	private AtomicInteger matchingFeeXAuditTrail = new AtomicInteger();
	private AtomicInteger nonMatchingFeeXAuditTrail = new AtomicInteger();
	private AtomicInteger matchingXTotalFeeAmount = new AtomicInteger();
	private AtomicInteger nonMatchingXTotalFeeAmount = new AtomicInteger();
	private AtomicInteger matchingXActualFixedFeeAmount = new AtomicInteger();
	private AtomicInteger nonMatchingXActualFixedFeeAmount = new AtomicInteger();
	private AtomicInteger matchingXActualPercentFeeAmount = new AtomicInteger();
	private AtomicInteger nonMatchingXActualPercentFeeAmount = new AtomicInteger();

	// statistics for fee X Refund row fields (during placeholder generation)
	private AtomicInteger matchingFeeXRefundAuditTrail = new AtomicInteger();
	private AtomicInteger nonMatchingFeeXRefundAuditTrail = new AtomicInteger();
	private AtomicInteger matchingXRefundTotalFeeAmount = new AtomicInteger();
	private AtomicInteger nonMatchingXRefundTotalFeeAmount = new AtomicInteger();
	private AtomicInteger matchingXRefundActualFixedFeeAmount = new AtomicInteger();
	private AtomicInteger nonMatchingXRefundActualFixedFeeAmount = new AtomicInteger();
	private AtomicInteger matchingXRefundActualPercentFeeAmount = new AtomicInteger();
	private AtomicInteger nonMatchingXRefundActualPercentFeeAmount = new AtomicInteger();

	public void incrementExceptionCount() {
		exceptionCount.incrementAndGet();
	}

	public void incrementSenderPaysFeeCount() {
		senderPaysFeeCount.incrementAndGet();
	}

	public void incrementRecipientPaysFeeCount() {
		recipientPaysFeeCount.incrementAndGet();
	}

	public void incrementMoreThan1FRow() {
		moreThan1FRow.incrementAndGet();
	}

	public void incrementSenderSideCurrencyConversionCount() {
		senderSideCurrencyConversionCount.incrementAndGet();
	}

	public void incrementRecipientSideCurrencyConversionCount() {
		recipientSideCurrencyConversionCount.incrementAndGet();
	}

	public void incrementMoreThan1XRow() {
		moreThan1XRow.incrementAndGet();
	}

	public void incrementMatchingFeeAuditTrail() {
		matchingFeeAuditTrail.incrementAndGet();
	}

	public void incrementNonMatchingFeeAuditTrail() {
		nonMatchingFeeAuditTrail.incrementAndGet();
	}

	public void incrementMatchingTotalFeeAmount() {
		matchingTotalFeeAmount.incrementAndGet();
	}

	public void incrementNonMatchingTotalFeeAmount() {
		nonMatchingTotalFeeAmount.incrementAndGet();
	}

	public void incrementMatchingActualFixedFeeAmount() {
		matchingActualFixedFeeAmount.incrementAndGet();
	}

	public void incrementNonMatchingActualFixedFeeAmount() {
		nonMatchingActualFixedFeeAmount.incrementAndGet();
	}

	public void incrementMatchingActualPercentFeeAmount() {
		matchingActualPercentFeeAmount.incrementAndGet();
	}

	public void incrementNonMatchingActualPercentFeeAmount() {
		nonMatchingActualPercentFeeAmount.incrementAndGet();
	}

	public void incrementMatchingFXAuditTrail() {
		matchingFXAuditTrail.incrementAndGet();
	}

	public void incrementNonMatchingFXAuditTrail() {
		nonMatchingFXAuditTrail.incrementAndGet();
	}

	public void incrementMatchingFXExchangeRate() {
		matchingFXExchangeRate.incrementAndGet();
	}

	public void incrementNonMatchingFXExchangeRate() {
		nonMatchingFXExchangeRate.incrementAndGet();
	}

	public void incrementMatchingFXAmountFrom() {
		matchingFXAmountFrom.incrementAndGet();
	}

	public void incrementNonMatchingFXAmountFrom() {
		nonMatchingFXAmountFrom.incrementAndGet();
	}

	public void incrementMatchingFXAmountTo() {
		matchingFXAmountTo.incrementAndGet();
	}

	public void incrementNonMatchingFXAmountTo() {
		nonMatchingFXAmountTo.incrementAndGet();
	}

	public void incrementMatchingFXAmount() {
		matchingFXAmount.incrementAndGet();
	}

	public void incrementNonMatchingFXAmount() {
		nonMatchingFXAmount.incrementAndGet();
	}

	public void incrementMatchingFXUsdAmount() {
		matchingFXUsdAmount.incrementAndGet();
	}

	public void incrementNonMatchingFXUsdAmount() {
		nonMatchingFXUsdAmount.incrementAndGet();
	}

	public void incrementMatchingFXRefundAuditTrail() {
		matchingFXRefundAuditTrail.incrementAndGet();
	}

	public void incrementNonMatchingFXRefundAuditTrail() {
		nonMatchingFXRefundAuditTrail.incrementAndGet();
	}

	public void incrementMatchingFXRefundExchangeRate() {
		matchingFXRefundExchangeRate.incrementAndGet();
	}

	public void incrementNonMatchingFXRefundExchangeRate() {
		nonMatchingFXRefundExchangeRate.incrementAndGet();
	}

	public void incrementMatchingFXRefundAmountFrom() {
		matchingFXRefundAmountFrom.incrementAndGet();
	}

	public void incrementNonMatchingFXRefundAmountFrom() {
		nonMatchingFXRefundAmountFrom.incrementAndGet();
	}

	public void incrementMatchingFXRefundAmountTo() {
		matchingFXRefundAmountTo.incrementAndGet();
	}

	public void incrementNonMatchingFXRefundAmountTo() {
		nonMatchingFXRefundAmountTo.incrementAndGet();
	}

	public void incrementMatchingFXRefundAmount() {
		matchingFXRefundAmount.incrementAndGet();
	}

	public void incrementNonMatchingFXRefundAmount() {
		nonMatchingFXRefundAmount.incrementAndGet();
	}

	public void incrementMatchingFXRefundUsdAmount() {
		matchingFXRefundUsdAmount.incrementAndGet();
	}

	public void incrementNonMatchingFXRefundUsdAmount() {
		nonMatchingFXRefundUsdAmount.incrementAndGet();
	}

	public void incrementMatchingFeeXAuditTrail() {
		matchingFeeXAuditTrail.incrementAndGet();
	}

	public void incrementNonMatchingFeeXAuditTrail() {
		nonMatchingFeeXAuditTrail.incrementAndGet();
	}

	public void incrementMatchingXTotalFeeAmount() {
		matchingXTotalFeeAmount.incrementAndGet();
	}

	public void incrementNonMatchingXTotalFeeAmount() {
		nonMatchingXTotalFeeAmount.incrementAndGet();
	}

	public void incrementMatchingXActualFixedFeeAmount() {
		matchingXActualFixedFeeAmount.incrementAndGet();
	}

	public void incrementNonMatchingXActualFixedFeeAmount() {
		nonMatchingXActualFixedFeeAmount.incrementAndGet();
	}

	public void incrementMatchingXActualPercentFeeAmount() {
		matchingXActualPercentFeeAmount.incrementAndGet();
	}

	public void incrementNonMatchingXActualPercentFeeAmount() {
		nonMatchingXActualPercentFeeAmount.incrementAndGet();
	}

	public void incrementMatchingFeeXRefundAuditTrail() {
		matchingFeeXRefundAuditTrail.incrementAndGet();
	}

	public void incrementNonMatchingFeeXRefundAuditTrail() {
		nonMatchingFeeXRefundAuditTrail.incrementAndGet();
	}

	public void incrementMatchingXRefundTotalFeeAmount() {
		matchingXRefundTotalFeeAmount.incrementAndGet();
	}

	public void incrementNonMatchingXRefundTotalFeeAmount() {
		nonMatchingXRefundTotalFeeAmount.incrementAndGet();
	}

	public void incrementMatchingXRefundActualFixedFeeAmount() {
		matchingXRefundActualFixedFeeAmount.incrementAndGet();
	}

	public void incrementNonMatchingXRefundActualFixedFeeAmount() {
		nonMatchingXRefundActualFixedFeeAmount.incrementAndGet();
	}

	public void incrementMatchingXRefundActualPercentFeeAmount() {
		matchingXRefundActualPercentFeeAmount.incrementAndGet();
	}

	public void incrementNonMatchingXRefundActualPercentFeeAmount() {
		nonMatchingXRefundActualPercentFeeAmount.incrementAndGet();
	}

	public void print() {
		LOGGER.warn("exceptionCount:{}", exceptionCount);
		LOGGER.warn("senderPaysFeeCount:{}, recipientPaysFeeCount:{}, moreThan1FRow:{}", senderPaysFeeCount,
				recipientPaysFeeCount, moreThan1FRow);
		LOGGER.warn("senderSideCurrencyConversionCount:{}, recipientSideCurrencyConversionCount:{}, moreThan1XRow:{}",
				senderSideCurrencyConversionCount, recipientSideCurrencyConversionCount, moreThan1XRow);
		// fee F row related stats
		LOGGER.warn("matchingActualFixedFeeAmount:{}, nonMatchingActualFixedFeeAmount:{}", matchingActualFixedFeeAmount,
				nonMatchingActualFixedFeeAmount);
		LOGGER.warn("matchingActualPercentFeeAmount:{}, nonMatchingFeeAuditTrail:{}", matchingActualPercentFeeAmount,
				nonMatchingActualPercentFeeAmount);
		LOGGER.warn("matchingFeeAuditTrail:{}, nonMatchingFeeAuditTrail:{}", matchingFeeAuditTrail,
				nonMatchingFeeAuditTrail);
		LOGGER.warn("matchingTotalFeeAmount:{}, nonMatchingTotalFeeAmount:{}", matchingTotalFeeAmount,
				nonMatchingTotalFeeAmount);

		// fee X row related stats
		LOGGER.warn("matchingFeeXAuditTrail:{}, nonMatchingFeeXAuditTrail:{}", matchingFeeXAuditTrail,
				nonMatchingFeeXAuditTrail);
		LOGGER.warn("matchingXTotalFeeAmount:{}, nonMatchingXTotalFeeAmount:{}", matchingXTotalFeeAmount,
				nonMatchingXTotalFeeAmount);
		LOGGER.warn("matchingXActualFixedFeeAmount:{}, nonMatchingXActualFixedFeeAmount:{}",
				matchingXActualFixedFeeAmount, nonMatchingXActualFixedFeeAmount);
		LOGGER.warn("matchingXActualPercentFeeAmount:{}, nonMatchingXActualPercentFeeAmount:{}",
				matchingXActualPercentFeeAmount, nonMatchingXActualPercentFeeAmount);

		// FX related stats
		LOGGER.warn("matchingFXAuditTrail:{}, nonMatchingFXAuditTrail:{}", matchingFXAuditTrail,
				nonMatchingFXAuditTrail);
		LOGGER.warn("matchingFXExchangeRate:{}, nonMatchingFXExchangeRate:{}", matchingFXExchangeRate,
				nonMatchingFXExchangeRate);
		LOGGER.warn("matchingFXAmountFrom:{}, nonMatchingFXAmountFrom:{}", matchingFXAmountFrom,
				nonMatchingFXAmountFrom);
		LOGGER.warn("matchingFXAmountTo:{}, nonMatchingFXAmountTo:{}", matchingFXAmountTo, nonMatchingFXAmountTo);
		LOGGER.warn("matchingFXAmount:{}, nonMatchingFXAmount:{}", matchingFXAmount, nonMatchingFXAmount);
		LOGGER.warn("matchingFXUsdAmount:{}, nonMatchingFXUsdAmount:{}", matchingFXUsdAmount, nonMatchingFXUsdAmount);

		// FX-Refund related stats
		LOGGER.warn("matchingFXRefundAuditTrail:{}, nonMatchingFXRefundAuditTrail:{}", matchingFXRefundAuditTrail,
				nonMatchingFXRefundAuditTrail);
		LOGGER.warn("matchingFXRefundExchangeRate:{}, nonMatchingFXRefundExchangeRate:{}", matchingFXRefundExchangeRate,
				nonMatchingFXRefundExchangeRate);
		LOGGER.warn("matchingFXRefundAmountFrom:{}, nonMatchingFXRefundAmountFrom:{}", matchingFXRefundAmountFrom,
				nonMatchingFXRefundAmountFrom);
		LOGGER.warn("matchingFXRefundAmountTo:{}, nonMatchingFXRefundAmountTo:{}", matchingFXRefundAmountTo,
				nonMatchingFXRefundAmountTo);
		LOGGER.warn("matchingFXRefundAmount:{}, nonMatchingFXRefundAmount:{}", matchingFXRefundAmount,
				nonMatchingFXRefundAmount);
		LOGGER.warn("matchingFXRefundUsdAmount:{}, nonMatchingFXRefundUsdAmount:{}", matchingFXRefundUsdAmount,
				nonMatchingFXRefundUsdAmount);

		// fee X Refund row related stats
		LOGGER.warn("matchingFeeXRefundAuditTrail:{}, nonMatchingFeeXRefundAuditTrail:{}", matchingFeeXRefundAuditTrail,
				nonMatchingFeeXRefundAuditTrail);
		LOGGER.warn("matchingXRefundTotalFeeAmount:{}, nonMatchingXRefundTotalFeeAmount:{}",
				matchingXRefundTotalFeeAmount, nonMatchingXRefundTotalFeeAmount);
		LOGGER.warn("matchingXRefundActualFixedFeeAmount:{}, nonMatchingXRefundActualFixedFeeAmount:{}",
				matchingXRefundActualFixedFeeAmount, nonMatchingXRefundActualFixedFeeAmount);
		LOGGER.warn("matchingXRefundActualPercentFeeAmount:{}, nonMatchingXRefundActualPercentFeeAmount:{}",
				matchingXRefundActualPercentFeeAmount, nonMatchingXRefundActualPercentFeeAmount);
	}
}
