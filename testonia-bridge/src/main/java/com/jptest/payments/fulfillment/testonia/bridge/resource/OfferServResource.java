package com.jptest.payments.fulfillment.testonia.bridge.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jptest.interfaces.rs.resources.OfferProgramVO;
import com.jptest.interfaces.rs.resources.OfferVO;


@Path("/v1/offers")
@Produces({ APPLICATION_JSON })
@Consumes({ APPLICATION_JSON })
@JsonIgnoreProperties(ignoreUnknown = true)
public interface OfferServResource {

    String SEC_CTX_HEADER = "X-jptest-SECURITY-CONTEXT";

    @POST
    @Path("/programs")
    Response createOfferProgram(@HeaderParam(SEC_CTX_HEADER) String securityContext, OfferProgramVO offerProgramVO);

    @POST
    @Path("/offers")
    Response createOffer(@HeaderParam(SEC_CTX_HEADER) String securityContext, OfferVO offerVO);

    @GET
    @Path("/offers")
    Response getOfferById(@HeaderParam(SEC_CTX_HEADER) String securityContext, @QueryParam("offer_id") String offerId);
}
