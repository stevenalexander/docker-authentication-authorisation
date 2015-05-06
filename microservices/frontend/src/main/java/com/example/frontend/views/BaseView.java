package com.example.frontend.views;

import io.dropwizard.views.View;

public abstract class BaseView extends View {

    final String callerId;

    public String getCallerId() {
        return callerId;
    }

    public BaseView(String template, String callerId) {
        super(template);
        this.callerId = callerId;
    }
}
