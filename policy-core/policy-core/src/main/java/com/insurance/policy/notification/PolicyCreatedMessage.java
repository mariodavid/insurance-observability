package com.insurance.policy.notification;

public record PolicyCreatedMessage(
        String policyId,
        String policyNo
) {
}
