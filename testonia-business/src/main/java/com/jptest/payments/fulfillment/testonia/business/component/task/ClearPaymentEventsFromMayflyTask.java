package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.bridge.MayflyBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;

/**
 * @JP Inc.
 *
 */
public class ClearPaymentEventsFromMayflyTask extends BaseTask<Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClearPaymentEventsFromMayflyTask.class);

    private Map<String, Set<String>> mayflyKeyMap;
    private String dataRetrivalKey;
    @Inject
    private MayflyBridge bridge;

    public ClearPaymentEventsFromMayflyTask(String dataRetrivalKey) {
        this.dataRetrivalKey = dataRetrivalKey;    
    }
    private static final String MAYFLY_NAMESPACE_MONEY = "Money";
    private static final String MAYFLY_NAMESPACE_MONEY_ID_MAP = "Money_IdMap";
    @Override
    public Integer process(Context context) {
        int status = 0;
        mayflyKeyMap = (Map<String, Set<String>>) this.getDataFromContext(context, dataRetrivalKey);
        for (String mayflyTaskKey : mayflyKeyMap.get(MAYFLY_NAMESPACE_MONEY)) {
            LOGGER.info("Mayfly Task Key in : " + MAYFLY_NAMESPACE_MONEY +" namespace " + mayflyTaskKey);
            clearDataFromMayfly(mayflyTaskKey, MAYFLY_NAMESPACE_MONEY);
        }
        for (String mayflyTaskKey : mayflyKeyMap.get(MAYFLY_NAMESPACE_MONEY_ID_MAP)) {
            LOGGER.info("Mayfly Task Key in : " + MAYFLY_NAMESPACE_MONEY_ID_MAP +" namespace " + mayflyTaskKey);
            clearDataFromMayfly(mayflyTaskKey, MAYFLY_NAMESPACE_MONEY_ID_MAP);
        }
        return status;
    }

    private void clearDataFromMayfly(String key, String nameSpace) {
        LOGGER.info("Removing data from Mayfly. Key:" + key);
        if(MAYFLY_NAMESPACE_MONEY.equals(nameSpace))
            bridge.getMayflyMoneyClient().destroy(key);
        else
            bridge.getMayflyClient().destroy(key);
    }
}
