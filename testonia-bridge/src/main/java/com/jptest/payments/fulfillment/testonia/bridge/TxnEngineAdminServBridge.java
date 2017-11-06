package com.jptest.payments.fulfillment.testonia.bridge;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jptest.money.GetMustangActivityLogsRequest;
import com.jptest.money.GetMustangActivityLogsResponse;
import com.jptest.money.MustangEngineAdmin;
import com.jptest.payments.te.admin.api.ReadFulfillmentActivityLogResponse;
import com.jptest.payments.te.admin.api.TxnEngineAdminServResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import java.math.BigInteger;

/**
 * Represents bridge for txnengineadminserv REST APIs
 */

@Singleton
public class TxnEngineAdminServBridge {
    private static final Logger LOGGER = LoggerFactory.getLogger(TxnEngineAdminServBridge.class);
    @Inject
    @Named("txnengineadminserv_rest")
    private TxnEngineAdminServResource restTxnEngineAdminServ;
    
    @Inject
	@Named("txnengineadminserv")
	private MustangEngineAdmin asfTxnEngineAdminServ;

    public ReadFulfillmentActivityLogResponse fetchActivityLogs(final BigInteger engineActivityId) {
        LOGGER.info("TxnEngineAdminServBridge.fetchActivityLogs: activityId = {}", engineActivityId);
        ReadFulfillmentActivityLogResponse response = restTxnEngineAdminServ.readFulfillmentActivityLog(
                engineActivityId, 1);
        if (response != null && response.getActivityLogs() != null) {
            LOGGER.info("TxnEngineAdminServBridge.fetchActivityLogs: No. of activity logs fetched {}",
                    response.getActivityLogs().size());
        } else {
            LOGGER.info("TxnEngineAdminServBridge.fetchActivityLogs: NO activity log found!!!");
        }
        return response;
    }
    
    /*
     * Perform ASF call to TeAS to fetch activity logs
     */
    public GetMustangActivityLogsResponse getMustangActivityLogsByASF(BigInteger activityId) {
    	LOGGER.info("TxnEngineAdminServBridge.getMustangActivityLogsByASF: activityId = {}", activityId);
    	GetMustangActivityLogsRequest request = new GetMustangActivityLogsRequest();
    	request.setActivityId(activityId);
    	LOGGER.info("get_mustang_activity_logs teas asf request: {}", printValueObject(request));
    	GetMustangActivityLogsResponse response =  asfTxnEngineAdminServ.get_mustang_activity_logs(request);
    	LOGGER.info("get_mustang_activity_logs teas asf response: {}", printValueObject(response));
    	return response;
    }
}
