package com.example.frontend;

import com.example.frontend.resources.FrontendResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FrontendApplication extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        new FrontendApplication().run(args);
    }

    @Override
    public String getName() {
        return "frontend-application";
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        final FrontendResource frontendResource = new FrontendResource();

        environment.jersey().register(frontendResource);
    }
}
