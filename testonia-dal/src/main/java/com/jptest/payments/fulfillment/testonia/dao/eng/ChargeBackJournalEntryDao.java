package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.ChargeBackJournalEntryDTO;

/**
 * Represents CHARGEBACK_JOURNAL_ENTRY table of ENG database
 */
@Singleton
public class ChargeBackJournalEntryDao extends EngDao {

    private static final String GET_CHARGEBACK_JOURNAL_ENTRY_DETAILS_QUERY = "SELECT * FROM CHARGEBACK_JOURNAL_ENTRY where activity_id = {activityIds}";
    private static final String CHARGEBACK_FACTORS_COL = "CHARGEBACK_FACTORS";
    private static final String PAYABLE_REASON_CD_COL = "PAYABLE_REASON_CD";
    private static final String VENDOR_SETTLEMENT_ID_COL = "VENDOR_SETTLEMENT_ID";

    /**
     * Queries CHARGEBACK_JOURNAL_ENTRY table for input activity-id
     * @param activityId
     * @return List<ChargeBackJournalEntryDTO> 
     */
    public List<ChargeBackJournalEntryDTO> getChargeBackJournalEntryDetails(BigInteger activityId) {
        String query = GET_CHARGEBACK_JOURNAL_ENTRY_DETAILS_QUERY.replace(
                COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<ChargeBackJournalEntryDTO> chargeBackJournalEntries = new ArrayList<ChargeBackJournalEntryDTO>();
        for (Map<String, Object> result : queryResult) {
            ChargeBackJournalEntryDTO chargeBackJournalEntry = new ChargeBackJournalEntryDTO();
            chargeBackJournalEntry.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            chargeBackJournalEntry.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            chargeBackJournalEntry.setAmount(getLong(result.get(AMOUNT_COL)));
            chargeBackJournalEntry.setChargebackFactors(getString(result.get(CHARGEBACK_FACTORS_COL)));
            chargeBackJournalEntry.setCurrencyCd(getString(result.get(CURRENCY_CD_COL)));
            chargeBackJournalEntry.setDirectionCd(getString(result.get(DIRECTION_CD_COL)));
            chargeBackJournalEntry.setParticipantTransactionId(getString(result.get(PARTICIPANT_TRANSACTION_ID_COL)));
            chargeBackJournalEntry.setPayableReasonCd(getString(result.get(PAYABLE_REASON_CD_COL)));
            chargeBackJournalEntry.setjptestLegalEntityCd(getString(result.get(jptest_LEGAL_ENTITY_CD_COL)));
            chargeBackJournalEntry.setRowCreatedTime(getLong(result.get(ROW_CREATED_TIME_COL)));
            chargeBackJournalEntry.setTimeCreatedUnixSecs(getLong(result.get(TIME_CREATED_UNIX_SECS_COL)));
            chargeBackJournalEntry.setVendorSettlementId(getBigInteger(result.get(VENDOR_SETTLEMENT_ID_COL)));
            chargeBackJournalEntries.add(chargeBackJournalEntry);
        }

        return chargeBackJournalEntries;
    }
}
