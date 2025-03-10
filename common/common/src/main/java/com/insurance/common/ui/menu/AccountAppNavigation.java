package com.insurance.common.ui.menu;

import org.springframework.stereotype.Component;

import com.insurance.common.ui.AccountUiProperties;

import com.vaadin.flow.component.UI;

@Component("common_accountAppNavigation")
public class AccountAppNavigation {
    private final AccountUiProperties accountUiProperties;

    public AccountAppNavigation(AccountUiProperties accountUiProperties) {
        this.accountUiProperties = accountUiProperties;
    }

    public void navigate() {
        UI.getCurrent().getPage().setLocation(accountUiProperties.getUiBaseUrl() + "/accounts");
    }
}
