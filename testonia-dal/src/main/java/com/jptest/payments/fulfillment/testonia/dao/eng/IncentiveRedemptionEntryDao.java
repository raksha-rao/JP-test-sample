package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.IncentiveRedemptionEntryDTO;

/**
 * Represents INCENTIVE_REDEMPTION_ENTRY table of ENG database
 */
@Singleton
public class IncentiveRedemptionEntryDao extends EngDao {

    private static final String GET_INCENTIVE_REDEMPTION_ENTRY_DETAILS_QUERY = "SELECT * FROM INCENTIVE_REDEMPTION_ENTRY where activity_id = {activityIds}";
    private static final String TIME_CREATED_COL = "TIME_CREATED";
    private static final String INCENTIVE_TYPE_COL = "INCENTIVE_TYPE";

    /**
     * Queries INCENTIVE_REDEMPTION_ENTRY table for input activity-id
     * @param activityId
     * @return List<IncentiveRedemptionEntryDTO>
     */
    public List<IncentiveRedemptionEntryDTO> getIncentiveRedemptionEntryDetails(BigInteger activityId) {
        String query = GET_INCENTIVE_REDEMPTION_ENTRY_DETAILS_QUERY.replace(
                COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<IncentiveRedemptionEntryDTO> incentiveRedemptionEntries = new ArrayList<IncentiveRedemptionEntryDTO>();
        for (Map<String, Object> result : queryResult) {
            IncentiveRedemptionEntryDTO incentiveRedemptionEntry = new IncentiveRedemptionEntryDTO();
            incentiveRedemptionEntry.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            incentiveRedemptionEntry.setAmount(getLong(result.get(AMOUNT_COL)));
            incentiveRedemptionEntry.setCurrencyCode(getString(result.get(CURRENCY_CD_COL)));
            incentiveRedemptionEntry.setIncentiveType(getString(result.get(INCENTIVE_TYPE_COL)));
            incentiveRedemptionEntry.setParticipantTransactionId(getString(result.get(PARTICIPANT_TRANSACTION_ID_COL)));
            incentiveRedemptionEntry.setTimeCreated(getLong(result.get(TIME_CREATED_COL)));
            incentiveRedemptionEntries.add(incentiveRedemptionEntry);
        }
        return incentiveRedemptionEntries;
    }
}
