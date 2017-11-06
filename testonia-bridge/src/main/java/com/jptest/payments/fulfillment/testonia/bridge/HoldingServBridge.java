package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.inject.name.Named;
import com.jptest.financialinstrument.ActivateTabRequest;
import com.jptest.financialinstrument.ActivateTabResponse;
import com.jptest.financialinstrument.CreateTabRequest;
import com.jptest.financialinstrument.CreateTabResponse;
import com.jptest.financialinstrument.TabLifecycle;
import com.jptest.financialinstrument.TabReturnCodeType;
import com.jptest.financialinstrument.TabType;
import com.jptest.financialinstrument.TabVO;
import com.jptest.types.Currency;

/**
 * Represents bridge for holdingserv API calls
 */
@Singleton
public class HoldingServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(HoldingServBridge.class);

    @Inject
    @Named("tablifecycle")
    private TabLifecycle tabLifecycle;

    /**
     * Creates tab for given account in input currency 
     * 
     * @param accountNumber
     * @param currencyCode
     * @param tabType
     * @return TabVO
     */
    public TabVO createTab(BigInteger accountNumber, String currencyCode, TabType tabType) {
        CreateTabRequest request = new CreateTabRequest();
        request.setAccountNumber(accountNumber);
        request.setCurrencyCode(currencyCode);
        request.setTabType(tabType);
        LOGGER.info("create_tab request: {}", printValueObject(request));
        CreateTabResponse response = tabLifecycle.create_tab(request);
        LOGGER.info("create_tab response: {}", printValueObject(response));
        if (response.getReturnCode().getReturnCodeAsEnum() != TabReturnCodeType.SUCCESS) {
            Assert.fail("createTab call failed with returnCode [" + response.getReturnCode().getReturnCodeAsEnum()
                    + "] and returnCodeDescription [" + response.getReturnCode().getReturnCodeDescription() + "]");
        }
        LOGGER.info("Tab created with ID {} on account {}", response.getTab().getTabId(), accountNumber);
        return response.getTab();
    }

    /**
     * Activates tab for input tabId
     * 
     * @param tabId
     * @param duration
     * @param limit
     * @return TabVO
     */
    public TabVO activateTab(BigInteger tabId, BigInteger duration, Currency limit) {
        ActivateTabRequest request = new ActivateTabRequest();
        request.setTabId(tabId);
        request.setDuration(duration);
        request.setLimit(limit);
        LOGGER.info("activate_tab request: {}", printValueObject(request));
        ActivateTabResponse response = tabLifecycle.activate_tab(request);
        LOGGER.info("activate_tab response: {}", printValueObject(response));
        if (response.getReturnCode().getReturnCodeAsEnum() != TabReturnCodeType.SUCCESS) {
            Assert.fail("activateTab call failed with returnCode [" + response.getReturnCode().getReturnCodeAsEnum()
                    + "] and returnCodeDescription [" + response.getReturnCode().getReturnCodeDescription() + "]");
        }

        return response.getTab();
    }

}
