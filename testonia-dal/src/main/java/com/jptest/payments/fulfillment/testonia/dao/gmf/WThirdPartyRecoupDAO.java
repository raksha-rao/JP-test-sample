/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.dao.gmf;

import java.util.List;
import java.util.Map;

import com.jptest.payments.fulfillment.testonia.model.gmf.WThirdPartyRecoupDTO;

/**
 * @JP Inc.
 * 
 *         DAO Class for insert, and select queries related wthird_party_recoup
 *         table
 *
 */
public class WThirdPartyRecoupDAO extends GmfDao {

	private static final String RECOUP_COUNT_QUERY = "select * from wthird_party_recoup where payout_transaction_id = '{PAYOUT_TRANSACTION_ID}'";
	private static final String RECOUP_INSERT_QUERY = "insert into wthird_party_recoup (ID, PAYOUT_TRANSACTION_ID, SELLER_ACCOUNT_NUMBER, PAYOUT_FUNDING_ACCOUNT_NUMBER, INVOICE_ID, RECOUP_ASK_AMOUNT, RECOUP_ASK_AMOUNT_CURR, RECOUP_SCHEDULED_DATE, RECOUP_STATUS, WAIVE_FEE_POLICY, MIN_RECOUP_AMOUNT, MIN_RECOUP_AMOUNT_CURR, PYPL_TIME_TOUCHED, TIME_CREATED) values ({ID},'{PAYOUT_TRANSACTION_ID}',{SELLER_ACCOUNT_NUMBER},{PAYOUT_FUNDING_ACCOUNT_NUMBER},'{INVOICE_ID}',{RECOUP_ASK_AMOUNT},'{RECOUP_ASK_AMOUNT_CURR}',{RECOUP_SCHEDULED_DATE},'{RECOUP_STATUS}','{WAIVE_FEE_POLICY}',{MIN_RECOUP_AMOUNT},'{MIN_RECOUP_AMOUNT_CURR}',{PYPL_TIME_TOUCHED},{TIME_CREATED})";

	// replacement tokens
	private static final String ID_REPLACEMENT_TOKEN = "{ID}";
	private static final String PAYOUT_TRANSACTION_ID_REPLACEMENT_TOKEN = "{PAYOUT_TRANSACTION_ID}";
	private static final String SELLER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{SELLER_ACCOUNT_NUMBER}";
	private static final String PAYOUT_FUNDING_ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{PAYOUT_FUNDING_ACCOUNT_NUMBER}";
	private static final String RECOUP_FUNDING_ACCOUNT_NUMBER_REPLACEMENT_TOKEN = "{RECOUP_FUNDING_ACCOUNT_NUMBER}";
	private static final String INVOICE_ID_REPLACEMENT_TOKEN = "{INVOICE_ID}";
	private static final String RECOUP_ASK_AMOUNT_REPLACEMENT_TOKEN = "{RECOUP_ASK_AMOUNT}";
	private static final String RECOUP_ASK_AMOUNT_CURR_REPLACEMENT_TOKEN = "{RECOUP_ASK_AMOUNT_CURR}";
	private static final String RECOUP_SCHEDULED_DATE_REPLACEMENT_TOKEN = "{RECOUP_SCHEDULED_DATE}";
	private static final String RECOUP_STATUS_REPLACEMENT_TOKEN = "{RECOUP_STATUS}";
	private static final String WAIVE_FEE_POLICY_REPLACEMENT_TOKEN = "{WAIVE_FEE_POLICY}";
	private static final String MIN_RECOUP_AMOUNT_REPLACEMENT_TOKEN = "{MIN_RECOUP_AMOUNT}";
	private static final String MIN_RECOUP_AMOUNT_CURR_REPLACEMENT_TOKEN = "{MIN_RECOUP_AMOUNT_CURR}";
	private static final String AMOUNT_RECOVERED_REPLACEMENT_TOKEN = "{AMOUNT_RECOVERED}";
	private static final String AMOUNT_RECOVERED_CURR_REPLACEMENT_TOKEN = "{AMOUNT_RECOVERED_CURR}";
	private static final String RECOUP_RECOVERY_TYPE_REPLACEMENT_TOKEN = "{RECOUP_RECOVERY_TYPE}";
	private static final String RECOUP_END_DATE_REPLACEMENT_TOKEN = "{RECOUP_END_DATE}";
	private static final String CANCEL_REASON_CODE_REPLACEMENT_TOKEN = "{CANCEL_REASON_CODE}";
	private static final String PYPL_TIME_TOUCHED_REPLACEMENT_TOKEN = "{PYPL_TIME_TOUCHED}";
	private static final String TIME_CREATED_REPLACEMENT_TOKEN = "{TIME_CREATED}";

	// column names
	private static final String ID_COL = "ID";
	private static final String PAYOUT_TRANSACTION_ID_COL = "PAYOUT_TRANSACTION_ID";
	private static final String SELLER_ACCOUNT_NUMBER_COL = "SELLER_ACCOUNT_NUMBER";
	private static final String PAYOUT_FUNDING_ACCOUNT_NUMBER_COL = "PAYOUT_FUNDING_ACCOUNT_NUMBER";
	private static final String RECOUP_FUNDING_ACCOUNT_NUMBER_COL = "RECOUP_FUNDING_ACCOUNT_NUMBER";
	private static final String INVOICE_ID_COL = "INVOICE_ID";
	private static final String RECOUP_ASK_AMOUNT_COL = "RECOUP_ASK_AMOUNT";
	private static final String RECOUP_ASK_AMOUNT_CURR_COL = "RECOUP_ASK_AMOUNT_CURR";
	private static final String RECOUP_SCHEDULED_DATE_COL = "RECOUP_SCHEDULED_DATE";
	private static final String RECOUP_STATUS_COL = "RECOUP_STATUS";
	private static final String WAIVE_FEE_POLICY_COL = "WAIVE_FEE_POLICY";
	private static final String MIN_RECOUP_AMOUNT_COL = "MIN_RECOUP_AMOUNT";
	private static final String MIN_RECOUP_AMOUNT_CURR_COL = "MIN_RECOUP_AMOUNT_CURR";
	private static final String AMOUNT_RECOVERED_COL = "AMOUNT_RECOVERED";
	private static final String AMOUNT_RECOVERED_CURR_COL = "AMOUNT_RECOVERED_CURR";
	private static final String RECOUP_RECOVERY_TYPE_COL = "RECOUP_RECOVERY_TYPE";
	private static final String RECOUP_END_DATE_COL = "RECOUP_END_DATE";
	private static final String CANCEL_REASON_CODE_COL = "CANCEL_REASON_CODE";
	private static final String PYPL_TIME_TOUCHED_COL = "PYPL_TIME_TOUCHED";
	private static final String TIME_CREATED_COL = "TIME_CREATED";

	/**
	 * 
	 * @param payoutTransactionId
	 * @return Object of type {@link WThirdPartyRecoupDTO}
	 */
	public WThirdPartyRecoupDTO getRecoupDetails(String payoutTransactionId) {
		final String query = RECOUP_COUNT_QUERY.replace(PAYOUT_TRANSACTION_ID_REPLACEMENT_TOKEN, payoutTransactionId);
		WThirdPartyRecoupDTO wThirdPartyRecoup = null;
		List<Map<String, Object>> dbResults = dbHelper.executeSelectQuery(getDatabaseName(), query);
		for (Map<String, Object> dbResult : dbResults) {
			wThirdPartyRecoup = populateWThirdPartyRecoupDTO(dbResult);
		}
		return wThirdPartyRecoup;
	}

	private WThirdPartyRecoupDTO populateWThirdPartyRecoupDTO(final Map<String, Object> result) {
		WThirdPartyRecoupDTO wThirdPartyRecoup = new WThirdPartyRecoupDTO();
		wThirdPartyRecoup.setId(this.getLong(result.get(ID_COL)));
		wThirdPartyRecoup.setPayoutTransactionId(this.getString(result.get(PAYOUT_TRANSACTION_ID_COL)));
		wThirdPartyRecoup.setSellerAccountNumber(this.getBigInteger(result.get(SELLER_ACCOUNT_NUMBER_COL)));
		wThirdPartyRecoup
				.setPayoutFundingAccountNumber(this.getBigInteger(result.get(PAYOUT_FUNDING_ACCOUNT_NUMBER_COL)));
		wThirdPartyRecoup
				.setRecoupFundingAccountNumber(this.getBigInteger(result.get(RECOUP_FUNDING_ACCOUNT_NUMBER_COL)));
		wThirdPartyRecoup.setInvoiceId(this.getString(result.get(INVOICE_ID_COL)));
		wThirdPartyRecoup.setRecoupAskAmount(this.getLong(result.get(RECOUP_ASK_AMOUNT_COL)));
		wThirdPartyRecoup.setRecoupAskAmountCurr(this.getString(result.get(RECOUP_ASK_AMOUNT_CURR_COL)));
		wThirdPartyRecoup.setRecoupScheduledDate(this.getLong(result.get(RECOUP_SCHEDULED_DATE_COL)));
		wThirdPartyRecoup.setRecoupStatus(this.getByte(result.get(RECOUP_STATUS_COL)));
		wThirdPartyRecoup.setWaiveFeePolicy(this.getByte(result.get(WAIVE_FEE_POLICY_COL)));
		wThirdPartyRecoup.setMinRecoupAmount(this.getLong(result.get(MIN_RECOUP_AMOUNT_COL)));
		wThirdPartyRecoup.setMinRecoupAmountCurr(this.getString(result.get(MIN_RECOUP_AMOUNT_CURR_COL)));
		wThirdPartyRecoup.setAmountRecovered(this.getLong(result.get(AMOUNT_RECOVERED_COL)));
		wThirdPartyRecoup.setAmountRecoveredCurr(this.getString(result.get(AMOUNT_RECOVERED_CURR_COL)));
		wThirdPartyRecoup.setRecoupRecoveryType(this.getByte(result.get(RECOUP_RECOVERY_TYPE_COL)));
		wThirdPartyRecoup.setRecoupEndDate(this.getLong(result.get(RECOUP_END_DATE_COL)));
		wThirdPartyRecoup.setCancelReasonCode(this.getByte(result.get(CANCEL_REASON_CODE_COL)));
		wThirdPartyRecoup.setPyplTimeTouched(this.getLong(result.get(PYPL_TIME_TOUCHED_COL)));
		wThirdPartyRecoup.setTimeCreated(this.getLong(result.get(TIME_CREATED_COL)));
		return wThirdPartyRecoup;
	}

	/**
	 * 
	 * @param recoupData
	 *            of type {@link WThirdPartyRecoupDTO}
	 * @return
	 */
	public int createRecoupRecord(WThirdPartyRecoupDTO recoupData) {
		final String updateQuery = RECOUP_INSERT_QUERY.replace(ID_REPLACEMENT_TOKEN, "WTHIRD_PARTY_RECOUP_SEQ.nextval")
				.replace(PAYOUT_TRANSACTION_ID_REPLACEMENT_TOKEN,
						convertNullToEmptyString(recoupData.getPayoutTransactionId()))
				.replace(SELLER_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
						convertNullToEmptyString(recoupData.getSellerAccountNumber()))
				.replace(PAYOUT_FUNDING_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
						convertNullToEmptyString(recoupData.getPayoutFundingAccountNumber()))
				.replace(RECOUP_FUNDING_ACCOUNT_NUMBER_REPLACEMENT_TOKEN,
						convertNullToEmptyString(recoupData.getRecoupFundingAccountNumber()))
				.replace(INVOICE_ID_REPLACEMENT_TOKEN, convertNullToEmptyString(recoupData.getInvoiceId()))
				.replace(RECOUP_ASK_AMOUNT_REPLACEMENT_TOKEN, convertNullToEmptyString(recoupData.getRecoupAskAmount()))
				.replace(RECOUP_ASK_AMOUNT_CURR_REPLACEMENT_TOKEN,
						convertNullToEmptyString(recoupData.getRecoupAskAmountCurr()))
				.replace(RECOUP_SCHEDULED_DATE_REPLACEMENT_TOKEN,
						convertNullToEmptyString(recoupData.getRecoupScheduledDate()))
				.replace(RECOUP_STATUS_REPLACEMENT_TOKEN, convertByteToChar(recoupData.getRecoupStatus()))
				.replace(WAIVE_FEE_POLICY_REPLACEMENT_TOKEN, convertByteToChar(recoupData.getWaiveFeePolicy()))
				.replace(MIN_RECOUP_AMOUNT_REPLACEMENT_TOKEN, convertNullToEmptyString(recoupData.getMinRecoupAmount()))
				.replace(MIN_RECOUP_AMOUNT_CURR_REPLACEMENT_TOKEN,
						convertNullToEmptyString(recoupData.getMinRecoupAmountCurr()))
				.replace(AMOUNT_RECOVERED_REPLACEMENT_TOKEN, convertNullToEmptyString(recoupData.getAmountRecovered()))
				.replace(AMOUNT_RECOVERED_CURR_REPLACEMENT_TOKEN,
						convertNullToEmptyString(recoupData.getAmountRecoveredCurr()))
				.replace(RECOUP_RECOVERY_TYPE_REPLACEMENT_TOKEN, convertByteToChar(recoupData.getRecoupRecoveryType()))
				.replace(RECOUP_END_DATE_REPLACEMENT_TOKEN, convertNullToEmptyString(recoupData.getRecoupEndDate()))
				.replace(CANCEL_REASON_CODE_REPLACEMENT_TOKEN, convertByteToChar(recoupData.getCancelReasonCode()))
				.replace(PYPL_TIME_TOUCHED_REPLACEMENT_TOKEN, convertNullToEmptyString(recoupData.getPyplTimeTouched()))
				.replace(TIME_CREATED_REPLACEMENT_TOKEN, convertNullToEmptyString(recoupData.getTimeCreated()));
		return dbHelper.executeUpdateQuery(getDatabaseName(), updateQuery);
	}

}
