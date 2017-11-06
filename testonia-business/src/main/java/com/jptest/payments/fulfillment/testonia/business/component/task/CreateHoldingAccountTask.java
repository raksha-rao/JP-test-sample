package com.jptest.payments.fulfillment.testonia.business.component.task;

import com.google.inject.Inject;
import com.jptest.api.platform.wallet.BalanceAccountsSpecification.SubBalanceType;
import com.jptest.payments.fulfillment.testonia.bridge.StoredValueServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.sv.api.rest.resources.CreditRequest;
import com.jptest.sv.api.rest.resources.CurrentAccount;
import com.jptest.sv.api.rest.resources.CurrentAccountCreate;
import com.jptest.sv.api.rest.resources.LegacyBalanceFields;
import com.jptest.sv.api.rest.resources.LegalEntity;
import com.jptest.sv.api.rest.resources.Money;
import com.jptest.sv.api.rest.resources.SubBalance;
import org.apache.commons.lang3.StringUtils;
import java.util.Collections;


/**
 * Create a holding account for seller with the specified currency code.
 */
public class CreateHoldingAccountTask extends BaseTask<Void> {

    @Inject
    private StoredValueServBridge svsBridge;
    
    private String currencyCode;
    private String userContextKey;
    private String sumInDollars;
    private static final String accountType = "STANDARD_BALANCE_ACCOUNT";

    /**
     * @param currencyCode currency code for holding. The same currency code is used for addFunds.
     * @param userContextKey userContextKey for seller or buyer.
     *
     */
    public CreateHoldingAccountTask(String currencyCode, String userContextKey) {
        this.currencyCode = currencyCode;
        this.userContextKey = userContextKey;
    }

    /**
     *
     * @param currencyCode currency code for holding. The same currency code is used for addFunds.
     * @param userContextKey userContextKey for seller or buyer.
     * @param sumInDollars this amount will be used for addFunds. If null or empty string is passed the funds will be not added.
     *                     "0" is incorrect value here.
     */
    public CreateHoldingAccountTask(String currencyCode, String userContextKey, String sumInDollars) {
        this.currencyCode = currencyCode;
        this.userContextKey = userContextKey;
        this.sumInDollars = sumInDollars;
    }
    
    @Override
    public Void process(Context context) {

        String accountNumber = getAccountNumber(userContextKey, context);

        CurrentAccount account = svsBridge.createAccount(accountNumber,
                    createHoldingAccountRequest());
        if(StringUtils.isNotBlank(sumInDollars)) {
            svsBridge.creditAccount(accountNumber, account.getId(), createRequest(currencyCode, sumInDollars));
        }
		
		return null;
	}
	
    private CreditRequest createRequest(String currencyCode, String sumInDollars) {
        CreditRequest request = new CreditRequest();
        request.setCredits(Collections.singletonList(getSubBalance(currencyCode, sumInDollars)));
        // These 2 fields are used only for auditing and accounting purposes
        // we set it to dummy values since this debit is not associated with any real transaction.
        request.setExternalReference("12344");
        request.setTransactionReference("1331431");

        LegacyBalanceFields legacyBalanceFields = getLegacyBalanceFields();
        request.setLegacyBalanceFields(legacyBalanceFields);

        return request;
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
	/**
     * @param userContextKey
     * @param context
     * @return
     */
    private String getAccountNumber(String userContextKey, Context context) {
        User user = (User) this.getDataFromContext(context, userContextKey);
        if (user == null)
            throw new TestExecutionException("Couldn't find user object in context");
        return user.getAccountNumber();
    }
    
    private CurrentAccountCreate createHoldingAccountRequest(){
    	
    	final CurrentAccountCreate holdingAccountCreateRequest = new CurrentAccountCreate();
    	holdingAccountCreateRequest.setCurrencyCode(currencyCode);
    	holdingAccountCreateRequest.setAccountType(accountType);
    	
    	return holdingAccountCreateRequest;
    }

    
}
