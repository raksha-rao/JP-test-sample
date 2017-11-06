package com.jptest.payments.fulfillment.testonia.dao;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

@Singleton
public class WTransactionDao implements IwTransactionDao {

	@Inject
	@Named("WTransactionDaoP10")
	private IwTransactionDao daoP10;

	@Inject
	@Named("WTransactionDaoP20")
	private IwTransactionDao daoP20;

	private enum Platform {
		P10, P20
	};

	public WTransactionDao() {
	}

	@Override
	public Integer getTransactionRecordCount(String transactionId) {
		Integer result = null;
		if (transactionId != null) {
			if (Platform.P10.equals(getPlatForm(transactionId))) {
				result = daoP10.getTransactionRecordCount(transactionId);
			} else {
				result = daoP20.getTransactionRecordCount(transactionId);
			}
		}
		return result;
	}

	@Override
	public WTransactionDTO getTransactionDetails(final BigInteger transactionId) {
		WTransactionDTO result = null;
		if (transactionId != null) {
			if (Platform.P10.equals(this.getPlatForm(transactionId.toString()))) {
				result = this.daoP10.getTransactionDetails(transactionId);
			} else {
				result = this.daoP20.getTransactionDetails(transactionId);
			}
		}
		return result;
	}

	private Platform getPlatForm(final String transactionId) {
		if (transactionId.length() <= 12) {
			return Platform.P10;
		} else {
			return Platform.P20;
		}
	}

	@Override
	public List<WTransactionDTO> getParentTransactionsDetails(final BigInteger transactionId) {
		List<WTransactionDTO> result = null;
		if (transactionId != null) {
			if (Platform.P10.equals(this.getPlatForm(transactionId.toString()))) {
				result = this.daoP10.getParentTransactionsDetails(transactionId);
			} else {
				result = this.daoP20.getParentTransactionsDetails(transactionId);
			}
		}
		return result;
	}

	@Override
	public List<BigInteger> getTransactionIdByAccountNumberAndType(final BigInteger accountNumber, final String type) {
		return this.daoP10.getTransactionIdByAccountNumberAndType(accountNumber, type);
	}

	@Override
	public int getTypeURowCount(final BigInteger senderAccountNumber, final BigInteger recipientAccountNumber,
			final String type, final String amount, final String currencyCode, final long flag5) {
		return this.daoP10.getTypeURowCount(senderAccountNumber, recipientAccountNumber, type, amount, currencyCode,
				flag5);
	}

	@Override
	public int getTypeCRowCount(final BigInteger senderAccountNumber, final BigInteger recipientAccountNumber,
			final String type, final String subType, final String amount, final String currencyCode) {
		return this.daoP10.getTypeCRowCount(senderAccountNumber, recipientAccountNumber, type, subType, amount,
				currencyCode);
	}

	@Override
	public int getType3RowCount(final BigInteger recipientAccountNumber, final String type, final String amount,
			final String currencyCode) {
		return this.daoP10.getType3RowCount(recipientAccountNumber, type, amount, currencyCode);
	}

	@Override
	public int getType5RowCount(final BigInteger recipientAccountNumber, final String type, final String amount,
			final String currencyCode, final String reason, String status) {
		return this.daoP20.getType5RowCount(recipientAccountNumber, type, amount, currencyCode, reason, status);
	}

	@Override
	public List<BigInteger> getTransactionIdByAccountNumberAndTypeAndStatus(final BigInteger accountNumber,
			final String type, final String status) {
		throw new UnsupportedOperationException("Not implemented yet");
		// TODO Auto-generated method stub

	}

	@Override
	public List<WTransactionDTO> getTransactionsByAccountNumber(final String accountNumber) {
		return this.daoP20.getTransactionsByAccountNumber(accountNumber);
	}

}
