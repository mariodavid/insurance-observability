package com.insurance.txo.entity;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.flowui.component.propertyfilter.PropertyFilter;

@JmixEntity
@Table(name = "TXO_TXO_ENTRY", indexes = {
        @Index(name = "IDX_TXO_TXO_ENTRY", columnList = "STATUS")
})
@Entity(name = "txo_TxoEntry")
public class TxoEntry {

    @InstanceName
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;
    @Column(name = "STATUS", nullable = false)
    @NotNull
    private String status;
    @Column(name = "TRACE_ID", nullable = false)
    @NotNull
    private String traceId;
    @Column(name = "EVENT_DATA")
    private String eventData;
    @Column(name = "EVENT_TOPIC", nullable = false)
    @NotNull
    private String eventTopic;

    public TxoEntryStatus getStatus() {
        return status == null ? null : TxoEntryStatus.fromId(status);
    }

    public void setStatus(TxoEntryStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public String getEventTopic() {
        return eventTopic;
    }

    public void setEventTopic(String eventTopic) {
        this.eventTopic = eventTopic;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}