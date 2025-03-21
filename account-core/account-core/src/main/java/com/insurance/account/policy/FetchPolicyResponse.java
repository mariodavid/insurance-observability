
package com.insurance.account.policy;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.insurance.common.entity.PaymentFrequency;

public record FetchPolicyResponse(
    String policyId,
    String policyNo,
    LocalDate coverageStart,
    LocalDate coverageEnd,
    BigDecimal premium,
    PaymentFrequency paymentFrequency
) {}