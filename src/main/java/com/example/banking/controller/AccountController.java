package com.example.banking.controller;

import com.example.banking.dto.ApiResponse;
import com.example.banking.entity.Account;
import com.example.banking.entity.Transaction;
import com.example.banking.service.AccountService;
import com.example.banking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Account>>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(new ApiResponse<>(true, "Accounts retrieved successfully", accounts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Account>> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id)
                .map(account -> ResponseEntity.ok(new ApiResponse<>(true, "Account retrieved successfully", account)))
                .orElseGet(() -> ResponseEntity.badRequest().body(new ApiResponse<>(false, "Account not found", null)));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsByAccount(@PathVariable Long id) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Account transactions retrieved successfully", transactions));
    }
}
