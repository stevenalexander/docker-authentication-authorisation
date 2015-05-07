package com.example.person;

import com.example.person.configuration.PersonConfiguration;
import com.example.person.dao.PersonDao;
import com.example.person.filters.AuthorisationFilter;
import com.example.person.resources.PersonResource;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.client.Client;

public class PersonApplication extends Application<PersonConfiguration> {
    public static void main(String[] args) throws Exception {
        new PersonApplication().run(args);
    }

    @Override
    public String getName() {
        return "person-application";
    }

    @Override
    public void initialize(Bootstrap<PersonConfiguration> bootstrap) {
    }

    @Override
    public void run(PersonConfiguration configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment,
            configuration.getDataSourceFactory(),
            configuration.getDatabaseName());

        final PersonDao personDao = jdbi.onDemand(PersonDao.class);

        // Create table if running H2 embedded DB
        if (configuration.getDatabaseName().equalsIgnoreCase("h2")) {
            personDao.createTable();
        }

        final PersonResource PersonResource = new PersonResource(personDao);

        environment.jersey().register(PersonResource);
        environment.jersey().register(new AuthorisationFilter(environment, configuration.getHttpAuthorisationClient(), configuration.getAuthorisationApiUri()));
    }
}
