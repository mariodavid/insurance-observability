package com.insurance.quote.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.insurance.common.entity.PaymentFrequency;
import com.insurance.product.entity.InsuranceProduct;
import com.insurance.product.entity.ProductType;
import com.insurance.product.entity.ProductVariant;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
@Table(name = "QUOTE_QUOTE")
@Entity(name = "quote_Quote")
public class Quote {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;
    @Column(name = "QUOTE_NO", nullable = false)
    @NotNull
    private String quoteNo;
    @Column(name = "STATUS", nullable = false)
    @NotNull
    private String status;
    @Column(name = "PRODUCT_TYPE", nullable = false)
    @NotNull
    private String productType;
    @Column(name = "PRODUCT_VARIANT", nullable = false)
    @NotNull
    private String productVariant;
    @Column(name = "PAYMENT_FREQUENCY", nullable = false)
    @NotNull
    private String paymentFrequency;
    @Column(name = "ACCEPTED_AT")
    private LocalDateTime acceptedAt;
    @Column(name = "REJECTED_AT")
    private LocalDateTime rejectedAt;
    @Column(name = "CREATED_POLICY_NO")
    private String createdPolicyNo;
    @Column(name = "CREATED_POLICY_ID")
    private String createdPolicyId;
    @Column(name = "INSURANCE_PRODUCT", nullable = false)
    @NotNull
    private String insuranceProduct;
    @Column(name = "EFFECTIVE_DATE", nullable = false)
    @NotNull
    private LocalDate effectiveDate;
    @Column(name = "SQUARE_METERS", nullable = false)
    @NotNull
    private Integer squareMeters;
    @Column(name = "CALCULATED_PREMIUM", nullable = false, precision = 19, scale = 2)
    @NotNull
    private BigDecimal calculatedPremium;
    @Column(name = "VALID_FROM", nullable = false)
    @NotNull
    private LocalDate validFrom;
    @Column(name = "VALID_UNTIL", nullable = false)
    @NotNull
    private LocalDate validUntil;

    public String getCreatedPolicyId() {
        return createdPolicyId;
    }

    public void setCreatedPolicyId(String createdPolicyId) {
        this.createdPolicyId = createdPolicyId;
    }

    public String getCreatedPolicyNo() {
        return createdPolicyNo;
    }

    public void setCreatedPolicyNo(String createdPolicyNo) {
        this.createdPolicyNo = createdPolicyNo;
    }

    public LocalDateTime getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(LocalDateTime rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public PaymentFrequency getPaymentFrequency() {
        return paymentFrequency == null ? null : PaymentFrequency.fromId(paymentFrequency);
    }

    public void setPaymentFrequency(PaymentFrequency paymentFrequency) {
        this.paymentFrequency = paymentFrequency == null ? null : paymentFrequency.getId();
    }

    public ProductVariant getProductVariant() {
        return productVariant == null ? null : ProductVariant.fromId(productVariant);
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant == null ? null : productVariant.getId();
    }

    public ProductType getProductType() {
        return productType == null ? null : ProductType.fromId(productType);
    }

    public void setProductType(ProductType productType) {
        this.productType = productType == null ? null : productType.getId();
    }

    public Integer getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(Integer squareMeters) {
        this.squareMeters = squareMeters;
    }

    public QuoteStatus getStatus() {
        return status == null ? null : QuoteStatus.fromId(status);
    }

    public void setStatus(QuoteStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public BigDecimal getCalculatedPremium() {
        return calculatedPremium;
    }

    public void setCalculatedPremium(BigDecimal calculatedPremium) {
        this.calculatedPremium = calculatedPremium;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public InsuranceProduct getInsuranceProduct() {
        return insuranceProduct == null ? null : InsuranceProduct.fromId(insuranceProduct);
    }

    public void setInsuranceProduct(InsuranceProduct insuranceProduct) {
        this.insuranceProduct = insuranceProduct == null ? null : insuranceProduct.getId();
    }

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}