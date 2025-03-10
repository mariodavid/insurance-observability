package com.insurance.quote.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.insurance.common.response.ErrorResponse;

@Service
public class PolicyClient {

    private static final Logger log = LoggerFactory.getLogger(PolicyClient.class);
    private final RestClient restClient;

    public PolicyClient(
            PolicyProperties policyProperties,
            RestClient.Builder builder) {
        this.restClient = builder.baseUrl(policyProperties.getApiBaseUrl()).build();
    }

    public CreatePolicyResponse createPolicy(CreatePolicyRequest request) {
        try {
            log.info("Creating policy in policy-app");
            log.debug("POST /policies. Request body: {}", request);
            ResponseEntity<CreatePolicyResponse> response = restClient.post()
                    .uri("/policies")
                    .body(request)
                    .retrieve()
                    .toEntity(CreatePolicyResponse.class);
            CreatePolicyResponse createPolicyResponse = response.getBody();

            log.debug("Response Code: {}. Body: {}", response.getStatusCode(), createPolicyResponse);
            return createPolicyResponse;
        } catch (RestClientResponseException e) {
            ErrorResponse errorResponse = e.getResponseBodyAs(ErrorResponse.class);
            log.error("Policy creation failed. Response: {}", errorResponse, e);
            return null;
        }
    }
}