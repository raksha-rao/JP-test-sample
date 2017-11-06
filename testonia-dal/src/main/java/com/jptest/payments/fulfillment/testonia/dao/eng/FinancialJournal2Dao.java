package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.FinancialJournal2DTO;

/**
 * Represents FINANCIAL_JOURNAL_2 table of ENG database
 */
@Singleton
public class FinancialJournal2Dao extends EngDao {

    private static final String GET_FINANCIAL_JOURNAL_2_DETAILS_QUERY = "SELECT * FROM FINANCIAL_JOURNAL_2 where activity_id in ({activityIds}) order by debit_type, credit_type, amount, currency_code";
    private static final String CREDIT_ACTIVITY_EVENT_ID_COL = "CREDIT_ACTIVITY_EVENT_ID";
    private static final String CREDIT_LEGAL_ENTITY_COL = "CREDIT_LEGAL_ENTITY";
    private static final String CREDIT_PT_ID_COL = "CREDIT_PT_ID";
    private static final String CREDIT_TYPE_COL = "CREDIT_TYPE";
    private static final String CREDIT_USER_COUNTRY_COL = "CREDIT_USER_COUNTRY";
    private static final String CREDIT_USER_PP_ACCOUNT_NUMBER_COL = "CREDIT_USER_PP_ACCOUNT_NUMBER";
    private static final String CURRENCY_CODE_COL = "CURRENCY_CODE";
    private static final String DB_ID_COL = "DB_ID";
    private static final String DEBIT_ACTIVITY_EVENT_ID_COL = "DEBIT_ACTIVITY_EVENT_ID";
    private static final String DEBIT_LEGAL_ENTITY_COL = "DEBIT_LEGAL_ENTITY";
    private static final String DEBIT_PT_ID_COL = "DEBIT_PT_ID";
    private static final String DEBIT_TYPE_COL = "DEBIT_TYPE";
    private static final String DEBIT_USER_COUNTRY_COL = "DEBIT_USER_COUNTRY";
    private static final String DEBIT_USER_PP_ACCOUNT_NUMBER_COL = "DEBIT_USER_PP_ACCOUNT_NUMBER";
    private static final String JOURNAL_AID_COL = "JOURNAL_AID";
    private static final String JOURNAL_ENTRY_TYPE_COL = "JOURNAL_ENTRY_TYPE";
    private static final String VOID_JOURNAL_ID_COL = "VOID_JOURNAL_ID";

    /**
     * Queries FINANCIAL_JOURNAL_2 table for input activity-id
     * @param activityId
     * @return
     */
    public List<FinancialJournal2DTO> getFinancialJournal2Details(BigInteger activityId) {
        String query = GET_FINANCIAL_JOURNAL_2_DETAILS_QUERY.replace(COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<FinancialJournal2DTO> financialJournal2Entries = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            FinancialJournal2DTO financialJournal2Entry = new FinancialJournal2DTO();
            financialJournal2Entry.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            financialJournal2Entry.setAmount(getLong(result.get(AMOUNT_COL)));
            financialJournal2Entry.setCreditActivityEventId(getString(result.get(CREDIT_ACTIVITY_EVENT_ID_COL)));
            financialJournal2Entry.setCreditLegalEntity(getByte(result.get(CREDIT_LEGAL_ENTITY_COL)));
            financialJournal2Entry.setCreditPtId(getString(result.get(CREDIT_PT_ID_COL)));
            financialJournal2Entry.setCreditType(getString(result.get(CREDIT_TYPE_COL)));
            financialJournal2Entry.setCreditUserCountry(getString(result.get(CREDIT_USER_COUNTRY_COL)));
            financialJournal2Entry
                    .setCreditUserPpAccountNumber(getBigInteger(result.get(CREDIT_USER_PP_ACCOUNT_NUMBER_COL)));
            financialJournal2Entry.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
            financialJournal2Entry.setDbId(getBigInteger(result.get(DB_ID_COL)));
            financialJournal2Entry.setDebitActivityEventId(getString(result.get(DEBIT_ACTIVITY_EVENT_ID_COL)));
            financialJournal2Entry.setDebitLegalEntity(getByte(result.get(DEBIT_LEGAL_ENTITY_COL)));
            financialJournal2Entry.setDebitPtId(getString(result.get(DEBIT_PT_ID_COL)));
            financialJournal2Entry.setDebitType(getString(result.get(DEBIT_TYPE_COL)));
            financialJournal2Entry.setDebitUserCountry(getString(result.get(DEBIT_USER_COUNTRY_COL)));
            financialJournal2Entry
                    .setDebitUserPpAccountNumber(getBigInteger(result.get(DEBIT_USER_PP_ACCOUNT_NUMBER_COL)));
            financialJournal2Entry.setJournalAid(getBigInteger(result.get(JOURNAL_AID_COL)));
            financialJournal2Entry.setJournalEntryType(getInteger(result.get(JOURNAL_ENTRY_TYPE_COL)));
            financialJournal2Entry.setVoidJournalId(getBigInteger(result.get(VOID_JOURNAL_ID_COL)));
            financialJournal2Entries.add(financialJournal2Entry);
        }

        return financialJournal2Entries;
    }
}
