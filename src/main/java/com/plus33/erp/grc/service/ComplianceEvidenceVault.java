package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

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

    public void verifyEvidence(Long evidenceId, Long verifiedById) {
        ComplianceEvidence e = evidenceRepo.findById(evidenceId)
            .orElseThrow(() -> new IllegalArgumentException("Evidence not found: " + evidenceId));
        e.setVerifiedById(verifiedById);
        e.setVerificationDate(LocalDate.now());
        evidenceRepo.save(e);
    }

    public long countEvidenceFor(String referenceType, Long referenceId) {
        return evidenceRepo.countByReferenceTypeAndReferenceId(referenceType, referenceId);
    }
}
