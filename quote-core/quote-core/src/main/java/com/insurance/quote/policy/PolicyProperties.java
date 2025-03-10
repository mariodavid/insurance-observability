package com.insurance.quote.policy;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("policy")
public class PolicyProperties {

    private final String apiBaseUrl;

    public PolicyProperties(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }
}
