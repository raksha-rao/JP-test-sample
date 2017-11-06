package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.FeesChargedJournalEntryDTO;

/**
 * Represents FEES_CHARGED_JOURNAL_ENTRY table of ENG database
 */
@Singleton
public class FeesChargedJournalEntryDao extends EngDao {

    private static final String GET_FEES_CHARGED_JOURNAL_ENTRY_DETAILS_QUERY = "SELECT * FROM FEES_CHARGED_JOURNAL_ENTRY where activity_id in ({activityIds}) order by FEE_REASON_CD, FEE_AMOUNT, FEE_CURRENCY_CD, FEE_AMOUNT_USD, DIRECTION_CD, jptest_LEGAL_ENTITY_CD, COUNTRY_CD, PARTICIPANT_TYPE_CD, USER_GROUP_CD";
    private static final String ACTIVITY_EVENT_SEQUENCE_COL = "ACTIVITY_EVENT_SEQUENCE";
    private static final String AGGREGATE_ACCOUNT_NUMBER_COL = "AGGREGATE_ACCOUNT_NUMBER";
    private static final String DATA_COL = "DATA";
    private static final String FEE_AMOUNT_COL = "FEE_AMOUNT";
    private static final String FEE_AMOUNT_USD_COL = "FEE_AMOUNT_USD";
    private static final String FEE_CURRENCY_CD_COL = "FEE_CURRENCY_CD";
    private static final String FEE_REASON_CD_COL = "FEE_REASON_CD";

    /**
     * Queries FEES_CHARGED_JOURNAL_ENTRY table for input activity-id
     * @param activityId
     * @return
     */
    public List<FeesChargedJournalEntryDTO> getFeesChargedJournalEntryDetails(BigInteger activityId) {
        String query = GET_FEES_CHARGED_JOURNAL_ENTRY_DETAILS_QUERY.replace(
                COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<FeesChargedJournalEntryDTO> feesChargedJournalEntries = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            FeesChargedJournalEntryDTO feesChargedJournalEntry = new FeesChargedJournalEntryDTO();
            feesChargedJournalEntry.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            feesChargedJournalEntry.setActivityEventSequence(getLong(result.get(ACTIVITY_EVENT_SEQUENCE_COL)));
            feesChargedJournalEntry.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            feesChargedJournalEntry.setAggregateAccountNumber(getBigInteger(result.get(AGGREGATE_ACCOUNT_NUMBER_COL)));
            feesChargedJournalEntry.setData(getString(result.get(DATA_COL)));
            feesChargedJournalEntry.setDirectionCode(getLong(result.get(DIRECTION_CD_COL)));
            feesChargedJournalEntry.setFeeAmount(getLong(result.get(FEE_AMOUNT_COL)));
            feesChargedJournalEntry.setFeeAmountUsd(getLong(result.get(FEE_AMOUNT_USD_COL)));
            feesChargedJournalEntry.setFeeCurrencyCode(getString(result.get(FEE_CURRENCY_CD_COL)));
            feesChargedJournalEntry.setFeeReasonCode(getString(result.get(FEE_REASON_CD_COL)));
            feesChargedJournalEntry.setIndividualAccountNumber(getBigInteger(result.get(INDIVIDUAL_ACCOUNT_NUM_COL)));
            feesChargedJournalEntry.setIndividualAccountType(getLong(result.get(INDIVIDUAL_ACCOUNT_TYPE_COL)));
            feesChargedJournalEntry.setParticipantTransactionId(getString(result.get(PARTICIPANT_TRANSACTION_ID_COL)));
            feesChargedJournalEntry.setParticipantTypeCode(getString(result.get(PARTICIPANT_TYPE_CD_COL)));
            feesChargedJournalEntry.setjptestLegalEntityCode(getByte(result.get(jptest_LEGAL_ENTITY_CD_COL)));
            feesChargedJournalEntry.setRowCreatedTime(getLong(result.get(ROW_CREATED_TIME_COL)));
            feesChargedJournalEntry.setTimeCreatedUnixSecs(getLong(result.get(TIME_CREATED_UNIX_SECS_COL)));
            feesChargedJournalEntry.setUserGroupCode(getLong(result.get(USER_GROUP_CD_COL)));
            feesChargedJournalEntries.add(feesChargedJournalEntry);
        }

        return feesChargedJournalEntries;
    }

}
