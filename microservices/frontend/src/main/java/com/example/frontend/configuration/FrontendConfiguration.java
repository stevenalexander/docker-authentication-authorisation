package com.example.frontend.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class FrontendConfiguration extends Configuration {

    @NotNull
    @JsonProperty("viewRendererConfiguration")
    private Map<String, Map<String, String>> viewRendererConfiguration;

    public Map<String, Map<String, String>> getViewRendererConfiguration() {
        return viewRendererConfiguration;
    }
}
