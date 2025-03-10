
package com.insurance.quote.policy;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePolicyRequest(
    String quoteNo,
    String insuranceProductId,
    LocalDate effectiveDate,
    BigDecimal premium,
    String paymentFrequencyId
) {}