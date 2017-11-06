package com.jptest.payments.fulfillment.testonia.dao.txn;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Represents event_os_alternate_id table of TXN database
 */
@Singleton
public class EventOsAlternateIdDao extends TxnDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventOsAlternateIdDao.class);

    private static final String GET_ACTIVITY_IDS_FROM_TRANSACTION_IDS = "select value as alternate_id, activity_id, db_ts_created as row_created_time "
            + "from event_os_alternate_id where issuer_type != 'ActivityId' and handle_type='FinancialTransactionVO' "
            + "and value in ({alternateId})";

    private static final String GET_ACTIVITY_ID_FROM_TRANSACTION_ID = "select activity_id "
            + "from event_os_alternate_id where issuer_type != 'ActivityId' and handle_type='FinancialTransactionVO' "
            + "and value = '{alternateId}'";
    private static final String ALTERNATE_ID_REPLACEMENT_TOKEN = "{alternateId}";

    /**
     * Returns activityIds for input transactions
     *
     * @param wTransactionList
     * @return
     */
    public Set<BigInteger> getActivityIdFromTransactionIds(String commaSeparatedIds) {
        Set<BigInteger> activitySet = new HashSet<>();
        String query = GET_ACTIVITY_IDS_FROM_TRANSACTION_IDS.replace(ALTERNATE_ID_REPLACEMENT_TOKEN, commaSeparatedIds);
        List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
        for (Map<String, Object> result : queryResult) {
            String activityId = (String) result.get(ACTIVITY_ID_COL);
            activitySet.add(new BigInteger(activityId));
        }
        LOGGER.debug("Found activitySet:{}", activitySet);
        return activitySet;
    }

    public BigInteger getActivityIdFromTransactionId(String transactionId) {
        String query = GET_ACTIVITY_ID_FROM_TRANSACTION_ID.replace(ALTERNATE_ID_REPLACEMENT_TOKEN, transactionId);
        Map<String, Object> queryResult = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
        if (queryResult != null) {
            String activityId = (String) queryResult.get(ACTIVITY_ID_COL);
            if (activityId != null) {
                LOGGER.info("ACTIVITY ID : {}, TRANSACTION ID : {} from DB : {} ", activityId, transactionId);
                return new BigInteger(activityId);
            }
        }

        return null;
    }
}
