package com.jptest.payments.fulfillment.testonia.bridge.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.GZIP;

import com.jptest.qi.rest.domain.pojo.User;

/**
 *  Wrapper over restjaws UserService to provide
 *  correlation id as a http header.
 */
@Path("user")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface UserServiceResource {

    @GZIP
    @POST
    Response createUser(@HeaderParam("corr_id") String corrId, @HeaderParam("hostName") String hostName,
            User user);

    @GET
    @Path("/{accountNumber}")
    Response getUser(@HeaderParam("corr_id") String corrId, @HeaderParam("hostName") String hostName,
            @PathParam("accountNumber") String accountNumber,
            @QueryParam("FullProfile") boolean fullProfile);

    @GET
    @Path("/findByEmail")
    Response getUserByEmailAddress(@HeaderParam("corr_id") String corrId, @HeaderParam("hostName") String hostName,
            @QueryParam("emailAddress") String emailAddress);

}
