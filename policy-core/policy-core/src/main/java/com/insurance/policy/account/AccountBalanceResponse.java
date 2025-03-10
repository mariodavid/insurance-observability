package com.insurance.policy.account;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountBalanceResponse(
    String accountNo,
    LocalDate effectiveDate,
    BigDecimal balance
) {}