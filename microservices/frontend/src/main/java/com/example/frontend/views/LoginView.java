package com.example.frontend.views;

import io.dropwizard.views.View;

public class LoginView extends View {

    public LoginView() {
        super("/templates/partials/login.ftl");
    }
}
