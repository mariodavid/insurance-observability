package com.insurance.account.kafka;

public record PolicyCreatedMessage(
        String policyId,
        String policyNo
) {
}
