package com.example.frontend;

import com.example.frontend.views.ErrorView;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class FrontendExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        if (exception.getClass().getSimpleName().equals("NotFoundException")) {
            return Response.status(404).entity(new ErrorView("/templates/partials/404.ftl")).build();
        } else {
            return Response.status(500).entity(new ErrorView("/templates/partials/500.ftl")).build();
        }
    }
}
