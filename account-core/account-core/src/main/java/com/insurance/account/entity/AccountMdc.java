package com.insurance.account.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum AccountMdc implements EnumClass<String> {

    ACCOUNT_NO("account_no"),
    ACCOUNT_ID("account_id");

    private final String id;

    AccountMdc(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static AccountMdc fromId(String id) {
        for (AccountMdc at : AccountMdc.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}