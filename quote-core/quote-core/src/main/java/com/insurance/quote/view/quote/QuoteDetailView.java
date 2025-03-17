package com.insurance.quote.view.quote;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.insurance.product.entity.InsuranceProduct;
import com.insurance.quote.entity.Quote;
import com.insurance.quote.entity.QuoteStatus;

import com.insurance.quote.partner.PartnerClient;
import com.insurance.quote.partner.PartnerDto;
import com.insurance.quote.partner.PartnerResponse;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.TimeSource;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.combobox.EntityComboBox;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "quotes/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "quote_Quote.detail")
@ViewDescriptor(path = "quote-detail-view.xml")
@EditedEntityContainer("quoteDc")
public class QuoteDetailView extends StandardDetailView<Quote> {
    @Autowired
    private Sequences sequences;
    @Autowired
    private TimeSource timeSource;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private MessageBundle messageBundle;
    @ViewComponent
    private JmixButton saveAndCloseButton;
    @Autowired
    private PartnerClient partnerClient;
    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Quote> event) {

        long quoteSequenceNumber = sequences.createNextValue(Sequence.withName("quote"));
        Quote quote = event.getEntity();
        LocalDate today = timeSource.now().toLocalDate();
        quote.setQuoteNo("%s-%s".formatted(today.getYear(),quoteSequenceNumber));
        quote.setStatus(QuoteStatus.PENDING);
        quote.setValidFrom(today);
        quote.setValidUntil(today.plusDays(14));

    }



    @Subscribe("calculatePremiumAction")
    public void onCalculatePremiumAction(final ActionPerformedEvent event) {
        saveAndCloseButton.setEnabled(false);

        Quote quote = getEditedEntity();
        Optional<InsuranceProduct> matchingProduct = InsuranceProduct.findFirstMatchingProduct(
                quote.getProductType(),
                quote.getProductVariant(),
                quote.getEffectiveDate()
        );

        if (matchingProduct.isEmpty()) {
            notifications.create(messageBundle.formatMessage("noMatchingProduct"))
                    .withType(Notifications.Type.ERROR)
                    .show();
            return;
        }

        InsuranceProduct insuranceProduct = matchingProduct.get();
        quote.setInsuranceProduct(insuranceProduct);
        BigDecimal calculatedPremium = insuranceProduct.calculatePremium(BigDecimal.valueOf(quote.getSquareMeters()));
        quote.setCalculatedPremium(calculatedPremium);

        saveAndCloseButton.setEnabled(true);
    }

    @Install(to = "partnerComboBox", subject = "itemsFetchCallback")
    private Stream<PartnerDto> partnerComboBoxItemsFetchCallback(final Query<PartnerDto, String> query) {
        Optional<String> filter = query.getFilter();
        int limit = query.getLimit();
        int pageSize = query.getPageSize();
        int page = query.getPage();
        int offset = query.getOffset();

        if (filter.isEmpty() || filter.get().isEmpty()) {
            return Stream.empty();
        }
        return partnerClient.findPartners(filter.get(), limit, offset).stream()
                .map(this::toPartnerDto);
    }

    private PartnerDto toPartnerDto(PartnerResponse partnerResponse) {
        PartnerDto partnerDto = dataManager.create(PartnerDto.class);
        partnerDto.setId(UUID.fromString(partnerResponse.partnerId()));
        partnerDto.setPartnerNo(partnerResponse.partnerNo());
        partnerDto.setFirstName(partnerResponse.firstName());
        partnerDto.setLastName(partnerResponse.lastName());
        return partnerDto;
    }

    @Subscribe("partnerComboBox")
    public void onPartnerComboBoxComponentValueChange(final AbstractField.ComponentValueChangeEvent<EntityComboBox<PartnerDto>, PartnerDto> event) {
        PartnerDto value = event.getValue();
        if (value != null) {
            getEditedEntity().setPartnerNo(value.getPartnerNo());
        }
        else {
            getEditedEntity().setPartnerNo(null);
        }
    }


}