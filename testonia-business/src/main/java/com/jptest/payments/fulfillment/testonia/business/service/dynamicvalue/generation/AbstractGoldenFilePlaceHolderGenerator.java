package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import java.util.List;
import javax.inject.Inject;
import javax.xml.transform.TransformerException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xpath.XPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.jptest.payments.fulfillment.testonia.business.component.validation.FileRetrieverTask;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.AbstractFXPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FeePlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderDTO;
import com.jptest.types.Currency;

/**
 * 
 * Parent class for different GoldenFilePlaceHolderGenerator implementations
 * e.g. {@link SenderGoldenFilePlaceHolderGenerator},
 * {@link RecipientGoldenFilePlaceHolderGenerator}
 * <p>
 * This class exposes common methods used by child classes e.g. it generates FX
 * and FEE placeholder strings using input JSON file and sender/recipient golden
 * file and then replaces golden file fields with these placeholder strings
 * 
 * 
 * @see SenderGoldenFilePlaceHolderGenerator
 * @see RecipientGoldenFilePlaceHolderGenerator
 */
public abstract class AbstractGoldenFilePlaceHolderGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGoldenFilePlaceHolderGenerator.class);

	private static final String XPATH_FOR_U_ROW = "//validation_fixture/*/wtransaction/item[type/text() = 'U']";
	private static final String CURRENCY_CODE_FIELD = "currency_code";
	private static final String AMOUNT_FIELD = "amount";

	@Inject
	private FileRetrieverTask fileRetriever;

	@Inject
	private XMLHelper xmlHelper;

	@Inject
	private FileHelper fileHelper;

	@Inject
	private FeePlaceHolderGenerator feePlaceHolderGenerator;

	@Inject
	private FXPlaceHolderGenerator fxPlaceHolderGenerator;

	public void generateAndReplacePlaceHolder(GoldenFilePlaceHolderDTO dto) throws TransformerException {
		// 1. get golden file XML document from golden file location
		dto.setGoldenFileDocument(fileRetriever.execute(dto.getGoldenFileLocation()));

		// 2. get transaction amount
		Currency transactionAmount = getTransactionAmount(dto);
		if (!transactionAmount.getCurrencyCode().equals(dto.getTransactionCurrency())) {
			LOGGER.error("Expected currency {}, but found transaction currency {} in {}", dto.getTransactionCurrency(),
					transactionAmount.getCurrencyCode(), dto.getPath());
			throw new IllegalStateException("Currency does not match:" + dto.getTransactionCurrency() + " "
					+ transactionAmount.getCurrencyCode() + " in " + dto.getPath());
		}

		// 3. transform transaction amount as required for fee call
		transactionAmount = changeTransactionAmountForFeeCall(transactionAmount);

		// 4. get fee placeholder object with values
        List<FeePlaceHolder> feePlaceHolders = feePlaceHolderGenerator.getFeePlaceHolders(dto, transactionAmount,
                getFeeType());

		// 5. validate and replace fee related fields with placeholders in
		// golden file
        if (CollectionUtils.isNotEmpty(feePlaceHolders)) {
            updateGoldenFileForFeePlaceHolders(dto, feePlaceHolders);
		}

		// 6. calculate total fee using feePlaceHolder
        String totalFee = calculateTotalFee(dto, feePlaceHolders);

		// 7. transform transaction amount as required for FX call
		transactionAmount = changeTransactionAmountForFXCall(transactionAmount, totalFee);

		// 8. get FX placeholder object with values
        List<AbstractFXPlaceHolder> fxPlaceHolders = getFXPlaceHolders(dto, transactionAmount);

		// 9. validate and replace FX related fields with placeholders in golden
		// file
        if (CollectionUtils.isNotEmpty(fxPlaceHolders)) {
			incrementFXCountStats(dto);
            updateGoldenFileForFXPlaceHolders(dto, fxPlaceHolders);
		}

		// 10. store document back to golden file if its changed
		if (dto.isChanged()) {
			storeXmlOnFileSystem(dto);
		}
	}

	private Currency getTransactionAmount(GoldenFilePlaceHolderDTO dto) throws TransformerException {
		NodeList uRowNodes = XPathAPI.selectNodeList(dto.getGoldenFileDocument(), XPATH_FOR_U_ROW);
		if (uRowNodes.getLength() < 1) {
			throw new IllegalStateException("Golden file should have 1 U row in " + dto.getPath());
		} else if (uRowNodes.getLength() > 1) {
			throw new IllegalStateException("Golden file should not have more than 1 U row in " + dto.getPath());
		}
		Element uRowNode = (Element) uRowNodes.item(0);
		return new Currency(uRowNode.getElementsByTagName(CURRENCY_CODE_FIELD).item(0).getTextContent(),
				Long.parseLong(uRowNode.getElementsByTagName(AMOUNT_FIELD).item(0).getTextContent()));
	}

    private void updateGoldenFileForFeePlaceHolders(GoldenFilePlaceHolderDTO dto,
            List<FeePlaceHolder> feePlaceHolders) {
        for (int i = 0; i < feePlaceHolders.size(); i++) {
            FeePlaceHolder feePlaceHolder = feePlaceHolders.get(i);
            feePlaceHolderGenerator.compareAndReplaceActualFixedFee(dto, feePlaceHolder, i);
            feePlaceHolderGenerator.compareAndReplaceActualPercentFee(dto, feePlaceHolder, i);
            feePlaceHolderGenerator.compareAndReplaceTotalFeeAmount(dto, feePlaceHolder, i);
            feePlaceHolderGenerator.compareAndReplaceAuditTrail(dto, feePlaceHolder, i);
            feePlaceHolderGenerator.compareAndReplaceAmountAndUsdAmount(dto, feePlaceHolder, i);
        }
        
        feePlaceHolderGenerator.compareAndReplaceWsubbalanceAmount(dto, feePlaceHolders.get(0));
	}

    private String calculateTotalFee(GoldenFilePlaceHolderDTO dto, List<FeePlaceHolder> feePlaceHolders) {
		String totalFee = "0";
		// case where fee is null - meaning fee is paid by recipient
        if (CollectionUtils.isNotEmpty(feePlaceHolders)) {
            FeePlaceHolder firstFeePlaceHolder = feePlaceHolders.get(0);
			incrementFeeCountStats(dto);
            totalFee = firstFeePlaceHolder
                    .getValue(firstFeePlaceHolder.buildKey(FeePlaceHolder.FIELD_TOTAL_FEE_AMOUNT));
		}

		if (StringUtils.isBlank(totalFee)) {
			LOGGER.error("totalFee is invalid for {}", dto.getPath());
			throw new IllegalStateException("totalFee is invalid for " + dto.getPath());
		}

		return totalFee;
	}

    private void updateGoldenFileForFXPlaceHolders(GoldenFilePlaceHolderDTO dto,
            List<AbstractFXPlaceHolder> fxPlaceHolders) {
        for (int i = 0; i < fxPlaceHolders.size(); i++) {
            AbstractFXPlaceHolder fxPlaceHolder = fxPlaceHolders.get(i);
            fxPlaceHolderGenerator.compareAndReplaceAuditTrail(dto, fxPlaceHolder, i);
            fxPlaceHolderGenerator.compareAndReplaceExchangeRate(dto, fxPlaceHolder, i);
            fxPlaceHolderGenerator.compareAndReplaceActualFixedFee(dto, fxPlaceHolder, i);
            fxPlaceHolderGenerator.compareAndReplaceActualPercentFee(dto, fxPlaceHolder, i);
            fxPlaceHolderGenerator.compareAndReplaceTotalFeeAmount(dto, fxPlaceHolder, i);
            fxPlaceHolderGenerator.compareAndReplaceXRowAuditTrail(dto, fxPlaceHolder, i);
            // perform amount-from placeholder replacement for sender and
            // perform amount-to placeholder replacement for recipient
            updateSpecificGoldenFileForFXPlaceHolders(dto, fxPlaceHolder, i);
            fxPlaceHolderGenerator.compareAndReplaceAmountAndUsdAmount(dto, fxPlaceHolder, i);
        }

	}

	protected void storeXmlOnFileSystem(GoldenFilePlaceHolderDTO dto) {
		String prettyXml = xmlHelper.getPrettyXml(dto.getGoldenFileDocument());
		String fileLocation = dto.getGoldenFileLocationSource();
		fileHelper.writeToFile(fileLocation, prettyXml);
		LOGGER.warn("Updated file {} with new placeholder values", fileLocation);
	}

	protected abstract Currency changeTransactionAmountForFeeCall(Currency transactionAmount);

	protected abstract String getFeeType();

	protected abstract void incrementFeeCountStats(GoldenFilePlaceHolderDTO dto);

	protected abstract Currency changeTransactionAmountForFXCall(Currency transactionAmount, String totalFee);

    protected abstract List<AbstractFXPlaceHolder> getFXPlaceHolders(GoldenFilePlaceHolderDTO dto,
            Currency transactionAmount) throws TransformerException;

	protected abstract void incrementFXCountStats(GoldenFilePlaceHolderDTO dto);

	protected abstract void updateSpecificGoldenFileForFXPlaceHolders(GoldenFilePlaceHolderDTO dto,
            AbstractFXPlaceHolder fxPlaceHolder, int rowIndex);

}
