package com.insurance.quote.partner;

public record PartnerResponse(
    String partnerId,
    String partnerNo,
    String firstName,
    String lastName
) {
}