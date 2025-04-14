package com.insurance.policy.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.policy.txo.TxoPublisherClient;

@Component
public class PolicyCreatedNotifier {

    private static final Logger log = LoggerFactory.getLogger(PolicyCreatedNotifier.class);
    private final String topicName;
    private final ObjectMapper objectMapper;
    private final TxoPublisherClient txoPublisherClient;


    public PolicyCreatedNotifier(
            @Value("${policy.topic.policy-created}") String topicName, ObjectMapper objectMapper, TxoPublisherClient txoPublisherClient) {
        this.topicName = topicName;
        this.objectMapper = objectMapper;
        this.txoPublisherClient = txoPublisherClient;
    }

    public void notify(PolicyCreatedMessage policyCreatedMessage) {
        try {
            log.debug("Notifying about policy created {}", policyCreatedMessage);
            String messageBody = objectMapper.writeValueAsString(policyCreatedMessage);
            txoPublisherClient.publishMessage(topicName, messageBody);
        } catch (JsonProcessingException e) {
            log.error("Error while sending message to topic {}: {}", topicName, policyCreatedMessage, e);
            throw new RuntimeException(e);
        }
    }

}
