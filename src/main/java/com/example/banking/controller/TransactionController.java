package com.example.banking.controller;

import com.example.banking.dto.ApiResponse;
import com.example.banking.dto.ApprovalRequest;
import com.example.banking.dto.FundTransferRequest;
import com.example.banking.dto.RejectionRequest;
import com.example.banking.entity.Transaction;
import com.example.banking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(@Valid @RequestBody FundTransferRequest request, @RequestParam Long makerId) {
        Transaction transaction = transactionService.createTransaction(request, makerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction submitted successfully. Waiting for Checker approval.", transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(transaction -> ResponseEntity.ok(new ApiResponse<>(true, "Transaction retrieved successfully", transaction)))
                .orElseGet(() -> ResponseEntity.badRequest().body(new ApiResponse<>(false, "Transaction not found", null)));
    }

    @GetMapping("/maker/{makerId}")
    public ResponseEntity<ApiResponse<List<Transaction>>> getMakerTransactions(@PathVariable Long makerId) {
        List<Transaction> transactions = transactionService.getMakerTransactions(makerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Maker transactions retrieved successfully", transactions));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<Transaction>>> getPendingTransactions() {
        List<Transaction> transactions = transactionService.getPendingApprovalTransactions();
        return ResponseEntity.ok(new ApiResponse<>(true, "Pending approval transactions retrieved successfully", transactions));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Transaction>> approveTransaction(@PathVariable Long id, @Valid @RequestBody ApprovalRequest request) {
        Transaction transaction = transactionService.approveTransaction(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction approved successfully.", transaction));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Transaction>> rejectTransaction(@PathVariable Long id, @Valid @RequestBody RejectionRequest request) {
        Transaction transaction = transactionService.rejectTransaction(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction rejected successfully.", transaction));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionHistory(@RequestParam Optional<String> status, @RequestParam Optional<String> transactionId) {
        List<Transaction> transactions = transactionService.getTransactionHistory(status, transactionId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction history retrieved successfully", transactions));
    }
}
