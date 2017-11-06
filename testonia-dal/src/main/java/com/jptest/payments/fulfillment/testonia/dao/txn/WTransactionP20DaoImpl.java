package com.jptest.payments.fulfillment.testonia.dao.txn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import com.jptest.payments.fulfillment.testonia.dao.IwTransactionDao;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;

/**
 * Represents WTRANSACTION_P2 table of TXN database
 */
@Singleton
public class WTransactionP20DaoImpl extends TxnDao implements IwTransactionDao {

	private static final String WTRANSACTION_P2_QUERY_COUNT = "select count(id) as count from wtransaction_p2 where id = {id}";
	private static final String WTRANSACTION_P2_REPLACEMENT_TOKEN = "{id}";

	private static final String GET_TRANSACTION_DETAILS_QUERY = "SELECT * FROM WTRANSACTION_P2 WHERE rowid  = (SELECT DISTINCT FIRST_VALUE(rowid) OVER (ORDER BY wt_join.id DESC) FROM WTRANSACTION_P2 wt_join WHERE wt_join.base_id = {transactionId})";
	private static final String TRANSACTION_ID_REPLACEMENT_TOKEN = "{transactionId}";

	private static final String GET_PARENT_TRANSACTION_DETAILS_QUERY = "select * from wtransaction_p2 where (parent_id, base_id,id) in (select parent_id,base_id,max(id) from WTRANSACTION_P2 "
			+ "where base_id not in {transactionId} connect by parent_id = prior base_id  start with base_id = {transactionId}  group by parent_id, base_id) order by type,time_created,base_id,id";

	private static final String GET_TRANSACTION_ID_WITH_STATUS_QUERY = "select id from wtransaction_p2 where account_number={accountNumber} and type='{accountType}'  and status='{status}' order by id";

	private static final String GET_TRANSACTIONS_WITH_STATUS_QUERY = "select * from wtransaction_p2 where account_number={accountNumber} and type='{accountType}'  and status='{status}'";
	private static final String GET_PAYMENT_ID_QUERY = "select payment_id from wtransaction_p2 where id = '{transactionId}'";
	// private static final String GET_TRANSACTION_ID_QUERY = "select id from
	// wtransaction_p2 where
	// account_number={accountNumber} and type='{accountType}' order by id";
	private static final String GET_ROW_COUNT_FOR_ANYTYPE_ANYSTATUS_QUERY = "select count(*) count from wtransaction_p2 where account_number='{accountNumber}' and "
			+ "type='{type}' and reason='{reason}' and status='{status}' and amount='{amount}' and currency_code = '{currencyCode}'";

	private static final String GET_TRANSACTIONS_COUNT_QUERY = "select count(*) count from wtransaction_p2 where account_number={accountNumber} and type='{type}' and amount={amount} and currency_code='{currencyCode}' and status='{status}'";
	
	private static final String GET_TRANSACTIONS_BY_ACCOUNT_QUERY = "select * from wtransaction_p2 where account_number={accountNumber} order by type, subtype";

	private static final String ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{accountNumber}";
	private static final String ACCOUNT_TYPE_REPLACEMENT_TOKEN = "{accountType}";
	private static final String AMOUNT_REPLACEMENT_TOKEN = "{amount}";
	private static final String STATUS_REPLACEMENT_TOKEN = "{status}";
	private static final String CURRENCY_CODE_REPLACEMENT_TOKEN = "{currencyCode}";
	private static final String REASON_REPLACEMENT_TOKEN = "{reason}";
	private static final String TYPE_REPLACEMENT_TOKEN = "{type}";

	// Column names common across tables
	private static final String COUNT_COL = "COUNT";
	private static final String COUNTERPARTY_COL = "COUNTERPARTY";
	private static final String TIME_UPDATED_COL = "TIME_UPDATED";

	// WTRANSACTION_P2 table
	private static final String ACCOUNT_NUMBER_COL = "ACCOUNT_NUMBER";
	private static final String ACH_ID_COL = "ACH_ID";
	private static final String ADDRESS_ID_COL = "ADDRESS_ID";
	private static final String AMOUNT_COL = "AMOUNT";
	private static final String BALANCE_AT_TIME_CREATED_COL = "BALANCE_AT_TIME_CREATED";
	private static final String BASE_ID_COL = "BASE_ID";
	private static final String CC_TRANS_ID_COL = "CCTRANS_ID";
	private static final String COUNTERPARTY_ALIAS_COL = "COUNTERPARTY_ALIAS";
	private static final String COUNTERPARTY_ALIAS_TYPE_COL = "COUNTERPARTY_ALIAS_TYPE";
	private static final String COUNTERPARTY_LAST_LOGIN_IP_COL = "COUNTERPARTY_LAST_LOGIN_IP";
	private static final String CURRENCY_CODE_COL = "CURRENCY_CODE";
	private static final String FLAGS_COL = "FLAGS";
	private static final String FLAGS2_COL = "FLAGS2";
	private static final String FLAGS3_COL = "FLAGS3";
	private static final String FLAGS4_COL = "FLAGS4";
	private static final String FLAGS5_COL = "FLAGS5";
	private static final String FLAGS6_COL = "FLAGS6";
	private static final String FLAGS7_COL = "FLAGS7";
	private static final String MEMO_COL = "MEMO";
	private static final String MESSAGE_ID_COL = "MESSAGE_ID";
	private static final String PARENT_ID_COL = "PARENT_ID";
	private static final String PAYMENT_ID_COL = "PAYMENT_ID";
	private static final String REASON_COL = "REASON";
	private static final String SHARED_ID_COL = "SHARED_ID";
	private static final String STATUS_COL = "STATUS";
	private static final String SUBTYPE_COL = "SUBTYPE";
	private static final String TARGET_ALIAS_ID_COL = "TARGET_ALIAS_ID";
	private static final String TIME_CREATED_COL = "TIME_CREATED";
	private static final String TIME_PROCESSED_COL = "TIME_PROCESSED";
	private static final String TIME_USER_COL = "TIME_USER";
	private static final String TRANSITION_COL = "TRANSITION";
	private static final String TYPE_COL = "TYPE";
	private static final String USD_AMOUNT_COL = "USD_AMOUNT";

	private static final String GET_BASE_ID_QUERY = "SELECT BASE_ID FROM WTRANSACTION_P2 WHERE ACCOUNT_NUMBER = '{account_number}' and type='U' and status='P' order by base_id desc";

	private static final String GET_DEBIT_TRANSACTION_ID = "SELECT BASE_ID FROM WTRANSACTION_P2 WHERE ACCOUNT_NUMBER = {accountNumber} AND TYPE='{accountType}' AND STATUS='{status}' AND SIGN(AMOUNT) = -1";
	private static final String GET_BASE_ID_WITH_TYPE_QUERY = "SELECT BASE_ID FROM WTRANSACTION_P2 WHERE ACCOUNT_NUMBER = '{accountNumber}' and type='{accountType}' order by base_id desc";

	private static final String ACCOUNT_NUMBER_TOKEN = "{account_number}";

	@Override
	public Integer getTransactionRecordCount(final String transactionId) {
		final String query = WTRANSACTION_P2_QUERY_COUNT.replace(WTRANSACTION_P2_REPLACEMENT_TOKEN, transactionId);
		final Map<String, Object> dbResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
				query);
		return this.getInteger(dbResult.get(COUNT_COL));
	}

	@Override
	public WTransactionDTO getTransactionDetails(final BigInteger transactionId) {
		final String query = GET_TRANSACTION_DETAILS_QUERY.replace(TRANSACTION_ID_REPLACEMENT_TOKEN,
				transactionId.toString());
		final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
		WTransactionDTO transaction = null;
		for (final Map<String, Object> result : queryResult) {
			transaction = this.populateWTransactionDTO(result);
		}

		return transaction;
	}

	@Override
	public List<WTransactionDTO> getParentTransactionsDetails(final BigInteger transactionId) {
		final String query = GET_PARENT_TRANSACTION_DETAILS_QUERY.replace(TRANSACTION_ID_REPLACEMENT_TOKEN,
				transactionId.toString());
		final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
		final List<WTransactionDTO> transactions = new ArrayList<>();
		for (final Map<String, Object> result : queryResult) {
			transactions.add(this.populateWTransactionDTO(result));
		}

		return transactions;
	}

	public Integer getTransactionCount(final String accountNumber, final String type, final String amount,
			final String currencyCode, final String status) {
		final String query = GET_TRANSACTIONS_COUNT_QUERY.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber)
				.replace(ACCOUNT_TYPE_REPLACEMENT_TOKEN, type).replace(AMOUNT_REPLACEMENT_TOKEN, amount)
				.replace(CURRENCY_CODE_REPLACEMENT_TOKEN, currencyCode).replace(STATUS_REPLACEMENT_TOKEN, status);
		final Map<String, Object> queryResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
				query);
		return this.getInteger(queryResult.get(COUNT_COL));
	}

	private WTransactionDTO populateWTransactionDTO(final Map<String, Object> result) {
		final WTransactionDTO transaction = new WTransactionDTO();
		transaction.setAccountNumber(this.getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
		transaction.setAchId(this.getBigInteger(result.get(ACH_ID_COL)));
		transaction.setAddressId(this.getBigInteger(result.get(ADDRESS_ID_COL)));
		transaction.setAmount(this.getLong(result.get(AMOUNT_COL)));
		transaction.setBalanceAtTimeCreated(this.getLong(result.get(BALANCE_AT_TIME_CREATED_COL)));
		transaction.setId(this.getBigInteger(result.get(BASE_ID_COL)));
		transaction.setCctransId(this.getBigInteger(result.get(CC_TRANS_ID_COL)));
		transaction.setCounterpartyAlias(this.getString(result.get(COUNTERPARTY_ALIAS_COL)));
		transaction.setCounterpartyAliasType(this.getByte(result.get(COUNTERPARTY_ALIAS_TYPE_COL)));
		// transaction.set(getBigInteger(result.get(COUNTERPARTY_ALIAS_UPPER_COL)));
		transaction.setCounterparty(this.getBigInteger(result.get(COUNTERPARTY_COL)));
		transaction.setCounterpartyLastLoginIp(this.getString(result.get(COUNTERPARTY_LAST_LOGIN_IP_COL)));
		transaction.setCurrencyCode(this.getString(result.get(CURRENCY_CODE_COL)));
		transaction.setFlags1(this.getLong(result.get(FLAGS_COL)));
		transaction.setFlags2(this.getLong(result.get(FLAGS2_COL)));
		transaction.setFlags3(this.getLong(result.get(FLAGS3_COL)));
		transaction.setFlags4(this.getLong(result.get(FLAGS4_COL)));
		transaction.setFlags5(this.getBigInteger(result.get(FLAGS5_COL)));
		transaction.setFlags6(this.getBigInteger(result.get(FLAGS6_COL)));
		transaction.setFlags7(this.getBigInteger(result.get(FLAGS7_COL)));
		transaction.setMemo(this.getString(result.get(MEMO_COL)));
		// transaction.set(getBigInteger(result.get(MESSAGE_COL)));
		transaction.setMessageId(this.getBigInteger(result.get(MESSAGE_ID_COL)));
		transaction.setParentId(this.getBigInteger(result.get(PARENT_ID_COL)));
		// transaction.set(getBigInteger(result.get(PAYEE_EMAIL_COL)));
		// transaction.set(getBigInteger(result.get(PAYEE_EMAIL_UPPER_COL)));
		transaction.setActivityId(this.getBigInteger(result.get(PAYMENT_ID_COL)));
		transaction.setReason(this.getByte(result.get(REASON_COL)));
		transaction.setSharedId(this.getBigInteger(result.get(SHARED_ID_COL)));
		transaction.setStatus(this.getByte(result.get(STATUS_COL)));
		transaction.setSubtype(this.getByte(result.get(SUBTYPE_COL)));
		// transaction.set(getBigInteger(result.get(SYNC_GROUP_COL)));
		transaction.setTargetAliasId(this.getBigInteger(result.get(TARGET_ALIAS_ID_COL)));
		transaction.setTimeCreated(this.getLong(result.get(TIME_CREATED_COL)));
		// transaction.set(getBigInteger(result.get(TIME_CREATED_PAYER_COL)));
		// transaction.set(getBigInteger(result.get(TIME_INACTIVE_COL)));
		transaction.setTimeProcessed(this.getLong(result.get(TIME_PROCESSED_COL)));
		// transaction.set(getBigInteger(result.get(TIME_RECEIVED_PAYEE)));
		// transaction.set(getBigInteger(result.get(TIME_ROW_UPDATED_COL)));
		transaction.setTimeUpdated(this.getLong(result.get(TIME_UPDATED_COL)));
		transaction.setTimeUser(this.getLong(result.get(TIME_USER_COL)));
		// transaction.set(getBigInteger(result.get(TRANSACTION_ID_COL)));
		transaction.setTransition(this.getByte(result.get(TRANSITION_COL)));
		transaction.setType(this.getByte(result.get(TYPE_COL)));
		transaction.setUsdAmount(this.getLong(result.get(USD_AMOUNT_COL)));
		return transaction;
	}

	@Override
	public List<BigInteger> getTransactionIdByAccountNumberAndTypeAndStatus(final BigInteger accountNumber,
			final String type, final String status) {

		final String query = GET_TRANSACTION_ID_WITH_STATUS_QUERY
				.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
				.replace(ACCOUNT_TYPE_REPLACEMENT_TOKEN, type).replace(STATUS_REPLACEMENT_TOKEN, status);
		final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
		final List<BigInteger> txnIds = new ArrayList<BigInteger>();
		for (final Map<String, Object> result : queryResult) {
			txnIds.add(this.getBigInteger(result.get("ID")));
		}

		return txnIds;
	}

	@Override
	public List<BigInteger> getTransactionIdByAccountNumberAndType(final BigInteger accountNumber, final String type) {
		final String query = GET_BASE_ID_WITH_TYPE_QUERY
				.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
				.replace(ACCOUNT_TYPE_REPLACEMENT_TOKEN, type);

		final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
		final List<BigInteger> txnIds = new ArrayList<BigInteger>();
		for (final Map<String, Object> result : queryResult) {
			txnIds.add(this.getBigInteger(result.get(BASE_ID_COL)));
		}

		return txnIds;

	}

	@Override
	public int getTypeURowCount(final BigInteger senderAccountNumber, final BigInteger recipientAccountNumber,
			final String type, final String amount, final String currencyCode, final long flag5) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public int getTypeCRowCount(final BigInteger senderAccountNumber, final BigInteger recipientAccountNumber,
			final String type, final String subType, final String amount, final String currencyCode) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public int getType3RowCount(final BigInteger recipientAccountNumber, final String type, final String amount,
			final String currencyCode) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public String getBaseIdForPendingTransaction(final String accountNumber) {
		final String query = GET_BASE_ID_QUERY.replace(ACCOUNT_NUMBER_TOKEN, accountNumber);
		final Map<String, Object> queryResult = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
				query);
		final String baseId = this.getString(queryResult.get(BASE_ID_COL));

		return baseId;
	}

	public List<WTransactionDTO> getTransactionsByAccountNumberAndTypeAndStatus(final BigInteger accountNumber,
			final String type, final String status) {

		final String query = GET_TRANSACTIONS_WITH_STATUS_QUERY
				.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
				.replace(ACCOUNT_TYPE_REPLACEMENT_TOKEN, type).replace(STATUS_REPLACEMENT_TOKEN, status);
		final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
		final List<WTransactionDTO> transactions = new ArrayList<>();
		for (final Map<String, Object> result : queryResult) {
			transactions.add(this.populateWTransactionDTO(result));
		}

		return transactions;
	}

	public List<BigInteger> getDebitTransactionIdByAccountNumberAndTypeAndStatus(final BigInteger accountNumber,
			final String type, final String status) {
		final String query = GET_DEBIT_TRANSACTION_ID
				.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
				.replace(ACCOUNT_TYPE_REPLACEMENT_TOKEN, type).replace(STATUS_REPLACEMENT_TOKEN, status);
		final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
		final List<BigInteger> txnIds = new ArrayList<BigInteger>();
		for (final Map<String, Object> result : queryResult) {
			txnIds.add(this.getBigInteger(result.get("ID")));
		}

		return txnIds;
	}

	public int getType5RowCount(final BigInteger accountNumber, final String type, final String amount,
			final String currencyCode, final String reason, String status) {
		final String query = GET_ROW_COUNT_FOR_ANYTYPE_ANYSTATUS_QUERY
				.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
				.replace(TYPE_REPLACEMENT_TOKEN, type).replace(AMOUNT_REPLACEMENT_TOKEN, amount)
				.replace(CURRENCY_CODE_REPLACEMENT_TOKEN, currencyCode).replace(REASON_REPLACEMENT_TOKEN, reason)
				.replace(STATUS_REPLACEMENT_TOKEN, status);

		final Map<String, Object> result = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
				query);

		return this.getInteger(result.get(COUNT_COL));
	}
	
	public List<WTransactionDTO> getTransactionsByAccountNumber(final String accountNumber) {

		final String query = GET_TRANSACTIONS_BY_ACCOUNT_QUERY.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber);
		final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
		final List<WTransactionDTO> transactions = new ArrayList<>();
		for (final Map<String, Object> result : queryResult) {
			transactions.add(this.populateWTransactionDTO(result));
		}

		return transactions;
	}
	
	
	 
	 public String getPaymentIdByTxnId(String txnId) {
	        String sql = GET_PAYMENT_ID_QUERY.replace(TRANSACTION_ID_REPLACEMENT_TOKEN, txnId) ;
	        final Map<String, Object> result = this.dbHelper.executeSelectQueryForSingleResult(this.getDatabaseName(),
	                sql);
	        return getString(result.get(PAYMENT_ID_COL).toString());
	    }

}
