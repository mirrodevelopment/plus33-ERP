/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : RiskAppetiteStatementRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RiskAppetiteStatementController
 * Related Service   : RiskAppetiteStatementService, RiskAppetiteStatementServiceImpl
 * Related Repository: RiskAppetiteStatementRepository
 * Related Entity    : RiskAppetiteStatement
 * Related DTO       : N/A
 * Related Mapper    : RiskAppetiteStatementMapper
 * Related DB Table  : risk_appetite_statements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RiskAppetiteStatementService, RiskAppetiteStatementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'risk_appetite_statements' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;

import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code RiskAppetiteStatementRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'risk_appetite_statements' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code risk_appetite_statements}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RiskAppetiteStatementRepository extends JpaRepository<RiskAppetiteStatement, Long> {
    Optional<RiskAppetiteStatement> findByCompanyIdAndRiskCategoryAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
        Long companyId, String riskCategory, LocalDate date1, LocalDate date2);
}
