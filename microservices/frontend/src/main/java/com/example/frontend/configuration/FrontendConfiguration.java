package com.example.frontend.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class FrontendConfiguration extends Configuration {

    @NotNull
    @JsonProperty("viewRendererConfiguration")
    private Map<String, Map<String, String>> viewRendererConfiguration;

    public Map<String, Map<String, String>> getViewRendererConfiguration() {
        return viewRendererConfiguration;
    }

    @Valid
    @NotNull
    @JsonProperty("httpPersonClient")
    private JerseyClientConfiguration httpPersonClient = new JerseyClientConfiguration();

    public JerseyClientConfiguration getHttpPersonClient() {
        return httpPersonClient;
    }

    @NotNull
    @JsonProperty("personApiUri")
    private String personApiUri;

    public String getPersonApiUri() {
        return personApiUri;
    }
}
