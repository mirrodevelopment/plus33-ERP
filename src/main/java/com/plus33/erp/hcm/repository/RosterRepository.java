/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : RosterRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RosterController
 * Related Service   : RosterService, RosterServiceImpl
 * Related Repository: RosterRepository
 * Related Entity    : Roster
 * Related DTO       : N/A
 * Related Mapper    : RosterMapper
 * Related DB Table  : rosters
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RosterService, RosterServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'rosters' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code RosterRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'rosters' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code rosters}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RosterRepository extends JpaRepository<Roster, Long> {
    List<Roster> findByEmployeeId(Long employeeId);
}
