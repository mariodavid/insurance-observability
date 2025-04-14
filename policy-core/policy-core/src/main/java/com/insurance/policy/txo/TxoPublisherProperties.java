package com.insurance.policy.txo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("txo")
public class TxoPublisherProperties {

    private String apiBaseUrl;
    private Boolean enabled;

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}