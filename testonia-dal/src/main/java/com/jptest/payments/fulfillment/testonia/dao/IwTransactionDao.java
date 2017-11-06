package com.jptest.payments.fulfillment.testonia.dao;

import java.math.BigInteger;
import java.util.List;

import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

public interface IwTransactionDao {

	Integer getTransactionRecordCount(String transactionId);

	WTransactionDTO getTransactionDetails(BigInteger transactionId);

	List<WTransactionDTO> getParentTransactionsDetails(BigInteger transactionId);

	List<BigInteger> getTransactionIdByAccountNumberAndTypeAndStatus(BigInteger accountNumber, String type,
			String status);

	List<BigInteger> getTransactionIdByAccountNumberAndType(BigInteger accountNumber, String type);

	int getTypeURowCount(BigInteger senderAccountNumber, BigInteger recipientAccountNumber, String type, String amount,
			String currencyCode, long flag5);

	int getTypeCRowCount(BigInteger senderAccountNumber, BigInteger recipientAccountNumber, String type, String subType,
			String amount, String currencyCode);

	int getType3RowCount(BigInteger recipientAccountNumber, String type, String amount, String currencyCode);

	int getType5RowCount(BigInteger senderAccountNumber, String type, String amount, String currencyCode, String reason,
			String status);
	
	List<WTransactionDTO> getTransactionsByAccountNumber(String accountNumber);

}
