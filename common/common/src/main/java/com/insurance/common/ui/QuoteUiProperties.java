package com.insurance.common.ui;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("common.quote")
public class QuoteUiProperties {

    private final String uiBaseUrl;

    public QuoteUiProperties(String uiBaseUrl) {
        this.uiBaseUrl = uiBaseUrl;
    }

    public String getUiBaseUrl() {
        return uiBaseUrl;
    }
}
