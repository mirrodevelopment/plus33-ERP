/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : AuditProgramRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuditProgramController
 * Related Service   : AuditProgramService, AuditProgramServiceImpl
 * Related Repository: AuditProgramRepository
 * Related Entity    : AuditProgram
 * Related DTO       : N/A
 * Related Mapper    : AuditProgramMapper
 * Related DB Table  : audit_programs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AuditProgramService, AuditProgramServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'audit_programs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AuditProgramRepository extends JpaRepository<AuditProgram, Long> {
    List<AuditProgram> findByCompanyIdAndFiscalYear(Long companyId, Integer year);
}
