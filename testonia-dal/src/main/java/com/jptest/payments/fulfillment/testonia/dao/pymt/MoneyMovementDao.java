package com.jptest.payments.fulfillment.testonia.dao.pymt;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.model.pymt.MoneyMovementDTO;

/**
 * Represents money_movement table of PYMT database
 */
public class MoneyMovementDao extends PymtDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoneyMovementDao.class);

	private static final String GET_MONEY_MOVEMENT_FOR_ACCOUNT_NUMBER = "select * from money_movement where payment_side_id in ({payment_side_id}) order by type_code, debit_credit_code, subtype_code, status_code, reason_code, money_amount, flags06";
	private static final String PAYMENT_SIDE_ID_REPLACEMENT = "{payment_side_id}";

	private static final String GET_PAYMENT_SIDE_ID_QUERY = "SELECT PAYMENT_SIDE_ID FROM money_movement WHERE id in ({transactionId})";
	private static final String TRANSACTION_ID_REPLACEMENT_TOKEN = "{transactionId}";

	private static final String ACCOUNT_NUMBER_COL = "ACCOUNT_NUMBER";
	private static final String GET_MM_TRANSACTION_COUNT_QUERY = "select count(*) CNT from money_movement where account_number='{accountNumber}' "
			+ "and TYPE_CODE='{type}' and SUBTYPE_CODE='{subType}' and REASON_CODE='{reasonCode}' and ID='{txnId}' and DEBIT_CREDIT_CODE='{debitCreditCode}' and PAYMENT_SIDE_ID='{payment_side_id}'";
	private static final String GET_FEE_MM_TRANSACTION_COUNT_QUERY = "select count(*) CNT from money_movement where account_number='{accountNumber}' "
			+ "and TYPE_CODE='{type}' and ID='{txnId}' and DEBIT_CREDIT_CODE='{debitCreditCode}' and PAYMENT_SIDE_ID='{payment_side_id}'";
	private static final String GET_BAL_MML_TRANSACTION_COUNT_QUERY = "select count(*) CNT from money_movement where account_number='{accountNumber}' "
			+ "and TYPE_CODE='{type}' and SUBTYPE_CODE='{subType}' and DEBIT_CREDIT_CODE='{debitCreditCode}' and PAYMENT_SIDE_ID='{payment_side_id}'";

	private static final String ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{accountNumber}";
	private static final String TYPE_REPLACEMENT_TOKEN = "{type}";
	private static final String SUBTYPE_REPLACEMENT_TOKEN = "{subType}";
	private static final String REASON_CODE_REPLACEMENT_TOKEN = "{reasonCode}";
	private static final String TXN_IDS_REPLACEMENT_TOKEN = "{txnId}";
	private static final String DEBIT_ICREDIT_REPLACEMENT_TOKEN = "{debitCreditCode}";
	private static final String PAYMENT_SIDE_ID_REPLACEMENT_TOKEN = "{payment_side_id}";
	private static final String COUNT_COL = "CNT";

	public List<MoneyMovementDTO> findByPaymentSideIds(Set<String> paymentSetIdsSet) {
		List<MoneyMovementDTO> moneyMovements = new ArrayList<MoneyMovementDTO>();
		if (paymentSetIdsSet != null) {
			String query = GET_MONEY_MOVEMENT_FOR_ACCOUNT_NUMBER.replace(PAYMENT_SIDE_ID_REPLACEMENT,
					mapIdToString(paymentSetIdsSet, paymentSideId -> paymentSideId));
			List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
			for (Map<String, Object> result : queryResult) {
				moneyMovements.add(mapMoneyMovement(result));
			}
		}
		LOGGER.debug("Found Moneymovements:{}", moneyMovements.size());
		return moneyMovements;

	}

	public BigInteger findPaymentSideId(final BigInteger transactionId) {
		BigInteger paymentSideId = null;

		final String query = GET_PAYMENT_SIDE_ID_QUERY.replace(TRANSACTION_ID_REPLACEMENT_TOKEN,
				transactionId.toString());
		final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
		for (final Map<String, Object> result : queryResult) {
			paymentSideId = mapMoneyMovement(result).getPaymentSideId();
		}

		LOGGER.debug("Found paymentSideId:{}", paymentSideId.toString());
		return paymentSideId;
	}

	public int getTransactionMMCount(String accountNumber, String type, String subType, String reasonCode,
			String debitCreditCode, BigInteger txnId, String payment_side_id) {
		String query = null;

		query = GET_MM_TRANSACTION_COUNT_QUERY.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
				.replace(TYPE_REPLACEMENT_TOKEN, type).replace(SUBTYPE_REPLACEMENT_TOKEN, subType)
				.replace(REASON_CODE_REPLACEMENT_TOKEN, reasonCode).replace(TXN_IDS_REPLACEMENT_TOKEN, txnId.toString())
				.replace(DEBIT_ICREDIT_REPLACEMENT_TOKEN, debitCreditCode)
				.replace(PAYMENT_SIDE_ID_REPLACEMENT_TOKEN, payment_side_id);
		Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
		return getInteger(result.get(COUNT_COL));
	}

	public int getTransactionMMCount(String accountNumber, String type, String reasonCode, String debitCreditCode,
			BigInteger txnId, String payment_side_id) {

		String query = GET_FEE_MM_TRANSACTION_COUNT_QUERY
				.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
				.replace(TYPE_REPLACEMENT_TOKEN, type).replace(REASON_CODE_REPLACEMENT_TOKEN, reasonCode)
				.replace(TXN_IDS_REPLACEMENT_TOKEN, txnId.toString())
				.replace(DEBIT_ICREDIT_REPLACEMENT_TOKEN, debitCreditCode)
				.replace(PAYMENT_SIDE_ID_REPLACEMENT_TOKEN, payment_side_id);

		Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
		return getInteger(result.get(COUNT_COL));
	}

	public int getTransactionMMCount(String accountNumber, String type, String subType, String debitCreditCode,
			String payment_side_id) {

		String query = GET_BAL_MML_TRANSACTION_COUNT_QUERY
				.replace(ACCOUNT_NUMBER_REPLACEMENT_TOKEN, accountNumber.toString())
				.replace(TYPE_REPLACEMENT_TOKEN, type).replace(SUBTYPE_REPLACEMENT_TOKEN, subType)
				.replace(DEBIT_ICREDIT_REPLACEMENT_TOKEN, debitCreditCode)
				.replace(PAYMENT_SIDE_ID_REPLACEMENT_TOKEN, payment_side_id);

		Map<String, Object> result = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
		return getInteger(result.get(COUNT_COL));
	}

	private MoneyMovementDTO mapMoneyMovement(Map<String, Object> result) {
		MoneyMovementDTO moneyMovement = new MoneyMovementDTO();
		moneyMovement.setId(getBigInteger(result.get("ID")));
		moneyMovement.setPaymentSideId(getBigInteger(result.get("PAYMENT_SIDE_ID")));
		moneyMovement.setDebitCreditCode(getString(result.get("DEBIT_CREDIT_CODE")));
		moneyMovement.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
		moneyMovement.setWalletInstrumentId(getString(result.get("WALLET_INSTRUMENT_ID")));
		moneyMovement.setWalletInstrumentTypeCode(getString(result.get("WALLET_INSTRUMENT_TYPE_CODE")));
		moneyMovement.setTypeCode(getString(result.get(TYPE_CODE_COL)));
		moneyMovement.setSubtypeCode(getString(result.get(SUBTYPE_CODE_COL)));
		moneyMovement.setReasonCode(getString(result.get(REASON_CODE_COL)));
		moneyMovement.setStatusCode(getString(result.get(STATUS_CODE_COL)));
		moneyMovement.setMoneyAmount(getLong(result.get(MONEY_AMOUNT_COL)));
		moneyMovement.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
		moneyMovement.setCurrencyExchangeId(getBigInteger(result.get("CURRENCY_EXCHANGE_ID")));
		moneyMovement.setUsdMoneyAmount(getLong(result.get(USD_MONEY_AMOUNT_COL)));
		moneyMovement.setExpectedClearingTime(getLong(result.get("EXPECTED_CLEARING_TIME")));
		moneyMovement.setAtPostingBalance(getLong(result.get("AT_POSTING_BALANCE")));
		moneyMovement.setAtPostingWithheldBalance(getLong(result.get("AT_POSTING_WITHHELD_BALANCE")));
		moneyMovement.setBalancePostingTime(getLong(result.get("BALANCE_POSTING_TIME")));
		moneyMovement.setInitiationTime(getLong(result.get(INITIATION_TIME_COL)));
		moneyMovement.setLastUpdatedTime(getLong(result.get(LAST_UPDATED_TIME_COL)));
		moneyMovement.setLegacyInstrumentTypeCode(getString(result.get("LEGACY_INSTRUMENT_TYPE_CODE")));
		moneyMovement.setLegacyInstrumentId(getLong(result.get("LEGACY_INSTRUMENT_ID")));
		moneyMovement.setLegacyParentId(getBigInteger(result.get("LEGACY_PARENT_ID")));
		moneyMovement.setBackupForMoneyMovementId(getLong(result.get("BACKUP_FOR_MONEY_MOVEMENT_ID")));
		moneyMovement.setReplacedMoneyMovementId(getLong(result.get("REPLACED_MONEY_MOVEMENT_ID")));
		moneyMovement.setFlags01(getBigInteger(result.get(FLAGS01_COL)));
		moneyMovement.setFlags02(getBigInteger(result.get(FLAGS02_COL)));
		moneyMovement.setFlags03(getBigInteger(result.get(FLAGS03_COL)));
		moneyMovement.setFlags04(getBigInteger(result.get(FLAGS04_COL)));
		moneyMovement.setFlags05(getBigInteger(result.get(FLAGS05_COL)));
		moneyMovement.setFlags06(getBigInteger(result.get(FLAGS06_COL)));
		moneyMovement.setFlags07(getBigInteger(result.get(FLAGS07_COL)));
		moneyMovement.setFlags08(getBigInteger(result.get(FLAGS08_COL)));
		moneyMovement.setFlags09(getBigInteger(result.get(FLAGS09_COL)));
		moneyMovement.setFlags10(getBigInteger(result.get(FLAGS10_COL)));
		moneyMovement.setRowCreatedTime(getLong(result.get("ROW_CREATED_TIME")));
		moneyMovement.setRowUpdatedTime(getLong(result.get("ROW_UPDATED_TIME")));
		moneyMovement.setFinancialJournalAid(getLong(result.get("FINANCIAL_JOURNAL_AID")));
		moneyMovement.setFjCreatedTime(getLong(result.get("FJ_CREATED_TIME")));
		moneyMovement.setFjUpdatedTime(getLong(result.get("FJ_UPDATED_TIME")));
		moneyMovement.setMemo(getString(result.get("MEMO")));
		moneyMovement.setLegacySharedId(getBigInteger(result.get("LEGACY_SHARED_ID")));
		return moneyMovement;
	}
}
