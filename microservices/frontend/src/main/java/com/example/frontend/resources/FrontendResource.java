package com.example.frontend.resources;

import com.example.frontend.views.IndexView;
import com.example.frontend.views.LoginView;
import com.example.frontend.views.PersonsView;
import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces({MediaType.TEXT_HTML})
public class FrontendResource {

    @GET
    public View index(){
        return new IndexView();
    }

    @GET
    @Path("/login")
    public View login(){
        return new LoginView();
    }

    @GET
    @Path("/persons")
    public View persons(){
        return new PersonsView();
    }
}