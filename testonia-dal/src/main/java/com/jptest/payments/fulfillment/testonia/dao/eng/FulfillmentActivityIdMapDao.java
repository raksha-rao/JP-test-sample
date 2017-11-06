package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityIdMapDTO;

/**
 * Represents FULFILLMENT_ACTIVITY_ID_MAP table in ENG DB
 */
@Singleton
public class FulfillmentActivityIdMapDao extends EngDao {

    private static final String GET_RECORD_BY_ACTIVITY_ID = "SELECT * FROM FULFILLMENT_ACTIVITY_ID_MAP where activity_id in ({activityIds})";

    private static final String ID_COL = "ID";

    /**
     * Returns list of records matching input activityId
     * 
     * @param activityId
     * @return
     */
    public List<FulfillmentActivityIdMapDTO> getRecordsByActivityId(BigInteger activityId) {
        String query = GET_RECORD_BY_ACTIVITY_ID.replace(COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN,
                activityId.toString());
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDBName(activityId), query);
        List<FulfillmentActivityIdMapDTO> fulfillmentActivityIdMapList = new ArrayList<>();
        for (Map<String, Object> result : queryResult) {
            FulfillmentActivityIdMapDTO fulfillmentActivityIdMap = new FulfillmentActivityIdMapDTO();
            fulfillmentActivityIdMap.setActivityId(getBigInteger(result.get(ACTIVITY_ID_COL)));
            fulfillmentActivityIdMap.setId(getString(result.get(ID_COL)));
            fulfillmentActivityIdMapList.add(fulfillmentActivityIdMap);
        }
        return fulfillmentActivityIdMapList;
    }
}
