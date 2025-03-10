package com.insurance.common.ui;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("common.account")
public class AccountUiProperties {

    private final String uiBaseUrl;

    public AccountUiProperties(String uiBaseUrl) {
        this.uiBaseUrl = uiBaseUrl;
    }

    public String getUiBaseUrl() {
        return uiBaseUrl;
    }
}
