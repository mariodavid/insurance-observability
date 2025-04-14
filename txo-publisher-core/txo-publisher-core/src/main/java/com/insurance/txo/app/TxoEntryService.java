package com.insurance.txo.app;

import java.util.List;

import org.springframework.stereotype.Component;

import com.insurance.txo.entity.TxoEntry;
import com.insurance.txo.entity.TxoEntryStatus;
import com.insurance.txo.repository.TxoEntryRepository;

@Component("txo_TxoEntryService")
public class TxoEntryService {

    private final TxoEntryRepository txoEntryRepository;
    private final TraceparentSupplier traceparentSupplier;

    public TxoEntryService(TxoEntryRepository txoEntryRepository, TraceparentSupplier traceparentSupplier) {
        this.txoEntryRepository = txoEntryRepository;
        this.traceparentSupplier = traceparentSupplier;
    }

    public TxoEntry saveTxoEntry(String topicName, String eventData) {

        TxoEntry txoEntry = txoEntryRepository.create();
        txoEntry.setEventTopic(topicName);
        txoEntry.setEventData(eventData);
        txoEntry.setTraceId(traceparentSupplier.get());
        txoEntry.setStatus(TxoEntryStatus.READY);

        return txoEntryRepository.save(txoEntry);

    }

    public List<TxoEntry> findUnprocessedEntries() {
        return txoEntryRepository.findAllByStatus(TxoEntryStatus.READY.getId());
    }

    public void markAsProcessed(TxoEntry txoEntry) {
        txoEntry.setStatus(TxoEntryStatus.PUBLISHED);
        txoEntryRepository.save(txoEntry);
    }
}