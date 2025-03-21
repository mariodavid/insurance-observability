/*
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 *
 * All Rights Reserved - Alle Rechte vorbehalten.
 */

package com.insurance.account.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.insurance.account.entity.Account;
import com.insurance.account.policy.FetchPolicyResponse;
import com.insurance.account.policy.PolicyClient;
import com.insurance.account.service.AccountService;
import com.insurance.common.mdc.CommonMdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jmix.core.security.Authenticated;

@Component
public class PolicyCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(PolicyCreatedListener.class);
    private final ObjectMapper objectMapper;
    private final AccountService accountService;
    private final PolicyClient policyClient;

    public PolicyCreatedListener(
            ObjectMapper objectMapper, AccountService accountService, PolicyClient policyClient) {
        this.objectMapper = objectMapper;
        this.accountService = accountService;
        this.policyClient = policyClient;
    }

    @Authenticated
    @KafkaListener(topics = "${account.topic.policy-created}")
    public void onMessage(
            ConsumerRecord<String, String> message) {
        try {
            Headers headers = message.headers();

            log.debug("Received record: {}", message);
            log.debug("Received headers: {}", headers);

            var event = extractPayload(message.value());

            handleEvent(event);
        }
        finally {
            MDC.clear();
        }
    }

    private void handleEvent(PolicyCreatedMessage event) {

        MDC.put(CommonMdc.POLICY_ID.getId(), event.policyId());
        MDC.put(CommonMdc.POLICY_NO.getId(), event.policyNo());

        log.info("Policy created event received: {}", event);

        FetchPolicyResponse fetchPolicyResponse = policyClient.fetchPolicy(event.policyId());

        Account account = accountService.createAccount(
                event.policyId(),
                fetchPolicyResponse.policyNo(),
                fetchPolicyResponse.coverageStart(),
                fetchPolicyResponse.premium(),
                fetchPolicyResponse.paymentFrequency()
        );

        log.info("Account created: {}", account);
    }

    private PolicyCreatedMessage extractPayload(String message) {
        try {
            return objectMapper.readValue(message, PolicyCreatedMessage.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error parsing event as JSON", e);
        }
    }

}
