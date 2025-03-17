package com.insurance.policy.partner;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("partner")
public class PartnerProperties {

    private String apiBaseUrl;

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }
}