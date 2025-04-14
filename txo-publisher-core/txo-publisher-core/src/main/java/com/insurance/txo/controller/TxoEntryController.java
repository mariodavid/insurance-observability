package com.insurance.txo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.txo.app.TxoEntryService;
import com.insurance.txo.entity.TxoEntry;
import io.jmix.core.security.Authenticated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TxoEntryController {

    private final ObjectMapper objectMapper;
    private final TxoEntryService txoEntryService;

    @Autowired
    public TxoEntryController(ObjectMapper objectMapper, TxoEntryService txoEntryService) {
        this.objectMapper = objectMapper;
        this.txoEntryService = txoEntryService;
    }

    @Authenticated
    @PostMapping(value = "/publish/{topicName}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TxoEntry> publishEvent(@PathVariable String topicName, @RequestBody String requestBody) {
        try {
            JsonNode parsedEventData = objectMapper.readTree(requestBody);

            TxoEntry txoEntry = txoEntryService.saveTxoEntry(topicName, requestBody);
            return ResponseEntity.ok(txoEntry);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}