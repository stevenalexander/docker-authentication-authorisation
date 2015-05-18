package com.example.session;

import com.example.session.configuration.SessionConfiguration;
import com.example.session.dao.SessionDao;
import com.example.session.resources.SessionResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.jdbi.DBIFactory;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionApplication extends Application<SessionConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionApplication.class);

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

        try {
            sessionDao.createTable();
        } catch (Exception ex) {
            LOGGER.info("Session table already exists");
        }

        final SessionResource sessionResource = new SessionResource(sessionDao);

        environment.jersey().register(sessionResource);
    }
}
