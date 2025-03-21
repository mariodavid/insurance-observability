
package com.insurance.policy.controller;

import java.math.BigDecimal;

import com.insurance.common.entity.PaymentFrequency;

import java.time.LocalDate;

public record FetchPolicyResponse(
        String policyId,
        String policyNo,
        LocalDate coverageStart,
        LocalDate coverageEnd,
        BigDecimal premium,
        PaymentFrequency paymentFrequency
) {}