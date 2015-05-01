package com.example.authentication.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class AuthenticationResource {

    @POST
    public String authenticateUser(){
        throw new WebApplicationException(500);
    }
}