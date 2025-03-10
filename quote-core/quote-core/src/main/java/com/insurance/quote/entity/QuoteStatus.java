package com.insurance.quote.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum QuoteStatus implements EnumClass<String> {

    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED");

    private final String id;

    QuoteStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static QuoteStatus fromId(String id) {
        for (QuoteStatus at : QuoteStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}