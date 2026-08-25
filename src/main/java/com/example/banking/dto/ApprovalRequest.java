package com.example.banking.dto;

import jakarta.validation.constraints.NotNull;

public class ApprovalRequest {

    @NotNull(message = "Checker id is required")
    private Long checkerId;

    public Long getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(Long checkerId) {
        this.checkerId = checkerId;
    }
}
