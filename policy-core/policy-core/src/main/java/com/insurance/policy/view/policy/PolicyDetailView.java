package com.insurance.policy.view.policy;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.insurance.policy.account.AccountBalanceResponse;
import com.insurance.policy.account.AccountClient;
import com.insurance.policy.entity.Policy;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.jmix.core.TimeSource;
import io.jmix.flowui.component.datepicker.TypedDatePicker;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "policies/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "policy_Policy.detail")
@ViewDescriptor(path = "policy-detail-view.xml")
@EditedEntityContainer("policyDc")
public class PolicyDetailView extends StandardDetailView<Policy> {
    @Autowired
    private AccountClient accountClient;
    @ViewComponent
    private TextField accountBalanceResult;
    @ViewComponent
    private TypedDatePicker<LocalDate> accountBalanceEffectiveDatePicker;
    @Autowired
    private TimeSource timeSource;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        accountBalanceEffectiveDatePicker.setValue(timeSource.now().toLocalDate());
    }


    @Subscribe("accountBalanceEffectiveDatePicker")
    public void onAccountBalanceEffectiveDatePickerComponentValueChange(final AbstractField.ComponentValueChangeEvent<TypedDatePicker<LocalDate>, LocalDate> event) {

        AccountBalanceResponse accountBalance = accountClient.getAccountBalance(getEditedEntity().getPolicyNo(), event.getValue());

        accountBalanceResult.setValue(accountBalance.balance().toString());

    }

}