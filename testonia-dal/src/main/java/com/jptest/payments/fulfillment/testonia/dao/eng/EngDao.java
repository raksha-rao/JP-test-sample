package com.jptest.payments.fulfillment.testonia.dao.eng;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.dao.BaseDao;

/**
 * This class serves as a parent class for all ENG DB related DAO classes
 * It holds all common items such as common methods, common fields etc for ENG DB related queries.
 */
public abstract class EngDao extends BaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EngDao.class);

    private static final BigInteger engineNodeBits = new BigInteger(
            "FF00000000", 16);
    private static final BigInteger engineConstant = new BigInteger(
            "100000000", 16);

    // common tokens
    protected static final String COMMA_SEPARATED_ACTIVITY_IDS_REPLACEMENT_TOKEN = "{activityIds}";

    // Column names common across tables
    protected static final String ACCOUNT_NUMBER_COL = "ACCOUNT_NUMBER";
    protected static final String ACTIVITY_ID_COL = "ACTIVITY_ID";
    protected static final String AGGREGATE_ACCOUNT_NUM_COL = "AGGREGATE_ACCOUNT_NUM";
    protected static final String AMOUNT_COL = "AMOUNT";
    protected static final String CURRENCY_CD_COL = "CURRENCY_CD";
    protected static final String DIRECTION_CD_COL = "DIRECTION_CD";
    protected static final String INDIVIDUAL_ACCOUNT_NUM_COL = "INDIVIDUAL_ACCOUNT_NUM";
    protected static final String INDIVIDUAL_ACCOUNT_TYPE_COL = "INDIVIDUAL_ACCOUNT_TYPE";
    protected static final String PARTICIPANT_TRANSACTION_ID_COL = "PARTICIPANT_TRANSACTION_ID";
    protected static final String PARTICIPANT_TYPE_CD_COL = "PARTICIPANT_TYPE_CD";
    protected static final String jptest_LEGAL_ENTITY_CD_COL = "jptest_LEGAL_ENTITY_CD";
    protected static final String PP_CUSTOMER_ACCT_NUM_COL = "PP_CUSTOMER_ACCT_NUM";
    protected static final String ROW_CREATED_TIME_COL = "ROW_CREATED_TIME";
    protected static final String STATUS_COL = "STATUS";
    protected static final String TIME_CREATED_UNIX_SECS_COL = "TIME_CREATED_UNIX_SECS";
    protected static final String USER_GROUP_CD_COL = "USER_GROUP_CD";

    @Override
    protected String getDatabaseName() {
        throw new UnsupportedOperationException(
                "This method is not supported for ENG DB. Please use getDBName() method instead");
    }

    protected static String getDBName(BigInteger activityId) {
        int dbId = getDBId(activityId);
        String dbName = null;
        switch (dbId) {
        case 9:
            dbName = "ENG01";
            break;
        case 10:
            dbName = "ENG02";
            break;
        case 11:
            dbName = "ENG03";
            break;
        case 12:
            dbName = "ENG04";
            break;
        case 13:
            dbName = "ENG05";
            break;
        case 14:
            dbName = "ENG06";
            break;
        case 15:
            dbName = "ENG07";
            break;
        case 16:
            dbName = "ENG08";
            break;
        default:
            LOGGER.error("No ENG database found for activityId:{}, dbID:{}", activityId, dbId);
            throw new TestExecutionException("No ENG database found for activityId " + activityId);
        }
        LOGGER.debug("Returning dbName {} for activityId {}", dbName, activityId);
        return dbName;
    }

    private static int getDBId(BigInteger activityId) {
        int dbId = activityId.and(engineNodeBits).divide(engineConstant).intValue();
        if (dbId == 0) {
            Assert.fail("ActivityId passed is not a valid engine activity id");
        }
        return dbId;
    }

}
