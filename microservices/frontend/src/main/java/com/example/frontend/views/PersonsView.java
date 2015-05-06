package com.example.frontend.views;

public class PersonsView extends BaseView {

    public PersonsView(String callerId) {
        super("/templates/partials/persons.ftl", callerId);
    }
}
