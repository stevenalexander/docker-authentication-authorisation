package com.example.frontend;

import com.example.frontend.configuration.FrontendConfiguration;
import com.example.frontend.resources.FrontendResource;
import com.example.frontend.services.PersonService;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import javax.ws.rs.client.Client;
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
        final Client personClient = new JerseyClientBuilder(environment)
            .using(configuration.getHttpPersonClient())
            .build("personClient");

        final PersonService personService = new PersonService(personClient, configuration.getPersonApiUri());

        final FrontendResource frontendResource = new FrontendResource(personService);

        environment.jersey().register(frontendResource);
        environment.jersey().register(new FrontendExceptionMapper());
    }
}
