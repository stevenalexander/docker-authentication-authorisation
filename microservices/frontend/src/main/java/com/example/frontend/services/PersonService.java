package com.example.frontend.services;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class PersonService {

    final Client client;
    final String baseUri;
    final WebTarget personTarget;

    public PersonService(Client client, String baseUri) {
        this.client = client;
        this.baseUri = baseUri;

        this.personTarget = client.target(baseUri);
    }

    public String getAll(String callerId) {
        return personTarget
            .request(MediaType.APPLICATION_JSON)
            .header("callerId", callerId)
            .get(String.class);
    }
}
