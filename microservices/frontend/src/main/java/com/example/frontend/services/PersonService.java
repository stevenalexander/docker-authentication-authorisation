package com.example.frontend.services;

import com.example.api.model.Person;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class PersonService {

    final Client client;
    final String baseUri;
    final WebTarget personTarget;

    public PersonService(Client client, String baseUri) {
        this.client = client;
        this.baseUri = baseUri;

        this.personTarget = client.target(baseUri);
    }

    public List<Person> getAll(String callerId) {
        return personTarget
            .request(MediaType.APPLICATION_JSON)
            .header("callerId", callerId)
            .get(new GenericType<List<Person>>() {});
    }

    public Person get(String callerId, int personId) {
        return personTarget
            .path("/" + personId)
            .request(MediaType.APPLICATION_JSON)
            .header("callerId", callerId)
            .get(Person.class);
    }

    public Person post(String callerId, Person person) {
        return personTarget
            .request(MediaType.APPLICATION_JSON)
            .header("callerId", callerId)
            .post(Entity.entity(person, MediaType.APPLICATION_JSON), Person.class);
    }

    public Person put(String callerId, Person person) {
        return personTarget
            .path("/" + person.getId())
            .request(MediaType.APPLICATION_JSON)
            .header("callerId", callerId)
            .put(Entity.entity(person, MediaType.APPLICATION_JSON), Person.class);
    }
}
