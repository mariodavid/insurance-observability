package com.insurance.policy.view.policy;

import java.time.LocalDate;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import com.insurance.policy.account.AccountBalanceResponse;
import com.insurance.policy.account.AccountClient;
import com.insurance.policy.entity.Policy;

import com.insurance.policy.partner.PartnerClient;
import com.insurance.policy.partner.PartnerDto;
import com.insurance.policy.partner.PartnerResponse;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.jmix.core.Metadata;
import io.jmix.core.TimeSource;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.datepicker.TypedDatePicker;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;

@Route(value = "policies/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "policy_Policy.detail")
@ViewDescriptor(path = "policy-detail-view.xml")
@EditedEntityContainer("policyDc")
public class PolicyDetailView extends StandardDetailView<Policy> {
    private static final Logger log = LoggerFactory.getLogger(PolicyDetailView.class);
    @Autowired
    private AccountClient accountClient;
    @ViewComponent
    private TextField accountBalanceResult;
    @ViewComponent
    private TypedDatePicker<LocalDate> accountBalanceEffectiveDatePicker;
    @Autowired
    private TimeSource timeSource;
    @Autowired
    private PartnerClient partnerClient;
    @Autowired
    private Metadata metadata;
    @ViewComponent
    private InstanceContainer<PartnerDto> partnerDc;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private MessageBundle messageBundle;


    @Subscribe
    public void onReady(final ReadyEvent event) {
        accountBalanceEffectiveDatePicker.setValue(timeSource.now().toLocalDate());

        PartnerResponse partnerResponse = partnerClient.getPartner(getEditedEntity().getPartnerNo());

        if (partnerResponse != null) {
            PartnerDto partnerDto = metadata.create(PartnerDto.class);
            partnerDto.setId(UUID.fromString(partnerResponse.partnerId()));
            partnerDto.setPartnerNo(partnerResponse.partnerNo());
            partnerDto.setFirstName(partnerResponse.firstName());
            partnerDto.setLastName(partnerResponse.lastName());

            partnerDc.setItem(partnerDto);
        }
        else {
            notifications.create(messageBundle.getMessage("noPartner"))
                    .withType(Notifications.Type.WARNING)
                    .withPosition(Notification.Position.TOP_END)
                    .show();
        }

    }


    @Subscribe("accountBalanceEffectiveDatePicker")
    public void onAccountBalanceEffectiveDatePickerComponentValueChange(final AbstractField.ComponentValueChangeEvent<TypedDatePicker<LocalDate>, LocalDate> event) {

        try {
            AccountBalanceResponse accountBalance = accountClient.getAccountBalance(getEditedEntity().getPolicyNo(), event.getValue());

            if (accountBalance != null) {
                accountBalanceResult.setValue(accountBalance.balance().toString());
            }
            else {
                notifications.create(messageBundle.getMessage("noAccountBalance"))
                        .withType(Notifications.Type.WARNING)
                        .withPosition(Notification.Position.TOP_END)
                        .show();
            }
        }
        catch (Exception e) {
            log.error("Could not calculate account balance: {}", e.getMessage(), e);
            throw new BalanceCalculationFailedException("Could not calculate account balance. Trace-ID: " + getCurrentTraceId());
        }
    }

    public static String getCurrentTraceId() {
        Span currentSpan = Span.current();
        SpanContext spanContext = currentSpan.getSpanContext();

        if (spanContext.isValid()) {
            return spanContext.getTraceId();
        }
        return "No active trace";
    }

}