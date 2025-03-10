package com.insurance.policy.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.insurance.common.entity.PaymentFrequency;
import com.insurance.product.entity.InsuranceProduct;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
@Table(name = "POLICY_POLICY")
@Entity(name = "policy_Policy")
public class Policy {

    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;
    @Column(name = "INSURANCE_PRODUCT", nullable = false)
    @NotNull
    private String insuranceProduct;
    @Column(name = "POLICY_NO", nullable = false)
    @NotNull
    private String policyNo;
    @Column(name = "COVERAGE_START", nullable = false)
    @NotNull
    private LocalDate coverageStart;
    @Column(name = "COVERAGE_END")
    private LocalDate coverageEnd;
    @Column(name = "PREMIUM", nullable = false, precision = 19, scale = 2)
    @NotNull
    private BigDecimal premium;
    @Column(name = "PAYMENT_FREQUENCY", nullable = false)
    @NotNull
    private String paymentFrequency;

    public PaymentFrequency getPaymentFrequency() {
        return paymentFrequency == null ? null : PaymentFrequency.fromId(paymentFrequency);
    }

    public void setPaymentFrequency(PaymentFrequency paymentFrequency) {
        this.paymentFrequency = paymentFrequency == null ? null : paymentFrequency.getId();
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public InsuranceProduct getInsuranceProduct() {
        return insuranceProduct == null ? null : InsuranceProduct.fromId(insuranceProduct);
    }

    public void setInsuranceProduct(InsuranceProduct insuranceProduct) {
        this.insuranceProduct = insuranceProduct == null ? null : insuranceProduct.getId();
    }


    public LocalDate getCoverageEnd() {
        return coverageEnd;
    }

    public void setCoverageEnd(LocalDate coverageEnd) {
        this.coverageEnd = coverageEnd;
    }

    public LocalDate getCoverageStart() {
        return coverageStart;
    }

    public void setCoverageStart(LocalDate coverageStart) {
        this.coverageStart = coverageStart;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}