package com.jptest.payments.fulfillment.testonia.bridge;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpinc.inc.platform.mayfly.MayflyClient;
import com.jpinc.inc.platform.mayfly.MayflyResponse;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Represents bridge for mayfly,  API bridge for read key value from mayfly
 */
@Singleton
public class MayflyBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(MayflyBridge.class);

    @Inject
    @Named("mayflyserv")
    private MayflyClient mayflyClient;
    
    @Inject
    @Named("mayflymoneyserv")
    private MayflyClient mayflyMoneyClient;

    /**
     *      Looks up for key in mayfly
    		* @param key String - key to lookup
    		* @return MayflyResponse
     */
    public MayflyResponse readKey(String key) {
        LOGGER.info("Calling mayFly for lookup key - " + key);
        MayflyResponse response = mayflyClient.read(key);
        LOGGER.info("mayfly lookup status - " + response.getMayflyOperationStatus());
        return response;
    }
    
    public MayflyClient getMayflyClient() {
        return mayflyClient;
    }

    public MayflyClient getMayflyMoneyClient() {
        return mayflyMoneyClient;
    }
}
