package com.example.frontend.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.TEXT_HTML})
public class FrontendResource {

    @GET
    public String getLogin(){
        throw new WebApplicationException(500);
    }
}