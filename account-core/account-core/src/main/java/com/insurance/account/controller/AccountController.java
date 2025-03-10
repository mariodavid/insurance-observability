package com.insurance.account.controller;

import com.insurance.account.entity.AccountMdc;
import com.insurance.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import io.jmix.core.security.Authenticated;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;
    private final MeterRegistry meterRegistry;

    public AccountController(AccountService accountService, MeterRegistry meterRegistry) {
        this.accountService = accountService;
        this.meterRegistry = meterRegistry;
    }

    @Authenticated
    @GetMapping("/{accountNo}")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(
            @PathVariable String accountNo,
            @RequestParam("effectiveDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate) {
        try {
            MDC.put(AccountMdc.ACCOUNT_NO.getId(), accountNo);

            log.info("Request to get account balance received.");
            log.debug("Parameters - accountNo: {}, effectiveDate: {}", accountNo, effectiveDate);

            Optional<BigDecimal> calculatedBalance = accountService.getAccountBalance(accountNo, effectiveDate);

            if (calculatedBalance.isEmpty()) {
                meterRegistry.counter("account.balance.not_found").increment();
                log.warn("Account with accountNo {} not found or no balance available for effectiveDate {}.", accountNo, effectiveDate);
                return ResponseEntity.notFound().build();
            }

            BigDecimal balance = calculatedBalance.get();
            log.info("Account balance for accountNo {} calculated as: {}", accountNo, balance);

            AccountBalanceResponse response = new AccountBalanceResponse(
                    accountNo,
                    effectiveDate,
                    balance
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error calculating account balance for accountNo {} and effectiveDate {}.", accountNo, effectiveDate, e);
            return ResponseEntity.internalServerError().build();
        } finally {
            MDC.clear();
        }
    }
}