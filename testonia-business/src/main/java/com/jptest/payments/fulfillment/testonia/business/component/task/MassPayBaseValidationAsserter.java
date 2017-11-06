package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.business.util.BizConfigKeys;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.eng.FulfillmentActivityDao;
import com.jptest.payments.fulfillment.testonia.dao.pymt.MoneyMovementDao;
import com.jptest.payments.fulfillment.testonia.dao.pymt.PaymentSideDao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.PaymentSideDTO;
import com.jptest.qi.rest.domain.pojo.User;

public abstract class MassPayBaseValidationAsserter extends BaseAsserter implements TimeoutAwareComponent {

	protected static final Logger LOGGER = LoggerFactory.getLogger(MassPayBaseValidationAsserter.class);


	@Inject
	protected FulfillmentActivityDao fulfillmentActivityDao;
	@Inject
	private WTransactionP20DaoImpl masspaytxnmapTxndao;
	@Inject
	private PaymentSideDao masspaytxnmapPymtdao;
	@Inject
	protected MoneyMovementDao masspaytxnmapmoneymovementdao;
	@Inject
	private Configuration config;
	@Inject
	private CryptoUtil cryptoUtil;

	protected MassPayValidationInputTask massPayValidationInput;

	protected FulfillPaymentPlanOptions fulfillmentOptions;
	Context context;
	public MassPayBaseValidationAsserter(final TestCaseInputData inputData) {
		fulfillmentOptions = inputData.getFulfillPaymentPlanOptions();

	}

	@Override
	public void validate(Context context) {
		this.context = context;
		final User sender = (User) this.getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
		final User receiver = (User) this.getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());
		BigInteger debitPaymentSideId = null;
		Object txnId = context.getData(ContextKeys.WTRANSACTION_ID_KEY.getName());
		LOGGER.debug("SUCCESS TRANSACTION ID :{} ", txnId.toString());
		LOGGER.info("senderAccountNumber:{} ", sender.getAccountNumber());
		if (receiver != null)
			LOGGER.info("receiverAccountNumber:{} ", receiver.getAccountNumber());

		long decyrptTransactionId;
		try {
			decyrptTransactionId = cryptoUtil.decryptTxnId(txnId.toString());
			LOGGER.info("Decrypt Transaction Handle:{} ", decyrptTransactionId);
		} catch (Exception e) {
			throw new TestExecutionException("Decryption of Transaction ID failed", e);
		}

		massPayValidationInput = new MassPayValidationInputTask(sender, receiver, BigInteger.valueOf(decyrptTransactionId),
				context, fulfillmentOptions, debitPaymentSideId);

			this.validateWTransaction();

	}

	protected abstract void validateWTransaction();


	@Override
	public long getTimeoutInMs() {
		long timeout = config.getLong(BizConfigKeys.TRANSACTION_EXISTS_CHECK_WAIT_TIME_IN_MS.getName());
		long interval = config.getLong(BizConfigKeys.TRANSACTION_EXISTS_CHECK_RETRY_INTERVAL_IN_MS.getName());
		return (timeout + interval);
	}

	public BigInteger getWTxnPaymentID(String accountNumber, String type, String reason, BigInteger txnId,
			BigInteger flags, String status) {
		List<WTransactionDTO> wTransactionDTOList = masspaytxnmapTxndao
				.getTransactionsByAccountNumberAndTypeAndStatus(new BigInteger(accountNumber), type, status);
		List<WTransactionDTO> wTransactionDTOfilteredList;
		if (Objects.isNull(reason)) {
			wTransactionDTOfilteredList = wTransactionDTOList.stream()
					.filter(x -> x.getFlags5().equals(flags) && x.getId().equals(txnId)).collect(Collectors.toList());
		} else {
			wTransactionDTOfilteredList = wTransactionDTOList.stream().filter(x -> Objects.nonNull(x.getReason()))
					.filter(x -> Character.toString((char) x.getReason().byteValue()).equals(reason)
							&& x.getFlags5().equals(flags) && x.getId().equals(txnId))
					.collect(Collectors.toList());
		}
		if (wTransactionDTOfilteredList != null && wTransactionDTOfilteredList.size() == 1) {
			return wTransactionDTOfilteredList.get(0).getActivityId();
		}
		return BigInteger.ZERO;
	}

	public BigInteger getWTxnID(String accountNumber, String type, String reason, BigInteger flags) {
		List<WTransactionDTO> wTransactionDTOList = masspaytxnmapTxndao
				.getTransactionsByAccountNumberAndTypeAndStatus(new BigInteger(accountNumber), type, "S");
		List<WTransactionDTO> wTransactionDTOfilteredList;
		if (Objects.isNull(reason)) {
			wTransactionDTOfilteredList = wTransactionDTOList.stream().filter(x -> x.getFlags5().equals(flags))
					.collect(Collectors.toList());
		} else {
			wTransactionDTOfilteredList = wTransactionDTOList.stream().filter(x -> Objects.nonNull(x.getReason()))
					.filter(x -> Character.toString((char) x.getReason().byteValue()).equals(reason)
							&& x.getFlags5().equals(flags))
					.collect(Collectors.toList());
		}
		if (wTransactionDTOfilteredList != null && wTransactionDTOfilteredList.size() == 1) {
			return wTransactionDTOfilteredList.get(0).getId();
		}
		return BigInteger.ZERO;
	}

	public BigInteger getTransactionIdByAccountNumberAndType(String accountNumber, String type, String status,
			String amount, String currencyCode, BigInteger flags) {
		List<WTransactionDTO> wTransactionDTOList = masspaytxnmapTxndao
				.getTransactionsByAccountNumberAndTypeAndStatus(new BigInteger(accountNumber), type, status);
		List<WTransactionDTO> wTransactionDTOfilteredList = wTransactionDTOList.stream()
				.filter(x -> x.getAmount().equals(Long.valueOf(amount)) && x.getFlags5().equals(flags)
						&& x.getCurrencyCode().equals(currencyCode))
				.collect(Collectors.toList());
		if (wTransactionDTOfilteredList != null && wTransactionDTOfilteredList.size() == 1) {
			return wTransactionDTOfilteredList.get(0).getId();
		}
		return BigInteger.ZERO;
	}

	public BigInteger getPymtPaymentSideID(String accountNumber, String type, String subType, String txnId) {
		Set paymentIds = new HashSet<>();
		paymentIds.add(txnId);
		List<PaymentSideDTO> paymentSideDaoList = masspaytxnmapPymtdao.findByPaymentId(paymentIds);
		List<PaymentSideDTO> paymentSideDTOFilteredList = paymentSideDaoList.stream()
				.filter(x -> x.getTypeCode().equals(type) && x.getSubtypeCode().equals(subType)
						&& x.getAccountNumber().equals(new BigInteger(accountNumber)))
				.collect(Collectors.toList());
		if (paymentSideDTOFilteredList != null && paymentSideDTOFilteredList.size() == 1) {
			return paymentSideDTOFilteredList.get(0).getId();
		}
		return BigInteger.ZERO;
	}

	public BigInteger getPymtPaymentSideID(String accountNumber, String type, String txnId) {
		Set paymentIds = new HashSet<>();
		paymentIds.add(txnId);
		List<PaymentSideDTO> paymentSideDaoList = masspaytxnmapPymtdao.findByPaymentId(paymentIds);
		List<PaymentSideDTO> paymentSideDTOFilteredList = paymentSideDaoList.stream()
				.filter(x -> x.getTypeCode().equals(type) && x.getAccountNumber().equals(new BigInteger(accountNumber)))
				.collect(Collectors.toList());
		if (paymentSideDTOFilteredList != null && paymentSideDTOFilteredList.size() == 1) {
			return paymentSideDTOFilteredList.get(0).getId();
		}
		return BigInteger.ZERO;
	}

}
