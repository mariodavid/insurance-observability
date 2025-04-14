package com.insurance.policy.txo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@Service
public class TxoPublisherClient {

    private static final Logger log = LoggerFactory.getLogger(TxoPublisherClient.class);
    private final WebClient webClient;

    public TxoPublisherClient(
            TxoPublisherProperties txoPublisherProperties,
            WebClient.Builder builder) {
        this.webClient = builder.baseUrl(txoPublisherProperties.getApiBaseUrl()).build();
    }

    public String publishMessage(String topicName, Object eventData) {
        try {
            log.info("Publishing message to topic via TXO API: {}", topicName);

            ResponseEntity<TxoEntryResponse> response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/publish/{topicName}")
                            .build(topicName))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(eventData)
                    .retrieve()
                    .toEntity(TxoEntryResponse.class)
                    .block();

            TxoEntryResponse txoEntryResponse = response.getBody();
            log.debug("Response Code: {}. Body: {}", response.getStatusCode(), txoEntryResponse);

            return txoEntryResponse.txoEntryId();
        } catch (WebClientRequestException e) {
            log.error("Publishing event failed.", e);
            return null;
        }
    }
}