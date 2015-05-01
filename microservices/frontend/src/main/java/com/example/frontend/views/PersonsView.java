package com.example.frontend.views;

import io.dropwizard.views.View;

public class PersonsView extends View {

    public PersonsView() {
        super("/templates/partials/persons.ftl");
    }
}
