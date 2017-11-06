package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.jptest.financialinstrument.FinancialInstrumentTypeClass;
import com.jptest.money.PayoffTabFinancialInstrumentVO;
import com.jptest.money.PayoffTabFundingConstraintType;
import com.jptest.money.PayoffTabFundingConstraintVO;
import com.jptest.money.PayoffTabStrategyVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.types.Currency;

/**
 * Builds PayoffTabStrategyVO used in PlanPayoffTabV2Request
 * 
 * @JP Inc.
 */
public class PayoffTabStrategyVOBuilder extends ListWrappedVOBuilder<PayoffTabStrategyVO> {

	private BigInteger senderAccountNumber;

	private String encryptedActivityId;

	private String achId;

	private BigInteger transactionId;

	private BigInteger tabId;

	private Currency amount;

	public PayoffTabStrategyVOBuilder senderAccountNumber(BigInteger accountNumber) {
		this.senderAccountNumber = accountNumber;
		return this;
	}

	public PayoffTabStrategyVOBuilder encryptedActivityId(String handle) {
		this.encryptedActivityId = handle;
		return this;
	}

	public PayoffTabStrategyVOBuilder achId(String achId) {
		this.achId = achId;
		return this;
	}

	public PayoffTabStrategyVOBuilder transactionId(BigInteger transactionId) {
		this.transactionId = transactionId;
		return this;
	}

	public PayoffTabStrategyVOBuilder tabId(BigInteger tabId) {
		this.tabId = tabId;
		return this;
	}

	public PayoffTabStrategyVOBuilder amount(Currency amount) {
		this.amount = amount;
		return this;
	}

	public PayoffTabStrategyVO build() {
		// build the funding constraints list
		PayoffTabFinancialInstrumentVO financialInstrumentVO = new PayoffTabFinancialInstrumentVO();
		financialInstrumentVO.setFiId(new BigInteger(achId));
		financialInstrumentVO.setFiType(FinancialInstrumentTypeClass.FI_BANK);

		List<PayoffTabFinancialInstrumentVO> financialInstrumentVOList = new ArrayList<PayoffTabFinancialInstrumentVO>();
		financialInstrumentVOList.add(financialInstrumentVO);

		PayoffTabFundingConstraintVO fundingConstraintVO = new PayoffTabFundingConstraintVO();
		fundingConstraintVO.setFiList(financialInstrumentVOList);
		fundingConstraintVO.setType(PayoffTabFundingConstraintType.FORCE);

		List<PayoffTabFundingConstraintVO> fundingConstraintVOList = new ArrayList<PayoffTabFundingConstraintVO>();
		fundingConstraintVOList.add(fundingConstraintVO);

		PayoffTabStrategyVO strategyVO = new PayoffTabStrategyVO();
		strategyVO.setSenderAccountNumber(senderAccountNumber);
		strategyVO.setFundingConstraints(fundingConstraintVOList);
		strategyVO.setPaymentHandle(encryptedActivityId);
		strategyVO.setAmount(amount);
		strategyVO.setReferenceTransactionId(transactionId);
		strategyVO.setFundingSinkFiId(tabId);

		return strategyVO;
	}
}
