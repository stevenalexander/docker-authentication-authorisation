package com.example.session.model;

import java.util.UUID;

public class Session {

    String accessToken;
    String callerId;

    public String getAccessToken() {
        return accessToken;
    }

    public String getCallerId() {
        return callerId;
    }

    public Session(String callerId) {
        this.accessToken = UUID.randomUUID().toString();
        this.callerId = callerId;
    }

    public Session(String accessToken, String callerId) {
        this.accessToken = accessToken;
        this.callerId = callerId;
    }
}
