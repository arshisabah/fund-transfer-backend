package com.example.banking.controller;

import com.example.banking.dto.ApiResponse;
import com.example.banking.entity.Beneficiary;
import com.example.banking.service.BeneficiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Beneficiary>>> getAllBeneficiaries() {
        List<Beneficiary> beneficiaries = beneficiaryService.getAllBeneficiaries();
        return ResponseEntity.ok(new ApiResponse<>(true, "Beneficiaries retrieved successfully", beneficiaries));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Beneficiary>> addBeneficiary(@RequestBody Beneficiary beneficiary) {
        beneficiary.setStatus(beneficiary.getStatus() == null ? "ACTIVE" : beneficiary.getStatus());
        Beneficiary saved = beneficiaryService.saveBeneficiary(beneficiary);
        return ResponseEntity.ok(new ApiResponse<>(true, "Beneficiary created successfully", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Beneficiary>> updateBeneficiary(@PathVariable Long id, @RequestBody Beneficiary beneficiary) {
        return beneficiaryService.getBeneficiaryById(id)
                .map(existing -> {
                    existing.setName(beneficiary.getName());
                    existing.setAccountNumber(beneficiary.getAccountNumber());
                    existing.setBankName(beneficiary.getBankName());
                    Beneficiary updated = beneficiaryService.saveBeneficiary(existing);
                    return ResponseEntity.ok(new ApiResponse<>(true, "Beneficiary updated successfully", updated));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(new ApiResponse<>(false, "Beneficiary not found", null)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Beneficiary>> changeStatus(@PathVariable Long id, @RequestParam String status) {
        return beneficiaryService.getBeneficiaryById(id)
                .map(existing -> {
                    existing.setStatus(status);
                    Beneficiary updated = beneficiaryService.saveBeneficiary(existing);
                    return ResponseEntity.ok(new ApiResponse<>(true, "Beneficiary status updated successfully", updated));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(new ApiResponse<>(false, "Beneficiary not found", null)));
    }
}
