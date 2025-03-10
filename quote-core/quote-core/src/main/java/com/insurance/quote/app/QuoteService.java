package com.insurance.quote.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.insurance.common.mdc.CommonMdc;
import com.insurance.quote.entity.Quote;
import com.insurance.quote.entity.QuoteStatus;
import com.insurance.quote.policy.CreatePolicyRequest;
import com.insurance.quote.policy.CreatePolicyResponse;
import com.insurance.quote.policy.PolicyClient;

import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.TimeSource;
import io.opentelemetry.instrumentation.annotations.WithSpan;

@Component("quote_QuoteService")
public class QuoteService {
    private static final Logger log = LoggerFactory.getLogger(QuoteService.class);
    private final DataManager dataManager;
    private final PolicyClient policyClient;
    private final TimeSource timeSource;

    public QuoteService(DataManager dataManager, PolicyClient policyClient, TimeSource timeSource) {
        this.dataManager = dataManager;
        this.policyClient = policyClient;
        this.timeSource = timeSource;
    }

    public void reject(Id<Quote> quoteId) {
        Quote quote = loadQuote(quoteId);
        quote.setStatus(QuoteStatus.REJECTED);
        quote.setRejectedAt(timeSource.now().toLocalDateTime());
        dataManager.save(quote);
    }


    @WithSpan("Accept Quote") // [optional] creating a custom span for better visibility
    public Quote accept(Id<Quote> quoteId) {
        try {
            MDC.put(CommonMdc.QUOTE_ID.getId(), quoteId.getValue().toString());
            log.info("Trying to accept quote");

            log.debug("Loading quote with id {}", quoteId);
            Quote quote = loadQuote(quoteId);
            MDC.put(CommonMdc.QUOTE_NO.getId(), quote.getQuoteNo());

            quote.setStatus(QuoteStatus.ACCEPTED);
            quote.setAcceptedAt(timeSource.now().toLocalDateTime());

            log.debug("Saving accepted quote");
            Quote savedQuote = dataManager.save(quote);
            log.info("Quote accepted successfully");

            CreatePolicyRequest request = new CreatePolicyRequest(
                    quote.getQuoteNo(),
                    quote.getInsuranceProduct().getId(),
                    quote.getEffectiveDate(),
                    quote.getCalculatedPremium(),
                    quote.getPaymentFrequency().getId()
            );

            CreatePolicyResponse policyResponse = policyClient.createPolicy(request);

            MDC.put(CommonMdc.POLICY_ID.getId(), policyResponse.policyId());
            MDC.put(CommonMdc.POLICY_NO.getId(), policyResponse.policyNo());

            log.info("Policy creation in policy-app successful");

            log.debug("Updating quote policy references");
            savedQuote.setCreatedPolicyNo(policyResponse.policyNo());
            savedQuote.setCreatedPolicyId(policyResponse.policyId());
            Quote updatedQuote = dataManager.save(savedQuote);
            log.debug("Quote policy references updated successfully");

            return updatedQuote;
        }
        finally {
            MDC.clear();
        }


    }

    private Quote loadQuote(Id<Quote> quoteId) {
        return dataManager.load(quoteId).one();
    }

}