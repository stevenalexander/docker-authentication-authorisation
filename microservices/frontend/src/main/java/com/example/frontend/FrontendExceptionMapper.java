package com.example.frontend;

import com.example.frontend.views.ErrorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FrontendExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendExceptionMapper.class);

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(Throwable exception) throws WebApplicationException {

        if (exception.getClass().getSimpleName().equals("WebApplicationException")) {
            return ((WebApplicationException)exception).getResponse();
        }

        LOGGER.error("Caught exception for callerId: " + request != null ? request.getHeader("callerId") : "N/A", exception);

        if (exception.getClass().getSimpleName().equals("NotFoundException")) {
            return Response.status(404).entity(new ErrorView("/templates/partials/404.ftl", request != null ? request.getHeader("callerId") : null)).build();
        } else {
            return Response.status(500).entity(new ErrorView("/templates/partials/500.ftl", request != null ? request.getHeader("callerId") : null)).build();
        }
    }
}
