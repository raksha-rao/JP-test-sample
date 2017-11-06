package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.transform.TransformerException;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.AbstractFXPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FXPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FeePlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderDTO;
import com.jptest.types.Currency;

/**
 * This class generates FX and FEE placeholder strings using data from input
 * JSON file and recipient golden file.
 * <p>
 * It then replaces recipient golden file fields with these placeholders
 * 
 * @see AbstractGoldenFilePlaceHolderGenerator
 */
@Singleton
public class RecipientGoldenFilePlaceHolderGenerator extends AbstractGoldenFilePlaceHolderGenerator {

	@Inject
	private FXPlaceHolderGenerator fxPlaceHolderGenerator;

	/**
	 * Only sender needs to change transaction amount. Transaction amount for
	 * recipient can be returned as is.
	 */
	@Override
	protected Currency changeTransactionAmountForFeeCall(Currency transactionAmount) {
		// Do Nothing
		return transactionAmount;
	}

	@Override
	protected String getFeeType() {
        return FeePlaceHolder.RECIPIENT_FEE_TYPE;
	}

	@Override
	protected void incrementFeeCountStats(GoldenFilePlaceHolderDTO dto) {
		dto.getStats().incrementRecipientPaysFeeCount();
	}

	/**
	 * calculate transaction amount by subtracting fee from it
	 */
	@Override
	protected Currency changeTransactionAmountForFXCall(Currency transactionAmount, String totalFee) {
		return new Currency(transactionAmount.getCurrencyCode(),
				transactionAmount.getAmount() - Long.parseLong(totalFee));
	}

	/**
	 * pass recipient specific data
	 */
	@Override
    protected List<AbstractFXPlaceHolder> getFXPlaceHolders(GoldenFilePlaceHolderDTO dto, Currency transactionAmount)
			throws TransformerException {
        return fxPlaceHolderGenerator.getFXPlaceHolders(dto, dto.getRecipient(), transactionAmount, false,
                AbstractFXPlaceHolder.RECIPIENT_CONVERSION_TYPE);
	}

	@Override
	protected void incrementFXCountStats(GoldenFilePlaceHolderDTO dto) {
		dto.getStats().incrementRecipientSideCurrencyConversionCount();
	}

	@Override
	protected void updateSpecificGoldenFileForFXPlaceHolders(GoldenFilePlaceHolderDTO dto,
            AbstractFXPlaceHolder fxPlaceHolder, int rowIndex) {
        if (fxPlaceHolder instanceof FXPlaceHolder) {
            fxPlaceHolderGenerator.compareAndReplaceAmountTo(dto, fxPlaceHolder, rowIndex);
        }
        else {
            fxPlaceHolderGenerator.compareAndReplaceAmountFrom(dto, fxPlaceHolder, rowIndex);
        }
	}

}
