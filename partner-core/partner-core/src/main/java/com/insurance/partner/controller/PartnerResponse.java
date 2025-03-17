package com.insurance.partner.controller;

public record PartnerResponse(
    String partnerId,
    String partnerNo,
    String firstName,
    String lastName
) {
}