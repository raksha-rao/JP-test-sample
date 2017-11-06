package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.AbstractFXPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FXPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.FeePlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderDTO;
import com.jptest.types.Currency;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.transform.TransformerException;
import java.util.List;

/**
 * This class generates FX and FEE placeholder strings using data from input
 * JSON file and sender golden file.
 * <p>
 * It then replaces sender golden file fields with these placeholders
 * 
 * @see AbstractGoldenFilePlaceHolderGenerator
 */
@Singleton
public class SenderGoldenFilePlaceHolderGenerator extends AbstractGoldenFilePlaceHolderGenerator {

	@Inject
	private FXPlaceHolderGenerator fxPlaceHolderGenerator;

	/**
	 * sender U row has negated amount - so negate it to return positive amount
	 */
	@Override
	protected Currency changeTransactionAmountForFeeCall(Currency transactionAmount) {
		return new Currency(transactionAmount.getCurrencyCode(), -transactionAmount.getAmount());
	}

	/**
	 * calculate transaction amount by adding fee to it
	 */
	@Override
	protected Currency changeTransactionAmountForFXCall(Currency transactionAmount, String totalFee) {
		return new Currency(transactionAmount.getCurrencyCode(),
				transactionAmount.getAmount() + Long.parseLong(totalFee));
	}

	@Override
	protected String getFeeType() {
        return FeePlaceHolder.SENDER_FEE_TYPE;
	}

	@Override
	protected void incrementFeeCountStats(GoldenFilePlaceHolderDTO dto) {
		dto.getStats().incrementSenderPaysFeeCount();
	}

	/**
	 * pass sender specific data
	 */
	@Override
    protected List<AbstractFXPlaceHolder> getFXPlaceHolders(GoldenFilePlaceHolderDTO dto, Currency transactionAmount)
			throws TransformerException {
       /* return fxPlaceHolderGenerator.getFXPlaceHolders(dto, dto.getSender(), transactionAmount, true,
                AbstractFXPlaceHolder.SENDER_CONVERSION_TYPE);
*/
		return fxPlaceHolderGenerator.getFXPlaceHolders(dto, null, transactionAmount, true,
				AbstractFXPlaceHolder.SENDER_CONVERSION_TYPE);
	}

	@Override
	protected void incrementFXCountStats(GoldenFilePlaceHolderDTO dto) {
		dto.getStats().incrementSenderSideCurrencyConversionCount();
	}

	@Override
	protected void updateSpecificGoldenFileForFXPlaceHolders(GoldenFilePlaceHolderDTO dto,
            AbstractFXPlaceHolder fxPlaceHolder, int rowIndex) {
        if (fxPlaceHolder instanceof FXPlaceHolder) {
            fxPlaceHolderGenerator.compareAndReplaceAmountFrom(dto, fxPlaceHolder, rowIndex);
        }
        else {
            fxPlaceHolderGenerator.compareAndReplaceAmountTo(dto, fxPlaceHolder, rowIndex);
        }
	}

}
