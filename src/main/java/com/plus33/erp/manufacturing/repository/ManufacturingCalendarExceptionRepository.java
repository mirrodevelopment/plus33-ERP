/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ManufacturingCalendarExceptionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingCalendarExceptionController
 * Related Service   : ManufacturingCalendarExceptionService, ManufacturingCalendarExceptionServiceImpl
 * Related Repository: ManufacturingCalendarExceptionRepository
 * Related Entity    : ManufacturingCalendarException
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingCalendarExceptionMapper
 * Related DB Table  : manufacturing_calendar_exceptions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingCalendarExceptionService, ManufacturingCalendarExceptionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'manufacturing_calendar_exceptions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingCalendarException;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingCalendarExceptionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'manufacturing_calendar_exceptions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_calendar_exceptions}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ManufacturingCalendarExceptionRepository extends JpaRepository<ManufacturingCalendarException, Long> {
    List<ManufacturingCalendarException> findByCalendarId(Long calendarId);
    List<ManufacturingCalendarException> findByCalendarIdAndExceptionDate(Long calendarId, LocalDate exceptionDate);
    List<ManufacturingCalendarException> findByCalendarIdAndExceptionDateBetween(Long calendarId, LocalDate start, LocalDate end);
}
