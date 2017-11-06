package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.ReceivableJournalEntryDTO;

/**
 * Represents RECEIVABLE_JOURNAL_ENTRY table of ENG database
 */
@Singleton
public class ReceivableJournalEntryDao extends EngDao {

    private static final String GET_RECEIVABLE_JOURNAL_ENTRY_DETAILS_QUERY = "SELECT * FROM RECEIVABLE_JOURNAL_ENTRY where activity_id in ({activityIds}) order by AMOUNT, CURRENCY_CD, DIRECTION_CD, jptest_LEGAL_ENTITY_CD, COUNTRY_CD, PARTICIPANT_TYPE_CD, PAYMENT_FUNDING_METHOD, PAYOFF_REASON";
    private static final String PAYMENT_FUNDING_METHOD_COL = "PAYMENT_FUNDING_METHOD";
    private static final String PAYOFF_REASON_COL = "PAYOFF_REASON";
    private static final String RECLASSIFIED_ACTIVITY_ID_COL = "RECLASSIFIED_ACTIVITY_ID";

    /**
     * Queries RECEIVABLE_JOURNAL_ENTRY table for input activity-id
     * @param activityId
     * @return
     */
    public List<ReceivableJournalEntryDTO> getReceivableJournalEntryDetails(BigInteger activityId) {
        String query = GET_RECEIVABLE_JOURNAL_ENTRY_DETAILS_QUERY.replace(
                COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<ReceivableJournalEntryDTO> receivableJournalEntries = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            ReceivableJournalEntryDTO receivableJournalEntry = new ReceivableJournalEntryDTO();
            receivableJournalEntry.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            receivableJournalEntry.setAmount(getLong(result.get(AMOUNT_COL)));
            receivableJournalEntry.setCurrencyCode(getString(result.get(CURRENCY_CD_COL)));
            receivableJournalEntry.setDirectionCode(getLong(result.get(DIRECTION_CD_COL)));
            receivableJournalEntry.setParticipantTransactionId(getString(result.get(PARTICIPANT_TRANSACTION_ID_COL)));
            receivableJournalEntry.setParticipantTypeCode(getString(result.get(PARTICIPANT_TYPE_CD_COL)));
            receivableJournalEntry.setPaymentFundingMethod(getString(result.get(PAYMENT_FUNDING_METHOD_COL)));
            receivableJournalEntry.setPayoffReason(getString(result.get(PAYOFF_REASON_COL)));
            receivableJournalEntry.setjptestLegalEntityCode(getByte(result.get(jptest_LEGAL_ENTITY_CD_COL)));
            receivableJournalEntry.setReclassifiedActivityId(getBigInteger(result.get(RECLASSIFIED_ACTIVITY_ID_COL)));
            receivableJournalEntry.setRowCreatedTime(getLong(result.get(ROW_CREATED_TIME_COL)));
            receivableJournalEntry.setTimeCreatedUnixSecs(getLong(result.get(TIME_CREATED_UNIX_SECS_COL)));
            receivableJournalEntries.add(receivableJournalEntry);
        }

        return receivableJournalEntries;
    }

}
