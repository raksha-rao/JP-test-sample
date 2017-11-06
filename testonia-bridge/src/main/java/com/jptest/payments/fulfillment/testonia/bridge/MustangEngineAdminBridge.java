package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import java.math.BigInteger;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.inject.Inject;
import com.jptest.money.GetMustangActivityLogsRequest;
import com.jptest.money.GetMustangActivityLogsResponse;
import com.jptest.money.MustangEngineAdmin;

/**
 * Represents bridge for mustang engine admin API calls
 */
@Singleton
public class MustangEngineAdminBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(MustangEngineAdminBridge.class);

    private static final BigInteger engineNodeBits = new BigInteger(
            "FF00000000", 16);
    private static final BigInteger engineConstant = new BigInteger(
            "100000000", 16);

    @Inject
    @Named("moneyengineadminserv1")
    private MustangEngineAdmin moneyengineadminserv1;

    @Inject
    @Named("moneyengineadminserv2")
    private MustangEngineAdmin moneyengineadminserv2;

    @Inject
    @Named("moneyengineadminserv3")
    private MustangEngineAdmin moneyengineadminserv3;

    @Inject
    @Named("moneyengineadminserv4")
    private MustangEngineAdmin moneyengineadminserv4;

    @Inject
    @Named("moneyengineadminserv6")
    private MustangEngineAdmin moneyengineadminserv6;

    @Inject
    @Named("moneyasyncengineadminserv1")
    private MustangEngineAdmin moneyasyncengineadminserv1;

    public GetMustangActivityLogsResponse getMustangActivityLogs(BigInteger activityId) {
    	LOGGER.info("MustangEngineAdminBridge.getMustangActivityLogs: activityId = {}", activityId);
        GetMustangActivityLogsRequest request = new GetMustangActivityLogsRequest();
        request.setActivityId(activityId);
        LOGGER.info("get_mustang_activity_logs request: {}", printValueObject(request));
        GetMustangActivityLogsResponse response = getEngineFromActivityId(activityId)
                .get_mustang_activity_logs(request);
        LOGGER.info("get_mustang_activity_logs response: {}", printValueObject(response));
        return response;
    }

    private MustangEngineAdmin getEngineFromActivityId(BigInteger activityId) {
        int dbId = getDBId(activityId);
        return getEngineFromDBId(dbId);
    }

    private static int getDBId(BigInteger activityId) {
        int dbId = activityId.and(engineNodeBits).divide(engineConstant).intValue();
        if (dbId == 0) {
            Assert.fail("ActivityId passed is not a valid engine activity id");
        }
        return dbId;
    }

    private MustangEngineAdmin getEngineFromDBId(int dbId) {
        MustangEngineAdmin client = null;
        switch (dbId) {
        case 9:
            client = moneyengineadminserv1;
            break;
        case 10:
            client = moneyengineadminserv2;
            break;
        case 11:
            client = moneyengineadminserv3;
            break;
        case 12:
            client = moneyengineadminserv4;
            break;
        case 13:
            client = moneyengineadminserv1;
            break;
        case 14:
            client = moneyengineadminserv6;
            break;
        case 15:
            client = moneyasyncengineadminserv1;
            break;
        case 16:
            client = moneyasyncengineadminserv1;
            break;
        default:
            throw new IllegalStateException("dbId " + dbId + " is not supported to query MustangEngineAdmin.");
        }

        return client;
    }
}
