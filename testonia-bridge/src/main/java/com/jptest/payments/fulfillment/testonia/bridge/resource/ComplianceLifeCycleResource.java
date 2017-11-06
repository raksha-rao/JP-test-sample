package com.jptest.payments.fulfillment.testonia.bridge.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.jptest.api.platform.compliance.levels.ComplianceLevel;


@Path("/v1/compliance/")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public interface ComplianceLifeCycleResource {

    @PUT
    @Path("/levels/")
    Response updateLevelsByCustomerID(
            @NotNull @QueryParam("customer_identifier") String customerIdentifier,
            @NotNull @QueryParam("customer_type") String customerType,
            List<ComplianceLevel> request);
}
