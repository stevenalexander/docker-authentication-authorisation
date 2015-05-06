package com.example.frontend.resources;

import com.example.api.model.Person;
import com.example.frontend.services.PersonService;
import com.example.frontend.views.*;
import io.dropwizard.views.View;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Produces({MediaType.TEXT_HTML})
public class FrontendResource {

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
        List<Person> persons = personService.getAll(callerId);

        return new PersonsView(callerId, persons);
    }

    @GET
    @Path("/persons/add")
    public View personsAdd(@HeaderParam("callerId") String callerId){
        return new PersonEditView(callerId, null, true);
    }

    @GET
    @Path("/persons/{personId}")
    public View getPerson(@HeaderParam("callerId") String callerId, @PathParam("personId") int personId){
        Person person = personService.get(callerId, personId);

        return new PersonView(callerId, person);
    }

    @GET
    @Path("/persons/{personId}/edit")
    public View personsAdd(@HeaderParam("callerId") String callerId, @PathParam("personId") int personId){
        Person person = personService.get(callerId, personId);

        return new PersonEditView(callerId, person, false);
    }
}