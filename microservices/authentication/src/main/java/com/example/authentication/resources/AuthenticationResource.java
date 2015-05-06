package com.example.authentication.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;

@Path("/authentication")
public class AuthenticationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationResource.class);

    @POST
    public String authenticate(String body){
    //public String authenticate(@FormParam("emailaddress") String emailAddress, @FormParam("password") String password){
        LOGGER.info("Received authentication attempt for email: ");
        String callerId = "1"; // TODO authenticate via form data
        return callerId;
    }
}