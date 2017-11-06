package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.name.Named;
import com.jptest.payments.fulfillment.testonia.bridge.util.SecurityContextHelper;
import com.jptest.sv.api.rest.CurrentAccountResource;
import com.jptest.sv.api.rest.resources.CreditRequest;
import com.jptest.sv.api.rest.resources.CurrentAccount;
import com.jptest.sv.api.rest.resources.CurrentAccountClose;
import com.jptest.sv.api.rest.resources.CurrentAccountCreate;
import com.jptest.sv.api.rest.resources.DebitRequest;
import com.jptest.sv.api.rest.resources.DebitResponse;

/**
 * Represents bridge for storedvalueserv API calls
 */
@Singleton
public class StoredValueServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoredValueServBridge.class);

    @Inject
    @Named("currentaccountresource")
    protected CurrentAccountResource currentAccountResource;
    
    private static final List<String> SECURITY_CONTEXT_SCOPES = Arrays.asList("*");

    public CurrentAccount createAccount(final String accountNumber, final CurrentAccountCreate createRequest) {
        LOGGER.info("createAccount accountNumber:{} request: {}", accountNumber, printJsonObject(createRequest));
        final String securityContextString = SecurityContextHelper.getSecurityContext(accountNumber, SECURITY_CONTEXT_SCOPES);
        final CurrentAccount currentAccount = this.currentAccountResource.createAccount(securityContextString, null,
                null,
                createRequest);
        LOGGER.info("createAccount response: {}", printJsonObject(currentAccount));
        return currentAccount;
    }

    public CurrentAccount creditAccount(final String accountNumber, final String accountId,
            final CreditRequest creditRequest) {
        final String securityContextString = SecurityContextHelper.getSecurityContext(accountNumber, SECURITY_CONTEXT_SCOPES);
        LOGGER.info("creditAccount accountNumber:{} request: {}", accountNumber, printJsonObject(creditRequest));
        final CurrentAccount creditedAccount = this.currentAccountResource.creditAccount(securityContextString, null,
                SecurityContextHelper.getRandomRequestId(), accountId, creditRequest);
        LOGGER.info("creditAccount response: {}", printJsonObject(creditedAccount));
        return creditedAccount;
    }

    public CurrentAccount debitAccount(final String accountNumber, final String accountId,
            final DebitRequest debitRequest) {
        final String securityContextString = SecurityContextHelper.getSecurityContext(accountNumber, SECURITY_CONTEXT_SCOPES);
        LOGGER.info("debitRequest accountNumber:{} request: {}", accountNumber, printJsonObject(debitRequest));
        final DebitResponse debitResponse = this.currentAccountResource.debitAccount(securityContextString, null,
                SecurityContextHelper.getRandomRequestId(), accountId, debitRequest);
        LOGGER.info("debitRequest response: {}", printJsonObject(debitResponse));
        return debitResponse.getAccount();
    }

    public void closeAccount(final String accountNumber, final String accountId,
            final CurrentAccountClose closeRequest) {
        LOGGER.info("closeAccount accountNumber:{} request: {}", accountNumber, printJsonObject(closeRequest));
        final String securityContextString = SecurityContextHelper.getSecurityContext(accountNumber, SECURITY_CONTEXT_SCOPES);
        this.currentAccountResource.closeAccount(securityContextString, null, accountId,
                closeRequest);
    }

}
