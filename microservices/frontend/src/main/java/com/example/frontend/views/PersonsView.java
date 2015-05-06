package com.example.frontend.views;

import com.example.api.model.Person;

import java.util.List;

public class PersonsView extends BaseView {

    List<Person> persons;

    public List<Person> getPersons() {
        return persons;
    }

    public PersonsView(String callerId, List<Person> persons) {
        super("/templates/partials/persons.ftl", callerId);
        this.persons = persons;
    }
}
