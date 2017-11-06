package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.name.Named;
import com.jptest.api.platform.wallet.BalanceAccountsSpecification.BalanceAccounts;
import com.jptest.api.platform.wallet.FinancialInstrumentsSpecification.v2.Wallet;
import com.jptest.api.platform.wallet.TabAccountsSpecification.TabAccounts;
import com.jptest.api.platform.wallet.TabAccountsSpecification.TabType;
import com.jptest.payments.fulfillment.testonia.bridge.resource.WalletResource;
import com.jptest.payments.fulfillment.testonia.bridge.util.SecurityContextHelper;

/**
 * Represents bridge for FiManagementServ API calls
 */
@Singleton
public class FiManagementServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(FiManagementServBridge.class);
    private static final List<String> SECURITY_CONTEXT_SCOPES = Arrays.asList("*");

    @Inject
    @Named("walletresource")
    private WalletResource walletResource;

    public Wallet getWallet(String accountNumber) {
        LOGGER.info("getWallet request for accountNumber: {}", accountNumber);
        Wallet wallet = walletResource.getWallet(SecurityContextHelper.getSecurityContext(accountNumber, SECURITY_CONTEXT_SCOPES));
        LOGGER.info("getWallet response: {}", printJsonObject(wallet));
        return wallet;

    }

    public BalanceAccounts getBalanceAccounts(String accountNumber) {
        LOGGER.info("getBalanceAccounts request for accountNumber: {}", accountNumber);
        BalanceAccounts balanceAccounts = walletResource.getActiveBalance(accountNumber);
        LOGGER.info("getBalanceAccounts response: {}", printJsonObject(balanceAccounts));
        return balanceAccounts;
    }

	public String getTabAccountId(String accountNumber) {
		String securityCtx = SecurityContextHelper.getSecurityContext(accountNumber, SECURITY_CONTEXT_SCOPES);
		LOGGER.info("getTabId accountNumber: {}, SecurityContext: {}", accountNumber, securityCtx);

		TabAccounts tabAccounts = walletResource.getTabAccounts(securityCtx, TabType.PAY_AFTER_DELIVERY.name());
		LOGGER.info("getTabId: response: {}", printJsonObject(tabAccounts));

		return tabAccounts.getTabAccounts().get(0).getAuxiliaryData().getWalletInstrumentId();
	}
}
