package com.insurance.policy.service;

import com.insurance.common.entity.PaymentFrequency;
import com.insurance.policy.entity.Policy;
import com.insurance.policy.notification.PolicyCreatedMessage;
import com.insurance.policy.notification.PolicyCreatedNotifier;
import com.insurance.product.entity.InsuranceProduct;
import io.jmix.core.DataManager;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class PolicyService {

    private static final Logger log = LoggerFactory.getLogger(PolicyService.class);
    private final DataManager dataManager;
    private final Sequences sequences;
    private final Validator validator;
    private final PolicyCreatedNotifier policyCreatedNotifier;

    public PolicyService(DataManager dataManager, Sequences sequences, Validator validator, PolicyCreatedNotifier policyCreatedNotifier) {
        this.dataManager = dataManager;
        this.sequences = sequences;
        this.validator = validator;
        this.policyCreatedNotifier = policyCreatedNotifier;
    }

    @Transactional
    public Policy createPolicy(InsuranceProduct insuranceProduct, LocalDate policyCoverageStart, PaymentFrequency paymentFrequency, BigDecimal premium, String partnerNo) {

        log.debug("Starting policy creation");
        long policySequenceNumber = sequences.createNextValue(Sequence.withName("policy"));

        String policyNo = "%s-%s-%06d".formatted(insuranceProduct.getProductType().getName(), policyCoverageStart.getYear(), policySequenceNumber);
        log.debug("Policy no: {}", policyNo);

        Policy policy = dataManager.create(Policy.class);
        policy.setPolicyNo(policyNo);
        policy.setPartnerNo(partnerNo);
        policy.setInsuranceProduct(insuranceProduct);
        policy.setCoverageStart(policyCoverageStart);
        policy.setPaymentFrequency(paymentFrequency);
        policy.setPremium(premium);

        LocalDate coverageEnd = policyCoverageStart.plusYears(1);
        log.debug("Calculated coverage end: {}", coverageEnd);
        policy.setCoverageEnd(coverageEnd);


        Set<ConstraintViolation<Policy>> validationResult = validator.validate(policy);

        if (!validationResult.isEmpty()) {
            log.warn("Validation failed: {}", validationResult);
            return null;
        }
        else {
            log.debug("Policy validation successful");
        }

        log.debug("Trying to save policy");
        Policy savedPolicy = dataManager.save(policy);
        log.debug("Policy saving successful");

        policyCreatedNotifier.notify(new PolicyCreatedMessage(savedPolicy.getId().toString(), savedPolicy.getPolicyNo()));
        return savedPolicy;

    }

    public Optional<Policy> findPolicyById(String policyId) {
        return dataManager.load(Policy.class).id(UUID.fromString(policyId)).optional();
    }
}