/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : AuditFindingRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuditFindingController
 * Related Service   : AuditFindingService, AuditFindingServiceImpl
 * Related Repository: AuditFindingRepository
 * Related Entity    : AuditFinding
 * Related DTO       : N/A
 * Related Mapper    : AuditFindingMapper
 * Related DB Table  : audit_findings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AuditFindingService, AuditFindingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'audit_findings' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AuditFindingRepository extends JpaRepository<AuditFinding, Long> {
    Optional<AuditFinding> findByFindingNumber(String num);
    List<AuditFinding> findByEngagementId(Long engagementId);
    List<AuditFinding> findByStatus(String status);
    long countByStatus(String status);
}
