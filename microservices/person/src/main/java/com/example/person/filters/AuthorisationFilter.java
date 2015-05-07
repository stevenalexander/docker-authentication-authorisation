package com.example.person.filters;

import com.example.person.filters.annotations.Secured;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Secured
@Priority(Priorities.AUTHORIZATION)
// in real solution this would be split out into separate library
public class AuthorisationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorisationFilter.class);

    final Client authorisationClient;
    final WebTarget authorisationTarget;

    public AuthorisationFilter(Environment environment,
                               JerseyClientConfiguration httpAuthorisationClientConfiguration,
                               String authorisationApiUri) {
        authorisationClient = new JerseyClientBuilder(environment)
            .using(httpAuthorisationClientConfiguration)
            .build("authorisationClient");

        authorisationTarget = authorisationClient.target(authorisationApiUri);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String permission = getPermissionFromSecuredAnnotation(requestContext);
        String callerId = requestContext.getHeaderString("callerId");

        LOGGER.info("Checking callerId: " + callerId + " has permission: " + permission);

        // in real solution this would be heavily cached
        Response response = authorisationTarget
            .path("/" + permission)
            .request(MediaType.TEXT_PLAIN)
            .header("callerId", callerId)
            .get();

        if (response.getStatus() != 200) {
            requestContext.abortWith(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("You do not have required permission: " + permission)
                .build());
        }
    }

    private String getPermissionFromSecuredAnnotation(ContainerRequestContext requestContext) {
        final ExtendedUriInfo extendedUriInfo = (ExtendedUriInfo) requestContext.getUriInfo();

        return extendedUriInfo
            .getMatchedResourceMethod()
            .getInvocable()
            .getHandlingMethod().getAnnotation(Secured.class)
            .permission();
    }
}
