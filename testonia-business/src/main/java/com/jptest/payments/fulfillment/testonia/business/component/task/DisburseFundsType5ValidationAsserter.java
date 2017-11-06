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
 * Validating DisburseFundsType5 transaction
 */
public class DisburseFundsType5ValidationAsserter extends MassPayBaseValidationAsserter {


	private final static String TYPE_5 = "5";
	private final static String TYPE_U = "U";
	private final static String TYPE_F = "F";
	private final static String TYPE_BAL = "BAL";
	private final static String REASON = "D";
	private final static String REASON_U = "O";
	private final static String REASON_DECLINED_U = "D";
	private final static String DEBIT_CODE = "DR";
	private final static String CREDIT_CODE = "CR";
	private final static String STATUS_SUCCESS = "S";
	private final static String STATUS_PENDING = "P";
	private final static String STATUS_DECLINED = "D";
	private final static String SUBTYPE_M = "M";
	private final static String SUBTYPE_HAV = "HAV";
	private final static String SUBTYPE_MLP = "MLP";
	private final static String SUBTYPE_HPF = "HPF";
	private final static BigInteger FLAGS5 = new BigInteger("18014398513676288");
	private final BigInteger FLAGS5_U;
	private final static BigInteger FLAGS_FEE = new BigInteger("18014398509481984");
	private final static int RESULT_COUNT = 1;
	private final static String RNHCP = "RNHCP";
	private final static String SUCCESS = "SUCCESS";
	private final static String PENDING = "PENDING";
	private final static String DECLINED = "DECLINED";
	private final static String FEE = "-100";
	private BigInteger debitPaymentSideId = null;

	@Inject
	RetriableMassPayAsserter innerAsserter;

	public DisburseFundsType5ValidationAsserter(final TestCaseInputData inputData) {
		super(inputData);
		this.FLAGS5_U = new BigInteger(inputData.getValidationsInput().getFlags());

	}

	protected void validateWTransaction() {
		innerAsserter.execute(massPayValidationInput);
		switch (massPayValidationInput.getValidateStatus()) {
		case SUCCESS:
			validateSuccessTransaction(massPayValidationInput);
			break;
		case PENDING:
			validatePendingTransaction(massPayValidationInput);
			break;
		case DECLINED:
			validateDeclinedTransaction(massPayValidationInput);
			break;
		default:
			LOGGER.info("Response Validation status is wrong and the status is :{}"+massPayValidationInput.getValidateStatus());
			break;

		}

	}

	// This Method Validate the MassPay_Type5 Success scenarios
	public void validateSuccessTransaction(MassPayValidationInputTask masspayInput) {
		BigInteger paymentID, buyerDecrytId, creditPaymentSideID, debitPaymentSideId = null;
		buyerDecrytId = getWTxnID(masspayInput.getSender().getAccountNumber(), TYPE_5, REASON, FLAGS5);
		LOGGER.info("BUYER SIDE TYPE5 DECRYPT ID IS :{} ", buyerDecrytId);
		long amt = -(Long.parseLong(fulfillmentOptions.getTxnAmount().getAmount()));

		BigInteger senderURowId = getTransactionIdByAccountNumberAndType(masspayInput.getSender().getAccountNumber(),
				TYPE_U, STATUS_SUCCESS, Long.toString(amt), fulfillmentOptions.getTxnAmount().getCurrencyCode(),
				FLAGS5_U);
		LOGGER.info("BUYER U ROW DECRYPT ID IS :{} ", senderURowId);

		BigInteger receiverURowId = getTransactionIdByAccountNumberAndType(
				masspayInput.getReceiver().getAccountNumber(), TYPE_U, STATUS_SUCCESS,
				fulfillmentOptions.getTxnAmount().getAmount(), fulfillmentOptions.getTxnAmount().getCurrencyCode(),
				FLAGS5_U);

		LOGGER.info("RECEIEVER U ROW DECRYPT ID IS :{} ", receiverURowId);

		BigInteger feeRowId = getTransactionIdByAccountNumberAndType(masspayInput.getSender().getAccountNumber(),
				TYPE_F, STATUS_SUCCESS, FEE, fulfillmentOptions.getTxnAmount().getCurrencyCode(), FLAGS_FEE);

		LOGGER.info("FEE ROW DECRYPT ID IS :{} ", feeRowId);

		paymentID = getWTxnPaymentID(masspayInput.getSender().getAccountNumber(), TYPE_5, REASON, buyerDecrytId, FLAGS5,
				STATUS_SUCCESS);

		debitPaymentSideId = getPymtPaymentSideID(masspayInput.getSender().getAccountNumber(), TYPE_U,
				paymentID.toString());
		LOGGER.info("DEBIT SIDE PAYMENT SIDE ID IS :{} ", debitPaymentSideId);
		masspayInput.setDebitPaymentSideId(debitPaymentSideId);

		Assert.assertEquals(debitPaymentSideId, senderURowId,
				this.getClass().getSimpleName() + ".validate() failed for debitPaymentSideId and senderURowId");

		creditPaymentSideID = getPymtPaymentSideID(masspayInput.getReceiver().getAccountNumber(), TYPE_U,
				paymentID.toString());

		LOGGER.info("CREDIT SIDE PAYMENT SIDE ID IS :{} ", creditPaymentSideID);

		Assert.assertEquals(creditPaymentSideID, receiverURowId,
				this.getClass().getSimpleName() + ".validate() failed for creditPaymentSideID and receiverURowId");
			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getSender().getAccountNumber(), TYPE_5, SUBTYPE_MLP, REASON, DEBIT_CODE,
							buyerDecrytId, debitPaymentSideId.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");

			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getSender().getAccountNumber(), TYPE_F, SUBTYPE_M, CREDIT_CODE, feeRowId,
							debitPaymentSideId.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");

			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getReceiver().getAccountNumber(), TYPE_BAL, SUBTYPE_HAV, CREDIT_CODE,
							creditPaymentSideID.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");
	}

	// This Method Validate the MassPay_Type5 Pending scenarios
	public void validatePendingTransaction(MassPayValidationInputTask masspayInput) {
		BigInteger paymentID, buyerDecrytId, creditPaymentSideID;
		buyerDecrytId = getWTxnID(masspayInput.getSender().getAccountNumber(), TYPE_5, REASON, FLAGS5);
		LOGGER.info("BUYER SIDE TYPE5 DECRYPT ID IS :{} ", buyerDecrytId);
		long amt = -(Long.parseLong(fulfillmentOptions.getTxnAmount().getAmount()));

		BigInteger senderURowId = getTransactionIdByAccountNumberAndType(masspayInput.getSender().getAccountNumber(),
				TYPE_U, STATUS_PENDING, Long.toString(amt), fulfillmentOptions.getTxnAmount().getCurrencyCode(),
				FLAGS5_U);

		LOGGER.info("BUYER U ROW DECRYPT ID IS :{} ", senderURowId);

		BigInteger receiverURowId = getTransactionIdByAccountNumberAndType(
				masspayInput.getReceiver().getAccountNumber(), TYPE_U, STATUS_PENDING,
				fulfillmentOptions.getTxnAmount().getAmount(), fulfillmentOptions.getTxnAmount().getCurrencyCode(),
				FLAGS5_U);

		LOGGER.info("RECEIEVER U ROW DECRYPT ID IS :{} ", receiverURowId);

		BigInteger feeRowId = getTransactionIdByAccountNumberAndType(masspayInput.getSender().getAccountNumber(),
				TYPE_F, STATUS_SUCCESS, FEE, fulfillmentOptions.getTxnAmount().getCurrencyCode(), FLAGS_FEE);

		LOGGER.info("FEE ROW DECRYPT ID IS :{} ", feeRowId);

		paymentID = getWTxnPaymentID(masspayInput.getSender().getAccountNumber(), TYPE_5, REASON, buyerDecrytId, FLAGS5,
				STATUS_SUCCESS);

		debitPaymentSideId = getPymtPaymentSideID(masspayInput.getSender().getAccountNumber(), TYPE_U,
				paymentID.toString());

		LOGGER.info("DEBIT SIDE PAYMENT SIDE ID IS :{} ", debitPaymentSideId);
		masspayInput.setDebitPaymentSideId(debitPaymentSideId);
		Assert.assertEquals(debitPaymentSideId, senderURowId,
				this.getClass().getSimpleName() + ".validate() failed for debitPaymentSideId and senderURowId");

		creditPaymentSideID = getPymtPaymentSideID(masspayInput.getReceiver().getAccountNumber(), TYPE_U,
				paymentID.toString());

		LOGGER.info("CREDIT SIDE PAYMENT SIDE ID IS :{} ", creditPaymentSideID);

		Assert.assertEquals(creditPaymentSideID, receiverURowId,
				this.getClass().getSimpleName() + ".validate() failed for creditPaymentSideID and receiverURowId");


			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getSender().getAccountNumber(), TYPE_5, SUBTYPE_MLP, REASON, DEBIT_CODE,
							buyerDecrytId, debitPaymentSideId.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");
			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getSender().getAccountNumber(), TYPE_F, SUBTYPE_M, CREDIT_CODE, feeRowId,
							debitPaymentSideId.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");
			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getSender().getAccountNumber(), TYPE_BAL, SUBTYPE_HPF, CREDIT_CODE,
							debitPaymentSideId.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");
	}

	// This Method Validate the MassPay_Type5 Declined scenarios
	public void validateDeclinedTransaction(MassPayValidationInputTask masspayInput) {
		BigInteger paymentID, creditPaymentSideID,senderURowId;
		long amt = -(Long.parseLong(fulfillmentOptions.getTxnAmount().getAmount()));
		// In OFAC special deny scenario the transaction will be denied state
		// for Receiver side only and for the sender side the transaction will
		// be in Pending.
		if (RNHCP.equals(masspayInput.getFulfillmentOptions().getContingency())) {
			senderURowId = getTransactionIdByAccountNumberAndType(masspayInput.getSender().getAccountNumber(),
					TYPE_U, STATUS_PENDING, Long.toString(amt), fulfillmentOptions.getTxnAmount().getCurrencyCode(),
				FLAGS5_U);

		LOGGER.info("BUYER U ROW DECRYPT ID IS :{} ", senderURowId);
		paymentID = getWTxnPaymentID(masspayInput.getSender().getAccountNumber(), TYPE_U, REASON_DECLINED_U,
				senderURowId,
				FLAGS5_U, STATUS_PENDING);
		}else{
		
		 senderURowId = getTransactionIdByAccountNumberAndType(masspayInput.getSender().getAccountNumber(),
				TYPE_U, STATUS_DECLINED, Long.toString(amt), fulfillmentOptions.getTxnAmount().getCurrencyCode(),
				FLAGS5_U);

		LOGGER.info("BUYER U ROW DECRYPT ID IS :{} ", senderURowId);
		paymentID = getWTxnPaymentID(masspayInput.getSender().getAccountNumber(), TYPE_U, REASON_U, senderURowId,
				FLAGS5_U, STATUS_DECLINED);
		}
		BigInteger receiverURowId = getTransactionIdByAccountNumberAndType(
				masspayInput.getReceiver().getAccountNumber(), TYPE_U, STATUS_DECLINED,
				fulfillmentOptions.getTxnAmount().getAmount(), fulfillmentOptions.getTxnAmount().getCurrencyCode(),
				FLAGS5_U);

		LOGGER.info("RECEIEVER U ROW DECRYPT ID IS :{} ", receiverURowId);

		

		debitPaymentSideId = getPymtPaymentSideID(masspayInput.getSender().getAccountNumber(), TYPE_U,
				paymentID.toString());

		LOGGER.info("DEBIT SIDE PAYMENT SIDE ID IS :{} ", debitPaymentSideId);
		masspayInput.setDebitPaymentSideId(debitPaymentSideId);

		Assert.assertEquals(debitPaymentSideId, senderURowId,
				this.getClass().getSimpleName() + ".validate() failed for debitPaymentSideId andsenderURowId");

		creditPaymentSideID = getPymtPaymentSideID(masspayInput.getReceiver().getAccountNumber(), TYPE_U,
				paymentID.toString());

		LOGGER.info("CREDIT SIDE PAYMENT SIDE ID IS :{} ", creditPaymentSideID);

		Assert.assertEquals(creditPaymentSideID, receiverURowId,
				this.getClass().getSimpleName() + ".validate() failed for creditPaymentSideID and receiverURowId");

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
			LOGGER.info("Disburse Fund - PAYABLE case::: {}");
			Integer resultCount = null;
			long amt = (Long.parseLong(input.getFulfillmentOptions().getTxnAmount().getAmount()) + 100);
			if (input.getValidateStatus().equals(SUCCESS)) {
				resultCount = wTransactionDao.getType5RowCount(new BigInteger(input.getSender().getAccountNumber()),
						TYPE_5, Long.toString(amt), input.getFulfillmentOptions().getTxnAmount().getCurrencyCode(),
						REASON, STATUS_SUCCESS);
			} else if (input.getValidateStatus().equals(PENDING)) {
				resultCount = wTransactionDao.getType5RowCount(new BigInteger(input.getSender().getAccountNumber()),
						TYPE_5, Long.toString(amt), input.getFulfillmentOptions().getTxnAmount().getCurrencyCode(),
						REASON, STATUS_SUCCESS);

			} else if (input.getValidateStatus().equals(DECLINED)) {
				amt = -(Long.parseLong(input.getFulfillmentOptions().getTxnAmount().getAmount()));
				resultCount = wTransactionDao.getType5RowCount(new BigInteger(input.getSender().getAccountNumber()),
						TYPE_U, Long.toString(amt), input.getFulfillmentOptions().getTxnAmount().getCurrencyCode(),
						REASON_U, STATUS_DECLINED);
			}
			return resultCount;

		}

		@Override
		protected Integer onSuccess(MassPayValidationInputTask input, Integer output) {
			LOGGER.info("Type 5 Row exists in wtransaction_p2 for given input.:{}", output);
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
