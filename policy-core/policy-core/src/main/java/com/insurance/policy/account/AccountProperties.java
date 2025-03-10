package com.insurance.policy.account;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("account")
public class AccountProperties {

    private String apiBaseUrl;

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }
}