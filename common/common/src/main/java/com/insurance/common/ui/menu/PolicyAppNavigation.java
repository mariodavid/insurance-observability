package com.insurance.common.ui.menu;

import org.springframework.stereotype.Component;

import com.insurance.common.ui.PolicyUiProperties;

import com.vaadin.flow.component.UI;

@Component("common_policyAppNavigation")
public class PolicyAppNavigation {
    private final PolicyUiProperties policyUiProperties;

    public PolicyAppNavigation(PolicyUiProperties policyUiProperties) {
        this.policyUiProperties = policyUiProperties;
    }

    public void navigate() {
        UI.getCurrent().getPage().setLocation(policyUiProperties.getUiBaseUrl() + "/policies");
    }
}
