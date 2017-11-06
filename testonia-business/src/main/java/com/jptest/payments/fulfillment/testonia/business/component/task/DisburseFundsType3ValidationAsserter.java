package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.Assert;

import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;
import com.jptest.payments.fulfillment.testonia.model.pymt.MoneyMovementDTO;

/**
@JP Inc.
 */
public class DisburseFundsType3ValidationAsserter extends MassPayBaseValidationAsserter {


	private final static String TYPE_3 = "3";
	private final static String TYPE_U = "U";
	private final static String TYPE_F = "F";
	private final static String TYPE_BAL = "BAL";
	private final static String REASON = null;
	private final static String REASON_U = "O";
	private final static String REASON_DECLINED_U = "D";
	private final static String DEBIT_CODE = "DR";
	private final static String CREDIT_CODE = "CR";
	private final static String STATUS_SUCCESS = "S";
	private final static String STATUS_PENDING = "P";
	private final static String STATUS_DECLINED = "D";
	private final static String SUBTYPE_CODE_M = "M";
	private final static String SUBTYPE_CODE_HAV = "HAV";
	private final static String SUBTYPE_CODE_HPF = "HPF";
	private final static BigInteger FLAGS5 = new BigInteger("18014398513676288");
	private final BigInteger FLAGS5_U;
	private final static int RESULT_COUNT = 1;
	private final static BigInteger FLAGS_FEE = new BigInteger("18014398509481984");
	private final static String SUCCESS = "SUCCESS";
	private final static String PENDING = "PENDING";
	private final static String DECLINED = "DECLINED";
	private final static String FEE = "-100";
	private final static String RNHCP = "RNHCP";

	public DisburseFundsType3ValidationAsserter(final TestCaseInputData inputData) {
		super(inputData);
		this.FLAGS5_U = new BigInteger(inputData.getValidationsInput().getFlags());

	}

	@Override
	protected void validateWTransaction() {
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

	// This Method Validate the MassPay_Type3 SUCCESS scenarios
	public void validateSuccessTransaction(MassPayValidationInputTask masspayInput) {
		BigInteger paymentID, buyerDecrytId, creditPaymentSideID, debitPaymentSideId = null;
		buyerDecrytId = getWTxnID(masspayInput.getSender().getAccountNumber(), TYPE_3, REASON, FLAGS5);
		LOGGER.info("BUYER SIDE TYPE3 DECRYPT ID IS :{} ", buyerDecrytId);
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

		paymentID = getWTxnPaymentID(masspayInput.getSender().getAccountNumber(), TYPE_3, REASON, buyerDecrytId, FLAGS5,
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

		Assert.assertEquals(getTransactionMMCount(masspayInput.getSender().getAccountNumber(), TYPE_3, null, null,
				DEBIT_CODE, buyerDecrytId, debitPaymentSideId.toString()), RESULT_COUNT,
				this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");

			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getSender().getAccountNumber(), TYPE_F, SUBTYPE_CODE_M, CREDIT_CODE, feeRowId,
							debitPaymentSideId.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");
			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getReceiver().getAccountNumber(), TYPE_BAL, SUBTYPE_CODE_HAV, CREDIT_CODE,
							creditPaymentSideID.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");

	}

	// This Method Validate the MassPay_Type3 Pending scenarios
	public void validatePendingTransaction(MassPayValidationInputTask masspayInput) {
		BigInteger paymentID, buyerDecrytId, creditPaymentSideID, debitPaymentSideId = null;
		buyerDecrytId = getWTxnID(masspayInput.getSender().getAccountNumber(), TYPE_3, REASON, FLAGS5);
		LOGGER.info("BUYER SIDE TYPE3 DECRYPT ID IS :{} ", buyerDecrytId);
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

		paymentID = getWTxnPaymentID(masspayInput.getSender().getAccountNumber(), TYPE_3, REASON, buyerDecrytId, FLAGS5,
				STATUS_SUCCESS);

		debitPaymentSideId = getPymtPaymentSideID(masspayInput.getSender().getAccountNumber(), TYPE_U,
				paymentID.toString());
		masspayInput.setDebitPaymentSideId(debitPaymentSideId);
		LOGGER.info("DEBIT SIDE PAYMENT SIDE ID IS :{} ", debitPaymentSideId);

		Assert.assertEquals(debitPaymentSideId, senderURowId,
				this.getClass().getSimpleName() + ".validate() failed for debitPaymentSideId and senderURowId");

		creditPaymentSideID = getPymtPaymentSideID(masspayInput.getReceiver().getAccountNumber(), TYPE_U,
				paymentID.toString());

		LOGGER.info("CREDIT SIDE PAYMENT SIDE ID IS :{} ", creditPaymentSideID);

		Assert.assertEquals(creditPaymentSideID, receiverURowId,
				this.getClass().getSimpleName() + ".validate() failed for creditPaymentSideID and receiverURowId");

		Assert.assertEquals(getTransactionMMCount(masspayInput.getSender().getAccountNumber(), TYPE_3, null, null,
				DEBIT_CODE, buyerDecrytId, debitPaymentSideId.toString()), RESULT_COUNT,
				this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");

			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getSender().getAccountNumber(), TYPE_F, SUBTYPE_CODE_M, CREDIT_CODE, feeRowId,
							debitPaymentSideId.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");
			Assert.assertEquals(
					this.masspaytxnmapmoneymovementdao.getTransactionMMCount(
							masspayInput.getSender().getAccountNumber(), TYPE_BAL, SUBTYPE_CODE_HPF, CREDIT_CODE,
							debitPaymentSideId.toString()),
					RESULT_COUNT, this.getClass().getSimpleName() + ".validate() failed for MoneyMovement table count");
	}

	// This Method Validate the MassPay_Type3 Declined scenarios
	public void validateDeclinedTransaction(MassPayValidationInputTask masspayInput) {
		BigInteger paymentID, creditPaymentSideID, debitPaymentSideId,senderURowId = null;
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
		}
		else {
			 senderURowId = getTransactionIdByAccountNumberAndType(
					masspayInput.getSender().getAccountNumber(), TYPE_U, STATUS_DECLINED, Long.toString(amt),
					fulfillmentOptions.getTxnAmount().getCurrencyCode(), FLAGS5_U);

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

		masspayInput.setDebitPaymentSideId(debitPaymentSideId);
		LOGGER.info("DEBIT SIDE PAYMENT SIDE ID IS :{} ", debitPaymentSideId);

		Assert.assertEquals(debitPaymentSideId, senderURowId,
				this.getClass().getSimpleName() + ".validate() failed for debitPaymentSideId and senderURowId");

		creditPaymentSideID = getPymtPaymentSideID(masspayInput.getReceiver().getAccountNumber(), TYPE_U,
				paymentID.toString());

		LOGGER.info("CREDIT SIDE PAYMENT SIDE ID IS :{} ", creditPaymentSideID);

		Assert.assertEquals(creditPaymentSideID, receiverURowId,
				this.getClass().getSimpleName() + ".validate() failed for creditPaymentSideID and receiverURowId");
	}


	public int getTransactionMMCount(String accountNumber, String type, String subType, String reasonCode,
			String debitCreditCode, BigInteger txnId, String payment_side_id) {
		Set paymentSideIds = new HashSet<>();
		paymentSideIds.add(payment_side_id);
		List<MoneyMovementDTO> moneyMovementDTOS = this.masspaytxnmapmoneymovementdao
				.findByPaymentSideIds(paymentSideIds);
		if (Objects.nonNull(type))
			moneyMovementDTOS = moneyMovementDTOS.stream().filter(x -> x.getTypeCode().equals(type))
					.collect(Collectors.toList());
		if (Objects.nonNull(subType))
			moneyMovementDTOS = moneyMovementDTOS.stream().filter(x -> x.getSubtypeCode().equals(subType))
					.collect(Collectors.toList());
		if (Objects.nonNull(txnId))
			moneyMovementDTOS = moneyMovementDTOS.stream().filter(x -> x.getId().equals(txnId))
					.collect(Collectors.toList());

		return moneyMovementDTOS.size();
	}


}
