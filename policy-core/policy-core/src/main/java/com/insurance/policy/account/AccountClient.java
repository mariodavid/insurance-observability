package com.insurance.policy.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.time.LocalDate;

@Service
public class AccountClient {

    private static final Logger log = LoggerFactory.getLogger(AccountClient.class);
    private final WebClient webClient;

    public AccountClient(
            AccountProperties accountProperties,
            WebClient.Builder builder) {
        this.webClient = builder.baseUrl(accountProperties.getApiBaseUrl()).build();
    }

    public AccountBalanceResponse getAccountBalance(String accountNo, LocalDate effectiveDate) {
        try {
            log.info("Fetching account balance from account-app");
            log.debug("GET /accounts/{}?effectiveDate={}", accountNo, effectiveDate);

            ResponseEntity<AccountBalanceResponse> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/accounts/{accountNo}")
                            .queryParam("effectiveDate", effectiveDate)
                            .build(accountNo))
                    .retrieve()
                    .toEntity(AccountBalanceResponse.class)
                    .block();

            AccountBalanceResponse balanceResponse = response.getBody();
            log.debug("Response Code: {}. Body: {}", response.getStatusCode(), balanceResponse);

            return balanceResponse;
        } catch (WebClientRequestException e) {
            log.error("Fetching account balance failed.", e);
            return null;
        }
    }
}