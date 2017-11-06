package com.jptest.payments.fulfillment.testonia.business.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.collections.CollectionUtils;

import com.jptest.api.platform.wallet.BalanceAccountsSpecification.BalanceAccount;
import com.jptest.api.platform.wallet.BalanceAccountsSpecification.BalanceAccounts;
import com.jptest.payments.fulfillment.testonia.bridge.FiManagementServBridge;

@Singleton
public class FiManagementHelper {

    @Inject
    private FiManagementServBridge fiBridge;

    /**
     * This method gets the Balance account for the given currency and the account number.
     * @param accountNumber
     * @param currencyCode
     * @return
     */
    public BalanceAccount getBalanceAccountForCurrencyAndAccount(String accountNumber, String currencyCode) {
        BalanceAccounts balanceAccounts = fiBridge.getBalanceAccounts(accountNumber);
        if (balanceAccounts == null)
            return null;
        List<BalanceAccount> balanceAccountList = balanceAccounts.getBalanceAccounts();
        BalanceAccount balanceAccount = null;
        if (CollectionUtils.isNotEmpty(balanceAccountList)) {
            for (BalanceAccount account : balanceAccountList) {
                if (account.getCurrencyCode().equalsIgnoreCase(currencyCode))
                    balanceAccount = account;
            }
        }
        return balanceAccount;
    }

    public String getHoldingIdForCurrencyAndAccount(String accountNumber, String currencyCode) {
        BalanceAccount balanceAccount = this.getBalanceAccountForCurrencyAndAccount(accountNumber, currencyCode);
        if (balanceAccount == null)
            return null;
        else
            return balanceAccount.getStoredValueId();
    }

}
