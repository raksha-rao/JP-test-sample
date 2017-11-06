package com.jptest.payments.fulfillment.testonia.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.Configuration;

import com.jptest.cipro.InternalClientSocketFactory;
import com.jptest.cipro.RawConnection;
import com.jptest.infra.protectedpkg.KeymakerProtectedProperties;
import com.jptest.infra.protectedpkg.KeymakerProtectedPropertiesProvider;
import com.jptest.payments.fulfillment.testonia.bridge.BridgeConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.riskadminserv.RiskAdministrationStub;
import com.jptest.riskadminserv.RiskControlAccountFilterSettingsCIPRO;
import com.jptest.riskadminserv.RiskControlAccountInfoCIPRO;
import com.jptest.riskadminserv.RiskControlExceptionStatusCIPRO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  This represents a resource to get client for riskadminserv. It initializes the CIPRO
 *  stub for this service and provides all the relevant APIs exposed by riskadminserv. It also
 *  hides the setup and destroy of connection CIPRO framework uses. The methods on this class are 
 *  intentionally kept same as the ones provided by the actual service (even though they are against java conventions) so that
 *  this client can be used in the same other REST and ASF clients are used.
 */
@Singleton
public class RiskAdminServClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RiskAdminServClient.class);

    private String keyStoreLocation;
    private String keyStorePassphraseKey;
    private String riskAdminServBaseUrl;

    @Inject
    private Configuration config;

    @Inject
    public void initializeClientProperties() {
        keyStoreLocation = config.getString(BridgeConfigKeys.PROTECTED_PACKAGE_LOCATION.getName());
        keyStorePassphraseKey = config.getString(BridgeConfigKeys.PROTECTED_KEYSTORE_PASSPHRASE_KEY.getName());
        riskAdminServBaseUrl = config.getString(BridgeConfigKeys.RISKADMINSERV_BASE_URL.getName());
    }

    private RiskAdministrationStub getRiskAdminStub() throws Exception {
        KeymakerProtectedProperties protectedPkgProperties = KeymakerProtectedPropertiesProvider.getInstance()
                .getProtectedProperties();
        RiskAdministrationStub riskAdministrationStub = null;
        if (protectedPkgProperties == null) {
            throw new TestExecutionException(
                    "Could not load Protected Package properties to connect to CIPRO client for riskadminserv");
        } else {
            try {
                Random seedGen = new Random();
                String tokenPass = protectedPkgProperties.getProperty(keyStorePassphraseKey);
                riskAdministrationStub = new RiskAdministrationStub(
                        new RawConnection(new InternalClientSocketFactory(seedGen.nextLong(),
                                keyStoreLocation, tokenPass.toCharArray())));

            } catch (Exception e) {
                throw new TestExecutionException("Error creating stub for riskadminserv", e);
            }
        }
        return riskAdministrationStub;
    }

    public void risk_control_enroll(RiskControlAccountInfoCIPRO accountInfo,
            RiskControlExceptionStatusCIPRO exceptionStatus) {
        RiskAdministrationStub riskStub = null;
        try {
            riskStub = getInitializedStub();
            riskStub.risk_control_enroll(accountInfo, exceptionStatus);
        } catch (Exception e) {
            throw new TestExecutionException("Error while executing risk_control_enroll", e);
        } finally {
            disconnectStub(riskStub);
        }
    }

    public void risk_control_update_filter_setting_for_account(
            RiskControlAccountFilterSettingsCIPRO accountFilterSettings,
            RiskControlExceptionStatusCIPRO riskControlExceptionStatus) {
        RiskAdministrationStub riskStub = null;
        try {
            riskStub = getInitializedStub();
            riskStub.risk_control_update_filter_setting_for_account(accountFilterSettings, riskControlExceptionStatus);
        } catch (Exception e) {
            throw new TestExecutionException("Error while executing risk_control_update_filter_setting_for_account", e);
        } finally {
            disconnectStub(riskStub);
        }
    }

    private RiskAdministrationStub getInitializedStub() throws MalformedURLException, Exception, IOException {
        RiskAdministrationStub riskStub = getRiskAdminStub();
        URL url = new URL(riskAdminServBaseUrl);
        riskStub.connect(url.getHost(), url.getPort());
        return riskStub;
    }

    private void disconnectStub(RiskAdministrationStub riskStub) {
        if (riskStub != null) {
            try {
                riskStub.disconnect();
            } catch (IOException e) {
                LOGGER.debug("Error while disconnecting riskadminserv connection");
            }
        } else {
            LOGGER.debug("Trying to disconnect a non-existent riskadminserv connection");
        }
    }

}
