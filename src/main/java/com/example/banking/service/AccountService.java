package com.example.banking.service;

import com.example.banking.entity.Account;
import com.example.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }
}
