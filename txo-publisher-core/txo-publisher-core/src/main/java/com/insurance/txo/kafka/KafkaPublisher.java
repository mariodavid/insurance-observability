package com.insurance.txo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPublisher.class);
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaPublisher(KafkaTemplate kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String topicName, Object message) {
        try {
            log.debug("Publishing message to topic {}", message);
            String messageBody = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topicName, messageBody);
        } catch (JsonProcessingException e) {
            log.error("Error while sending message to topic {}: {}", topicName, message, e);
            throw new RuntimeException(e);
        }
    }

}
