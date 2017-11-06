package com.jptest.payments.fulfillment.testonia.bridge;

public enum BridgeConfigKeys {

    PROTECTED_PACKAGE_LOCATION("protected_package_location"),
    PROTECTED_KEYSTORE_PASSPHRASE_KEY("protected_keystore_passphrase_key"),
    RISKADMINSERV_BASE_URL("riskadminserv.baseUrl"),
    MSMONITOR_ADD_URL("msmonitor.add.url"),
    MSMONITOR_GET_URL("msmonitor.get.url"),
	MSMONITOR_FAULTINJ_ADD_URL("msmonitor.faultInjection.add.url");

    private String name;

    private BridgeConfigKeys(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
