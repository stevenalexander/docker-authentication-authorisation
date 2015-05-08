package com.example.frontend;

import com.example.frontend.views.ErrorView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FrontendExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(Throwable exception) {
        if (exception.getClass().getSimpleName().equals("NotFoundException")) {
            return Response.status(404).entity(new ErrorView("/templates/partials/404.ftl", request != null ? request.getHeader("callerId") : null)).build();
        } else {
            return Response.status(500).entity(new ErrorView("/templates/partials/500.ftl", request != null ? request.getHeader("callerId") : null)).build();
        }
    }
}
