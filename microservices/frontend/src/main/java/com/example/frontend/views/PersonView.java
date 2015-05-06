package com.example.frontend.views;

import com.example.api.model.Person;

public class PersonView extends BaseView {

    Person person;

    public Person getPerson() {
        return person;
    }

    public PersonView(String callerId, Person person) {
        super("/templates/partials/person.ftl", callerId);
        this.person = person;
    }
}
