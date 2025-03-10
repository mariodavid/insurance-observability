package com.insurance.policy.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PolicyCreatedTopic {

    private static final Logger log = LoggerFactory.getLogger(PolicyCreatedTopic.class);
    private final String topicName;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PolicyCreatedTopic(@Value("${policy.topic.policy-created}") String topicName, KafkaTemplate kafkaTemplate, ObjectMapper objectMapper) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(PolicyCreatedMessage policyCreatedMessage) {
        try {
            log.debug("Publishing message to topic {}", policyCreatedMessage);
            String messageBody = objectMapper.writeValueAsString(policyCreatedMessage);
            kafkaTemplate.send(topicName, messageBody);
        } catch (JsonProcessingException e) {
            log.error("Error while sending message to topic {}: {}", topicName, policyCreatedMessage, e);
            throw new RuntimeException(e);
        }
    }

}
