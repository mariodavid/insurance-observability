package com.insurance.policy.partner;

public record PartnerResponse(
    String partnerId,
    String partnerNo,
    String firstName,
    String lastName
) {
}