package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.PayableJournalEntryDTO;

/**
 * Represents PAYABLE_JOURNAL_ENTRY table of ENG database
 */
@Singleton
public class PayableJournalEntryDao extends EngDao {

    private static final String GET_PAYABLE_JOURNAL_ENTRY_DETAILS_QUERY = "SELECT * FROM PAYABLE_JOURNAL_ENTRY where activity_id in ({activityIds}) order by AMOUNT, CURRENCY_CD, DIRECTION_CD, PAYABLE_REASON_CD, jptest_LEGAL_ENTITY_CD";
    private static final String PAYABLE_REASON_CD_COL = "PAYABLE_REASON_CD";

    /**
     * Queries PAYABLE_JOURNAL_ENTRY table for input activity-id
     * @param activityId
     * @return
     */
    public List<PayableJournalEntryDTO> getPayableJournalEntryDetails(BigInteger activityId) {
        String query = GET_PAYABLE_JOURNAL_ENTRY_DETAILS_QUERY.replace(COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<PayableJournalEntryDTO> payableJournalEntries = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            PayableJournalEntryDTO payableJournalEntry = new PayableJournalEntryDTO();
            payableJournalEntry.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            payableJournalEntry.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            payableJournalEntry.setAmount(getLong(result.get(AMOUNT_COL)));
            payableJournalEntry.setCurrencyCode(getString(result.get(CURRENCY_CD_COL)));
            payableJournalEntry.setDirectionCode(getLong(result.get(DIRECTION_CD_COL)));
            payableJournalEntry.setParticipantTransactionId(getString(result.get(PARTICIPANT_TRANSACTION_ID_COL)));
            payableJournalEntry.setPayableReasonCode(getString(result.get(PAYABLE_REASON_CD_COL)));
            payableJournalEntry.setjptestLegalEntityCode(getByte(result.get(jptest_LEGAL_ENTITY_CD_COL)));
            payableJournalEntry.setRowCreatedTime(getLong(result.get(ROW_CREATED_TIME_COL)));
            payableJournalEntry.setTimeCreatedUnixSecs(getLong(result.get(TIME_CREATED_UNIX_SECS_COL)));
            payableJournalEntries.add(payableJournalEntry);
        }

        return payableJournalEntries;
    }

}
