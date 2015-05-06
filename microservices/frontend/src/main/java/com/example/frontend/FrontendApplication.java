package com.example.frontend;

import com.example.frontend.configuration.FrontendConfiguration;
import com.example.frontend.resources.FrontendResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.util.Map;

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
            public Map<String, Map<String, String>> getViewConfiguration(FrontendConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
    }

    @Override
    public void run(FrontendConfiguration configuration, Environment environment) {
        final FrontendResource frontendResource = new FrontendResource();

        environment.jersey().register(frontendResource);
    }
}
