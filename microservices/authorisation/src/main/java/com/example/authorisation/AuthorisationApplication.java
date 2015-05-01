package com.example.authorisation;

import com.example.authorisation.resources.AuthorisationResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AuthorisationApplication extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        new AuthorisationApplication().run(args);
    }

    @Override
    public String getName() {
        return "authorisation-application";
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        final AuthorisationResource authorisationResource = new AuthorisationResource();

        environment.jersey().register(authorisationResource);
    }
}
