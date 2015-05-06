package com.example.session.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;

@Path("/sessions")
public class SessionResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionResource.class);

    @POST
    public String createSession(String callerId) {
        LOGGER.info("Create session for callerId: " + callerId);
        String accessToken = "1234";
        return accessToken;
    }

    @GET
    @Path("/{accessToken}")
    public String getSession(@PathParam("accessToken") String accessToken){
        LOGGER.info("Validate session for accessToken: " + accessToken);
        String callerId = "1"; // TODO break out accessToken and validate against session data store
        return callerId;
    }
}