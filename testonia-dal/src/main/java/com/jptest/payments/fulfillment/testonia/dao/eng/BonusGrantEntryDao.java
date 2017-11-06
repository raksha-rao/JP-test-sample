package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.BonusGrantEntryDTO;

/**
 * Represents BONUS_GRANT_ENTRY table of ENG database
 */
@Singleton
public class BonusGrantEntryDao extends EngDao {

    private static final String GET_BONUS_GRANT_ENTRY_DETAILS_QUERY = "SELECT * FROM BONUS_GRANT_ENTRY where activity_id in ({activityIds}) order by AMOUNT, AMOUNT_CURRENCY_CD, DIRECTION_CD, jptest_LEGAL_ENTITY_CD, USER_COUNTRY_CD, USER_GROUP_CD, PARTICIPANT_TYPE_CD, BONUS_REASON_CD";
    private static final String AMOUNT_CURRENCY_CD_COL = "AMOUNT_CURRENCY_CD";
    private static final String BONUS_REASON_CD_COL = "BONUS_REASON_CD";

    /**
     * Queries BONUS_GRANT_ENTRY table for input activity-id
     * @param activityId
     * @return
     */
    public List<BonusGrantEntryDTO> getBonusGrantEntryDetails(BigInteger activityId) {
        String query = GET_BONUS_GRANT_ENTRY_DETAILS_QUERY.replace(COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<BonusGrantEntryDTO> bonusGrantEntries = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            BonusGrantEntryDTO bonusGrantEntry = new BonusGrantEntryDTO();
            bonusGrantEntry.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            bonusGrantEntry.setAggregateAccountNumber(getBigInteger(result.get(AGGREGATE_ACCOUNT_NUM_COL)));
            bonusGrantEntry.setAmount(getLong(result.get(AMOUNT_COL)));
            bonusGrantEntry.setAmountCurrencyCode(getString(result.get(AMOUNT_CURRENCY_CD_COL)));
            bonusGrantEntry.setBonusReasonCode(getByte(result.get(BONUS_REASON_CD_COL)));
            bonusGrantEntry.setDirectionCode(getLong(result.get(DIRECTION_CD_COL)));
            bonusGrantEntry.setIndividualAccountNumber(getBigInteger(result.get(INDIVIDUAL_ACCOUNT_NUM_COL)));
            bonusGrantEntry.setIndividualAccountType(getLong(result.get(INDIVIDUAL_ACCOUNT_TYPE_COL)));
            bonusGrantEntry.setParticipantTransactionId(getString(result.get(PARTICIPANT_TRANSACTION_ID_COL)));
            bonusGrantEntry.setParticipantTypeCode(getString(result.get(PARTICIPANT_TYPE_CD_COL)));
            bonusGrantEntry.setjptestLegalEntityCode(getByte(result.get(jptest_LEGAL_ENTITY_CD_COL)));
            bonusGrantEntry.setPpCustomerAccountNumber(getBigInteger(result.get(PP_CUSTOMER_ACCT_NUM_COL)));
            bonusGrantEntry.setRowCreatedTime(getLong(result.get(ROW_CREATED_TIME_COL)));
            bonusGrantEntry.setTimeCreatedUnixSecs(getLong(result.get(TIME_CREATED_UNIX_SECS_COL)));
            bonusGrantEntry.setUserGroupCode(getLong(result.get(USER_GROUP_CD_COL)));
            bonusGrantEntries.add(bonusGrantEntry);
        }

        return bonusGrantEntries;
    }

}
