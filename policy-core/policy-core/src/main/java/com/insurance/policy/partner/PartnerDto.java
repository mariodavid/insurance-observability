package com.insurance.policy.partner;

import java.util.UUID;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity(name = "policy_PartnerDto")
public class PartnerDto {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    @InstanceName
    private String partnerNo;

    private String firstName;

    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPartnerNo() {
        return partnerNo;
    }

    public void setPartnerNo(String partnerNo) {
        this.partnerNo = partnerNo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}