package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.ClearingJournalEntryDTO;

/**
 * Represents CLEARING_JOURNAL_ENTRY table of ENG database
 */
@Singleton
public class ClearingJournalEntryDao extends EngDao {

    private static final String GET_CLEARING_JOURNAL_ENTRY_DETAILS_QUERY = "SELECT * FROM CLEARING_JOURNAL_ENTRY where activity_id in ({activityIds}) order by TRANS_AMOUNT, TRANS_AMOUNT_CURRENCY_CD, DIRECTION_CD, jptest_LEGAL_ENTITY_CD, USER_COUNTRY_CD, USER_GROUP_CD, PARTICIPANT_TYPE_CD";
    private static final String TRANS_AMOUNT_COL = "TRANS_AMOUNT";
    private static final String TRANS_AMOUNT_CURRENCY_CD_COL = "TRANS_AMOUNT_CURRENCY_CD";
    private static final String USER_ACCOUNT_TYPE_COL = "USER_ACCOUNT_TYPE";
    private static final String USER_COUNTRY_CD_COL = "USER_COUNTRY_CD";

    /**
     * Queries CLEARING_JOURNAL_ENTRY table for input activity-id
     * @param activityId
     * @return
     */
    public List<ClearingJournalEntryDTO> getClearingJournalEntryDetails(BigInteger activityId) {
        String query = GET_CLEARING_JOURNAL_ENTRY_DETAILS_QUERY.replace(COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<ClearingJournalEntryDTO> clearingJournalEntries = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            ClearingJournalEntryDTO clearingJournalEntry = new ClearingJournalEntryDTO();
            clearingJournalEntry.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            clearingJournalEntry.setAggregateAccountNumber(getBigInteger(result.get(AGGREGATE_ACCOUNT_NUM_COL)));
            clearingJournalEntry.setDirectionCode(getLong(result.get(DIRECTION_CD_COL)));
            clearingJournalEntry.setIndividualAccountNumber(getBigInteger(result.get(INDIVIDUAL_ACCOUNT_NUM_COL)));
            clearingJournalEntry.setIndividualAccountType(getLong(result.get(INDIVIDUAL_ACCOUNT_TYPE_COL)));
            clearingJournalEntry.setParticipantTransactionId(getString(result.get(PARTICIPANT_TRANSACTION_ID_COL)));
            clearingJournalEntry.setParticipantTypeCode(getString(result.get(PARTICIPANT_TYPE_CD_COL)));
            clearingJournalEntry.setjptestLegalEntityCode(getByte(result.get(jptest_LEGAL_ENTITY_CD_COL)));
            clearingJournalEntry.setPpCustomerAccountNumber(getBigInteger(result.get(PP_CUSTOMER_ACCT_NUM_COL)));
            clearingJournalEntry.setRowCreatedTime(getLong(result.get(ROW_CREATED_TIME_COL)));
            clearingJournalEntry.setTimeCreatedUnixSecs(getLong(result.get(TIME_CREATED_UNIX_SECS_COL)));
            clearingJournalEntry.setTransAmount(getLong(result.get(TRANS_AMOUNT_COL)));
            clearingJournalEntry.setTransAmountCurrencyCode(getString(result.get(TRANS_AMOUNT_CURRENCY_CD_COL)));
            clearingJournalEntry.setUserAccountType(getString(result.get(USER_ACCOUNT_TYPE_COL)));
            clearingJournalEntry.setUserCountryCode(getString(result.get(USER_COUNTRY_CD_COL)));
            clearingJournalEntry.setUserGroupCode(getLong(result.get(USER_GROUP_CD_COL)));
            clearingJournalEntries.add(clearingJournalEntry);
        }

        return clearingJournalEntries;
    }
}
