package com.insurance.quote.view.quote;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.insurance.product.entity.InsuranceProduct;
import com.insurance.quote.entity.Quote;
import com.insurance.quote.entity.QuoteStatus;

import com.vaadin.flow.router.Route;
import io.jmix.core.TimeSource;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.EditedEntityContainer;
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


}