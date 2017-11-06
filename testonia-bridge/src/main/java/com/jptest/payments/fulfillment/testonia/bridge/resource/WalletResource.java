package com.jptest.payments.fulfillment.testonia.bridge.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.jptest.api.platform.wallet.BalanceAccountsSpecification.BalanceAccounts;
import com.jptest.api.platform.wallet.FinancialInstrumentsSpecification.v2.Wallet;
import com.jptest.api.platform.wallet.TabAccountsSpecification.TabAccounts;

@Path("/")
@Produces({ APPLICATION_JSON })
@Consumes({ APPLICATION_JSON })
public interface WalletResource {

    String SEC_CTX_HEADER = "X-jptest-SECURITY-CONTEXT";
    String QUERY_PARAM_TYPE = "X-jptest-SECURITY-CONTEXT";

    @Path("/v2/wallet/wallets")
    @GET
    Wallet getWallet(@HeaderParam(SEC_CTX_HEADER) String securityContext);

    @Path("/v1/wallet/balance-accounts")
    @GET
    @Produces({ APPLICATION_JSON })
    @Consumes({ APPLICATION_JSON })
    BalanceAccounts getActiveBalance(@QueryParam("user") String accountNumber);

    @Path("/v1/wallet/tab-accounts")
    @GET
    TabAccounts getTabAccounts(@HeaderParam(SEC_CTX_HEADER) String securityContext, @QueryParam("type") String type);
}
