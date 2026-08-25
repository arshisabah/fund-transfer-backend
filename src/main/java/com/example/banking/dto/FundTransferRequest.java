package com.example.banking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FundTransferRequest {

    @NotNull(message = "Debit account id is required")
    private Long debitAccountId;

    @NotNull(message = "Beneficiary id is required")
    private Long beneficiaryId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than zero")
    private Long amount;

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
}
