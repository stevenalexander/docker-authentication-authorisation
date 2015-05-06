package com.example.authentication.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;

@Path("/authentication")
public class AuthenticationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationResource.class);

    @POST
    public String authenticate(@FormParam("emailaddress") String emailAddress, @FormParam("password") String password){

        LOGGER.info("Received authentication attempt for email: " + emailAddress);

        if (       emailAddress.equals("admin@test.com") && password.equals("admin")) {
            return "1";
        } else if (emailAddress.equals("user@test.com")  && password.equals("user")) {
            return "2";
        }

        throw new WebApplicationException("Invalid user", 401);
    }
}