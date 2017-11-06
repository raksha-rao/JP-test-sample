package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.configuration.Configuration;
import org.testng.Assert;

import com.jptest.payments.fulfillment.testonia.business.component.RetriableTask;
import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;

/**
@JP Inc.
 */
public class ReleaseFundsValidationAsserter extends MassPayBaseValidationAsserter {

	private final static String TYPE_5 = "5";
	private final static String TYPE_SPT = "SPT";
	private final static String TYPE_BAL = "BAL";
	private final static String REASON = "V";
	private final static String STATUS_SUCCESS = "S";
	private final static String SUBTYPE_CODE = "V";
	private final static String SUBTYPE_CODE_MLP = "MLP";
	private final static String SUBTYPE_CODE_HAV = "HAV";
	private final static String DEBIT_CODE = "DR";
	private final static String CREDIT_CODE = "CR";
	private final static BigInteger FLAGS = new BigInteger("18014398513676288");
	private final static int RESULT_COUNT = 1;

	@Inject
	RetriableMassPayAsserter innerAsserter;

    public ReleaseFundsValidationAsserter(TestCaseInputData testCaseInputData) {
        super(testCaseInputData);
    }


    @Override
    protected void validateWTransaction()  {
		BigInteger paymentID, debitPaymentSideId;
		innerAsserter.execute(massPayValidationInput);
		LOGGER.info("Release Fund case:::{} ");
        paymentID = getWTxnPaymentID(massPayValidationInput.getSender().getAccountNumber(), TYPE_5, REASON,
				massPayValidationInput.getDecyrptTransactionId(), FLAGS, STATUS_SUCCESS);
        LOGGER.info("PAYMENT ID IS :{} ", paymentID);
        debitPaymentSideId = getPymtPaymentSideID(massPayValidationInput.getSender().getAccountNumber(),
                TYPE_SPT, SUBTYPE_CODE, paymentID.toString());
        LOGGER.info("PAYMENT SIDE ID IS :{} ", debitPaymentSideId);
      
			Assert.assertEquals(this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
					massPayValidationInput.getSender().getAccountNumber(), TYPE_BAL, SUBTYPE_CODE_HAV, CREDIT_CODE,
					debitPaymentSideId.toString()), RESULT_COUNT,
					this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");
			Assert.assertEquals(this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
	                massPayValidationInput.getSender().getAccountNumber(), TYPE_5, SUBTYPE_CODE_MLP, REASON, DEBIT_CODE,
					massPayValidationInput.getDecyrptTransactionId(), debitPaymentSideId.toString()), RESULT_COUNT,
					this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");
		
    }

	private static class RetriableMassPayAsserter extends RetriableTask<MassPayValidationInputTask, Integer> {
		@Inject
		@Named("WTransactionDao")
		private com.jptest.payments.fulfillment.testonia.dao.IwTransactionDao wTransactionDao;
		@Inject
		private Configuration config;

		@Inject
		private void init() {
			setMaxWaitInMs(config.getLong(BizConfigKeys.TRANSACTION_EXISTS_CHECK_WAIT_TIME_IN_MS.getName()));
			setRepeatIntervalInMs(
					config.getLong(BizConfigKeys.TRANSACTION_EXISTS_CHECK_RETRY_INTERVAL_IN_MS.getName()));
		}

		@Override
		protected boolean isDesiredOutput(Integer output) {
			return output != null && output != 0;
		}

		@Override
		protected Integer retriableExecute(MassPayValidationInputTask input) {
			LOGGER.info("Release Fund case :{}");
			Integer resultCount = null;
			long amt = -(Long.parseLong(input.getFulfillmentOptions().getTxnAmount().getAmount()));
			resultCount = wTransactionDao.getType5RowCount(new BigInteger(input.getSender().getAccountNumber()), TYPE_5,
					Long.toString(amt), input.getFulfillmentOptions().getTxnAmount().getCurrencyCode(), REASON,
					STATUS_SUCCESS);
			return resultCount;

		}

		@Override
		protected Integer onSuccess(MassPayValidationInputTask input, Integer output) {
			LOGGER.info(" Type 5 Row exists in wtransaction_p2 for given input.:{}", output);
			return output;
		}

		@Override
		protected Integer onFailure(MassPayValidationInputTask input, Integer output) {
			LOGGER.warn("Unable to retrive Type 5 Row in wtransaction_p2 for given input. count:{}", output);
			return output;
		}

	}

	@Override
	public long getTimeoutInMs() {
		// It can take this long to get the db entry in the table
		return 60 * 3000;
	}
}
