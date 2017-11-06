package com.jptest.payments.fulfillment.testonia.business.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.jptest.api.platform.wallet.BalanceAccountsSpecification.BalanceAccount;
import com.jptest.api.platform.wallet.BalanceAccountsSpecification.SubBalanceType;
import com.jptest.payments.fulfillment.testonia.bridge.StoredValueServBridge;
import com.jptest.sv.api.rest.resources.CurrentAccount;
import com.jptest.sv.api.rest.resources.CurrentAccountCreate;
import com.jptest.sv.api.rest.resources.DebitRequest;
import com.jptest.sv.api.rest.resources.LegacyBalanceFields;
import com.jptest.sv.api.rest.resources.LegalEntity;
import com.jptest.sv.api.rest.resources.Money;
import com.jptest.sv.api.rest.resources.SubBalance;

@Singleton
public class StoredValueServHelper {

    @Inject
    private FiManagementHelper fiHelper;

    @Inject
    private StoredValueServBridge svBridge;

    /**
     * This method gets the appropriate holding account for the given currency and account number; If it is not 
     * present, then will create it and then debit the given amount from that account.
     * @param accountNumber
     * @param currency
     * @param unsignedAmount
     */
    public void debitMoneyFromBalance(String accountNumber, String currency, String unsignedAmount) {
        BalanceAccount balanceAccount = fiHelper.getBalanceAccountForCurrencyAndAccount(accountNumber,
                currency);
        String accountId = null;
        if (balanceAccount == null) {
            accountId = getIdForNewBalanceAccount(accountNumber, currency);
        } else {
            accountId = balanceAccount.getStoredValueId();
        }
        DebitRequest debitRequest = getDebitRequestForNegativeBalance(currency, unsignedAmount);
        svBridge.debitAccount(accountNumber, accountId, debitRequest);
    }

    private String getIdForNewBalanceAccount(String accountNumber, String currency) {
        String accountId;
        CurrentAccountCreate createReq = new CurrentAccountCreate();
        createReq.setCurrencyCode(currency);
        CurrentAccount account = svBridge.createAccount(accountNumber, createReq);
        accountId = account.getId();
        return accountId;
    }

    private DebitRequest getDebitRequestForNegativeBalance(String currency, String amount) {
        SubBalance subBalance = getSubBalance(currency, amount);
        DebitRequest debitRequest = new DebitRequest();
        debitRequest.setDebits(Lists.newArrayList(subBalance));
        // These 2 fields are used only for auditing and accounting purposes
        // we set it to dummy values since this debit is not associated with any real transaction.
        debitRequest.setExternalReference("12344");
        debitRequest.setTransactionReference("1331431");
        LegacyBalanceFields legacyBalanceFields = getLegacyBalanceFields();
        debitRequest.setLegacyBalanceFields(legacyBalanceFields);
        return debitRequest;
    }

    private LegacyBalanceFields getLegacyBalanceFields() {
        LegacyBalanceFields legacyBalanceFields = new LegacyBalanceFields();
        legacyBalanceFields.setLegalEntity(LegalEntity.INC);
        legacyBalanceFields.setLogNegativeBalance(false);
        legacyBalanceFields.setAllowNegativeBalance(true);
        return legacyBalanceFields;
    }

    private SubBalance getSubBalance(String currency, String amount) {
        SubBalance subBalance = new SubBalance();
        subBalance.setSubBalanceType(SubBalanceType.AVAILABLE.name());
        Money balanceAmount = new Money();
        balanceAmount.setCurrencyCode(currency);
        balanceAmount.setValue(amount);
        subBalance.setAmount(balanceAmount);
        return subBalance;
    }

}
