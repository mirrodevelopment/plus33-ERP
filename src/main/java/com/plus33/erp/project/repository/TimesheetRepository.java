/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : TimesheetRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TimesheetController
 * Related Service   : TimesheetService, TimesheetServiceImpl
 * Related Repository: TimesheetRepository
 * Related Entity    : Timesheet
 * Related DTO       : N/A
 * Related Mapper    : TimesheetMapper
 * Related DB Table  : timesheets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TimesheetService, TimesheetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'timesheets' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code TimesheetRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'timesheets' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code timesheets}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByResourceId(Long resourceId);
}
