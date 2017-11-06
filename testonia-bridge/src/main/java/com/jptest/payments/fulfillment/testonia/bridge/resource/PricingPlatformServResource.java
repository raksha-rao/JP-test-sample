package com.jptest.payments.fulfillment.testonia.bridge.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.jptest.pricing.interfaces.rest.fx.ForeignExchangeContext;


@Produces({ APPLICATION_JSON })
@Consumes({ APPLICATION_JSON })
public interface PricingPlatformServResource {

    String SEC_CTX_HEADER = "X-jptest-SECURITY-CONTEXT";

    @Path("v1/pricing/calculate-foreign-exchange")
    @POST

    Response calculateForeignExchange(ForeignExchangeContext fxCalculation, @HeaderParam(SEC_CTX_HEADER) String securityContext);
}
