package com.example.banking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String status;

    public Beneficiary() {
    }

    public Beneficiary(Long id, String name, String accountNumber, String bankName, String status) {
        this.id = id;
        this.name = name;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
