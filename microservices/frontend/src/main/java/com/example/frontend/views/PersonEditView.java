package com.example.frontend.views;

import com.example.api.model.Person;

public class PersonEditView extends BaseView {

    Person person;
    boolean isNew;

    public Person getPerson() {
        return person;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public PersonEditView(String callerId, Person person, boolean isNew) {
        super("/templates/partials/personEdit.ftl", callerId);
        this.person = person;
        this.isNew = isNew;
    }
}
