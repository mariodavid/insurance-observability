package com.insurance.account.view.account;

import com.insurance.account.entity.Account;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "accounts/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "account_Account.detail")
@ViewDescriptor(path = "account-detail-view.xml")
@EditedEntityContainer("accountDc")
public class AccountDetailView extends StandardDetailView<Account> {

}