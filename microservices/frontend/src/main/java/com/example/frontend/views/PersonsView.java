package com.example.frontend.views;

public class PersonsView extends BaseView {

    String persons;

    public String getPersons() {
        return persons;
    }

    public PersonsView(String callerId, String persons) {
        super("/templates/partials/persons.ftl", callerId);
        this.persons = persons;
    }
}
