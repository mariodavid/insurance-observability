package com.insurance.quote.partner;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.insurance.common.response.ErrorResponse;

@Service
public class PartnerClient {

    private static final Logger log = LoggerFactory.getLogger(PartnerClient.class);
    private final RestClient restClient;

    public PartnerClient(PartnerProperties partnerProperties, RestClient.Builder builder) {
        this.restClient = builder.baseUrl(partnerProperties.getApiBaseUrl()).build();
    }

    public List<PartnerResponse> findPartners(String search, int limit, int offset) {
        try {
            log.info("Searching partners with search parameter: {}, limit: {}, offset: {}", search, limit, offset);
            ResponseEntity<PartnerResponse[]> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/partners")
                            .queryParam("search", search)
                            .queryParam("limit", limit)
                            .queryParam("offset", offset)
                            .build())
                    .retrieve()
                    .toEntity(PartnerResponse[].class);
            PartnerResponse[] partnerResponses = response.getBody();
//            log.debug("Response Code: {}. Body: {}", response.getStatusCode(), partnerResponses);
            return Arrays.asList(partnerResponses);
        } catch (RestClientResponseException e) {
            ErrorResponse errorResponse = e.getResponseBodyAs(ErrorResponse.class);
            log.error("Error searching partners. Response: {}", errorResponse, e);
            return List.of();
        }
    }

    public PartnerResponse getPartner(String partnerNo) {
        try {
            log.info("Getting partner with partnerNo: {}", partnerNo);
            ResponseEntity<PartnerResponse> response = restClient.get()
                    .uri("/api/partners/{partnerNo}", partnerNo)
                    .retrieve()
                    .toEntity(PartnerResponse.class);
            PartnerResponse partnerResponse = response.getBody();
            log.debug("Response Code: {}. Body: {}", response.getStatusCode(), partnerResponse);
            return partnerResponse;
        } catch (RestClientResponseException e) {
            ErrorResponse errorResponse = e.getResponseBodyAs(ErrorResponse.class);
            log.error("Error getting partner. Response: {}", errorResponse, e);
            return null;
        }
    }
}