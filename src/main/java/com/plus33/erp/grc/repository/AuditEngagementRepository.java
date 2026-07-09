/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : AuditEngagementRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuditEngagementController
 * Related Service   : AuditEngagementService, AuditEngagementServiceImpl
 * Related Repository: AuditEngagementRepository
 * Related Entity    : AuditEngagement
 * Related DTO       : N/A
 * Related Mapper    : AuditEngagementMapper
 * Related DB Table  : audit_engagements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AuditEngagementService, AuditEngagementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'audit_engagements' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AuditEngagementRepository extends JpaRepository<AuditEngagement, Long> {
    Optional<AuditEngagement> findByEngagementNumber(String engagementNumber);
    List<AuditEngagement> findByProgramId(Long programId);
    List<AuditEngagement> findByStatus(String status);
}
