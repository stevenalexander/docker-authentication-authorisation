package com.example.frontend.resources;

import com.example.api.model.Person;
import com.example.frontend.services.PersonService;
import com.example.frontend.views.*;
import io.dropwizard.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("/")
@Produces({MediaType.TEXT_HTML})
public class FrontendResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendResource.class);

    final PersonService personService;

    public FrontendResource(PersonService personService) {
        this.personService = personService;
    }

    @GET
    public View index(@HeaderParam("callerId") String callerId){
        return new IndexView(callerId);
    }

    @GET
    @Path("/login")
    public View login(@HeaderParam("callerId") String callerId){
        return new LoginView(callerId);
    }

    @GET
    @Path("/persons")
    public View persons(@HeaderParam("callerId") String callerId){
        LOGGER.info("Getting persons list for callerId: " + callerId);

        List<Person> persons = personService.getAll(callerId);
        return new PersonsView(callerId, persons);
    }

    @GET
    @Path("/persons/add")
    public View personsAdd(@HeaderParam("callerId") String callerId){
        return new PersonEditView(callerId, null, true, null);
    }

    @POST
    @Path("/persons/add")
    public View personsAddSubmit(@HeaderParam("callerId") String callerId,
                                 @FormParam("firstName") String firstName, @FormParam("lastName") String lastName) {
        LOGGER.info("Adding person for callerId: " + callerId);

        Person person = new Person(firstName, lastName);
        // in real application validation would occur here, api errors caught and return view with errors
        try {
            personService.post(callerId, person);
        } catch (NotAuthorizedException notAuthorizedException) {
            return new PersonEditView(callerId, person, true, new String[] { "notAuthorised" });
        }

        throw new WebApplicationException(Response.seeOther(UriBuilder.fromUri("/persons").build()).build());
    }

    @GET
    @Path("/persons/{personId}")
    public View getPerson(@HeaderParam("callerId") String callerId, @PathParam("personId") int personId) {
        Person person = personService.get(callerId, personId);

        return new PersonView(callerId, person);
    }

    @GET
    @Path("/persons/{personId}/edit")
    public View personEdit(@HeaderParam("callerId") String callerId, @PathParam("personId") int personId) {
        Person person = personService.get(callerId, personId);

        return new PersonEditView(callerId, person, false, null);
    }

    @POST
    @Path("/persons/{personId}/edit")
    public View personEditSubmit(@HeaderParam("callerId") String callerId, @PathParam("personId") int personId,
                                 @FormParam("firstName") String firstName, @FormParam("lastName") String lastName) {
        LOGGER.info("Updating person for callerId: " + callerId);

        Person person = new Person(firstName, lastName);
        person.setId(personId);
        // in real application validation would occur here, api errors caught and return view with errors
        try {
            personService.put(callerId, person);
        } catch (NotAuthorizedException notAuthorizedException) {
            return new PersonEditView(callerId, person, true, new String[] { "notAuthorised" });
        }

        throw new WebApplicationException(Response.seeOther(UriBuilder.fromUri("/persons/" + personId).build()).build());
    }

    @GET
    @Path("/401")
    public View get401(@HeaderParam("callerId") String callerId) {
        return new ErrorView("/templates/partials/401.ftl", callerId);
    }
}