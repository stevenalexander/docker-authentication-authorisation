package com.example.session;

import com.example.session.resources.SessionResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class SessionApplication extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        new SessionApplication().run(args);
    }

    @Override
    public String getName() {
        return "session-application";
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        final SessionResource sessionResource = new SessionResource();

        environment.jersey().register(sessionResource);
    }
}
