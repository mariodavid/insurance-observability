package com.insurance.policy.partner;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.insurance.policy.account.AccountBalanceResponse;

@Service
public class PartnerClient {

    private static final Logger log = LoggerFactory.getLogger(PartnerClient.class);
    private final WebClient webClient;

    public PartnerClient(
            PartnerProperties partnerProperties,
            WebClient.Builder builder) {
        this.webClient = builder.baseUrl(partnerProperties.getApiBaseUrl()).build();
    }

    public PartnerResponse getPartner(String partnerNo) {
        try {
            log.info("Fetching partner from partner-app");
            log.debug("GET /partners/{}", partnerNo);

            ResponseEntity<PartnerResponse> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/partners/{partnerNo}")
                            .build(partnerNo))
                    .retrieve()
                    .toEntity(PartnerResponse.class)
                    .block();

            PartnerResponse partnerResponse = response.getBody();
            log.debug("Response Code: {}. Body: {}", response.getStatusCode(), partnerResponse);

            return partnerResponse;
        } catch (WebClientRequestException e) {
            log.error("Fetching partner failed.", e);
            return null;
        }
    }
}