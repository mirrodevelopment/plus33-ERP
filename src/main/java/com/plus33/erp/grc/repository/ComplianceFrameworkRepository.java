/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : ComplianceFrameworkRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceFrameworkController
 * Related Service   : ComplianceFrameworkService, ComplianceFrameworkServiceImpl
 * Related Repository: ComplianceFrameworkRepository
 * Related Entity    : ComplianceFramework
 * Related DTO       : N/A
 * Related Mapper    : ComplianceFrameworkMapper
 * Related DB Table  : compliance_frameworks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceFrameworkService, ComplianceFrameworkServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'compliance_frameworks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ComplianceFrameworkRepository extends JpaRepository<ComplianceFramework, Long> {
    Optional<ComplianceFramework> findByCode(String code);
    List<ComplianceFramework> findByCompanyId(Long companyId);
}
