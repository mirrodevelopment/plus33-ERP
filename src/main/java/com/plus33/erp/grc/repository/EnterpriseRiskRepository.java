/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : EnterpriseRiskRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EnterpriseRiskController
 * Related Service   : EnterpriseRiskService, EnterpriseRiskServiceImpl
 * Related Repository: EnterpriseRiskRepository
 * Related Entity    : EnterpriseRisk
 * Related DTO       : N/A
 * Related Mapper    : EnterpriseRiskMapper
 * Related DB Table  : enterprise_risks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EnterpriseRiskService, EnterpriseRiskServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'enterprise_risks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;

import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code EnterpriseRiskRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'enterprise_risks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code enterprise_risks}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EnterpriseRiskRepository extends JpaRepository<EnterpriseRisk, Long> {
    Optional<EnterpriseRisk> findByRiskNumber(String riskNumber);
    List<EnterpriseRisk> findByCompanyId(Long companyId);
    List<EnterpriseRisk> findByCompanyIdAndStatus(Long companyId, String status);
    List<EnterpriseRisk> findByCompanyIdAndCategory(Long companyId, String category);
}
