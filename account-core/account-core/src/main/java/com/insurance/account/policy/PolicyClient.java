package com.insurance.account.policy;

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

    public FetchPolicyResponse fetchPolicy(String policyId) {
        try {
            log.info("Fetching policy in policy-app");
            log.debug("GET /policies/{}", policyId);
            ResponseEntity<FetchPolicyResponse> response = restClient.get()
                    .uri("/policies/{policyId}", policyId)
                    .retrieve()
                    .toEntity(FetchPolicyResponse.class);
            FetchPolicyResponse fetchPolicyResponse = response.getBody();

            log.debug("Response Code: {}. Body: {}", response.getStatusCode(), fetchPolicyResponse);
            return fetchPolicyResponse;
        } catch (RestClientResponseException e) {
            ErrorResponse errorResponse = e.getResponseBodyAs(ErrorResponse.class);
            log.error("Policy fetching failed. Response: {}", errorResponse, e);
            return null;
        }
    }
}