package com.insurance.partner.entity;

import java.util.UUID;

public record Partner(
    UUID id,
    String partnerNo,
    String firstName,
    String lastName
) {}