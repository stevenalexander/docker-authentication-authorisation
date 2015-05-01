package com.example.session.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class SessionResource {

    @GET
    public String getSession(){
        throw new WebApplicationException(500);
    }
}