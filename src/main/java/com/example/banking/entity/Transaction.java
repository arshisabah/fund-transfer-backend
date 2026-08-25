package com.example.banking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId;

    @Column(nullable = false)
    private Long debitAccountId;

    @Column(nullable = false)
    private Long beneficiaryId;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String transactionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.example.banking.enums.TransactionStatus status;

    @Column(nullable = false)
    private Long createdBy;

    private Long approvedBy;

    private String rejectionReason;

    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;

    public Transaction() {
    }

    public Transaction(Long id, String transactionId, Long debitAccountId, Long beneficiaryId, Long amount, String transactionType, com.example.banking.enums.TransactionStatus status, Long createdBy, Long approvedBy, String rejectionReason, LocalDateTime createdAt, LocalDateTime approvedAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.debitAccountId = debitAccountId;
        this.beneficiaryId = beneficiaryId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.createdBy = createdBy;
        this.approvedBy = approvedBy;
        this.rejectionReason = rejectionReason;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getDebitAccountId() {
        return debitAccountId;
    }

    public void setDebitAccountId(Long debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public com.example.banking.enums.TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(com.example.banking.enums.TransactionStatus status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
}
