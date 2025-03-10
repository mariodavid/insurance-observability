package com.insurance.account.service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.insurance.account.entity.Account;
import com.insurance.account.entity.AccountDocument;
import com.insurance.account.entity.DocumentType;
import com.insurance.common.entity.PaymentFrequency;

import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.core.querycondition.PropertyCondition;
import io.micrometer.observation.annotation.Observed;

@Component
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final DataManager dataManager;
    private final Validator validator;

    public AccountService(DataManager dataManager, Validator validator) {
        this.dataManager = dataManager;
        this.validator = validator;
    }


    public Account createAccount(String accountNo, LocalDate coverageStart, BigDecimal premium, PaymentFrequency paymentFrequency) {

        log.info("Creating account {}", accountNo);
        Account account = dataManager.create(Account.class);

        SaveContext saveContext = new SaveContext();

        account.setAccountNo(accountNo);
        account.setAccountBalance(premium.negate());

        int payments = paymentFrequency.getFrequency();
        BigDecimal documentAmount = premium.divide(new BigDecimal(payments), RoundingMode.HALF_UP);

        saveContext.saving(account);

        int intervalMonths;
        switch (payments) {
            case 1:
                intervalMonths = 0;
                break;
            case 4:
                intervalMonths = 3;
                break;
            case 12:
                intervalMonths = 1;
                break;
            default:
                intervalMonths = 12 / payments;
                break;
        }

        Set<ConstraintViolation<Account>> accountValidationResult = validator.validate(account);
        if (!accountValidationResult.isEmpty()) {
            log.debug("Account validation failed for account {}: {}", accountNo, accountValidationResult);
            throw new InvalidAccountException("Invalid Account");
        }

        for (int i = 0; i < payments; i++) {
            LocalDate documentDate = coverageStart.plusMonths((long) i * intervalMonths);
            AccountDocument accountDocument = dataManager.create(AccountDocument.class);
            accountDocument.setAccount(account);
            accountDocument.setAmount(documentAmount.negate());
            accountDocument.setDescription("Payment for " + documentDate);
            accountDocument.setType(DocumentType.CREDIT);
            accountDocument.setDocumentDate(documentDate);
            saveContext.saving(accountDocument);

            Set<ConstraintViolation<AccountDocument>> validationResult = validator.validate(accountDocument);
            if (!validationResult.isEmpty()) {
                log.debug("AccountDocument validation failed for account {}: {}", accountNo, validationResult);
                throw new InvalidAccountException("Invalid Account Document");
            }
        }

        Account savedAccount = dataManager.save(saveContext).get(account);
        log.info("Account {} created successfully with balance {}", accountNo, savedAccount.getAccountBalance());
        return savedAccount;
    }

    @Observed(name = "account.balance.calculation", contextualName = "getAccountBalance")
    public Optional<BigDecimal> getAccountBalance(String accountNo, LocalDate effectiveDate) {
        log.info("Calculating balance for accountNo {} as of effectiveDate {}", accountNo, effectiveDate);
        Optional<Account> potentialAccount = dataManager.load(Account.class)
                .condition(PropertyCondition.equal("accountNo", accountNo))
                .optional();

        if (potentialAccount.isEmpty()) {
            log.warn("Account with accountNo {} not found.", accountNo);
            return Optional.empty();
        }

        BigDecimal computedBalance = potentialAccount.get().getDocuments().stream()
                .filter(doc -> !doc.getDocumentDate().isAfter(effectiveDate))
                .map(AccountDocument::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.debug("Computed balance for accountNo {}: {}", accountNo, computedBalance);
        return Optional.of(computedBalance);
    }
}