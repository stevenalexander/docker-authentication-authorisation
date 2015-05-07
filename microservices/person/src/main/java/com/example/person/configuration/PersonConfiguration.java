package com.example.person.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PersonConfiguration extends Configuration {

    @NotNull
    @JsonProperty("databaseName")
    private String databaseName;

    public String getDatabaseName() {
        return databaseName;
    }

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
    
    @Valid
    @NotNull
    @JsonProperty("httpAuthorisationClient")
    private JerseyClientConfiguration httpAuthorisationClient = new JerseyClientConfiguration();

    public JerseyClientConfiguration getHttpAuthorisationClient() {
        return httpAuthorisationClient;
    }

    @NotNull
    @JsonProperty("authorisationApiUri")
    private String authorisationApiUri;

    public String getAuthorisationApiUri() {
        return authorisationApiUri;
    }
}
