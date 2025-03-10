package com.insurance.policy.kafka;

public record PolicyCreatedMessage(
        String policyId,
        String policyNo
) {
}
