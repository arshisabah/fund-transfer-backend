package com.example.banking.repository;

import com.example.banking.entity.Transaction;
import com.example.banking.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCreatedBy(Long createdBy);
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findByDebitAccountId(Long debitAccountId);
    Optional<Transaction> findByTransactionId(String transactionId);
    boolean existsByCreatedByAndBeneficiaryIdAndAmountAndStatus(Long createdBy, Long beneficiaryId, Long amount, TransactionStatus status);
}
