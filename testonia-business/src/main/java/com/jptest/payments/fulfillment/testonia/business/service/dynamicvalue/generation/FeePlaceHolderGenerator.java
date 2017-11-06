package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.FeePlaceHolderResolver;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FeePlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderDTO;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.util.FXDataTrimmer;
import com.jptest.types.Currency;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xpath.XPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps in generating {@link FeePlaceHolder} from input JSON file
 * and sender/recipient golden file. It returns FeePlaceHolder object with
 * values retrieved from FFX bridge call
 * <p>
 * FeePlaceHolder object can then be used to compare Fee data in golden file
 * with data retrieved from FFX call.
 * <p>
 * The class exposes methods to do these comparisons and replace Fee field
 * values with placeholder strings.
 * <p>
 * It replaces below "fee_history" rows of type 'F' in sender and recipient
 * golden files with placeholder strings:
 * <li>data - ${FEE:AUDIT-TRAIL()}</li>
 * <li>actual_fixed_fee - ${FEE:ACTUAL-FIXED-FEE()}</li>
 * <li>actual_percent_fee - ${FEE:ACTUAL-PERCENT-FEE}</li>
 * <li>total_fee_amount - ${FEE:TOTAL-FEE-AMOUNT()}</li>
 * 
 */
@Singleton
public class FeePlaceHolderGenerator extends PlaceHolderGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeePlaceHolderGenerator.class);

	private static final String XPATH_FOR_FEE_HISTORY_F_ROW = "//validation_fixture/*/fee_history/item[transaction_type/text() = 'F']";

	private static final String XPATH_FOR_WTRANSACTION_F_ROW = "//validation_fixture/*/wtransaction/item[type/text() = 'F']";
	
    private static final String XPATH_FOR_WSUBBALANCE_ROW = "//validation_fixture/*/wsubbalance_transaction/item[subbalance_type/text() = 'T']";

	private static final String AUDIT_TRAIL_FIELD = "data";
	private static final String ACTUAL_FIXED_FEE_FIELD = "actual_fixed_fee";
	private static final String ACTUAL_PERCENT_FEE_FIELD = "actual_percent_fee";
	private static final String TOTAL_FEE_AMOUNT_FIELD = "total_fee_amount";
	private static final String AMOUNT_FIELD = "amount";
	private static final String USD_AMOUNT_FIELD = "usd_amount";
	private static final String REASON_CODE_FIELD = "reason_code";

	private static final String TEXT_ATTRIBUTE = "text()";

    private static final String CHARGEBACK_FEE_TYPE = "CHARGEBACK";

	private static final String ACTUAL_FIXED_FEE_XPATH_FOR_FEE_HISTORY_F_ROW = XPATH_FOR_FEE_HISTORY_F_ROW + "/"
			+ ACTUAL_FIXED_FEE_FIELD + "/" + TEXT_ATTRIBUTE;
	private static final String ACTUAL_PERCENT_FEE_XPATH_FOR_FEE_HISTORY_F_ROW = XPATH_FOR_FEE_HISTORY_F_ROW + "/"
			+ ACTUAL_PERCENT_FEE_FIELD + "/" + TEXT_ATTRIBUTE;
	
	private static final String TOTAL_FEE_AMOUNT_XPATH_FOR_FEE_HISTORY_F_ROW = XPATH_FOR_FEE_HISTORY_F_ROW + "/"
			+ TOTAL_FEE_AMOUNT_FIELD + "/" + TEXT_ATTRIBUTE;
	private static final String AUDIT_TRAIL_XPATH_FOR_FEE_HISTORY_F_ROW = XPATH_FOR_FEE_HISTORY_F_ROW + "/"
			+ AUDIT_TRAIL_FIELD + "/" + TEXT_ATTRIBUTE;
	
	private enum WsubbalanceReasonCode {
	    REASON_CODE_Z("Z"), REASON_CODE_Y("Y");
	    
	    private String name;
	    
	    public String getName() {
	        return this.name;
	    }
	    
	    private WsubbalanceReasonCode(String name) {
	        this.name = name;
	    }
	}

	@Inject
	private FeePlaceHolderResolver feePlaceHolderResolver;

    public List<FeePlaceHolder> getFeePlaceHolders(GoldenFilePlaceHolderDTO dto, Currency transactionAmount,
            String feeType)
			throws TransformerException {
		// 1. create fee placeholder from input
        List<FeePlaceHolder> feePlaceHolders = createFeePlaceHolders(dto, transactionAmount, feeType);
        // 2. resolve placeholder by making fee bridge call
        if (CollectionUtils.isNotEmpty(feePlaceHolders)) {
            for (FeePlaceHolder feePlaceHolder : feePlaceHolders) {
                try {
                    feePlaceHolderResolver.setPlaceHolderValues(feePlaceHolder);
                }
                catch (Exception e) {
                    LOGGER.error("Error occurred getting value for placeholder {} in {}",
                            feePlaceHolder.buildKey(StringUtils.EMPTY), dto.getPath());
                    throw e;
                }
            }

		}
        return feePlaceHolders;
	}

	/**
	 * Create placeholder string from input and return FeePlaceHolder object
	 * <p>
	 * fee bridge call understands "balance" and does not understand "holding".
	 * GoldenFilePlaceHolderDTO.getFundingSourceForFee() takes care of this
	 * logic
	 */
    private List<FeePlaceHolder> createFeePlaceHolders(GoldenFilePlaceHolderDTO dto, Currency transactionAmount,
			String feeType) throws TransformerException {
        List<FeePlaceHolder> feePlaceHolders = new ArrayList<>();
		NodeList feeNodes = XPathAPI.selectNodeList(dto.getGoldenFileDocument(), XPATH_FOR_FEE_HISTORY_F_ROW);
				
        // generate placeholder for 1st f row
		if (feeNodes.getLength() > 0) {
			/*StringBuilder sb = new StringBuilder(FeePlaceHolder.FEE_PLACEHOLDER_PREFIX + PlaceHolder.FUNCTION_PREFIX)
					.append(transactionAmount.getCurrencyCode()).append(PlaceHolder.PARAMETER_DELIMITER) // currencyIn
					.append(transactionAmount.getAmount()).append(PlaceHolder.PARAMETER_DELIMITER) // fromAmount
					.append(dto.getSender().getCountry()).append(PlaceHolder.PARAMETER_DELIMITER) // transaction
					// country
					.append(dto.getRecipient().getCurrency()).append(PlaceHolder.PARAMETER_DELIMITER) // currencyOut
					.append(dto.getRecipient().getCountry()).append(PlaceHolder.PARAMETER_DELIMITER) // recipientCountry
					.append(feeType).append(PlaceHolder.PARAMETER_DELIMITER) // feeType
                    .append(dto.getFundingSourceForFee()).append(PlaceHolder.FUNCTION_SUFFIX) // fundingSource
					.append(PlaceHolder.PLACEHOLDER_SUFFIX); // fundingSource
            feePlaceHolders.add(new FeePlaceHolder(sb.toString()));*/
		}
        // generate placeholder for 2nd f row - fee type is hardcoded to CHARGEBACK
        if (feeNodes.getLength() > 1) {
            LOGGER.warn("Found golden file with more than 1 F Row {}", dto.getPath());
            dto.getStats().incrementMoreThan1FRow();
            /*StringBuilder sb = new StringBuilder(FeePlaceHolder.FEE_PLACEHOLDER_PREFIX + PlaceHolder.FUNCTION_PREFIX)
                    .append(transactionAmount.getCurrencyCode()).append(PlaceHolder.PARAMETER_DELIMITER) // currencyIn
                    .append(transactionAmount.getAmount()).append(PlaceHolder.PARAMETER_DELIMITER) // fromAmount
                    .append(dto.getSender().getCountry()).append(PlaceHolder.PARAMETER_DELIMITER) // transaction
                    // country
                    .append(dto.getRecipient().getCurrency()).append(PlaceHolder.PARAMETER_DELIMITER) // currencyOut
                    .append(dto.getRecipient().getCountry()).append(PlaceHolder.PARAMETER_DELIMITER) // recipientCountry
                    .append(CHARGEBACK_FEE_TYPE).append(PlaceHolder.PARAMETER_DELIMITER) // feeType
                    .append(dto.getFundingSourceForFee()).append(PlaceHolder.PARAMETER_DELIMITER) // fundingSource
                    .append(dto.getChargeBackGrade()).append(PlaceHolder.FUNCTION_SUFFIX)
                    .append(PlaceHolder.PLACEHOLDER_SUFFIX); // fundingSource
            feePlaceHolders.add(new FeePlaceHolder(sb.toString()));*/
        }
        return feePlaceHolders;
	}

	/**
	 * checks correctness of feeplaceholder formula by matching actual fixed fee
	 * in golden file with the one retrieved by formula
	 */
    public void compareAndReplaceActualFixedFee(GoldenFilePlaceHolderDTO dto, FeePlaceHolder feePlaceHolder,
            int rowIndex) {
		try {
			Node fRowFixedFeeNode = XPathAPI
                    .selectNodeList(dto.getGoldenFileDocument(), ACTUAL_FIXED_FEE_XPATH_FOR_FEE_HISTORY_F_ROW)
                    .item(rowIndex);
			String expectedFixedFee = fRowFixedFeeNode.getTextContent();
			String fixedFeeKey = feePlaceHolder.buildKey(FeePlaceHolder.FIELD_ACTUAL_FIXED_FEE);
			String fixedFeeAmount = feePlaceHolder.getValue(fixedFeeKey);

			String actualString = expectedFixedFee.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? fixedFeeKey
					: fixedFeeAmount;

			if (expectedFixedFee.equals(actualString)) {
				dto.getStats().incrementMatchingActualFixedFeeAmount();
				if (!expectedFixedFee.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] Fixed Fee \n{} {}\n{} {}",
							new Object[] { expectedFixedFee, dto.getPath(), actualString, fixedFeeKey });
					replaceValue(dto, fRowFixedFeeNode, fixedFeeKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingActualFixedFeeAmount();
				LOGGER.error("[Not Matched] Fixed Fee \n{} {}\n{} {}",
						new Object[] { expectedFixedFee, dto.getPath(), actualString, fixedFeeKey });
				replaceValue(dto, fRowFixedFeeNode, fixedFeeKey, false);
			}
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing fixed fee for {}", dto.getPath(), expt);
		}

	}

	/**
	 * checks correctness of feeplaceholder formula by matching actual percent
	 * fee in golden file with the one retrieved by formula
	 */
    public void compareAndReplaceActualPercentFee(GoldenFilePlaceHolderDTO dto, FeePlaceHolder feePlaceHolder,
            int rowIndex) {
		try {
			Node fRowPercentFeeNode = XPathAPI
					.selectNodeList(dto.getGoldenFileDocument(), ACTUAL_PERCENT_FEE_XPATH_FOR_FEE_HISTORY_F_ROW)
                    .item(rowIndex);
			String expectedPercentFee = fRowPercentFeeNode.getTextContent();
			String percentFeeKey = feePlaceHolder.buildKey(FeePlaceHolder.FIELD_ACTUAL_PERCENT_FEE);
			String percentFeeAmount = feePlaceHolder.getValue(percentFeeKey);

			String actualString = expectedPercentFee.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? percentFeeKey
					: percentFeeAmount;

			if (expectedPercentFee.equals(actualString)) {
				dto.getStats().incrementMatchingActualPercentFeeAmount();
				if (!expectedPercentFee.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] Percent Fee \n{} {}\n{} {}",
							new Object[] { expectedPercentFee, dto.getPath(), actualString, percentFeeKey });
					replaceValue(dto, fRowPercentFeeNode, percentFeeKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingActualPercentFeeAmount();
				LOGGER.error("[Not Matched] Percent Fee \n{} {}\n{} {}",
						new Object[] { expectedPercentFee, dto.getPath(), actualString, percentFeeKey });
				replaceValue(dto, fRowPercentFeeNode, percentFeeKey, false);
			}

		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing percent fee for {}", dto.getPath(), expt);
		}

	}

	/**
	 * checks correctness of feeplaceholder formula by matching fee amount in
	 * golden file with the one retrieved by formula
	 */
    public void compareAndReplaceTotalFeeAmount(GoldenFilePlaceHolderDTO dto, FeePlaceHolder feePlaceHolder,
            int rowIndex) {
		try {
			Node fRowFeeAmountNode = XPathAPI
                    .selectNodeList(dto.getGoldenFileDocument(), TOTAL_FEE_AMOUNT_XPATH_FOR_FEE_HISTORY_F_ROW)
                    .item(rowIndex);
			String expectedTotalFeeAmount = fRowFeeAmountNode.getTextContent();
			String totalFeeAmountKey = feePlaceHolder.buildKey(FeePlaceHolder.FIELD_TOTAL_FEE_AMOUNT);
			String actualTotalFeeAmount = feePlaceHolder.getValue(totalFeeAmountKey);

			String actualString = expectedTotalFeeAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? totalFeeAmountKey
					: actualTotalFeeAmount;

			if (expectedTotalFeeAmount.equals(actualString)) {
				dto.getStats().incrementMatchingTotalFeeAmount();
				if (!expectedTotalFeeAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] Fee Amount\n{} {}\n{} {}",
							new Object[] { expectedTotalFeeAmount, dto.getPath(), actualString, totalFeeAmountKey });
					replaceValue(dto, fRowFeeAmountNode, totalFeeAmountKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingTotalFeeAmount();
				LOGGER.error("[Not Matched] Fee Amount\n{} {}\n{} {}",
						new Object[] { expectedTotalFeeAmount, dto.getPath(), actualString, totalFeeAmountKey });
				replaceValue(dto, fRowFeeAmountNode, totalFeeAmountKey, false);
			}
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing total fee amount for {}", dto.getPath(), expt);
		}

	}
    
    /**
     * checks correctness of wsubbalance amount formula by matching actual wsubbalance amount in golden file with the
     * one retrieved by formula
     */
    public void compareAndReplaceWsubbalanceAmount(GoldenFilePlaceHolderDTO dto, FeePlaceHolder feePlaceHolder) {
        try {
            NodeList wsubbalanceRowNodes = XPathAPI
                    .selectNodeList(dto.getGoldenFileDocument(), XPATH_FOR_WSUBBALANCE_ROW);
            
            if (0 == wsubbalanceRowNodes.getLength())
                return;

            String wsubbalanceAmountKey = feePlaceHolder.buildKey(FeePlaceHolder.FIELD_WSUBBALANCE_AMOUNT);
            String wsubbalanceAmountValue = feePlaceHolder.getValue(wsubbalanceAmountKey);
            
            String[] negatedFieldAndValue = getNegatedFieldAndValue(dto, feePlaceHolder, wsubbalanceAmountKey);
            
            String wsubbalanceAmountKeyNegated = negatedFieldAndValue[0];
            String wsubbalanceAmountValueNegated = negatedFieldAndValue[1];

            for (int i=0; i<wsubbalanceRowNodes.getLength(); ++i) {

                WsubbalanceReasonCode reason_code = null;
                
                Node wsubbalanceRowNode = wsubbalanceRowNodes.item(i);
                NodeList elementList = wsubbalanceRowNode.getChildNodes();
                
                Node amountNode = null;
                String expectedWsubbalanceAmount = null;
                String actualString = null;
                
                for (int i1=0; i1<elementList.getLength(); ++i1) {
                    if (AMOUNT_FIELD.equals(elementList.item(i1).getNodeName())) {
                        amountNode = elementList.item(i1);
                        expectedWsubbalanceAmount = amountNode.getTextContent();
                    }
                    if (REASON_CODE_FIELD.equals(elementList.item(i1).getNodeName())
                            && WsubbalanceReasonCode.REASON_CODE_Z.getName().equals(elementList.item(i1).getTextContent())) {
                        reason_code = WsubbalanceReasonCode.REASON_CODE_Z;
                    } else if (REASON_CODE_FIELD.equals(elementList.item(i1).getNodeName())
                            && WsubbalanceReasonCode.REASON_CODE_Y.getName().equals(elementList.item(i1).getTextContent())) {
                        reason_code = WsubbalanceReasonCode.REASON_CODE_Y;
                    }
                }
                
                switch (reason_code) {
                    case REASON_CODE_Z:
                        
                        actualString = expectedWsubbalanceAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)
                        ? wsubbalanceAmountKey
                        : wsubbalanceAmountValue;
                        
                        if (expectedWsubbalanceAmount.equals(actualString)) {
                            if (!expectedWsubbalanceAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
                                LOGGER.error("[Matched] Wsubbalance Amount\n{} {}\n{} {}",
                                        new Object[] { expectedWsubbalanceAmount, dto.getPath(), actualString, wsubbalanceAmountKey });
                                replaceValue(dto, amountNode, wsubbalanceAmountKey, true);
                            }
                        } else {
                            LOGGER.error("[Not Matched] Wsubbalance Amount\n{} {}\n{} {}",
                                    new Object[] { expectedWsubbalanceAmount, dto.getPath(), actualString, wsubbalanceAmountKey });
                            replaceValue(dto, amountNode, wsubbalanceAmountKey, false);
                        }
                        
                        break;
                    case REASON_CODE_Y:
                        
                        actualString = expectedWsubbalanceAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)
                        ? wsubbalanceAmountKeyNegated
                        : wsubbalanceAmountValueNegated;
                        
                        if (expectedWsubbalanceAmount.equals(actualString)) {
                            if (!expectedWsubbalanceAmount.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
                                LOGGER.error("[Matched] Wsubbalance Amount\n{} {}\n{} {}",
                                        new Object[] { expectedWsubbalanceAmount, dto.getPath(), actualString, wsubbalanceAmountKeyNegated });
                                replaceValue(dto, amountNode, wsubbalanceAmountKey, true);
                            }
                        } else {
                            LOGGER.error("[Not Matched] Wsubbalance Amount\n{} {}\n{} {}",
                                    new Object[] { expectedWsubbalanceAmount, dto.getPath(), actualString, wsubbalanceAmountKeyNegated });
                            replaceValue(dto, amountNode, wsubbalanceAmountKeyNegated, false);
                        }
                        
                        break;
                    default:
                        break;
                }
            }
        }
        catch (TransformerException expt) {
            LOGGER.error("Error occurred comparing and replacing wsubbalance amount for {}", dto.getPath(), expt);
        }

    }

	/**
	 * checks correctness of feeplaceholder formula by matching audit trail in
	 * golden file with the one retrieved by formula
	 */
    public void compareAndReplaceAuditTrail(GoldenFilePlaceHolderDTO dto, FeePlaceHolder feePlaceHolder, int rowIndex) {
		try {
			Node feeAuditTrailNode = XPathAPI
                    .selectNodeList(dto.getGoldenFileDocument(), AUDIT_TRAIL_XPATH_FOR_FEE_HISTORY_F_ROW)
                    .item(rowIndex);
            String expectedAuditTrail = feeAuditTrailNode.getTextContent();
			String auditTrailKey = feePlaceHolder.buildKey(FeePlaceHolder.FIELD_AUDIT_TRAIL);
			String actualAuditTrail = feePlaceHolder.getValue(auditTrailKey);

			String actualString = expectedAuditTrail.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? auditTrailKey
					: actualAuditTrail;

            if (!expectedAuditTrail.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
                expectedAuditTrail = FXDataTrimmer.trimFeeAuditTrail(expectedAuditTrail);
            }

			if (expectedAuditTrail.equals(actualString)) {
				dto.getStats().incrementMatchingFeeAuditTrail();
				if (!expectedAuditTrail.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
					LOGGER.error("[Matched] Fee Audit Trail:\n{} {}\n{} {}",
							new Object[] { expectedAuditTrail, dto.getPath(), actualString, auditTrailKey });
					replaceValue(dto, feeAuditTrailNode, auditTrailKey, true);
				}
			} else {
				dto.getStats().incrementNonMatchingFeeAuditTrail();
				LOGGER.error("[Not Matched] Fee Audit Trail:\n{} {}\n{} {}",
						new Object[] { expectedAuditTrail, dto.getPath(), actualString, auditTrailKey });
				replaceValue(dto, feeAuditTrailNode, auditTrailKey, false);
			}
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing audit trail for {}", dto.getPath(), expt);
		}
	}

	/**
	 * checks correctness of feeplaceholder formula by matching amount and
	 * usd-amount in golden file with the one retrieved by formula
	 */
    public void compareAndReplaceAmountAndUsdAmount(GoldenFilePlaceHolderDTO dto, FeePlaceHolder feePlaceHolder,
            int rowIndex) {
		try {
			Node wTransactionFRowNode = XPathAPI
                    .selectNodeList(dto.getGoldenFileDocument(), XPATH_FOR_WTRANSACTION_F_ROW).item(rowIndex);
			compareAndReplaceAmount(dto, feePlaceHolder, getChildNode(wTransactionFRowNode, AMOUNT_FIELD));
			compareAndReplaceUsdAmount(dto, feePlaceHolder, getChildNode(wTransactionFRowNode, USD_AMOUNT_FIELD));
			compareAndReplaceActualPercentFee(dto, feePlaceHolder,
					getChildNode(wTransactionFRowNode, ACTUAL_PERCENT_FEE_FIELD));
		} catch (TransformerException expt) {
			LOGGER.error("Error occurred comparing and replacing amount and usd-amount for {}", dto.getPath(), expt);
		}
	}

	private void compareAndReplaceAmount(GoldenFilePlaceHolderDTO dto, FeePlaceHolder feePlaceHolder, Node amountNode) {
		String expectedValue = amountNode.getTextContent();
		String originalKey = feePlaceHolder.buildKey(FeePlaceHolder.FIELD_TOTAL_FEE_AMOUNT);
		String[] negatedFieldAndValue = getNegatedFieldAndValue(dto, feePlaceHolder, originalKey);
		String fieldKey = negatedFieldAndValue[0];
		String actualValue = negatedFieldAndValue[1];

		String actualString = expectedValue.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? fieldKey : actualValue;

		if (expectedValue.equals(actualString)) {
			// dto.getStats().incrementMatching();
			if (!expectedValue.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
				LOGGER.error("[Matched] Wtransaction F Row Amount:\n{} {}\n{} {}",
						new Object[] { expectedValue, dto.getPath(), actualString, fieldKey });
				replaceValue(dto, amountNode, fieldKey, true);
			}
		} else {
			// dto.getStats().incrementNonMatchingFeeAuditTrail();
			LOGGER.error("[Not Matched] Wtransaction F Row Amount:\n{} {}\n{} {}",
					new Object[] { expectedValue, dto.getPath(), actualString, fieldKey });
			replaceValue(dto, amountNode, fieldKey, false);
		}
	}

	private void compareAndReplaceUsdAmount(GoldenFilePlaceHolderDTO dto, FeePlaceHolder feePlaceHolder,
			Node usdAmountNode) {
		String expectedValue = usdAmountNode.getTextContent();
		String originalKey = feePlaceHolder.buildKey(FeePlaceHolder.FIELD_TOTAL_FEE_USD_AMOUNT);
		String[] negatedFieldAndValue = getNegatedFieldAndValue(dto, feePlaceHolder, originalKey);
		String fieldKey = negatedFieldAndValue[0];
		String actualValue = negatedFieldAndValue[1];

		String actualString = expectedValue.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? fieldKey : actualValue;

		if (expectedValue.equals(actualString)) {
			// dto.getStats().incrementMatching();
			if (!expectedValue.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
				LOGGER.error("[Matched] Wtransaction F Row Usd-Amount:\n{} {}\n{} {}",
						new Object[] { expectedValue, dto.getPath(), actualString, fieldKey });
				replaceValue(dto, usdAmountNode, fieldKey, true);
			}
		} else {
			// dto.getStats().incrementNonMatchingFeeAuditTrail();
			LOGGER.error("[Not Matched] Wtransaction F Row Usd-Amount:\n{} {}\n{} {}",
					new Object[] { expectedValue, dto.getPath(), actualString, fieldKey });
			replaceValue(dto, usdAmountNode, fieldKey, false);
		}
	}

	private void compareAndReplaceActualPercentFee(GoldenFilePlaceHolderDTO dto, FeePlaceHolder feePlaceHolder,
			Node actualPercentFeeNode) {
		if (actualPercentFeeNode == null) {
			return;
		}
		String expectedValue = actualPercentFeeNode.getTextContent();
		String fieldKey = null;
		String actualValue = null;
		String originalKey = feePlaceHolder.buildKey(FeePlaceHolder.FIELD_ACTUAL_PERCENT_FEE);
        if (feePlaceHolder.isSenderSideFee()) {
			String[] negatedFieldAndValue = getNegatedFieldAndValue(dto, feePlaceHolder, originalKey);
			fieldKey = negatedFieldAndValue[0];
			actualValue = negatedFieldAndValue[1];
		} else {
			fieldKey = originalKey;
			actualValue = feePlaceHolder.getValue(fieldKey);
		}

		String actualString = expectedValue.startsWith(PlaceHolder.PLACEHOLDER_PREFIX) ? fieldKey : actualValue;

		if (expectedValue.equals(actualString)) {
			// dto.getStats().incrementMatching();
			if (!expectedValue.startsWith(PlaceHolder.PLACEHOLDER_PREFIX)) {
				LOGGER.error("[Matched] Wtransaction F Row actual percent fee:\n{} {}\n{} {}",
						new Object[] { expectedValue, dto.getPath(), actualString, fieldKey });
				replaceValue(dto, actualPercentFeeNode, fieldKey, true);
			}
		} else {
			// dto.getStats().incrementNonMatchingFeeAuditTrail();
			LOGGER.error("[Not Matched] Wtransaction F Row actual percent fee:\n{} {}\n{} {}",
					new Object[] { expectedValue, dto.getPath(), actualString, fieldKey });
			replaceValue(dto, actualPercentFeeNode, fieldKey, false);
		}
	}
}
