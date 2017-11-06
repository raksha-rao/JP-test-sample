package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;

/**
@JP Inc.
 */
public class MasspayValidationAsserterFactory {
	protected static final Logger LOGGER = LoggerFactory.getLogger(MasspayValidationAsserterFactory.class);

    private final static String DISBURSE_FUNDS = "DISBURSE_FUNDS";
    private final static String RESERVE_FUNDS = "RESERVE_FUNDS";
    private final static String RELEASE_FUNDS = "RELEASE_FUNDS";
    private final static String LEDGER_RECEIVABLE = "RECEIVABLE";
	private final static String LEDGER_PAYABLE = "PAYABLE";

	public static MassPayBaseValidationAsserter getMasspayValidationAsserter(TestCaseInputData testCaseInputData)
			throws InterruptedException {
        switch (testCaseInputData.getFulfillPaymentPlanOptions().getFulfillmentType().value()) {
            case DISBURSE_FUNDS :
			if (testCaseInputData.getFulfillPaymentPlanOptions().getFundingSource().equals(LEDGER_PAYABLE))
                    return new DisburseFundsType5ValidationAsserter(testCaseInputData);
			else if (testCaseInputData.getFulfillPaymentPlanOptions().getFundingSource().equals(LEDGER_RECEIVABLE))
                    return new DisburseFundsType3ValidationAsserter(testCaseInputData);
		case RESERVE_FUNDS:
                return new ReserveFundsValidationAsserter(testCaseInputData);
            case RELEASE_FUNDS :
                return new ReleaseFundsValidationAsserter(testCaseInputData);
            default:
			LOGGER.info("There is no such Fulfillment type: {}"
					+ testCaseInputData.getFulfillPaymentPlanOptions().getFulfillmentType().value());
			return null;
        }
    }
}
