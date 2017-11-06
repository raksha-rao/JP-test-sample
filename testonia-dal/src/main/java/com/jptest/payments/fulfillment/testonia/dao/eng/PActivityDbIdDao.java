package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Singleton;


/**
 * Represents the PACTIVITY_DBID in Eng DB. 
 * This DAO esp. used to generate the bulk activity from engine DB.
 * Note:This DAO is Testonia version of FulfillmentActivityIdDAO in Fulfillment-Engine
 * 
 * @JP Inc.
 */

@Singleton
public class PActivityDbIdDao extends EngDao {

    private static final String ACTIVITY_ID_COUNT_REPLACEMENT_TOKEN = "{activityIdCount}";
    private static final String GENERATE_ACTIVITY_IDS = "SELECT PD.DBID, (( trunc(sysdate - TO_DATE('19700101000000', 'YYYYMMDDHH24MISS')) * 256 ) + PD.dbid ) *256*256*256*256 + pactivity_id_seq.nextval as activity_id FROM PACTIVITY_DBID PD connect by level <= {activityIdCount}";

    /**
     * Generates bulk activity ids from given engine DB.
     * 
     * @param dbName
     * @param activityIdCount
     * @return generated activity ids
     */
    public List<BigInteger> generateBulkActivityIds(String engDbName, long activityIdCount) {

        String generateActivityIds = GENERATE_ACTIVITY_IDS.replace(ACTIVITY_ID_COUNT_REPLACEMENT_TOKEN,
                String.valueOf(activityIdCount));
        List<Map<String, Object>> results = dbHelper.executeSelectQuery(engDbName, generateActivityIds);
        List<BigInteger> activityIds = results.stream()
                .map(result -> getBigInteger(result.get(ACTIVITY_ID_COL)))
                .collect(Collectors.toList());
        return activityIds;

    }
}
