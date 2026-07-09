/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : RiskKriRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RiskKriController
 * Related Service   : RiskKriService, RiskKriServiceImpl
 * Related Repository: RiskKriRepository
 * Related Entity    : RiskKri
 * Related DTO       : N/A
 * Related Mapper    : RiskKriMapper
 * Related DB Table  : risk_kris
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RiskKriService, RiskKriServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'risk_kris' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;

import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code RiskKriRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'risk_kris' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code risk_kris}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RiskKriRepository extends JpaRepository<RiskKri, Long> {
    List<RiskKri> findByRiskId(Long riskId);
    List<RiskKri> findByBreached(Boolean breached);
}
