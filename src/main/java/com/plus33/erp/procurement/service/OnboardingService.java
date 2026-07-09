/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : OnboardingService.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OnboardingController
 * Related Service   : OnboardingService
 * Related Repository: SupplierQualificationRepository
 * Related Entity    : Onboarding
 * Related DTO       : N/A
 * Related Mapper    : OnboardingMapper
 * Related DB Table  : onboardings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OnboardingController, OnboardingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements OnboardingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.SupplierQualification;
import com.plus33.erp.procurement.repository.SupplierQualificationRepository;
import com.plus33.erp.procurement.event.ProcurementEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code OnboardingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * OnboardingController
 *   --> OnboardingService (this)
 *   --> Validate business rules
 *   --> OnboardingRepository (read/write 'onboardings')
 *   --> OnboardingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code onboardings}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class OnboardingService {

    private final SupplierQualificationRepository qualificationRepository;
    private final ProcurementEventBus eventBus;

    public OnboardingService(SupplierQualificationRepository qualificationRepository, ProcurementEventBus eventBus) {
        this.qualificationRepository = qualificationRepository;
        this.eventBus = eventBus;
    }

    /**
     * Handles the supplier event or exception in the business workflow.
     *
     * @param supplierId the supplierId input value
     * @return the SupplierQualification result
     */
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

    /**
     * Performs the evaluateQualificationRisk operation in this module.
     *
     * @param supplierId the supplierId input value
     * @param financialScore the financialScore input value
     * @param complianceScore the complianceScore input value
     * @param esgScore the esgScore input value
     */
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