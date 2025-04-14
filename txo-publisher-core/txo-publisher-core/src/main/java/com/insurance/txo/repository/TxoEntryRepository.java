package com.insurance.txo.repository;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import org.springframework.stereotype.Repository;

import com.insurance.txo.entity.TxoEntry;
import io.jmix.core.repository.JmixDataRepository;

@Repository
public interface TxoEntryRepository extends JmixDataRepository<TxoEntry, UUID> {

    List<TxoEntry> findAllByStatus(@NotNull String status);
}