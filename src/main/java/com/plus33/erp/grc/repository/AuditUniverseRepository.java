/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : AuditUniverseRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuditUniverseController
 * Related Service   : AuditUniverseService, AuditUniverseServiceImpl
 * Related Repository: AuditUniverseRepository
 * Related Entity    : AuditUniverse
 * Related DTO       : N/A
 * Related Mapper    : AuditUniverseMapper
 * Related DB Table  : audit_universes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AuditUniverseService, AuditUniverseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'audit_universes' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AuditUniverseRepository extends JpaRepository<AuditUniverse, Long> {
    List<AuditUniverse> findByCompanyId(Long companyId);
}
