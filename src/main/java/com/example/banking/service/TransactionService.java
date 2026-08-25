package com.example.banking.service;

import com.example.banking.dto.FundTransferRequest;
import com.example.banking.dto.RejectionRequest;
import com.example.banking.dto.ApprovalRequest;
import com.example.banking.entity.Account;
import com.example.banking.entity.Beneficiary;
import com.example.banking.entity.Transaction;
import com.example.banking.entity.User;
import com.example.banking.enums.Role;
import com.example.banking.enums.TransactionStatus;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.BeneficiaryRepository;
import com.example.banking.repository.TransactionRepository;
import com.example.banking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private static final long MAX_TRANSACTION_LIMIT = 50000L;

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, BeneficiaryRepository beneficiaryRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getTransactionHistory(Optional<String> status, Optional<String> transactionId) {
        if (status.isPresent()) {
            try {
                TransactionStatus transactionStatus = TransactionStatus.valueOf(status.get());
                return transactionRepository.findByStatus(transactionStatus);
            } catch (IllegalArgumentException ex) {
                return transactionRepository.findAll();
            }
        }
        if (transactionId.isPresent()) {
            Optional<Transaction> transaction = transactionRepository.findByTransactionId(transactionId.get());
            return transaction.map(List::of).orElse(List.of());
        }
        return transactionRepository.findAll();
    }

    public List<Transaction> getMakerTransactions(Long makerId) {
        return transactionRepository.findByCreatedBy(makerId);
    }

    public List<Transaction> getPendingApprovalTransactions() {
        return transactionRepository.findByStatus(TransactionStatus.PENDING_APPROVAL);
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByDebitAccountId(accountId);
    }

    @Transactional
    public Transaction createTransaction(FundTransferRequest request, Long makerId) {
        Account debitAccount = accountRepository.findById(request.getDebitAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid debit account"));

        if (!debitAccount.getUserId().equals(makerId)) {
            throw new IllegalArgumentException("Invalid debit account");
        }

        Beneficiary beneficiary = beneficiaryRepository.findById(request.getBeneficiaryId())
                .orElseThrow(() -> new IllegalArgumentException("Beneficiary does not exist"));

        if (!"ACTIVE".equalsIgnoreCase(beneficiary.getStatus())) {
            throw new IllegalArgumentException("Beneficiary is inactive");
        }

        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (request.getAmount() > debitAccount.getAvailableBalance()) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        if (request.getAmount() > MAX_TRANSACTION_LIMIT) {
            throw new IllegalArgumentException("Transaction limit exceeded");
        }

        if (transactionRepository.existsByCreatedByAndBeneficiaryIdAndAmountAndStatus(makerId, beneficiary.getId(), request.getAmount(), TransactionStatus.PENDING_APPROVAL)) {
            throw new IllegalArgumentException("Duplicate transaction/request");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setDebitAccountId(debitAccount.getId());
        transaction.setBeneficiaryId(beneficiary.getId());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType("FUND_TRANSFER");
        transaction.setStatus(TransactionStatus.PENDING_APPROVAL);
        transaction.setCreatedBy(makerId);
        transaction.setCreatedAt(LocalDateTime.now());

        // Place a hold on the funds so a maker cannot raise several transactions
        // that collectively exceed the real account balance while they're pending.
        debitAccount.setAvailableBalance(debitAccount.getAvailableBalance() - request.getAmount());
        accountRepository.save(debitAccount);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction approveTransaction(Long transactionId, ApprovalRequest request) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction does not exist"));

        if (!transaction.getStatus().equals(TransactionStatus.PENDING_APPROVAL)) {
            throw new IllegalArgumentException("Transaction has already been processed.");
        }

        User checker = userRepository.findById(request.getCheckerId())
                .orElseThrow(() -> new IllegalArgumentException("Checker user does not exist"));

        if (!checker.getRole().equals(Role.CHECKER)) {
            throw new IllegalArgumentException("Only authorized Checker users can approve or reject transactions.");
        }

        if (transaction.getCreatedBy().equals(checker.getId())) {
            throw new IllegalArgumentException("Maker cannot approve their own transaction.");
        }

        Account debitAccount = accountRepository.findById(transaction.getDebitAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid debit account"));

        // The available balance was already reduced (held) when the transaction was
        // created, so this is the actual ledger debit for the now-approved transfer.
        if (transaction.getAmount() > debitAccount.getBalance()) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        transaction.setStatus(TransactionStatus.APPROVED);
        transaction.setApprovedBy(checker.getId());
        transaction.setApprovedAt(LocalDateTime.now());
        transaction = transactionRepository.save(transaction);

        transaction.setStatus(TransactionStatus.PROCESSING);
        transaction = transactionRepository.save(transaction);

        debitAccount.setBalance(debitAccount.getBalance() - transaction.getAmount());
        accountRepository.save(debitAccount);

        transaction.setStatus(TransactionStatus.SUCCESS);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction rejectTransaction(Long transactionId, RejectionRequest request) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction does not exist"));

        if (!transaction.getStatus().equals(TransactionStatus.PENDING_APPROVAL)) {
            throw new IllegalArgumentException("Transaction has already been processed.");
        }

        User checker = userRepository.findById(request.getCheckerId())
                .orElseThrow(() -> new IllegalArgumentException("Checker user does not exist"));

        if (!checker.getRole().equals(Role.CHECKER)) {
            throw new IllegalArgumentException("Only authorized Checker users can approve or reject transactions.");
        }

        if (transaction.getCreatedBy().equals(checker.getId())) {
            throw new IllegalArgumentException("Maker cannot approve their own transaction.");
        }

        transaction.setStatus(TransactionStatus.REJECTED);
        transaction.setApprovedBy(checker.getId());
        transaction.setApprovedAt(LocalDateTime.now());
        transaction.setRejectionReason(request.getRejectionReason());

        // Release the hold placed on the funds when the transaction was created.
        Account debitAccount = accountRepository.findById(transaction.getDebitAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid debit account"));
        debitAccount.setAvailableBalance(debitAccount.getAvailableBalance() + transaction.getAmount());
        accountRepository.save(debitAccount);

        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
