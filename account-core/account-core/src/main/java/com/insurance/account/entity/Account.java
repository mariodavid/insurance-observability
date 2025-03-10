package com.insurance.account.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
@Table(name = "ACCOUNT_ACCOUNT", indexes = {
        @Index(name = "IDX_ACCOUNT_ACCOUNT", columnList = "ACCOUNT_NO")
})
@Entity(name = "account_Account")
public class Account {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;
    @Column(name = "ACCOUNT_NO", nullable = false)
    @NotNull
    private String accountNo;
    @Column(name = "ACCOUNT_BALANCE", nullable = false, precision = 19, scale = 2)
    @NotNull
    private BigDecimal accountBalance;
    @OrderBy("documentDate ASC")
    @Composition
    @OneToMany(mappedBy = "account")
    private List<AccountDocument> documents;

    public List<AccountDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<AccountDocument> documents) {
        this.documents = documents;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}