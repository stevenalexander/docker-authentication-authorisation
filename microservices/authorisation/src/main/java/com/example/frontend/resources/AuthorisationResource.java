package com.example.frontend.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class AuthorisationResource {

    @GET
    public String hasAuthorisation(){
        throw new WebApplicationException(500);
    }
}