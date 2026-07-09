/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : BomSubstituteRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomSubstituteController
 * Related Service   : BomSubstituteService, BomSubstituteServiceImpl
 * Related Repository: BomSubstituteRepository
 * Related Entity    : BomSubstitute
 * Related DTO       : N/A
 * Related Mapper    : BomSubstituteMapper
 * Related DB Table  : bom_substitutes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BomSubstituteService, BomSubstituteServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'bom_substitutes' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.BomSubstitute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code BomSubstituteRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bom_substitutes' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bom_substitutes}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface BomSubstituteRepository extends JpaRepository<BomSubstitute, Long> {
    List<BomSubstitute> findByBomLineId(Long bomLineId);
}
