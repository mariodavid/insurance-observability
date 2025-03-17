package com.insurance.partner.entity;


import org.springframework.lang.Nullable;


public enum PartnerMdc {

    PARTNER_NO("PARTNER_NO"),
    PARTNER_ID("PARTNER_ID");

    private final String id;

    PartnerMdc(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static PartnerMdc fromId(String id) {
        for (PartnerMdc at : PartnerMdc.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}