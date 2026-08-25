package com.example.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RejectionRequest {

    @NotNull(message = "Checker id is required")
    private Long checkerId;

    @NotBlank(message = "Rejection reason is required")
    private String rejectionReason;

    public Long getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(Long checkerId) {
        this.checkerId = checkerId;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
