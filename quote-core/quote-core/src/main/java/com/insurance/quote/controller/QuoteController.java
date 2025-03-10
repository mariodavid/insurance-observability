package com.insurance.quote.controller;

import com.insurance.common.entity.PaymentFrequency;
import com.insurance.product.entity.InsuranceProduct;
import com.insurance.product.entity.ProductType;
import com.insurance.product.entity.ProductVariant;
import com.insurance.quote.app.QuoteService;
import com.insurance.quote.entity.Quote;
import com.insurance.quote.entity.QuoteStatus;

import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.TimeSource;
import io.jmix.core.security.Authenticated;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private static final Logger log = LoggerFactory.getLogger(QuoteController.class);

    private final QuoteService quoteService;
    private final DataManager dataManager;
    private final Validator validator;
    private final Sequences sequences;
    private final TimeSource timeSource;

    public QuoteController(QuoteService quoteService, DataManager dataManager, Validator validator, Sequences sequences, TimeSource timeSource) {
        this.quoteService = quoteService;
        this.dataManager = dataManager;
        this.validator = validator;
        this.sequences = sequences;
        this.timeSource = timeSource;
    }

    @Authenticated
    @PostMapping("/random")
    public ResponseEntity<Quote> createRandomQuote() {
        Quote randomQuote = generateRandomQuote();
        log.info("Creating random Quote with id: {}", randomQuote.getId());

        Set<ConstraintViolation<Quote>> validationResult = validator.validate(randomQuote);

        if (!validationResult.isEmpty()) {
            log.error("Validation failed: {}", validationResult);
            return ResponseEntity.badRequest().body(randomQuote);
        }
        Quote savedQuote = dataManager.save(randomQuote);

        quoteService.accept(Id.of(savedQuote));

        log.info("Random Quote created with id: {}", savedQuote.getId());
        return ResponseEntity.ok(savedQuote);
    }

    private Quote generateRandomQuote() {
        Quote quote = dataManager.create(Quote.class);

        long quoteSequenceNumber = sequences.createNextValue(Sequence.withName("quote"));
        LocalDate today = timeSource.now().toLocalDate();
        quote.setQuoteNo("%s-%s".formatted(today.getYear(),quoteSequenceNumber));

        quote.setStatus(QuoteStatus.PENDING);

        quote.setEffectiveDate(LocalDate.of(2024, 1, 1).plusDays((int)(Math.random() * 730)));
        quote.setPaymentFrequency(randomEnumValue(PaymentFrequency.values()));
        quote.setValidFrom(LocalDate.now());
        quote.setSquareMeters(new Random().nextInt(50, 250));
        quote.setValidUntil(LocalDate.now().plusMonths(1));
        quote.setProductType(ProductType.HOME_CONTENT);
        quote.setProductVariant(randomEnumValue(ProductVariant.values()));
        quote.setProductType(randomEnumValue(ProductType.values()));

        Optional<InsuranceProduct> matchingProduct = InsuranceProduct.findFirstMatchingProduct(
                quote.getProductType(),
                quote.getProductVariant(),
                quote.getEffectiveDate()
        );

        if (matchingProduct.isEmpty()) {
            log.error("No product found matching product type: {}", quote.getProductType());
            return null;
        }

        InsuranceProduct insuranceProduct = matchingProduct.get();
        quote.setInsuranceProduct(insuranceProduct);
        BigDecimal calculatedPremium = insuranceProduct.calculatePremium(BigDecimal.valueOf(quote.getSquareMeters()));
        quote.setCalculatedPremium(calculatedPremium);

        return quote;
    }

    private static <T> T randomEnumValue(T[] values) {
        return values[(int) (Math.random() * values.length)];
    }
}