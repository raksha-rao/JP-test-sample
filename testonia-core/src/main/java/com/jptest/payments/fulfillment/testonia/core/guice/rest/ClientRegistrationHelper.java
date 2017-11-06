package com.jptest.payments.fulfillment.testonia.core.guice.rest;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.core.guice.ConfigurationHelper;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * @JP Inc.
 */
@CoverageIgnore
public class ClientRegistrationHelper {
    private static Logger logger = LoggerFactory.getLogger(ClientRegistrationHelper.class);

    private static final String BASE_URL = "baseUrl";
    private static final String CONNECTIONS = "http.connections";
    private static final String DEFAULTCLIENT = "default";

    public static void registerClients() {
        Configuration config = ConfigurationHelper.getTestConfiguration();
        createDefaultClient(config);
        Set<String> clientids = getClientIds(config);
        for (String clientid : clientids) {
            createClient(clientid, config);
        }
    }

    private static Set<String> getClientIds(Configuration config) {
        Set<String> clientids = new HashSet<>();
        Iterator keys = config.getKeys();
        while (keys.hasNext()) {
            String key = keys.next().toString();
            if (key.contains("." + BASE_URL)) {
                String clientid = key.substring(0, key.indexOf("."));
                clientids.add(clientid);
            }
        }
        return clientids;
    }

    private static void createDefaultClient(Configuration config) {
        String key = BASE_URL;
        //this cannot be null
        String value = config.getString(key);
        if (value == null || value.length() == 0) {
            logger.warn("No configuration found for the default client, injection for it will not be available");
            return;
        }
        ClientConfiguration clientconfig = ClientConfiguration.build(value);
        key = CONNECTIONS;
        value = config.getString(key, "");
        if (!value.isEmpty()) {
            clientconfig.setConnections(Integer.parseInt(value));
        }
        RestClientManager.getInstance().registerClient(DEFAULTCLIENT, clientconfig);
    }

    private static void createClient(String clientid, Configuration config) {
        String key = clientid + "." + BASE_URL;
        //this cannot be null
        String value = config.getString(key);
        ClientConfiguration clientconfig = ClientConfiguration.build(value);
        key = clientid + "." + CONNECTIONS;
        value = config.getString(key, "");
        if (!value.isEmpty()) {
            clientconfig.setConnections(Integer.parseInt(value));
        }
        RestClientManager.getInstance().registerClient(clientid, clientconfig);
    }
}
