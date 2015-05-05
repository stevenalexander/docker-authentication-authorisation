package com.example.session.resources;

import javax.ws.rs.*;

@Path("/sessions")
public class SessionResource {

    @GET
    @Path("/{accessToken}")
    public String getSession(@PathParam("accessToken") String accessToken){
        String callerId = "1"; // TODO break out accessToken and validate against session datastore
        return callerId;
    }
}