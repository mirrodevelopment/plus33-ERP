/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : ShiftPatternRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ShiftPatternController
 * Related Service   : ShiftPatternService, ShiftPatternServiceImpl
 * Related Repository: ShiftPatternRepository
 * Related Entity    : ShiftPattern
 * Related DTO       : N/A
 * Related Mapper    : ShiftPatternMapper
 * Related DB Table  : shift_patterns
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ShiftPatternService, ShiftPatternServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'shift_patterns' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.ShiftPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code ShiftPatternRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'shift_patterns' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code shift_patterns}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ShiftPatternRepository extends JpaRepository<ShiftPattern, Long> {
    Optional<ShiftPattern> findByName(String name);
}
