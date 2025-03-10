package com.insurance.common.ui;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("common.policy")
public class PolicyUiProperties {

    private final String apiBaseUrl;
    private final String uiBaseUrl;

    public PolicyUiProperties(String apiBaseUrl, String uiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        this.uiBaseUrl = uiBaseUrl;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public String getUiBaseUrl() {
        return uiBaseUrl;
    }
}
