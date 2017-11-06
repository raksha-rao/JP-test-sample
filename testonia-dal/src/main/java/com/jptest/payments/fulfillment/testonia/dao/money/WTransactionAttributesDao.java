package com.jptest.payments.fulfillment.testonia.dao.money;

import java.math.BigInteger;
import java.util.Map;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO for wtransaction_attributes table of MONEY DB
 */
@Singleton
public class WTransactionAttributesDao extends MoneyDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(WTransactionAttributesDao.class);

    private static final String GET_TXN_ATTRIBUTES_COUNT = "select count(*) as cnt from wtransaction_attributes where txn_activity_id = '{activityId}' and attribute_name = '{attributeName}'";
    private static final String ACTIVITY_ID_REPLACEMENT_TOKEN = "{activityId}";
    private static final String ATTRIBUTE_NAME_REPLACEMENT_TOKEN = "{attributeName}";

    /**
     * Returns count of Txn Attributes for input activityId and attributeName
     *  
     * @param accountNumber
     * @return
     */
    public int getTransAttributesCount(BigInteger activityId, String attributeName) {
        String query = GET_TXN_ATTRIBUTES_COUNT.replace(ACTIVITY_ID_REPLACEMENT_TOKEN, activityId.toString())
                .replace(ATTRIBUTE_NAME_REPLACEMENT_TOKEN, attributeName);
        Map<String, Object> queryResult = dbHelper.executeSelectQueryForSingleResult(getDatabaseName(), query);
        int rowCount = getInteger(queryResult.get(COUNT_COL));
        LOGGER.info("Found {} wtransaction_attributes entries for activityId:{} and attributeName:{}", rowCount,
                activityId, attributeName);
        return rowCount;
    }
}
