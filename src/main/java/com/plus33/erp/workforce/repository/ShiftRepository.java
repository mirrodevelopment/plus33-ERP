/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : ShiftRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ShiftController
 * Related Service   : ShiftService, ShiftServiceImpl
 * Related Repository: ShiftRepository
 * Related Entity    : Shift
 * Related DTO       : N/A
 * Related Mapper    : ShiftMapper
 * Related DB Table  : shifts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ShiftService, ShiftServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'shifts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code ShiftRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'shifts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code shifts}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByCompanyId(Long companyId);

    Optional<Shift> findByCode(String code);
}
