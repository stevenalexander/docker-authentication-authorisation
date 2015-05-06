package com.example.session;

import com.example.session.configuration.SessionConfiguration;
import com.example.session.dao.SessionDao;
import com.example.session.resources.SessionResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.jdbi.DBIFactory;
import org.skife.jdbi.v2.DBI;

public class SessionApplication extends Application<SessionConfiguration> {

    public static void main(String[] args) throws Exception {
        new SessionApplication().run(args);
    }

    @Override
    public String getName() {
        return "session-application";
    }

    @Override
    public void initialize(Bootstrap<SessionConfiguration> bootstrap) {
    }

    @Override
    public void run(SessionConfiguration sessionConfiguration, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment,
                                       sessionConfiguration.getDataSourceFactory(),
                                       sessionConfiguration.getDatabaseName());

        final SessionDao sessionDao = jdbi.onDemand(SessionDao.class);

        // Create table if running H2 embedded DB
        if (sessionConfiguration.getDatabaseName().equalsIgnoreCase("h2")) {
            sessionDao.createTable();
        }

        final SessionResource sessionResource = new SessionResource(sessionDao);

        environment.jersey().register(sessionResource);
    }
}
