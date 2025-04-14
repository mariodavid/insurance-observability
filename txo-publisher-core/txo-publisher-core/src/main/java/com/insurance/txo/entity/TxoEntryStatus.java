package com.insurance.txo.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum TxoEntryStatus implements EnumClass<String> {

    READY("READY"),
    PUBLISHED("PUBLISHED"),
    FAILED("FAILED");

    private final String id;

    TxoEntryStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TxoEntryStatus fromId(String id) {
        for (TxoEntryStatus at : TxoEntryStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}