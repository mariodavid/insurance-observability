package com.insurance.txo.app;


import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.txo.entity.TxoEntry;
import com.insurance.txo.kafka.KafkaPublisher;

@Component("txo_TxoEntryPublisher")
public class TxoEntryPublisher {
    private final TxoEntryService txoEntryService;
    private final TraceparentContextWrapper traceparentContextWriter;
    private final KafkaPublisher kafkaPublisher;
    private final ObjectMapper objectMapper;

    public TxoEntryPublisher(TxoEntryService txoEntryService, TraceparentContextWrapper traceparentContextWriter, KafkaPublisher kafkaPublisher, ObjectMapper objectMapper) {
        this.txoEntryService = txoEntryService;
        this.traceparentContextWriter = traceparentContextWriter;
        this.kafkaPublisher = kafkaPublisher;
        this.objectMapper = objectMapper;
    }


    public void publish() {
        txoEntryService.findUnprocessedEntries()
                .forEach(this::publishEntry);
    }

    private void publishEntry(TxoEntry txoEntry) {
        traceparentContextWriter.runInContext(txoEntry.getTraceId(), () -> {
            try {
                JsonNode eventData = objectMapper.readTree(txoEntry.getEventData());
                kafkaPublisher.publish(txoEntry.getEventTopic(), eventData);
                txoEntryService.markAsProcessed(txoEntry);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}