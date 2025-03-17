package com.insurance.policy.controller;

import java.util.Optional;

import com.insurance.common.entity.PaymentFrequency;
import com.insurance.common.mdc.CommonMdc;
import com.insurance.common.response.ErrorMessage;
import com.insurance.policy.entity.Policy;
import com.insurance.policy.service.PolicyService;
import com.insurance.product.entity.InsuranceProduct;

import io.jmix.core.security.Authenticated;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private static final Logger log = LoggerFactory.getLogger(PolicyController.class);
    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @Authenticated
    @PostMapping
    public ResponseEntity<?> createPolicy(@RequestBody @Valid CreatePolicyRequest request) {
        try {
            MDC.put(CommonMdc.QUOTE_NO.getId(), request.quoteNo());

            log.info("Policy creation requested");
            log.debug("Policy creation request details: {}", request);
            InsuranceProduct product = InsuranceProduct.fromId(request.insuranceProductId());
            if (product == null) {
                log.warn("Product with id '{}' not found. Policy creation rejected.", request.insuranceProductId());
                return ResponseEntity.unprocessableEntity()
                        .body(new ErrorMessage("invalid-product","Invalid insurance product"));
            }
            log.debug("Found product: {}", product);

            PaymentFrequency paymentFrequency = PaymentFrequency.fromId(request.paymentFrequencyId());
            if (paymentFrequency == null) {
                log.warn("Payment Frequency with id '{}' not found. Policy creation rejected.", request.paymentFrequencyId());
                return ResponseEntity.unprocessableEntity()
                        .body(new ErrorMessage("invalid-payment-frequency","Invalid payment frequency"));
            }
            log.debug("Found payment frequency: {}", paymentFrequency);

            try {
                log.debug("Preconditions for policy creation fulfilled. Continuing with policy creation");
                Policy policy = policyService.createPolicy(product, request.effectiveDate(), paymentFrequency, request.premium(), request.partnerNo());

                MDC.put(CommonMdc.POLICY_NO.getId(), policy.getPolicyNo());
                MDC.put(CommonMdc.POLICY_ID.getId(), policy.getId().toString());
                log.info("Policy creation successful");

                return ResponseEntity.ok(new CreatePolicyResponse(policy.getId().toString(), policy.getPolicyNo()));
            }
            catch (Exception e) {
                log.error("Policy creation failed", e);
                return ResponseEntity.internalServerError()
                        .body(new ErrorMessage("internal-error",e.getMessage()));
            }
        }
        finally {
            MDC.clear();
        }
    }

    @Authenticated
    @GetMapping("/{policyId}")
    public ResponseEntity<?> fetchPolicy(@PathVariable String policyId) {
        try {
            log.info("Fetching policy with ID: {}", policyId);

            Optional<Policy> policy = policyService.findPolicyById(policyId);
            if (policy.isEmpty()) {
                log.warn("Policy with ID '{}' not found", policyId);
                return ResponseEntity.notFound().build();
            }

            Policy foundPolicy = policy.get();
            FetchPolicyResponse response = new FetchPolicyResponse(
                    foundPolicy.getId().toString(),
                    foundPolicy.getPolicyNo(),
                    foundPolicy.getCoverageStart(),
                    foundPolicy.getPremium(),
                    foundPolicy.getPaymentFrequency()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching policy with ID: {}", policyId, e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorMessage("internal-error", e.getMessage()));
        }
    }
}