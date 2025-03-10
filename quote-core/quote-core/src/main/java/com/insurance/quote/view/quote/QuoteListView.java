package com.insurance.quote.view.quote;

import org.springframework.beans.factory.annotation.Autowired;

import com.insurance.common.ui.PolicyUiProperties;
import com.insurance.quote.app.QuoteService;
import com.insurance.quote.entity.Quote;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.Id;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Supply;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;


@Route(value = "quotes", layout = DefaultMainViewParent.class)
@ViewController(id = "quote_Quote.list")
@ViewDescriptor(path = "quote-list-view.xml")
@LookupComponent("quotesDataGrid")
@DialogMode(width = "64em")
public class QuoteListView extends StandardListView<Quote> {
    @ViewComponent
    private DataGrid<Quote> quotesDataGrid;

    @Autowired
    private QuoteService quoteService;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Notifications notifications;

    @Autowired
    private OpenTelemetry openTelemetry;
    @Autowired
    private PolicyUiProperties policyUiProperties;

    @Subscribe("quotesDataGrid.rejectAction")
    public void onQuotesDataGridRejectAction(final ActionPerformedEvent event) {

        Quote quote = quotesDataGrid.getSingleSelectedItem();
        if (quote != null) {
            quoteService.reject(Id.of(quote));
            notifications.create("Quote rejected", "Quote was succesfully rejected.")
                    .withType(Notifications.Type.SUCCESS)
                    .show();
            getViewData().loadAll();
        }
    }

    @Subscribe("quotesDataGrid.acceptAction")
    public void onQuotesDataGridAcceptAction(final ActionPerformedEvent event) {

        // programmatic creation of span as Vaadin's /POST span is too generic
        Tracer tracer = openTelemetry.getTracer(QuoteListView.class.getName());
        Span span = tracer.spanBuilder("UI: accept quote action").startSpan();

        try {
            Quote quote = quotesDataGrid.getSingleSelectedItem();
            if (quote != null) {
                Quote acceptedQuote = quoteService.accept(Id.of(quote));
                notifications.create("Quote accepted", "Policy issued: %s".formatted(acceptedQuote.getCreatedPolicyNo()))
                        .withType(Notifications.Type.SUCCESS)
                        .show();
                getViewData().loadAll();
            }
        }
        finally {
            span.end();
        }
    }

    @Supply(to = "quotesDataGrid.createdPolicyNo", subject = "renderer")
    private Renderer<Quote> quotesDataGridCreatedPolicyNoRenderer() {
        return new ComponentRenderer<>(quote -> {
            Anchor anchor = uiComponents.create(Anchor.class);
            anchor.setHref(policyUiProperties.getUiBaseUrl() + "/policies/" + quote.getCreatedPolicyId());
            anchor.setText(quote.getCreatedPolicyNo());
            return anchor;
        });
    }
}