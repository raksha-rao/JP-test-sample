package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import java.util.List;

import org.testng.Assert;

import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;

/**
 * SecondRepresentmentOperationTask takes care of constructing the post bank Request and executing post bank and validates
 * response
 *
 * @JP Inc.
 */
public class RepresentmentOperationTask extends PostBankReturnOperationTask {

	@Override
	protected WTransactionVO getBankFundingWTransactionVO(final List<WTransactionVO> wTransactionList) {

		WTransactionVO bankFundingVO = null;
		for (final WTransactionVO wTxn : wTransactionList) {
			if (wTxn.getType() == WTransactionConstants.Type.ACHDEPOSIT.getValue()
					&& wTxn.getStatus() == WTransactionConstants.Status.PROCESSING.getByte()) {
				bankFundingVO = wTxn;
				break;
			}
		}
		Assert.assertNotNull(bankFundingVO, this.getClass().getSimpleName() + ".getBankFundingWTransactionVO() Failed to find ACHDEPOSIT in PROCESSING state.");
		return bankFundingVO;
	}
}
