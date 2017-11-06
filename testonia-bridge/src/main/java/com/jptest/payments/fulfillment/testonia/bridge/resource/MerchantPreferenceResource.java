package com.jptest.payments.fulfillment.testonia.bridge.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jptest.business.merchantsettingserv.resource.merchantpreferences.CategoryCollection;

/**
 * Represents a client resource for merchantsettingserv endpoints.
 */
@Path("/v1/customer/merchant-preferences")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public interface MerchantPreferenceResource {

    String SEC_CTX_HEADER = "X-jptest-SECURITY-CONTEXT";

    @PUT
    Response addMerchantPreferences(@HeaderParam(SEC_CTX_HEADER) String securityContext,
            CategoryCollection categoryCollection);
}
