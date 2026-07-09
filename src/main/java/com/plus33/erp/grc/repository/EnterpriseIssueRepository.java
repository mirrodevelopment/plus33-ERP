/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : EnterpriseIssueRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EnterpriseIssueController
 * Related Service   : EnterpriseIssueService, EnterpriseIssueServiceImpl
 * Related Repository: EnterpriseIssueRepository
 * Related Entity    : EnterpriseIssue
 * Related DTO       : N/A
 * Related Mapper    : EnterpriseIssueMapper
 * Related DB Table  : enterprise_issues
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EnterpriseIssueService, EnterpriseIssueServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'enterprise_issues' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface EnterpriseIssueRepository extends JpaRepository<EnterpriseIssue, Long> {
    Optional<EnterpriseIssue> findByIssueNumber(String num);
    List<EnterpriseIssue> findByCompanyIdAndStatus(Long companyId, String status);
    long countByCompanyIdAndStatus(Long companyId, String status);
}
