package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.SupplierQualification;
import com.plus33.erp.procurement.repository.SupplierQualificationRepository;
import com.plus33.erp.procurement.event.ProcurementEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OnboardingService {

    private final SupplierQualificationRepository qualificationRepository;
    private final ProcurementEventBus eventBus;

    public OnboardingService(SupplierQualificationRepository qualificationRepository, ProcurementEventBus eventBus) {
        this.qualificationRepository = qualificationRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public SupplierQualification onboardSupplier(Long supplierId) {
        SupplierQualification qual = new SupplierQualification();
        qual.setSupplierId(supplierId);
        qual.setStatus("ONBOARDING");
        qual.setConsolidatedRiskLevel("LOW");
        qual.setApprovedVendorList(false);
        qualificationRepository.save(qual);
        return qual;
    }

    @Transactional
    public void evaluateQualificationRisk(Long supplierId, BigDecimal financialScore, BigDecimal complianceScore, BigDecimal esgScore) {
        SupplierQualification qual = qualificationRepository.findBySupplierId(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Qualification profile not found"));

        qual.setRiskScoreFinancial(financialScore);
        qual.setRiskScoreCompliance(complianceScore);
        qual.setRiskScoreEsg(esgScore);

        BigDecimal sum = financialScore.add(complianceScore).add(esgScore);
        if (sum.compareTo(new BigDecimal("2.5")) > 0) {
            qual.setConsolidatedRiskLevel("CRITICAL");
            qual.setStatus("SUSPENDED");
            qual.setApprovedVendorList(false);
            eventBus.publish("SupplierBlacklisted", 1L, supplierId, "Supplier blacklisted due to critical risk");
        } else if (sum.compareTo(new BigDecimal("1.5")) > 0) {
            qual.setConsolidatedRiskLevel("HIGH");
            qual.setStatus("APPROVED_WITH_CONDITIONS");
            qual.setApprovedVendorList(true);
        } else {
            qual.setConsolidatedRiskLevel("LOW");
            qual.setStatus("APPROVED");
            qual.setApprovedVendorList(true);
        }

        qual.setUpdatedAt(LocalDateTime.now());
        qualificationRepository.save(qual);
    }
}
