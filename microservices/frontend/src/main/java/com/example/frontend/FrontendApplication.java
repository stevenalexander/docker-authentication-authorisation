package com.example.frontend;

import com.example.frontend.configuration.FrontendConfiguration;
import com.example.frontend.resources.FrontendResource;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

public class FrontendApplication extends Application<FrontendConfiguration> {
    public static void main(String[] args) throws Exception {
        new FrontendApplication().run(args);
    }

    @Override
    public String getName() {
        return "frontend-application";
    }

    @Override
    public void initialize(Bootstrap<FrontendConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<FrontendConfiguration>() {
            @Override
            public ImmutableMap<String, ImmutableMap<String, String>> getViewConfiguration(FrontendConfiguration config) {
                return config.getViewRendererConfiguration();
            }
        });
    }

    @Override
    public void run(FrontendConfiguration configuration, Environment environment) {
        final FrontendResource frontendResource = new FrontendResource();

        environment.jersey().register(frontendResource);
    }
}
