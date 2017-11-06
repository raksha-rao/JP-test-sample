package com.jptest.payments.fulfillment.testonia.bridge.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Represents a client resource for MsMonitor endpoints.
 */
@Path("/user_details/")
@Produces({ MediaType.APPLICATION_JSON })
public interface MsMonitorResource {

    @POST
    @Path("/get")
    @Produces({ MediaType.TEXT_PLAIN })
    String getInstantUser(@QueryParam("msEnvironment") String msEnvironment, @QueryParam("domain") String domain,
            @QueryParam("key") String key);

    @POST
    @Path("/add")
    @Consumes("application/x-www-form-urlencoded")
    Response addUser(@FormParam("msEnvironment") String msEnvironment, @FormParam("domain") String domain,
            @FormParam("key") String key, @FormParam("value") String value);
}
