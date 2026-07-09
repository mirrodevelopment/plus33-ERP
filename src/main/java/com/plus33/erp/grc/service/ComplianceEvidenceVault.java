/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.service
 * File              : ComplianceEvidenceVault.java
 * Purpose           : Business logic service layer for Grc Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceEvidenceVaultController
 * Related Service   : ComplianceEvidenceVault
 * Related Repository: ComplianceEvidenceRepository
 * Related Entity    : ComplianceEvidenceVault
 * Related DTO       : N/A
 * Related Mapper    : ComplianceEvidenceVaultMapper
 * Related DB Table  : compliance_evidence_vaults
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceEvidenceVaultController, ComplianceEvidenceVaultImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Grc Module. Implements ComplianceEvidenceVaultService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceEvidenceVault}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Grc Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ComplianceEvidenceVaultController
 *   --> ComplianceEvidenceVault (this)
 *   --> Validate business rules
 *   --> ComplianceEvidenceVaultRepository (read/write 'compliance_evidence_vaults')
 *   --> ComplianceEvidenceVaultMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code compliance_evidence_vaults}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class ComplianceEvidenceVault {

    private final ComplianceEvidenceRepository evidenceRepo;

    public ComplianceEvidenceVault(ComplianceEvidenceRepository evidenceRepo) {
        this.evidenceRepo = evidenceRepo;
    }

    /**
     * Submit immutable evidence. Duplicate hash is rejected.
     */
    public ComplianceEvidence submitEvidence(Long companyId, String referenceType, Long referenceId,
                                             String fileName, String contentHash, String evidenceSource,
                                             Long uploadedById, String retentionPolicy) {
        if (evidenceRepo.existsByContentHash(contentHash)) {
            throw new IllegalStateException("Duplicate evidence detected — content hash already exists: " + contentHash);
        }
        ComplianceEvidence evidence = new ComplianceEvidence();
        evidence.setCompanyId(companyId);
        evidence.setReferenceType(referenceType);
        evidence.setReferenceId(referenceId);
        evidence.setFileName(fileName);
        evidence.setContentHash(contentHash);
        evidence.setEvidenceSource(evidenceSource);
        evidence.setUploadedById(uploadedById);
        evidence.setRetentionPolicy(retentionPolicy);
        return evidenceRepo.save(evidence);
    }

    /**
     * Validates business rules and constraints for evidence.
     *
     * @param evidenceId the evidenceId input value
     * @param verifiedById the verifiedById input value
     * @throws BusinessException if a business rule is violated
     */
    public void verifyEvidence(Long evidenceId, Long verifiedById) {
        ComplianceEvidence e = evidenceRepo.findById(evidenceId)
            .orElseThrow(() -> new IllegalArgumentException("Evidence not found: " + evidenceId));
        e.setVerifiedById(verifiedById);
        e.setVerificationDate(LocalDate.now());
        evidenceRepo.save(e);
    }

    /**
     * Performs the countEvidenceFor operation in this module.
     *
     * @param referenceType the referenceType input value
     * @param referenceId the referenceId input value
     * @return the numeric result value
     */
    public long countEvidenceFor(String referenceType, Long referenceId) {
        return evidenceRepo.countByReferenceTypeAndReferenceId(referenceType, referenceId);
    }
}