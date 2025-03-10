package com.insurance.common.ui.menu;

import org.springframework.stereotype.Component;

import com.insurance.common.ui.QuoteUiProperties;

import com.vaadin.flow.component.UI;

@Component("common_quoteAppNavigation")
public class QuoteAppNavigation {
    private final QuoteUiProperties quoteUiProperties;

    public QuoteAppNavigation(QuoteUiProperties quoteUiProperties) {
        this.quoteUiProperties = quoteUiProperties;
    }

    public void navigate() {
        UI.getCurrent().getPage().setLocation(quoteUiProperties.getUiBaseUrl() + "/quotes");
    }
}
