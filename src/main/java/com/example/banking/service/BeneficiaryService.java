package com.example.banking.service;

import com.example.banking.entity.Beneficiary;
import com.example.banking.repository.BeneficiaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public Optional<Beneficiary> getBeneficiaryById(Long id) {
        return beneficiaryRepository.findById(id);
    }

    public Beneficiary saveBeneficiary(Beneficiary beneficiary) {
        return beneficiaryRepository.save(beneficiary);
    }

    public List<Beneficiary> getActiveBeneficiaries() {
        return beneficiaryRepository.findByStatus("ACTIVE");
    }
}
