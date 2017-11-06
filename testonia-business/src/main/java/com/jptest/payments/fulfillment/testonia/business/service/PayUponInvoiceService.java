package com.jptest.payments.fulfillment.testonia.business.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.financialproduct.AddAccountOwnerOrEntityResponse;
import com.jptest.financialproduct.CreateAccountResponse;
import com.jptest.payments.fulfillment.testonia.bridge.FPAccountLifecycleBridge;

/**
 * PUI helper class
 * <p>
 * PUI product was launched in Germany for customers who don't wish to pay for an item till they receive the goods.
 * <p>
 * jptest fronts the money for the PUI txn immediately to seller, however buyer is invoiced after 14 days or confirmed delivery.
 * Buyer has X days to pay up the invoice from their bank directly.
 * <p>
 * More details about PUI can be found at https://engineering.jptestcorp.com/confluence/display/CRDTDCS/PUI+Product+Documentation
 */
@Singleton
public class PayUponInvoiceService {

    @Inject
    private FPAccountLifecycleBridge fpAccountLifecycleBridge;

    private static final String COUNTRY_GERMANY = "DE";

    /**
     * 
     * @param accountNumber
     * @return
     */
    public String createAndGetUnencryptedIBAN(String accountNumber) {
        CreateAccountResponse createAccountResponse = fpAccountLifecycleBridge.createPUIAccount(accountNumber);
        AddAccountOwnerOrEntityResponse addAccountOwnerOrEntityResponse = fpAccountLifecycleBridge
                .addAccountOwnerOrEntity(createAccountResponse
                        .getFpAccount().getFpAccountId());

        String userIBAN = null;

        for (int i = 0; i < addAccountOwnerOrEntityResponse.getFpAccount().getEntities().size(); i++) {
            if (addAccountOwnerOrEntityResponse.getFpAccount().getEntities()
                    .get(i).getEntityReference().toString().startsWith(COUNTRY_GERMANY)) {
                userIBAN = addAccountOwnerOrEntityResponse.getFpAccount()
                        .getEntities().get(i).getEntityReference().toString();
            }
        }

        return userIBAN;
    }
}
