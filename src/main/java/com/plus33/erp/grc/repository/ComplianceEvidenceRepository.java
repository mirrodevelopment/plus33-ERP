/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : ComplianceEvidenceRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceEvidenceController
 * Related Service   : ComplianceEvidenceService, ComplianceEvidenceServiceImpl
 * Related Repository: ComplianceEvidenceRepository
 * Related Entity    : ComplianceEvidence
 * Related DTO       : N/A
 * Related Mapper    : ComplianceEvidenceMapper
 * Related DB Table  : compliance_evidences
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceEvidenceService, ComplianceEvidenceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'compliance_evidences' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ComplianceEvidenceRepository extends JpaRepository<ComplianceEvidence, Long> {
    Optional<ComplianceEvidence> findByContentHash(String hash);
    boolean existsByContentHash(String hash);
    long countByReferenceTypeAndReferenceId(String type, Long referenceId);
}
